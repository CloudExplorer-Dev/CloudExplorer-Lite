package com.fit2cloud.service.impl;

import com.fit2cloud.common.utils.FileUtils;
import com.fit2cloud.common.utils.JsonUtil;
import com.fit2cloud.dao.entity.ExtraModule;
import com.fit2cloud.dao.entity.ExtraModules;
import com.fit2cloud.service.IModuleManageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ModuleManageServiceImpl implements IModuleManageService {

    @Value("${ce.module.repository.url:https://resource.fit2cloud.com/cloudexplorer-lite/modules/releases/latest/}")
    private String repositoryPath;

    @Override
    public ExtraModules list() {
        Yaml yaml = new Yaml();

        File versionFile = new File("/opt/cloudexplorer/VERSION");
        String version = StringUtils.trim(FileUtils.txt2String(versionFile));

        File installedModulesFile = new File("/opt/cloudexplorer/apps/extra/modules");

        Map<String, ExtraModule> installedModuleMap = new HashMap<>();
        try {
            String[] installedModules = StringUtils.trim(FileUtils.txt2String(installedModulesFile)).split("\\r?\\n");
            for (String installedModule : installedModules) {
                if (StringUtils.contains(installedModule, "|")) {
                    String[] ss = StringUtils.split(installedModule, "|");
                    ExtraModule module = new ExtraModule().setName(ss[0]).setPort(Integer.parseInt(ss[1])).setCurrentVersion(ss[2]).setInstalled(true);
                    //本地安装的信息
                    try {
                        File moduleYml = new File("/opt/cloudexplorer/apps/extra/" + ss[0] + "-" + ss[2] + ".yml");
                        if (moduleYml.exists()) {
                            ExtraModule m = JsonUtil.parseObject(JsonUtil.toJSONString(yaml.load(FileUtils.txt2String(moduleYml))), ExtraModule.class);
                            module.setDescription(m.getDescription()).setDisplayName(m.getDisplayName()).setIcon(m.getIcon());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    installedModuleMap.put(ss[0], module);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        ExtraModules modules = new ExtraModules();
        try {

//            String[] vList = StringUtils.split(version, ".");
//            String repositoryUrl = repositoryPath + "modules_" + vList[0] + "." + vList[1] + ".x.yml";
            String repositoryUrl = repositoryPath + "modules_" + version + ".yml";

            modules = JsonUtil.parseObject(JsonUtil.toJSONString(yaml.load(downloadTxt2String(repositoryUrl))), ExtraModules.class);
            if (modules == null) {
                modules = new ExtraModules();
            }
            if (modules.getModules() == null) {
                modules.setModules(new ArrayList<>());
            }
            List<String> installedKeys = new ArrayList<>();
            for (ExtraModule module : modules.getModules()) {
                if (installedModuleMap.containsKey(module.getName())) {
                    module.setCurrentVersion(installedModuleMap.get(module.getName()).getCurrentVersion());
                    module.setInstalled(true);
                    installedKeys.add(module.getName());
                }
            }
            //填入本地额外的模块
            for (ExtraModule module : installedModuleMap.values()) {
                if (!installedKeys.contains(module.getName())) {
                    modules.getModules().add(module);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            modules.setModules(new ArrayList<>());
            for (ExtraModule module : installedModuleMap.values()) {
                modules.getModules().add(module);
            }
        }

        modules.setVersion(version);

        //设置运行状态
        String psStr = ps();
        Map<String, String> statusMap = new HashMap<>();
        if (psStr != null) {
            try {
                String[] psInfos = psStr.split("\\r?\\n");
                for (String psInfo : psInfos) {
                    if (StringUtils.isEmpty(psInfo)) {
                        continue;
                    }
                    String[] statusStr = StringUtils.split(StringUtils.trim(psInfo), " ");
                    if (statusStr.length != 5) {
                        continue;
                    }
                    if (StringUtils.equals("pid:", statusStr[2])) {
                        statusMap.put(statusStr[1], "NOT_RUNNING");
                        continue;
                    }
                    if (StringUtils.equals("status:healthy", statusStr[3])) {
                        statusMap.put(statusStr[1], "HEALTHY");
                    } else {
                        statusMap.put(statusStr[1], "UNHEALTHY");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!statusMap.isEmpty()) {
            for (ExtraModule module : modules.getModules()) {
                if (module.isInstalled()) {
                    module.setStatus(statusMap.get(module.getName()));
                }
            }
        }

        return modules;
    }

    @Override
    public void install(String url) {
        execRunCore("updateModule", url);
    }

    @Override
    public void uninstall(String module) {
        execRunCore("uninstallModule", module);
    }

    @Override
    public void start(String module) {
        execRunCore("restartModule", module);
    }

    @Override
    public void stop(String module) {
        execRunCore("stopModule", module);
    }

    @Override
    public void upload(MultipartFile file) {
        String filePath = "/opt/cloudexplorer/downloads/";

        if (StringUtils.contains(file.getOriginalFilename(), "/") || StringUtils.contains(file.getOriginalFilename(), "../")) {
            throw new RuntimeException("文件名非法");
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {

            if (!StringUtils.endsWith(file.getOriginalFilename(), ".tar.gz")) {
                throw new RuntimeException("只能上传tar.gz格式文件");
            }
            File dest = new File(new File(filePath).getAbsolutePath() + "/" + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(dest.getAbsolutePath());
            Files.write(path, bytes);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        execRunCore("installModule", fileName);

    }

    @Override
    public void startGateway() {
        execRunCore("runGateway");
    }

    @Override
    public void startExtras() {
        execRunCore("runExtra");
    }

    private String ps() {
        try {
            return execRunCore("ps");
        } catch (Exception e) {
            return null;
        }
    }


    private static String downloadTxt2String(String url) {
        StringBuilder result = new StringBuilder();
        try {
            URL fileUrl = new URL(url);
            URLConnection connection = fileUrl.openConnection();
            InputStream is = connection.getInputStream();
            // 构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s = null;
            // 使用readLine方法，一次读一行
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator()).append(s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    //同步执行
    private static String execRunCore(String... props) {
        Runtime runtime = Runtime.getRuntime();  //获取Runtime实例
        //执行命令
        try {
            List<String> command = new ArrayList<>(List.of("/opt/cloudexplorer/apps/core/run-core.sh"));
            List<String> propList = new ArrayList<>(Arrays.asList(props));
            command.addAll(propList);

            Process process = runtime.exec(command.toArray(new String[0]));
            // 标准输入流（必须写在 waitFor 之前）
            String inStr = consumeInputStream(process.getInputStream());
            // 标准错误流（必须写在 waitFor 之前）
            String errStr = consumeInputStream(process.getErrorStream()); //若有错误信息则输出

            int proc = process.waitFor();
            //System.out.println(inStr);
            if (proc == 0) {
                //System.out.println("执行成功");
                return inStr;
            } else {
                throw new Exception("执行失败: " + errStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 消费inputstream，并返回
     */
    private static String consumeInputStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = br.readLine()) != null) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }

}
