package sonar;

import org.sonar.api.Plugin;
import sonar.define.MyBatisPlusRulesDefinition;

public class MyBatisPlusRulesPlugin implements Plugin {
    @Override
    public void define(Context context) {
        context.addExtensions(MyBatisPlusRulesDefinition.class, MyCheckRegistrar.class);
    }
}
