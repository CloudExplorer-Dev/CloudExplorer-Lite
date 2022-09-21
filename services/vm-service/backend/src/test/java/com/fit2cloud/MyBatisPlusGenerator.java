package com.fit2cloud;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = VmServiceApplication.class)
@TestPropertySource(locations = {
        "classpath:commons.properties",
        "file:${ce.config.file}"
})
public class MyBatisPlusGenerator {

    /**
     * 传入需要生成代码的表名
     */
    private static final List<String> TABLES = Arrays.asList("vm_cloud_disk","vm_cloud_image","vm_cloud_server");

    private static final Map<String, Object> CUSTOM_MAP = new HashMap<>();

    static {
        Map<String, Map<String, Map<String, Object>>> map = new HashMap<>();
        //指定需要生成enum的字段（Enum类需要自己生成）
        map.put("cloud_account", convert(Arrays.asList(
                new EnumCreator().setEnumField("status").setEnumClassName("CloudAccountConstants.Status").setEnumClass("com.fit2cloud.constants.CloudAccountConstants")
        )));

        CUSTOM_MAP.put("useEnum", true);
        CUSTOM_MAP.put("useEnumMap", map);
    }

    @Value("${ce.datasource.url}")
    private String datasource;

    @Value("${ce.datasource.username}")
    private String username;

    @Value("${ce.datasource.password}")
    private String password;

    private static final GlobalConfig GLOBAL_CONFIG = new GlobalConfig.Builder()
            //.fileOverride()
            .outputDir("./src/main/java/")
            .author("fit2cloud")
            //.enableSwagger()
            .disableOpenDir()
            .commentDate("") //避免生成的表仅有注释中日期变化
            .build();

    private static final StrategyConfig STRATEGY_CONFIG = new StrategyConfig.Builder()
            .addInclude(TABLES) //需要生成的表名
            //mapper
            .mapperBuilder()
            .formatMapperFileName("%sMapper")
            .formatMapperFileName("%sMapper")
            //entity
            .entityBuilder().enableFileOverride() //仅覆盖entity覆盖文件
            .enableLombok()
            .enableChainModel() //开启链式模型
            .enableTableFieldAnnotation() //开启生成实体时生成字段注解
            //.enableColumnConstant() //开启生成字段常量(就是把表内字段在类里生成常量)
            .idType(IdType.ASSIGN_UUID) //自动分配怒UUID
            .build();

    private static final PackageConfig PACKAGE_CONFIG = new PackageConfig.Builder()
            .parent("com.fit2cloud")
            .entity("dao.entity")
            .mapper("dao.mapper")
            .xml("dao.mapper")
            .service("service")
            .build();

    private static final TemplateConfig TEMPLATE_CONFIG = new TemplateConfig.Builder()
            .disable(TemplateType.CONTROLLER)
            .entity("/template/entity.java")
            .build();

    private static final InjectionConfig INJECTION_CONFIG = new InjectionConfig.Builder()
            .customMap(CUSTOM_MAP)
            .build();


    @Test
    public void run() {
        System.out.printf("-- datasource: %s%n", datasource);
        System.out.printf("-- username: %s%n", username);
        System.out.printf("-- password: %s%n", password);

        AutoGenerator generator = new AutoGenerator(
                new DataSourceConfig
                        .Builder(datasource, username, password).build()
        );
        // 生成dao层
        generator
                .strategy(STRATEGY_CONFIG)
                .global(GLOBAL_CONFIG)
                .packageInfo(PACKAGE_CONFIG)
                .template(TEMPLATE_CONFIG)
                .injection(INJECTION_CONFIG)
                .execute(new FreemarkerTemplateEngine());

    }

    @Data
    @Accessors(chain = true)
    private static class EnumCreator {
        private String enumField;
        private String enumClassName;
        private String enumClass;

        public Map<String, Object> getMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("enumField", enumField);
            map.put("enumClassName", enumClassName);
            map.put("enumClass", enumClass);
            return map;
        }
    }

    private static Map<String, Map<String, Object>> convert(List<EnumCreator> list) {
        Map<String, Map<String, Object>> map = new HashMap<>();
        for (EnumCreator enumCreator : list) {
            map.put(enumCreator.getEnumField(), enumCreator.getMap());
        }
        return map;
    }
}
