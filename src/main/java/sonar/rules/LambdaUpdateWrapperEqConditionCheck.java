package sonar.rules;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.Collections;
import java.util.List;

@Rule(key = "LambdaUpdateWrapperMissingEqCondition")
public class LambdaUpdateWrapperEqConditionCheck extends IssuableSubscriptionVisitor {

    private static final Logger LOG = Loggers.get(LambdaUpdateWrapperEqConditionCheck.class);


    private boolean hasEqCondition        = false;
    private boolean isLambdaUpdateWrapper = false;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        super.scanFile(context);
        // 在每个文件开始时重置状态，避免状态污染
        resetFlags();
    }

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.METHOD_INVOCATION);
    }

    @Override
    public void visitNode(Tree tree) {
        MethodInvocationTree mit = (MethodInvocationTree) tree;

        // 使用更可靠的方法获取方法名
        String methodName     = getMethodName(mit.methodSelect());
        String fullExpression = getFullExpression(mit.methodSelect());

        LOG.debug("Visiting method: " + methodName + ", expression: " + fullExpression);
        System.out.println("Visiting method: " + methodName + ", full expression: " + fullExpression);

        // 检测是否调用了 Wrappers.lambdaUpdate()
        if ("lambdaUpdate".equals(methodName) && fullExpression.endsWith("Wrappers.lambdaUpdate")) {
            hasEqCondition = false;
            isLambdaUpdateWrapper = true;
            return;
        }

        // 如果是 LambdaUpdateWrapper 的实例，检查是否包含 eq 方法
        if (isLambdaUpdateWrapper && "eq".equals(methodName)) {
            hasEqCondition = true;
        }

        // 检查 update 方法调用
        if (isLambdaUpdateWrapper && "update".equals(methodName)) {
            if (!hasEqCondition) {
                reportIssue(mit, "使用 LambdaUpdateWrapper 时必须包含至少一个 eq 条件，以避免全表更新。");
            }
            resetFlags();
        }
    }

    private String getMethodName(Tree methodSelect) {
        if (methodSelect instanceof IdentifierTree) {
            return ((IdentifierTree) methodSelect).name();
        } else if (methodSelect instanceof MemberSelectExpressionTree) {
            return ((MemberSelectExpressionTree) methodSelect).identifier().name();
        }
        return "unknown";
    }

    private String getFullExpression(Tree methodSelect) {
        if (methodSelect instanceof IdentifierTree) {
            return ((IdentifierTree) methodSelect).name();
        } else if (methodSelect instanceof MemberSelectExpressionTree) {
            MemberSelectExpressionTree mset = (MemberSelectExpressionTree) methodSelect;
            return getFullExpression(mset.expression()) + "." + mset.identifier().name();
        }
        return "unknown";
    }

    private void resetFlags() {
        isLambdaUpdateWrapper = false;
        hasEqCondition = false;
    }
}