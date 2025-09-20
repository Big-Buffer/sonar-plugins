package sonar;

import org.sonar.plugins.java.api.JavaCheck;
import sonar.rules.LambdaUpdateWrapperEqConditionCheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RulesList {

    private RulesList() {}

    public static List<Class<? extends JavaCheck>> getChecks() {
        List<Class<? extends JavaCheck>> checks = new ArrayList<>();
        checks.addAll(getJavaChecks());
        checks.addAll(getJavaTestChecks());
        return Collections.unmodifiableList(checks);
    }

    /**
     * These rules are going to target MAIN code only
     */
    public static List<Class<? extends JavaCheck>> getJavaChecks() {
        return List.of(LambdaUpdateWrapperEqConditionCheck.class);
    }

    /**
     * These rules are going to target TEST code only
     */
    public static List<Class<? extends JavaCheck>> getJavaTestChecks() {
        return new ArrayList<>();
    }

}
