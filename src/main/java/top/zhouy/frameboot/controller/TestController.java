package top.zhouy.frameboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zhouy.frameboot.model.User;
import top.zhouy.frameboot.service.UserService;

/**
 * 测试Controller
 * @author zy
 */
@RestController
public class TestController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUser")
    public User getUser(Integer id){
        return userService.selUserById(id);
    }

}
