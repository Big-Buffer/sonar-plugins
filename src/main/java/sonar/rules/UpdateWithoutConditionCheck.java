package sonar.rules;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.LiteralTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.VariableTree;
import org.sonar.plugins.java.api.semantic.Symbol;


@Rule(key = "UpdateWithoutConditionCheck")
public class UpdateWithoutConditionCheck extends BaseTreeVisitor implements JavaFileScanner {

    private JavaFileScannerContext context;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    @Override
    public void visitMethodInvocation(MethodInvocationTree tree) {
        MethodInvocationTree mit = (MethodInvocationTree) tree;

        // 使用更可靠的方法获取方法名
        String methodName = getMethodName(mit.methodSelect());

        if (isSqlExecutionMethod(methodName)) {
            checkUpdateStatement(tree);
        }

        super.visitMethodInvocation(tree);
    }

    private String getMethodName(Tree methodSelect) {
        if (methodSelect instanceof IdentifierTree) {
            return ((IdentifierTree) methodSelect).name();
        } else if (methodSelect instanceof MemberSelectExpressionTree) {
            return ((MemberSelectExpressionTree) methodSelect).identifier().name();
        }
        return "unknown";
    }

    private boolean isSqlExecutionMethod(String methodName) {
        return "execute".equals(methodName) || "executeUpdate".equals(methodName) || "update".equals(
                methodName) || "executeQuery".equals(methodName);
    }

    private void checkUpdateStatement(MethodInvocationTree tree) {
        if (tree.arguments().isEmpty()) {
            return;
        }

        ExpressionTree firstArg = tree.arguments().get(0);
        String         sql      = null;

        if (firstArg.is(Tree.Kind.STRING_LITERAL)) {
            sql = ((LiteralTree) firstArg).value();
            sql = sql.replaceAll("['\"]", ""); // 移除引号
        } else if (firstArg.is(Tree.Kind.IDENTIFIER)) {
            // 如果是标识符，尝试解析变量
            Symbol symbol = ((IdentifierTree) firstArg).symbol();
            if (symbol.isVariableSymbol()) {
                Tree declaration = symbol.declaration();
                if (declaration != null && declaration.is(Tree.Kind.VARIABLE)) {
                    VariableTree   variableDecl = (VariableTree) declaration;
                    ExpressionTree initializer  = variableDecl.initializer();
                    if (initializer != null && initializer.is(Tree.Kind.STRING_LITERAL)) {
                        sql = ((LiteralTree) initializer).value();
                        sql = sql.replaceAll("['\"]", "");
                    }
                }
            }
        }

        if (sql != null && isUpdateStatementWithoutWhere(sql.toLowerCase())) {
            context.reportIssue(this, firstArg,
                                "UPDATE statement should include a WHERE condition to prevent full table "
                                + "updates");
        }
    }

    private boolean isUpdateStatementWithoutWhere(String sql) {
        String lowerSql    = sql.toLowerCase().replaceAll("\\s+", " ");
        int    updateIndex = lowerSql.indexOf("update");
        if (updateIndex == -1) {
            return false;
        }
        int setIndex = lowerSql.indexOf("set", updateIndex);
        if (setIndex == -1) {
            return false;
        }
        String afterSet = lowerSql.substring(setIndex + 3);
        return !afterSet.contains("where");
    }
}