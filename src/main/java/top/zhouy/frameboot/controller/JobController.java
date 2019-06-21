package top.zhouy.frameboot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.*;
import top.zhouy.frameboot.task.BaseJob;

import java.util.HashMap;
import java.util.Map;

/**
 * jobController
 * @author zhouYan
 * @date 2019/6/20 17:58
 */
@Api(description = "job")
@RestController
@RequestMapping(value="/job")
public class JobController {

    /**
     * 加入Qualifier注解，通过名称注入bean
     */
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    private static Logger log = LoggerFactory.getLogger(JobController.class);


    @ApiOperation("添加job")
    @PostMapping(value="/addJob")
    public void addJob(@RequestParam(value="jobClassName")String jobClassName,
                       @RequestParam(value="jobGroupName")String jobGroupName,
                       @RequestParam(value="cronExpression")String cronExpression) throws Exception{
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        // 启动调度器
        scheduler.start();

        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass()).withIdentity(jobClassName, jobGroupName).build();

        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroupName)
                .withSchedule(scheduleBuilder).build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            System.out.println("创建定时任务失败"+e);
            throw new Exception("创建定时任务失败");
        }
    }

    @ApiOperation("暂停job")
    @PostMapping(value="/pauseJob")
    public void pauseJob(@RequestParam(value="jobClassName")String jobClassName,
                         @RequestParam(value="jobGroupName")String jobGroupName) throws Exception{
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
    }

    @ApiOperation("继续job")
    @PostMapping(value="/resumeJob")
    public void resumeJob(@RequestParam(value="jobClassName")String jobClassName, @RequestParam(value="jobGroupName")String jobGroupName) throws Exception {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
    }


    @ApiOperation("修改job")
    @PostMapping(value="/rescheduleJob")
    public void rescheduleJob(@RequestParam(value="jobClassName")String jobClassName,
                              @RequestParam(value="jobGroupName")String jobGroupName,
                              @RequestParam(value="cronExpression")String cronExpression) throws Exception {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroupName);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            System.out.println("更新定时任务失败"+e);
            throw new Exception("更新定时任务失败");
        }
    }

    @ApiOperation("删除job")
    @PostMapping(value="/deleteJob")
    public void deleteJob(@RequestParam(value="jobClassName")String jobClassName, @RequestParam(value="jobGroupName")String jobGroupName) throws Exception {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
        scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
    }

    @ApiOperation("查询job")
    @GetMapping(value="/queryJob")
    public Map<String, Object> queryJob(@RequestParam(value="pageNum")Integer pageNum, @RequestParam(value="pageSize")Integer pageSize) {
        // TODO 将设置的任务存入数据库并取出来展示。
        Map map = new HashMap();
        return map;
    }

    public static BaseJob getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (BaseJob)class1.newInstance();
    }

}
