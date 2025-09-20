package sonar;

import org.sonar.plugins.java.api.CheckRegistrar;
import org.sonarsource.api.sonarlint.SonarLintSide;
import sonar.define.MyBatisPlusRulesDefinition;

/**
 * 因为我们的规则依赖于Java API的SonarSource Analyzer，
 * 所以还需要告诉父Java插件必须检索一些新规则。
 */
@SonarLintSide
public class MyCheckRegistrar implements CheckRegistrar {
    /**
     * 在分析期间调用此方法以获取用于实例化检查的类。
     * @param registrarContext java插件将使用这个上下文来检索要检查的类
     */
    @Override
    public void register(RegistrarContext registrarContext) {
        registrarContext
                .registerClassesForRepository(MyBatisPlusRulesDefinition.REPOSITORY_KEY
                        , RulesList.getJavaChecks()
                        , RulesList.getJavaTestChecks());
    }
}

