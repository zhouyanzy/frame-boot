package top.zhouy.frameboot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.zhouy.frameboot.config.RabbitmqConfiguration;

import java.util.UUID;

/**
 * 消息生产者
 * @author zhouYan
 * @date 2019/6/17 17:55
 */
@Component
public class RabbitmqSendUtil implements RabbitTemplate.ConfirmCallback{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 由于rabbitTemplate的scope属性设置为ConfigurableBeanFactory.SCOPE_PROTOTYPE，所以不能自动注入
     */
    private RabbitTemplate rabbitTemplate;

    /**
     * 构造方法注入rabbitTemplate
     */
    @Autowired
    public RabbitmqSendUtil(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
        rabbitTemplate.setConfirmCallback(this);
    }

    public void sendMsg(String content, String exchange, String routingKey) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        switch (exchange){
            case RabbitmqConfiguration.EXCHANGE_A:
                //把消息放入ROUTINGKEY_A对应的队列当中去，对应的是队列A
                rabbitTemplate.convertAndSend(RabbitmqConfiguration.EXCHANGE_A, routingKey, content, correlationId);
                break;
            case RabbitmqConfiguration.EXCHANGE_B:
                //把消息放入ROUTINGKEY_B对应的队列当中去，对应的是队列A
                rabbitTemplate.convertAndSend(RabbitmqConfiguration.EXCHANGE_B, routingKey, content, correlationId);
                break;
            case RabbitmqConfiguration.FANOUT_EXCHANGE:
                //把消息放入FANOUT_EXCHANGE对应的队列当中去
                rabbitTemplate.convertAndSend(RabbitmqConfiguration.FANOUT_EXCHANGE, "", content, correlationId);
                break;
        }
    }

    /**
     * 回调
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        logger.info(" 回调id:" + correlationData);
        if (ack) {
            logger.info("消息成功消费");
        } else {
            logger.info("消息消费失败:" + cause);
        }
    }
}
