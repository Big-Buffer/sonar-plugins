import org.springframework.beans.factory.annotation.Autowired;
import sonar.rules.LambdaUpdateWrapperEqConditionCheck;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

class User {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

@Mapper
interface UserMapper extends BaseMapper<User> {
}

public class TestClass {
    @Autowired
    private UserMapper userMapper;

    public void testMethod() {
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(User::getName, "newName"); // 缺少eq条件
        userMapper.update(updateWrapper); // 这里应该触发规则
    }
}