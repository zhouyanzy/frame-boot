package top.zhouy.frameboot.service;

import top.zhouy.frameboot.model.User;

public interface UserService {

    /**
     * 根据主键id查找用户信息
     * @param id 主键id
     * @return
     */
    User selUserById(Integer id);

    /**
     * 保存用户
     * @return
     */
    Boolean saveUser(User user);
}
