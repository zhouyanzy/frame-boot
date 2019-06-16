package top.zhouy.frameboot.mapper;

import org.apache.ibatis.annotations.Param;
import top.zhouy.frameboot.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 根据手机号查找用户信息
     * @param phone 手机号
     * @return
     */
    User selectByPhone(@Param("phone") String phone);
}