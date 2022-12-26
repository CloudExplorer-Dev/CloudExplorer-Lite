package com.fit2cloud.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.IdentifierGeneratorAutoConfiguration;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fit2cloud.base.entity.JobRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;

@AutoConfigureBefore(IdentifierGeneratorAutoConfiguration.class)
public class MyBatisPlusConfig {

    @Bean
    public IdentifierGenerator idGenerator() {
        return new CustomIdGenerator();
    }

    public static class CustomIdGenerator extends DefaultIdentifierGenerator {

        @Override
        public String nextUUID(Object entity) {
            //针对任务进行ID的处理
            if (StringUtils.equals(entity.getClass().getName(), JobRecord.class.getName())) {
                return "task-" + IdWorker.getTimeId();
            }
            return super.nextUUID(entity);
        }
    }
}
