package com.fit2cloud.autoconfigure;

import com.fit2cloud.common.provider.IBaseCloudProvider;
import org.apache.commons.lang3.StringUtils;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/3  2:40 PM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
@Component
public class PluginsContextHolder {
    public static PluginManager pluginManager;

    private static final String pathPrefix = "/opt/cloudexplorer/plugins";


    @Value("${spring.application.name}")
    public void init(String applicationName) {
        File file = new File(pathPrefix + "/" + applicationName);
        if (!file.exists()) {
            file.mkdirs();
        }
        System.setProperty("pf4j.pluginsDir", pathPrefix + "/" + applicationName);
        pluginManager = new DefaultPluginManager();
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
    }

    public static <T> List<T> getExtensions(Class<T> type) {
        return pluginManager.getExtensions(type);
    }

    public static <T extends IBaseCloudProvider> T getPlatformExtension(Class<T> type, String platform) {
        return pluginManager.getExtensions(type).stream()
                .filter(p -> StringUtils.equals(p.getCloudAccountMeta().platform, platform))
                .findFirst().orElseThrow(() -> new RuntimeException("不支持的平台类型"));
    }
}
