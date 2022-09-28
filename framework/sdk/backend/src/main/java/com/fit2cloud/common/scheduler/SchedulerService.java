package com.fit2cloud.common.scheduler;

import com.fit2cloud.common.scheduler.entity.QuzrtzJobDetail;
import org.quartz.*;

import java.util.List;
import java.util.Map;

/**
 * @Author:张少虎
 * @Date: 2022/9/8  4:03 PM
 * @Version 1.0
 * @注释:
 */
public interface SchedulerService {

    /**
     * @param jobHandler  任务处理器
     * @param jobName     任务名称
     * @param groupName   分组名称
     * @param description 描述
     * @param cronExp     cron表达式
     * @param param       执行任务需要的参数
     */
    void addJob(Class<? extends Job> jobHandler, String jobName, String groupName, String description, String cronExp, Map<String, Object> param);


    /**
     * 指定从当前时间开始每隔多长时间掉用一次定时任务
     *
     * @param jobHandler  任务处理器
     * @param jobName     任务名称
     * @param groupName   分组名称
     * @param description 描述
     * @param amount      每个多长时间
     * @param unit        时间单位
     * @param param       参数
     */
    void addJob(Class<? extends Job> jobHandler, String jobName, String groupName, String description, int amount, int unit, Map<String, Object> param);

    /**
     * 当前方法适用于 设置每周中那几天 中哪些时间段,每隔多长时间执行多少次
     *
     * @param jobHandler   执行处理器
     * @param jobName      任务名称
     * @param groupName    分组名称
     * @param description  描述
     * @param param        参数
     * @param startTimeDay 每天的开始时间
     * @param endTimeDay   每天的结束时间
     * @param timeInterval 同步间隔
     * @param unit         同步间隔单位
     * @param repeatCount  执行多少次
     * @param weeks        每周的那几天执行
     */
    void addJob(Class<? extends Job> jobHandler, String jobName, String groupName, String description, Map<String, Object> param, TimeOfDay startTimeDay, TimeOfDay endTimeDay, int timeInterval, DateBuilder.IntervalUnit unit, int repeatCount, Integer... weeks);


    /**
     * 间隔时间可设置,年 月 日 时 分 秒 不需要计算闰年 问题
     *
     * @param jobHandler   处理器
     * @param jobName      任务名称
     * @param groupName    分组名称
     * @param description  描述
     * @param param        参数
     * @param timeInterval 间隔时间
     * @param unit         间隔时间单位
     */
    void addJob(Class<? extends Job> jobHandler, String jobName, String groupName, String description, Map<String, Object> param, int timeInterval, DateBuilder.IntervalUnit unit);

    /**
     * @param jobHandler      执行处理器
     * @param jobName         任务名称
     * @param groupName       分组名称
     * @param description     描述
     * @param scheduleBuilder 任务构建器
     * @param param           参数
     */
    void addJob(Class<? extends Job> jobHandler, String jobName, String groupName, String description, ScheduleBuilder scheduleBuilder, Map<String, Object> param);

    /**
     * 跟新定时任务
     *
     * @param jobName      定时任务Name
     * @param groupName    定时任务groupName
     * @param description  描述
     * @param param        参数
     * @param startTimeDay 每一天的开始时间
     * @param endTimeDay   每一天的结束时间
     * @param timeInterval 时间间隔
     * @param unit         时间间隔单位
     * @param repeatCount  重复多少次
     * @param weeks        每周那几天
     */
    void updateJob(String jobName, String groupName, String description, Map<String, Object> param, TimeOfDay startTimeDay, TimeOfDay endTimeDay, int timeInterval, DateBuilder.IntervalUnit unit, int repeatCount, Trigger.TriggerState triggerState, Integer... weeks);

    /**
     * 获取任务详情
     *
     * @param jobName   任务名称
     * @param groupName 分组名称
     * @return 任务详情
     */
    QuzrtzJobDetail getJobDetails(String jobName, String groupName);

    /**
     * 是否存在当前jobDetails
     *
     * @param jobName   任务名称
     * @param groupName 任务分组
     * @return
     */
    boolean inclusionJobDetails(String jobName, String groupName);

    /**
     * 获取所有任务
     *
     * @return 所有任务
     */
    List<QuzrtzJobDetail> list();

    /**
     * 根据任务组 获取定时任务
     *
     * @param groupName 任务组名称
     * @return 任务
     */
    List<QuzrtzJobDetail> list(String groupName);

    /**
     * 暂停任务
     *
     * @param jobName   任务名称
     * @param groupName 分组名称
     */
    void pauseJob(String jobName, String groupName);

    /**
     * 恢复任务
     *
     * @param jobName   任务名称
     * @param groupName 分组名称
     */
    void resumeJob(String jobName, String groupName);

    /**
     * 删除任务
     *
     * @param jobName   任务名称
     * @param groupName 分组名称
     */
    void deleteJob(String jobName, String groupName);

    /**
     * 启动所有任务
     */
    void startAllJobs();

    /**
     * 暂停所有任务
     */
    void pauseAllJobs();

    /**
     * 恢复所有任务
     */
    void resumeAllJobs();

    /**
     * 关闭所有任务
     */
    void shutdownAllJobs();

}
