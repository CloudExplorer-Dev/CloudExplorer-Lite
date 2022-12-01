package org.openstack4j.model.telemetry.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.telemetry.Alarm;
import org.openstack4j.model.telemetry.Alarm.Type;
import org.openstack4j.openstack.telemetry.domain.CeilometerAlarm.*;

import java.util.List;
import java.util.Map;

/**
 * An alarm builder
 *
 * @author Martin Belperchinov
 */

public interface AlarmBuilder extends Builder<AlarmBuilder, Alarm> {

    AlarmBuilder name(String name);

    AlarmBuilder okActions(List<String> okActions);

    AlarmBuilder alarmActions(List<String> alarmActions);

    AlarmBuilder type(Type type);

    AlarmBuilder thresholeRule(CeilometerThresholdRule te);

    AlarmBuilder combinationRule(CeilometerCombinationRule ce);

    AlarmBuilder compositeRule(Map<String, Object> cr);

    AlarmBuilder gnocchiResourcesThresholdRule(CeilometerGnocchiResourcesThresholdRule ceilometerGnocchiResourcesThreshold);

    AlarmBuilder gnocchiAggregationByMetricsThresholdRule(CeilometerGnocchiAggregationByMetricsThresholdRule ceilometerGnocchiAggregationByMetricsThreshold);

    AlarmBuilder gnocchiAggregationByResourcesThresholdRule(CeilometerGnocchiAggregationByResourcesThresholdRule ceilometerGnocchiAggregationByResourcesThreshold);

    AlarmBuilder repeatActions(boolean repeatActions);

    AlarmBuilder description(String description);

    AlarmBuilder isEnabled(boolean isEnabled);

    AlarmBuilder insufficientDataActions(List<String> insufficientDataActions);


}
