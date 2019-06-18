package top.zhouy.frameboot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import top.zhouy.frameboot.config.RabbitmqConfiguration;

/**
 * @author zhouYan
 * @date 2019/6/17 18:04
 */
@Component
@RabbitListener(queues = RabbitmqConfiguration.QUEUE_A)
public class MsgRecieverTwo {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RabbitHandler
    public void process(String content) {
        logger.info("接收处理队列A当中的消息： " + content);
    }
}
