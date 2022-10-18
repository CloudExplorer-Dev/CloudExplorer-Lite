package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.base.entity.CloudAccount;
import com.fit2cloud.base.entity.JobRecord;
import com.fit2cloud.base.service.IBaseCloudAccountService;
import com.fit2cloud.base.service.IBaseJobRecordService;
import com.fit2cloud.common.constants.JobStatusConstants;
import com.fit2cloud.common.constants.PlatformConstants;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.provider.exception.SkipPageException;
import com.fit2cloud.common.provider.util.CommonUtil;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Consumer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2022/10/18  4:19 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Service
public abstract class BaseSyncService {
    @Resource
    protected RedissonClient redissonClient;
    @Resource
    protected IBaseCloudAccountService cloudAccountService;
    @Resource
    protected IBaseJobRecordService baseJobRecordService;

    /**
     * 代理同步数据
     *
     * @param cloudAccountId    云账号id
     * @param jobDescription    定时任务描述
     * @param getCloudProvider  获取定时任务执行器
     * @param initJobRecord     初始化任务记录
     * @param execMethod        执行函数
     * @param getExecMethodArgs 获取执行函数参数
     * @param saveBatchOrUpdate 插入或者修改数据
     * @param writeJobRecord    写入任务记录参数
     * @param remote            云账号不存在的时候 删除云账号资源
     * @param <T>               同步数据泛型
     * @param <P>               执行器泛型
     */
    protected <T, P> void proxy(String cloudAccountId,
                                String jobDescription,
                                Function<String, Class<? extends P>> getCloudProvider,
                                Function<LocalDateTime, JobRecord> initJobRecord,
                                BiFunction<P, String, List<T>> execMethod,
                                Function<CloudAccount, String> getExecMethodArgs,
                                Consumer<SaveBatchOrUpdateParams<T>> saveBatchOrUpdate,
                                Consumer<SaveBatchOrUpdateParams<T>> writeJobRecord,
                                Runnable remote) {
        RLock lock = redissonClient.getLock(cloudAccountId + jobDescription);
        try {
            if (lock.tryLock()) {
                CloudAccount cloudAccount = cloudAccountService.getById(cloudAccountId);
                if (Objects.nonNull(cloudAccount)) {
                    LocalDateTime syncTime = getSyncTime();
                    // 初始化一条定时任务记录
                    JobRecord jobRecord = initJobRecord.apply(syncTime);
                    Class<? extends P> cloudProvider = getCloudProvider.apply(cloudAccount.getPlatform());
                    try {
                        // 同步数据
                        List<T> syncRecord = CommonUtil.exec(cloudProvider, getExecMethodArgs.apply(cloudAccount), execMethod);
                        SaveBatchOrUpdateParams<T> tSaveBatchOrUpdateParams = new SaveBatchOrUpdateParams<>(cloudAccountId, syncTime, null, syncRecord, jobRecord);
                        // 插入并且更新数据
                        saveBatchOrUpdate.accept(tSaveBatchOrUpdateParams);
                        // 记录同步日志
                        writeJobRecord.accept(tSaveBatchOrUpdateParams);
                        // 修改同步状态为成功
                        baseJobRecordService.update(new LambdaUpdateWrapper<JobRecord>().eq(JobRecord::getId, jobRecord.getId()).set(JobRecord::getStatus, JobStatusConstants.SUCCESS));
                    } catch (Throwable e) {
                        e.printStackTrace();
                        baseJobRecordService.update(new LambdaUpdateWrapper<JobRecord>().eq(JobRecord::getId, jobRecord.getId()).set(JobRecord::getStatus, JobStatusConstants.FAILED));
                    }
                } else {
                    // 删除云账号相关的资源
                    remote.run();
                    // 删除定时任务
                    cloudAccountService.deleteJobByCloudAccountId(cloudAccountId);
                }
            }
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    /**
     * @param cloudAccountId    云账号id
     * @param regions           区域
     * @param jobDescription    任务描述
     * @param getCloudProvider  获取任务处理器
     * @param initJobRecord     初始化任务记录
     * @param execMethod        执行定时任务函数
     * @param getExecMethodArgs 任务执行参数
     * @param saveBatchOrUpdate 插入或者更新同步数据
     * @param writeJobRecord    写入区域任务记录
     * @param remote            删除相关资源
     * @param <T>               同步记录泛型
     * @param <P>               执行处理器
     */
    protected <T, P> void proxy(String cloudAccountId,
                                List<Credential.Region> regions,
                                String jobDescription,
                                Function<String, Class<? extends P>> getCloudProvider,
                                Function<LocalDateTime, JobRecord> initJobRecord,
                                BiFunction<P, String, List<T>> execMethod,
                                BiFunction<CloudAccount, Credential.Region, String> getExecMethodArgs,
                                Consumer<SaveBatchOrUpdateParams<T>> saveBatchOrUpdate,
                                Consumer<SaveBatchOrUpdateParams<T>> writeJobRecord,
                                Runnable remote) {
        RLock lock = redissonClient.getLock(cloudAccountId + jobDescription);
        try {
            if (lock.tryLock()) {
                CloudAccount cloudAccount = cloudAccountService.getById(cloudAccountId);
                if (Objects.nonNull(cloudAccount)) {
                    LocalDateTime syncTime = getSyncTime();
                    // 初始化一条定时任务记录
                    JobRecord jobRecord = initJobRecord.apply(syncTime);
                    Class<? extends P> cloudProvider = getCloudProvider.apply(cloudAccount.getPlatform());
                    try {
                        for (Credential.Region region : regions) {
                            try {
                                // 同步数据
                                List<T> syncRecord = CommonUtil.exec(cloudProvider, getExecMethodArgs.apply(cloudAccount, region), execMethod);
                                SaveBatchOrUpdateParams<T> tSaveBatchOrUpdateParams = new SaveBatchOrUpdateParams<>(cloudAccountId, syncTime, region, syncRecord, jobRecord);
                                // 插入并且更新数据
                                saveBatchOrUpdate.accept(tSaveBatchOrUpdateParams);
                                // 记录同步日志
                                writeJobRecord.accept(tSaveBatchOrUpdateParams);
                            } catch (SkipPageException ignored) { // 如果发生跳过异常,那么就不同步当前区域
                                try {
                                    writeJobRecord.accept(new SaveBatchOrUpdateParams<>(cloudAccountId, syncTime, region, new ArrayList<>(), jobRecord));
                                } catch (Throwable e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        // 修改同步状态为成功
                        baseJobRecordService.update(new LambdaUpdateWrapper<JobRecord>().eq(JobRecord::getId, jobRecord.getId()).set(JobRecord::getStatus, JobStatusConstants.SUCCESS));
                    } catch (Throwable e) {
                        e.printStackTrace();
                        baseJobRecordService.update(new LambdaUpdateWrapper<JobRecord>().eq(JobRecord::getId, jobRecord.getId()).set(JobRecord::getStatus, JobStatusConstants.FAILED));
                    }
                } else {
                    // 删除云账号相关的资源
                    remote.run();
                    // 删除定时任务
                    cloudAccountService.deleteJobByCloudAccountId(cloudAccountId);
                }
            }
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }

    }

    /**
     * 批量插入并且逻辑删除
     *
     * @param service             服务
     * @param dataList            需要插入的数据
     * @param getUpdateWrapper    获取更新的mapper
     * @param updateDeleteWarpper 删除mapper
     * @param <T>                 数据泛型
     */
    protected <T> void saveBatchOrUpdate(IService<T> service, List<T> dataList, Function<T, Wrapper<T>> getUpdateWrapper, Wrapper<T> updateDeleteWarpper) {
        for (T entity : dataList) {
            Wrapper<T> updateWrapper = getUpdateWrapper.apply(entity);
            // 插入或者更新数据
            service.saveOrUpdate(entity, updateWrapper);
        }
        // 删除数据,因为是逻辑删除所以更新status字段
        service.update(updateDeleteWarpper);
    }

    /**
     * 根据云账号获取区域
     *
     * @param accountId 云账号id
     * @return 云账号区域
     */
    protected List<Credential.Region> getRegions(String accountId) {
        CloudAccount cloudAccount = cloudAccountService.getById(accountId);
        return Arrays.stream(PlatformConstants.values()).filter(platformConstants -> platformConstants.name().equals(cloudAccount.getPlatform())).findFirst().map(platformConstants -> {
            try {
                return platformConstants.getCredentialClass().getConstructor().newInstance().deCode(cloudAccount.getCredential()).regions();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new Fit2cloudException(10001, "获取区域错误");
            }
        }).orElseThrow(() -> new Fit2cloudException(10001, "获取区域错误"));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SaveBatchOrUpdateParams<T> {
        /**
         * 云账户id
         */
        private String cloudAccountId;
        /**
         * 更新时间
         */
        private LocalDateTime syncTime;
        /**
         * 区域
         */
        private Credential.Region region;
        /**
         * 数据
         */
        private List<T> syncRecord;
        /**
         * 任务记录
         */
        private JobRecord jobRecord;
    }

    /**
     * 更新的时候精确到秒 因为数据插入也是精确到秒
     *
     * @return 更新时间
     */
    protected LocalDateTime getSyncTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(now.format(dateTimeFormatter), dateTimeFormatter);
    }


}
