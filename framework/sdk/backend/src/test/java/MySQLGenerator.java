import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.fit2cloud.BaseTestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = BaseTestApplication.class)
@TestPropertySource(locations = {
        "classpath:commons.properties",
        "file:${ce.config.file}"
})
public class MySQLGenerator {

    private static final List<String> TABLES = Arrays.asList("user");

    @Value("${ce.datasource.url}")
    private String datasource;

    @Value("${ce.datasource.username}")
    private String username;

    @Value("${ce.datasource.password}")
    private String password;


    @Test
    public void run() {
        System.out.println(datasource);
        System.out.println(username);
        System.out.println(password);

        DataSourceConfig DATA_SOURCE_CONFIG = new DataSourceConfig
                .Builder(datasource, username, password)
                .build();

        GlobalConfig GLOBAL_CONFIG = new GlobalConfig.Builder()
                //.fileOverride()
                .outputDir("./src/main/java/")
                .author("fit2cloud")
                //.enableSwagger()
                .disableOpenDir()
                //.enableSpringdoc()
                .build();

        StrategyConfig STRATEGY_CONFIG = new StrategyConfig.Builder()
                .addInclude(TABLES) //需要生成的表名
                .entityBuilder()
                .enableLombok()
                .enableTableFieldAnnotation()
                .mapperBuilder()
                .formatMapperFileName("%sMapper")
                .formatMapperFileName("%sMapper")
                .enableFileOverride() //覆盖文件
                .build();

        PackageConfig PACKAGE_CONFIG = new PackageConfig.Builder()
                .parent("com.fit2cloud.base")
                .entity("entity")
                .mapper("mapper")
                .xml("mapper")
                .build();

        TemplateConfig TEMPLATE_CONFIG = new TemplateConfig.Builder()
                .disable(TemplateType.CONTROLLER)
                .build();


        AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
        generator
                .strategy(STRATEGY_CONFIG)
                .global(GLOBAL_CONFIG)
                .packageInfo(PACKAGE_CONFIG)
                .template(TEMPLATE_CONFIG)
                .execute();

    }


}
