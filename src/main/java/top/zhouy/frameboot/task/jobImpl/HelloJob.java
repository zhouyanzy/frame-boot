package top.zhouy.frameboot.task.jobImpl;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zhouy.frameboot.task.BaseJob;

import java.util.Date;

/**
 * @author zhouYan
 * @date 2019/6/20 17:56
 */
public class HelloJob implements BaseJob {

    private static Logger _log = LoggerFactory.getLogger(HelloJob.class);

    public HelloJob() {

    }

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        _log.error("Hello Job执行时间: " + new Date());

    }
}
