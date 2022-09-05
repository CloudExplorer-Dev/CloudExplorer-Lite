import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.fit2cloud.BaseTestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(classes = BaseTestApplication.class)
@TestPropertySource(locations = {
        "classpath:commons.properties",
        "file:${ce.config.file}"
})
public class MyBatisPlusGenerator {

    /**
     * 传入需要生成代码的表名
     */
    private static final List<String> TABLES = Arrays.asList("user");

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
            //.dateType(DateType.ONLY_DATE) //默认生成Date类，而不是LocalDateTime
            .build();

    private static final StrategyConfig STRATEGY_CONFIG = new StrategyConfig.Builder()
            .addInclude(TABLES) //需要生成的表名
            //mapper
            .mapperBuilder()
            .formatMapperFileName("Base%sMapper")
            .formatXmlFileName("Base%sMapper")
            .serviceBuilder()
            .formatServiceFileName("IBase%sService")
            .formatServiceImplFileName("Base%sServiceImpl")
            //entity
            .entityBuilder().enableFileOverride() //仅覆盖entity覆盖文件
            .enableLombok()
            .enableChainModel() //开启链式模型
            .enableTableFieldAnnotation() //开启生成实体时生成字段注解
            //.enableColumnConstant() //开启生成字段常量(就是把表内字段在类里生成常量)
            .build();

    private static final PackageConfig PACKAGE_CONFIG = new PackageConfig.Builder()
            .parent("com.fit2cloud.base")
            .entity("entity")
            .mapper("mapper")
            .xml("mapper")
            .build();

    private static final TemplateConfig TEMPLATE_CONFIG = new TemplateConfig.Builder()
            .disable(TemplateType.CONTROLLER)
            .entity("/template/entity.java")
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

        generator
                .strategy(STRATEGY_CONFIG)
                .global(GLOBAL_CONFIG)
                .packageInfo(PACKAGE_CONFIG)
                .template(TEMPLATE_CONFIG)
                .execute(new FreemarkerTemplateEngine());

    }


}
