package com.fit2cloud.autoconfigure;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@ConfigurationProperties(
        prefix = "quartz",
        ignoreUnknownFields = true
)
@MapperScan(basePackages = { "com.fit2cloud.common.scheduler.mapper"}, sqlSessionFactoryRef = "quartzSqlSessionFactory")
@Data
public class QuartzConfig {

    private boolean enabled;
    private int threadCount = 10;
    private String schedulerName = "quartzScheduler";

    /* 数据源 */
    @Bean
    @ConfigurationProperties("ce.datasource.quartz")
    public DataSourceProperties quartzDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("ce.datasource.quartz.hikari")
    @QuartzDataSource
    public HikariDataSource secondDataSource(
            @Qualifier("quartzDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "quartzSqlSessionFactory")
    public SqlSessionFactory quartzSqlSessionFactory(@Qualifier("secondDataSource") HikariDataSource secondDataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(secondDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:com/fit2cloud/common/scheduler/mapper/*.xml"));
        return bean.getObject();
    }


    //自定义配置
    @Bean
    public SchedulerFactoryBeanCustomizer schedulerFactoryBeanCustomizer() {
        return schedulerFactoryBean -> {
            schedulerFactoryBean.setStartupDelay(2);
            schedulerFactoryBean.setAutoStartup(true);
            schedulerFactoryBean.setOverwriteExistingJobs(true);
            schedulerFactoryBean.setBeanName(schedulerName);
        };
    }


}
