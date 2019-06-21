package top.zhouy.frameboot.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author zhouYan
 * @date 2019/6/20 17:55
 */
public interface BaseJob extends Job {

    public void execute(JobExecutionContext context) throws JobExecutionException;

}
