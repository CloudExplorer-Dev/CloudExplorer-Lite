package com.fit2cloud.vm.constants;

import com.fit2cloud.common.provider.entity.ActionInfo;

import java.util.Arrays;
import java.util.List;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/7/6  11:55 AM}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public enum ActionInfoConstants implements ActionInfo {

    SYNC_VIRTUAL_MACHINE("SYNC_VIRTUAL_MACHINE", "同步虚拟机", List.of("listVirtualMachine")),

    SYNC_DISK("SYNC_DISK", "同步磁盘", List.of("listDisk")),

    SYNC_IMAGE("SYNC_IMAGE", "同步镜像", List.of("listImage")),

    SYNC_HOST("SYNC_HOST", "同步宿主机", List.of("listHost")),

    SYNC_DATASTORE("SYNC_DATASTORE", "同步存储器", List.of("listDataStore")),

    SYNC_VIRTUAL_MACHINE_METRIC_MONITOR("SYNC_VIRTUAL_MACHINE_METRIC_MONITOR", "同步虚拟机监控", List.of("getF2CPerfMetricMonitorData")),

    SYNC_DISK_MACHINE_METRIC_MONITOR("SYNC_DISK_MACHINE_METRIC_MONITOR", "同步磁盘监控", List.of("getF2CDiskPerfMetricMonitorData")),

    SYNC_HOST_MACHINE_METRIC_MONITOR("SYNC_Host_MACHINE_METRIC_MONITOR", "同步宿主机监控", List.of("getF2CHostPerfMetricMonitorData")),

    SYNC_DATA_STORE_MACHINE_METRIC_MONITOR("SYNC_DATA_STORE_MACHINE_METRIC_MONITOR", "同步存储器监控", List.of("getF2CDatastorePerfMetricMonitorData"));

    private final String code;
    private final String message;
    private final List<String> methods;

    ActionInfoConstants(String code, String message, List<String> methods) {
        this.code = code;
        this.message = message;
        this.methods = methods;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public List<String> methods() {
        return methods;
    }

    /**
     * 所有的操作
     *
     * @return 注意 这里的所有操作包括私有云和公有云的所有操作,如果当前是公有云的话, 私有云部分数据同步操作是没有的 所有不能调用该函数
     */
    public static List<? extends ActionInfo> all() {
        return Arrays.stream(ActionInfoConstants.values()).toList();
    }

    /**
     * 排除部分
     *
     * @param exclude 排除部分操作
     * @return 可执行操作
     */
    public static List<? extends ActionInfo> all(ActionInfo... exclude) {
        List<ActionInfo> excludeActionInfos = Arrays.stream(exclude).toList();
        return all(excludeActionInfos);
    }

    /**
     * 排除部分
     *
     * @param excludes 排除部分操作
     * @return 可执行操作
     */
    public static List<? extends ActionInfo> all(List<ActionInfo> excludes) {
        return all()
                .stream()
                .filter(actionInfo -> excludes
                        .stream()
                        .noneMatch(ea -> ea.equals(actionInfo))
                ).toList();
    }
}
