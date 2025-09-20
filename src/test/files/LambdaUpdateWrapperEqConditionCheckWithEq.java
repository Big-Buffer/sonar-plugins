import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

public class TestClass {
    public void testMethod() {
        LambdaUpdateWrapper<Object> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Object::toString, "condition").set(Object::toString, "value"); // 有eq条件
        update(updateWrapper);
    }

    private void update(LambdaUpdateWrapper<Object> updateWrapper) {
        // 模拟update操作
    }
}