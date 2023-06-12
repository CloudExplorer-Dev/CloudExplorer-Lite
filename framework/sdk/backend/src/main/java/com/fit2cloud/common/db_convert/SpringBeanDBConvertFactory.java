package com.fit2cloud.common.db_convert;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fit2cloud.autoconfigure.ServerInfo;
import com.fit2cloud.base.entity.DataConvertVersion;
import com.fit2cloud.base.service.IBaseDataConvertVersionService;
import com.fit2cloud.common.utils.ClassScanUtil;
import com.fit2cloud.common.utils.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/17  10:47}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Component
public class SpringBeanDBConvertFactory implements CommandLineRunner {
    @Resource
    private ConfigurableApplicationContext context;
    @Resource
    private IBaseDataConvertVersionService convertVersionService;
    @Resource
    private ServerInfo serverInfo;

    /**
     * 退出程序
     *
     * @param context 应用程序上下文
     */
    private static void exitApplication(ApplicationContext context) {
        int exitCode = SpringApplication.exit(context);
        System.exit(exitCode);
    }


    @Override
    public void run(String... args) throws Exception {
        try {
            List<DataConvertVersion> dataConvertVersions = convertVersionService.list();
            List<Class<?>> classList = ClassScanUtil.getClassList("com.fit2cloud");
            List<DBConvert> dbConverts = classList.stream().filter(DBConvert.class::isAssignableFrom)
                    .filter(clazz -> dataConvertVersions.stream().noneMatch(dataConvertVersion ->
                            StringUtils.equals(dataConvertVersion.getModuleName(), serverInfo.getModule())
                                    &&
                                    StringUtils.equals(dataConvertVersion.getConvertClassName(), clazz.getName())
                                    &&
                                    dataConvertVersion.isSuccess())).map(clazz -> {
                        try {
                            return (DBConvert) clazz.getConstructor().newInstance();
                        } catch (Exception e) {
                            return null;
                        }
                    }).filter(Objects::nonNull).toList();

            for (DBConvert dbConvert : dbConverts) {
                //1.获取事务控制管理器
                DataSourceTransactionManager transactionManager = SpringUtil.getBean(DataSourceTransactionManager.class);
                DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                //3.设置事务隔离级别，开启新事务
                def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                //4.获得事务状态
                TransactionStatus status = transactionManager.getTransaction(def);
                try {
                    dbConvert.execute();
                    // 插入数据
                    convertVersionService.saveOrUpdate(getDataConvertVersion(dbConvert), new LambdaQueryWrapper<
                            DataConvertVersion>()
                            .eq(DataConvertVersion::getConvertClassName, dbConvert.getClass().getName())
                            .eq(DataConvertVersion::getModuleName, serverInfo.getModule()));
                    // 提交事务
                    transactionManager.commit(status);
                } catch (Exception e) {
                    // 插入数据
                    DataConvertVersion dataConvertVersion = getDataConvertVersion(dbConvert);
                    dataConvertVersion.setSuccess(false);
                    convertVersionService.saveOrUpdate(dataConvertVersion, new LambdaQueryWrapper<
                            DataConvertVersion>()
                            .eq(DataConvertVersion::getConvertClassName, dbConvert.getClass().getName())
                            .eq(DataConvertVersion::getModuleName, serverInfo.getModule()));
                    // 出现异常直接回滚 异常往上抛
                    transactionManager.rollback(status);
                    throw e;
                }
            }

        } catch (Exception e) {
            // 执行器出现异常 打印日志,退出程序
            e.printStackTrace();
            exitApplication(context);
        }
    }

    public DataConvertVersion getDataConvertVersion(DBConvert dbConvert) {
        DataConvertVersion dataConvertVersion = new DataConvertVersion();
        dataConvertVersion.setSuccess(true);
        dataConvertVersion.setConvertClassName(dbConvert.getClass().getName());
        dataConvertVersion.setModuleName(serverInfo.getModule());
        return dataConvertVersion;
    }

}
