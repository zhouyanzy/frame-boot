package top.zhouy.frameboot.util;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author zhouYan
 * @date 2019/7/1 17:57
 */
@Service
public class RocketmqTest1Listener implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(RocketmqTest1Listener.class);

    /**
     * 由于消费者是多线程的，所以对象要用static+set注入，把对象的级别提升到进程，
     * 这样多个线程就可以共用，但是无法调用父类的方法和变量
     */
//	protected static TestDao testDao;

//    @Resource
//    public void setTestDao(TestDao testDao){
//    	RocketmqTest1Listener.testDao=testDao;//加入持久层dao，可根据需求自行修改
//    }

    @Override
    public Action consume(Message message, ConsumeContext context) {
        try {
            Long startTime = System.currentTimeMillis();

            byte[] body = message.getBody();
            String msg = new String(body);//获取到接收的消息，由于接收到的是byte数组，所以需要转换成字符串

            //TODO 业务逻辑，自行设计
            //testDao.insertDatas();//持久层，这里不再展述，自行补全
            logger.info(msg);

            Long endTime = System.currentTimeMillis();
            System.out.println("单次消费耗时："+(endTime-startTime)/1000);
        } catch (Exception e) {
            logger.error("MessageListener.consume error:" + e.getMessage(), e);
        }

        logger.info("MessageListener.Receive message");
        //如果想测试消息重投的功能,可以将Action.CommitMessage 替换成Action.ReconsumeLater
        return Action.CommitMessage;
    }
}
