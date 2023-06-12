package com.fit2cloud.service;

import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.JobRecord;
import com.fit2cloud.common.job.context.JobContext;
import com.fit2cloud.common.job.result.Result;
import com.fit2cloud.common.job.step.JobStep;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.constants.SyncDimensionConstants;
import com.fit2cloud.es.entity.ResourceInstance;
import lombok.Data;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/12  19:51}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Resource
public interface IJobStepService {

    /**
     * 校验是否支持资源
     *
     * @param context 上下文
     * @param step    执行步骤
     * @return 任务返回值
     */
    Result checkSupportResource(JobContext<Context> context, JobStep<Context> step);

    /**
     * 校验云账号
     *
     * @param context 上下文
     * @param step    执行步骤
     * @return 任务返回值记录任务状态以及数据
     */
    Result checkCloudAccount(JobContext<Context> context, JobStep<Context> step);

    /**
     * 同步资源 -> 生成子任务
     *
     * @param context 任务执行上下文
     * @param step    任务步骤
     * @return 任务返回值记录任务状态以及数据
     */
    Result syncResource(JobContext<Context> context, JobStep<Context> step);

    /**
     * 同步资源子任务 -> 真正同步资源
     *
     * @param context 任务上下文
     * @param step    任务步骤
     * @return 任务返回值记录任务状态以及数据
     */
    Result syncResourceChildren(JobContext<Context> context, JobStep<Context> step);

    /**
     * 插入数据
     *
     * @param context 任务执行上下文
     * @param step    任务步骤
     * @return 任务返回值记录任务状态以及数据
     */
    Result saveOrUpdateData(JobContext<Context> context, JobStep<Context> step);


    /**
     * 扫描
     *
     * @param context 任务执行上下文
     * @param step    任务步骤
     * @return 任务返回值记录任务状态以及数据
     */
    Result scan(JobContext<Context> context, JobStep<Context> step);

    /**
     * 更新扫描时间
     *
     * @param context 上下文对象
     * @param step    任务步骤对象
     * @return 任务返回值记录任务状态以及数据
     */
    Result updateScanTime(JobContext<Context> context, JobStep<Context> step);

    /**
     * 删除不存在的
     *
     * @param context 上下文
     * @param step    步骤
     * @return 任务返回值
     */
    Result deleteNotFountCloudAccountData(JobContext<Context> context, JobStep<Context> step);

    class Context {

        public String cloudAccountId;
        /**
         * 云账号
         */
        public CloudAccount cloudAccount;

        /**
         * 资源类型
         */
        public ResourceTypeConstants instanceType;

        /**
         * 同步粒度
         */
        public SyncDimensionConstants syncDimensionConstants;

        /**
         * 认证信息
         */
        public Credential credential;

        /**
         * 同步记录
         */
        public JobRecord jobRecord;

        /**
         * 同步时间
         */
        public LocalDateTime syncTime;

        /**
         * 数据
         */
        public List<ResourceInstance> resourceInstances = new ArrayList<>();
    }

    @Data
    static class StepResultData {
        /**
         * 同步数据
         */
        int size;
        /**
         * 区域
         */
        Credential.Region region;

        private StepResultData(int size, Credential.Region region) {
            this.size = size;
            this.region = region;
        }

        public static StepResultData of(int size, Credential.Region region) {
            return new StepResultData(size, region);

        }
    }
}
