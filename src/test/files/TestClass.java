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

    public void updateLotPLocation(long PLocationRrn, String locationType, long lotRrn) {
        String sql = "UPDATE LOT_EXT L SET PLOCATION = ?, PLOCATION_TYPE = ?  WHERE L.LOT_RRN = ?";
        this.jdbcTemplate.update(sql, new Object[]{PLocationRrn, locationType, lotRrn});
    }

    public void updateLotPLocation2(long PLocationRrn, String locationType, long lotRrn) {
        String sql = "UPDATE LOT_EXT L SET PLOCATION = ?, PLOCATION_TYPE = ?";
        this.jdbcTemplate.update(sql, new Object[]{PLocationRrn, locationType, lotRrn});
    }

    public void deleteLotPLocation(long lotRrn) {
        String sql = "delete from LOT_EXT L WHERE L.LOT_RRN = ?";
        this.jdbcTemplate.update(sql, new Object[]{lotRrn});
    }

    public void deleteLotPLocation2(long lotRrn) {
        String sql = "DELETE from LOT_EXT L ";
        this.jdbcTemplate.update(sql);
    }
}