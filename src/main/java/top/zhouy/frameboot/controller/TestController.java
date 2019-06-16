package top.zhouy.frameboot.controller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zhouy.frameboot.model.User;
import top.zhouy.frameboot.service.UserService;

/**
 * 测试Controller
 * @author zy
 */
@Api(description = "用户接口")
@RestController
public class TestController {

    @Autowired
    private UserService userService;

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @ApiOperation("根据主键id，获取用户信息")
    @ApiImplicitParam(name = "id", value = "主键id", paramType = "query" , dataType = "Integer")
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数没填好"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    @GetMapping("/getUser")
    public User getUser(@ApiParam(value = "主键id") Integer id){
        log.info("查询用户开始");
        return userService.selUserById(id);
    }

    @ApiOperation("根据主键id，获取用户信息")
    @PostMapping("/saveUser")
    public Boolean saveUser(@ApiParam User user){
        return userService.saveUser(user);
    }
}
