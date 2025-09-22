import org.junit.Test;
import org.sonar.java.testing.CheckVerifier;
import sonar.rules.DeleteWithoutConditionCheck;
import sonar.rules.UpdateWithoutConditionCheck;
import sonar.rules.LambdaUpdateWrapperEqConditionCheck;



/**
 * @Title: Test1
 * @Author qiangjun.chen
 * @Package PACKAGE_NAME
 * @Date 2025/9/19 16:53
 * @description:
 */
public class Test1 {

    @Test
    public void test1() {
        CheckVerifier.newVerifier().withCheck(new LambdaUpdateWrapperEqConditionCheck()).onFile(
                "src/test/resources/TestClass.java").verifyIssues();
    }

    @Test
    public void testMissingEqCondition() {
        LambdaUpdateWrapperEqConditionCheck check = new LambdaUpdateWrapperEqConditionCheck();
        CheckVerifier.newVerifier().onFile("src/test/files/ScheduleService.java").withCheck(
                check).verifyIssues();
    }

    @Test
    public void testMissingEqCondition2() {
        LambdaUpdateWrapperEqConditionCheck check = new LambdaUpdateWrapperEqConditionCheck();
        CheckVerifier.newVerifier().onFile(
                "src/test/files/LambdaUpdateWrapperEqConditionCheck.java").withCheck(check).verifyIssues();
    }

    @Test
    public void testWithEqCondition() {
        LambdaUpdateWrapperEqConditionCheck check = new LambdaUpdateWrapperEqConditionCheck();
        CheckVerifier.newVerifier().onFile(
                "src/test/files/LambdaUpdateWrapperEqConditionCheckWithEq.java").withCheck(
                check).verifyNoIssues();
    }

    @Test
    public void testMissingWhereCondition() {
        UpdateWithoutConditionCheck check = new UpdateWithoutConditionCheck();
        CheckVerifier.newVerifier().onFile("src/test/files/TestClass.java").withCheck(
                check).verifyIssues();
    }

    @Test
    public void testMissingWhereCondition4delete() {
        DeleteWithoutConditionCheck check = new DeleteWithoutConditionCheck();
        CheckVerifier.newVerifier().onFile("src/test/files/TestClass.java").withCheck(
                check).verifyIssues();
    }
}
