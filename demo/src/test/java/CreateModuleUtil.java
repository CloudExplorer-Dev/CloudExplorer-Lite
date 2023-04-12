import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class CreateModuleUtil {

    /**
     * 模块名
     */
    private static final String NEW_MODULE_NAME = "sample-module";

    /**
     * 模块显示名称
     */
    private static final String NEW_MODULE_DISPLAY_NAME = "样例模块";

    /**
     * 模块显示名称（繁体）
     */
    private static final String NEW_MODULE_DISPLAY_NAME_TW = "樣例模塊";

    /**
     * 模块显示名称（英文）
     */
    private static final String NEW_MODULE_DISPLAY_NAME_EN = "Sample Module";

    /**
     * 后端服务端口
     */
    private static final long NEW_MODULE_PORT = 9021;

    /**
     * 开发时前端服务端口
     */
    private static final long NEW_MODULE_FRONTEND_PORT = 5021;

    /**
     * 服务管理端口
     */
    private static final long NEW_MODULE_MANAGEMENT_PORT = 9921;

    @SneakyThrows
    @Test
    public void createModule() {

        String currentPath = new File(".").getCanonicalPath();
        String parentPath = new File(currentPath).getParentFile().getCanonicalPath();
        String sourcePath = currentPath + "/DEMO-TEMPLATE";
        String targetPath = parentPath + "/services/" + NEW_MODULE_NAME;

        System.out.println(currentPath);
        //复制模版
        FileUtils.copyDirectory(new File(sourcePath), new File(targetPath));

        //替换文件内占位符
        List<String> needReplaces = Arrays.asList(
                "/pom.xml",
                "/DEMO-TEMPLATE.yml",
                "/frontend/package.json",
                "/frontend/pom.xml",
                "/frontend/env/.env",
                "/backend/pom.xml",
                "/backend/src/main/resources/application.yml"
                //其他文件
        );
        for (String needReplace : needReplaces) {
            File file = new File(targetPath + needReplace);
            String data = FileUtils.readFileToString(file);

            //DEMO-TEMPLATE-MANAGEMENT-PORT
            data = data.replaceAll("DEMO-TEMPLATE-MANAGEMENT-PORT", String.valueOf(NEW_MODULE_MANAGEMENT_PORT));
            //DEMO-TEMPLATE-FRONT-PORT
            data = data.replaceAll("DEMO-TEMPLATE-FRONT-PORT", String.valueOf(NEW_MODULE_FRONTEND_PORT));
            //DEMO-TEMPLATE-PORT
            data = data.replaceAll("DEMO-TEMPLATE-PORT", String.valueOf(NEW_MODULE_PORT));
            //DEMO-DISPLAY-NAME-EN
            data = data.replaceAll("DEMO-DISPLAY-NAME-EN", NEW_MODULE_DISPLAY_NAME_EN);
            //DEMO-DISPLAY-NAME-TW
            data = data.replaceAll("DEMO-DISPLAY-NAME-TW", NEW_MODULE_DISPLAY_NAME_TW);
            //DEMO-DISPLAY-NAME
            data = data.replaceAll("DEMO-DISPLAY-NAME", NEW_MODULE_DISPLAY_NAME);
            //DEMO-TEMPLATE
            data = data.replaceAll("DEMO-TEMPLATE", NEW_MODULE_NAME);


            FileUtils.writeStringToFile(file, data);
        }


        //重命名文件
        List<String> needRenames = Arrays.asList(
                "/DEMO-TEMPLATE.yml"
                //其他文件
        );
        for (String needRename : needRenames) {
            File oldFile = new File(targetPath + needRename);
            File newFile = new File(targetPath + needRename.replace("DEMO-TEMPLATE", NEW_MODULE_NAME));
            oldFile.renameTo(newFile);
        }

        //添加到services的pom
        File file = new File(parentPath + "/services/pom.xml");
        String data = FileUtils.readFileToString(file);
        data = data.replace("<!--NEW_MODULE_PLACE_HOLDER-->", "<module>" + NEW_MODULE_NAME + "</module>\n        <!--NEW_MODULE_PLACE_HOLDER-->");
        FileUtils.writeStringToFile(file, data);

        //添加到基座的代理中
        File file2 = new File(parentPath + "/framework/sdk/frontend/vite.config.ts");
        String data2 = FileUtils.readFileToString(file);
        data2 = data2.replace("//PROXY_PLACEHOLDER", "proxyConf[ENV.VITE_BASE_PATH + \"" + NEW_MODULE_NAME + "/api\"] =\n" +
                "    \"http://localhost:\" + Number(ENV.VITE_BASE_API_PORT);\n" +
                "  proxyConf[ENV.VITE_BASE_PATH + \"" + NEW_MODULE_NAME + "\"] =\n" +
                "    \"http://127.0.0.1:" + NEW_MODULE_FRONTEND_PORT + "\";\n" +
                "\n" +
                "  //PROXY_PLACEHOLDER");
        FileUtils.writeStringToFile(file2, data2);

        System.out.println("创建模块" + NEW_MODULE_NAME + "完成");
        System.out.println("要启动前端项目请先在项目根目录执行 yarn install\n" +
                "然后执行yarn workspace " + NEW_MODULE_NAME + " run dev");

    }


}
