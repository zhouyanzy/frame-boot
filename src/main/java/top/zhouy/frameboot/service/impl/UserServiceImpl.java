package top.zhouy.frameboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zhouy.frameboot.mapper.UserMapper;
import top.zhouy.frameboot.model.User;
import top.zhouy.frameboot.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean saveUser(User user) {
        return userMapper.insert(user) > 0 ? true : false;
    }
}
