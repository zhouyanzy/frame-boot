package top.zhouy.frameboot.controller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import top.zhouy.frameboot.model.Product;
import top.zhouy.frameboot.model.User;
import top.zhouy.frameboot.repository.ProductRepository;
import top.zhouy.frameboot.service.UserService;
import top.zhouy.frameboot.util.RabbitmqSendUtil;
import top.zhouy.frameboot.util.RedisUtil;
import top.zhouy.frameboot.util.RocketmqProducerService;

import java.util.List;
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

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RabbitmqSendUtil rabbitmqSendUtil;

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

    @ApiOperation(value = "保存用户信息")
    @PostMapping("/saveUser")
    public Boolean saveUser(@ModelAttribute User user){
        return userService.saveUser(user);
    }

    @ApiOperation("保存商品信息")
    @PostMapping("/saveProduct")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productName", value = "商品名称", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "marketPrice", value = "价格", required = true, dataType = "BigDecimal", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "商品id", required = true, dataType = "Integer", paramType = "query")
    })
    public Boolean saveProduct(Product product){
        productRepository.index(product);
        return true;
    }

    @ApiOperation("根据主键id，获取用户信息")
    @ApiImplicitParam(value = "商品名称", name = "name", paramType = "query", dataType = "String")
    @PostMapping("/selProduct")
    public List<Product> selProduct(String name){
        return productRepository.findByProductNameLike(name);
    }

    @ApiOperation("发送mq消息")
    @PostMapping("/sendMSG")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "内容", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "exchange", value = "交换机", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "routingKey", value = "路由", required = false, dataType = "String", paramType = "query")
    })
    public Boolean sendMSG(String content, String exchange, String routingKey){
        rabbitmqSendUtil.sendMsg(content, exchange, routingKey);
        return true;
    }

    @Autowired
    RocketmqProducerService rocketmqProducerService;

    @ApiOperation("发送mq消息，rocketMq")
    @PostMapping("/sendMSGRocketMq")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "内容", required = true, dataType = "String", paramType = "query")
    })
    public Boolean sendMSGRocketMq(String content){
        rocketmqProducerService.sendMsg(content);
        return true;
    }
}
