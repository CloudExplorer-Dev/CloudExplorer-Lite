package com.fit2cloud.service.impl;

import com.fit2cloud.constants.ResourcePaymentOptionsEnum;
import com.fit2cloud.constants.ResourcePerfMetricEnum;
import com.fit2cloud.dto.optimization.OptimizationRuleField;
import com.fit2cloud.dto.optimization.OptimizationRuleFieldType;
import com.fit2cloud.service.IOptimizationRuleFieldProvider;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 获取资源类型优化规则字段列表
 *
 * @author jianneng
 * @date 2023/5/29 18:30
 **/
@Service
public class OptimizationRuleFieldProviderImpl implements IOptimizationRuleFieldProvider {

    /**
     * 云主机优化字段列表
     *
     * @return {@link List }<{@link OptimizationRuleField }>
     * @author jianneng
     * @date 2023/05/31
     */
    @Override
    public List<OptimizationRuleField> getVmOptimizationRuleFieldList() {
        List<OptimizationRuleField> fieldList = new ArrayList<>();
        // ES监控指标字段
        Arrays.stream(ResourcePerfMetricEnum.values()).toList().stream()
                .filter(v -> v.getMetricName().startsWith("CPU_") || v.getMetricName().startsWith("MEMORY_")).toList()
                .forEach(metric -> {
                    OptimizationRuleField avgMetricName = new OptimizationRuleField(metric.getDescription() + "平均值", metric.getMetricName() + "@AVERAGE", true, "%", OptimizationRuleFieldType.Number);
                    fieldList.add(avgMetricName);
                    OptimizationRuleField maxMetricName = new OptimizationRuleField(metric.getDescription() + "最大值", metric.getMetricName() + "@MAX", true, "%", OptimizationRuleFieldType.Number);
                    fieldList.add(maxMetricName);
                    OptimizationRuleField minMetricName = new OptimizationRuleField(metric.getDescription() + "最小值", metric.getMetricName() + "@MIN", true, "%", OptimizationRuleFieldType.Number);
                    fieldList.add(minMetricName);
                });
        // 付费类型字段
        List<DefaultKeyValue<String, Object>> paymentOptionFieldList = new ArrayList<>();
        Arrays.stream(ResourcePaymentOptionsEnum.values()).toList().forEach(paymentOption -> paymentOptionFieldList.add(new DefaultKeyValue<>(paymentOption.getDescription(), paymentOption.getValue())));
        OptimizationRuleField paymentOptions = new OptimizationRuleField("付费方式", "instanceChargeType", false, null, OptimizationRuleFieldType.Enum, paymentOptionFieldList);
        fieldList.add(paymentOptions);
        // 持续开机时长
        OptimizationRuleField runningDuration = new OptimizationRuleField("持续运行时长", "runningDuration", false, "天", OptimizationRuleFieldType.Number);
        fieldList.add(runningDuration);
        // 持续关机时长
        OptimizationRuleField stoppedDuration = new OptimizationRuleField("持续关机时长", "shutdownDuration", false, "天", OptimizationRuleFieldType.Number);
        fieldList.add(stoppedDuration);
        // 状态
        OptimizationRuleField status = new OptimizationRuleField("实例状态", "instanceStatus", false, "null", OptimizationRuleFieldType.Enum,
                List.of(new DefaultKeyValue<>("待回收", "ToBeRecycled"),
                        new DefaultKeyValue<>("运行中", "Running"),
                        new DefaultKeyValue<>("已停止", "Stopped"),
                        new DefaultKeyValue<>("未知", "Unknown")));
        fieldList.add(status);
        return fieldList;
    }

}
