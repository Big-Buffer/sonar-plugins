import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

public class TestClass {
    public void testMethod() {
        LambdaUpdateWrapper<Object> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(Object::toString, "value"); // 缺少eq条件
        // 调用update方法
        update(updateWrapper);
    }

    private void update(LambdaUpdateWrapper<Object> updateWrapper) {
        // 模拟update操作
    }
}