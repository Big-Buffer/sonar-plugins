package sonar.define;


import org.sonar.api.SonarRuntime;
import org.sonar.api.config.Configuration;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonarsource.analyzer.commons.RuleMetadataLoader;
import sonar.RulesList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class MyBatisPlusRulesDefinition implements RulesDefinition {
    public static final  String REPOSITORY_KEY  = "mybatis-plus-rules";
    private static final String REPOSITORY_NAME = "MyBatis Plus Rules";

    //规则的元数据和json文件所在路径
    private static final String RESOURCE_BASE_PATH = "sonar/rules";

    // Add the rule keys of the rules which need to be considered as template-rules
    private static final Set<String> RULE_TEMPLATES_KEY = Collections.emptySet();

    /**
     * 核心组件org.sonar.api.config.Configuration提供对配置的访问。 它处理默认值和值的解密。它可用于所有栈(扫描器，web服务器，计算引擎)
     */
    private final Configuration config;
    private final SonarRuntime  runtime;

    public MyBatisPlusRulesDefinition(Configuration config, SonarRuntime runtime) {
        this.config = config;
        this.runtime = runtime;
    }

    @Override
    public void define(Context context) {
        //实例化一个新仓库，指定这个规则适应的编程语言
        NewRepository repository = context.createRepository(REPOSITORY_KEY, "java");
        repository.setName(REPOSITORY_NAME);
        //通过规则元数据进行加载的Loader
        RuleMetadataLoader ruleMetadataLoader = new RuleMetadataLoader(RESOURCE_BASE_PATH, runtime);
        //添加规则，同时会检查是否有添加了@Rule注解,以及根据key解析对应的html和json文件
        ruleMetadataLoader.addRulesByAnnotatedClass(repository, new ArrayList<>(RulesList.getChecks()));

        setTemplates(repository);

        repository.done();
    }

    private static void setTemplates(NewRepository repository) {
        RULE_TEMPLATES_KEY.stream().map(repository::rule).filter(Objects::nonNull).forEach(
                rule -> rule.setTemplate(true));
    }
}
