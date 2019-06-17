package top.zhouy.frameboot.controller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.zhouy.frameboot.model.User;
import top.zhouy.frameboot.service.UserService;
import top.zhouy.frameboot.util.RedisUtil;

import java.util.concurrent.TimeUnit;

/**
 * 测试Controller
 * @author zy
 */
@Api(description = "用户接口")
@RestController
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @ApiOperation("根据主键id，获取用户信息")
    @ApiImplicitParam(name = "id", value = "主键id", paramType = "query" , dataType = "Integer")
    @ApiResponses({
            @ApiResponse(code = 400, message = "请求参数没填好"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    @Cacheable(value = "user", key = "#id", unless = "#result==null")
    @GetMapping("/getUser")
    public User getUser(@ApiParam(value = "主键id") Integer id){
        log.info("查询用户开始");
        return userService.selUserById(id);
    }

    @ApiOperation("往redis中设置值")
    @ApiImplicitParams({@ApiImplicitParam(value = "key", name = "key", paramType = "query", dataType = "String"),
            @ApiImplicitParam(value = "值", name = "value", paramType = "query", dataType = "String")})
    @PostMapping("/setValue")
    public Boolean setValue(String key, String value){
        log.info("往redis中设置值");
        redisUtil.setEx(key, value, 10, TimeUnit.MINUTES);
        return true;
    }

    @ApiOperation("从redis中取值")
    @ApiImplicitParam(value = "值", name = "key", paramType = "query", dataType = "String")
    @PostMapping("/getValue")
    public String getValue(String key){
        log.info("从redis中取值");
        return redisUtil.get(key);
    }

    @ApiOperation("根据主键id，获取用户信息")
    @PostMapping("/saveUser")
    public Boolean saveUser(@ApiParam User user){
        return userService.saveUser(user);
    }
}
