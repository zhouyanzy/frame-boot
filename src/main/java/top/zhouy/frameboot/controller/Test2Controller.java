package top.zhouy.frameboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zhouy.frameboot.model.User;
import top.zhouy.frameboot.service.UserService;

@RestController
@RequestMapping("/admin")
public class Test2Controller {
    @Autowired
    private UserService userService;

    @GetMapping("/getUserAdmin")
    public User getUser(Integer id){
        return userService.selUserById(id);
    }

}
