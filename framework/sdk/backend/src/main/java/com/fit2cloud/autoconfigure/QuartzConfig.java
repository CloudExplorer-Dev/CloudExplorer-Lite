package com.fit2cloud.autoconfigure;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties(
        prefix = "quartz",
        ignoreUnknownFields = true
)
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

    //自定义配置
    @Bean
    public SchedulerFactoryBeanCustomizer schedulerFactoryBeanCustomizer() {
        return schedulerFactoryBean -> {
            schedulerFactoryBean.setBeanName(schedulerName);
        };
    }


}
