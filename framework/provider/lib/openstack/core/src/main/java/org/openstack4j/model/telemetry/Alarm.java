package org.openstack4j.model.telemetry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.common.BasicResource;
import org.openstack4j.model.telemetry.builder.AlarmBuilder;
import org.openstack4j.openstack.telemetry.domain.CeilometerAlarm.*;

import java.util.List;
import java.util.Map;

/**
 * An Alarm is triggered when a specificied rule is satisfied
 *
 * @author Massimiliano Romano
 */
public interface Alarm extends ModelEntity, BasicResource, Buildable<AlarmBuilder> {

    List<String> getAlarmActions();

    void setAlarmActions(List<String> alarmActions);

    String getAlarmId();

    String getDescription();

    void setDescription(String description);

    boolean isEnabled();

    void isEnabled(boolean newValue);

    List<String> getInsufficientDataActions();

    void setInsufficientDataActions(List<String> insufficientDataActions);

    /**
     * @return the unique name of the alarm
     */
    String getName();

    void setName(String name);

    List<String> getOkActions();

    void setOkActions(List<String> okActions);

    /**
     * @return the ID of the project/tenant that owns the resource
     */
    String getProjectId();

    boolean getRepeatActions();

    String getState();

    String getStateTimestamp();

    ThresholdRule getThresholdRule();

    void setThresholdRule(CeilometerThresholdRule tr);

    CombinationRule getCombinationRule();

    Map<String, Object> getCompositeRule();

    GnocchiResourcesThresholdRule getGnocchiResourcesThresholdRule();

    void setGnocchiResourcesThresholdRule(CeilometerGnocchiResourcesThresholdRule ceilometerGnocchiResourcesThresholdRule);

    GnocchiAggregationByMetricsThresholdRule getGnocchiAggregationByMetricsThresholdRule();

    void setGnocchiAggregationByMetricsThresholdRule(CeilometerGnocchiAggregationByMetricsThresholdRule ceilometerGnocchiAggregationByMetricsThresholdRule);

    GnocchiAggregationByResourcesThresholdRule getGnocchiAggregationByResourcesThresholdRule();

    void setGnocchiAggregationByResourcesThresholdRule(CeilometerGnocchiAggregationByResourcesThresholdRule ceilometerGnocchiAggregationByResourcesThresholdRule);

    String getTimestamp();

    /**
     * @return the alarm type
     */
    Type getType();

    void setType(Type type);

    /**
     * @return The user id who last modified the resource
     */
    String getUserId();

    void setUserId(String userId);

    void setRepeateActions(Boolean repeatActions);

    /**
     * The Alarm Type
     */
    public enum Type {
        GNOCCHI_AGGREGATION_BY_METRICS_THRESHOLD, COMPOSITE, GNOCCHI_RESOURCES_THRESHOLD, GNOCCHI_AGGREGATION_BY_RESOURCES_THRESHOLD, THRESHOLD, EVENT, COMBINATION, UNRECOGNIZED;
        //THRESHOLD, COMBINATION, UNRECOGNIZED;

        @JsonCreator
        public static Type fromValue(String type) {
            try {
                return valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                return UNRECOGNIZED;
            }
        }

        @JsonValue
        public String value() {
            return this.name().toLowerCase();
        }

        @Override
        public String toString() {
            return value();
        }
    }

    public enum Statistic {
        MAX, MIN, AVG, SUM, COUNT, UNRECOGNIZED;

        @JsonCreator
        public static Statistic fromValue(String statistic) {
            try {
                return valueOf(statistic.toUpperCase());
            } catch (IllegalArgumentException e) {
                return UNRECOGNIZED;
            }
        }

        @JsonValue
        public String value() {
            return this.name().toLowerCase();
        }

        @Override
        public String toString() {
            return value();
        }
    }

    public enum ComparisonOperator {
        LT, LE, EQ, NE, GE, GT, UNRECOGNIZED;

        @JsonCreator
        public static ComparisonOperator fromValue(String operator) {
            try {
                return valueOf(operator.toUpperCase());
            } catch (IllegalArgumentException e) {
                return UNRECOGNIZED;
            }
        }

        @JsonValue
        public String value() {
            return this.name().toLowerCase();
        }

        @Override
        public String toString() {
            return value();
        }
    }

    public enum Operator {
        AND, OR, UNRECOGNIZED;

        @JsonCreator
        public static Operator fromValue(String operator) {
            try {
                return valueOf(operator.toUpperCase());
            } catch (IllegalArgumentException e) {
                return UNRECOGNIZED;
            }
        }

        @JsonValue
        public String value() {
            return this.name().toLowerCase();
        }

        @Override
        public String toString() {
            return value();
        }
    }

    public enum AggregationMethod {
        COUNT, MAX, SUM, MIN, MEAN, UNRECOGNIZED;

        @JsonCreator
        public static AggregationMethod fromValue(String method) {
            try {
                return valueOf(method.toUpperCase());
            } catch (IllegalArgumentException e) {
                return UNRECOGNIZED;
            }
        }

        @JsonValue
        public String value() {
            return this.name().toLowerCase();
        }

        @Override
        public String toString() {
            return value();
        }
    }

    public interface CombinationRule {
        List<String> getAlarmIds();

        void setAlarmIds(List<String> alarmIds);

        Operator getOperator();

        void setOperator(Operator operator);
    }

    public interface ThresholdRule {
        String getMeterName();

        void setMeterName(String meterName);

        int getEvaluationPeriods();

        void setEvaluationPeriods(int evaluationPeriod);

        Statistic getStatistic();

        void setStatistic(Statistic statistic);

        int getPeriod();

        void setPeriod(int period);

        float getThreshold();

        void setThreshold(float threshold);

        List<? extends Query> getQuery();

        void setQuery(List<CeilometerQuery> query);

        ComparisonOperator getComparisonOperator();

        void setComparisonOperator(ComparisonOperator comparisonOperator);

        boolean getExcludeOutliers();

        void setExcludeOutliers(boolean excludeOutliers);

    }

    public interface Query {
        String getField();

        void setField(String field);

        String getValue();

        void setValue(String value);

        ComparisonOperator getOp();

        void setOp(ComparisonOperator comparisonOperator);
    }

    //gnocchi_resources_threshold
    public interface GnocchiResourcesThresholdRule {
        String getMetric();

        void setMetric(String metric);

        String getResourceId();

        void setResourceId(String resourceId);

        String getResourceType();

        void setResourceType(String resourceType);

        ComparisonOperator getComparisonOperator();

        void setComparisonOperator(ComparisonOperator comparisonOperator);

        AggregationMethod getAggregationMethod();

        void setAggregationMethod(AggregationMethod aggregationMethod);

        int getEvaluationPeriods();

        void setEvaluationPeriods(int evaluationPeriod);

        float getThreshold();

        void setThreshold(float threshold);

        long getGranularity();

        void setGranularity(long granularity);
    }

    //gnocchi_aggregation_by_metrics_threshold
    public interface GnocchiAggregationByMetricsThresholdRule {
        List<String> getMetrics();

        void setMetrics(List<String> metrics);

        ComparisonOperator getComparisonOperator();

        void setComparisonOperator(ComparisonOperator comparisonOperator);

        AggregationMethod getAggregationMethod();

        void setAggregationMethod(AggregationMethod aggregationMethod);

        float getThreshold();

        void setThreshold(float threshold);

        int getEvaluationPeriods();

        void setEvaluationPeriods(int evaluationPeriod);

        long getGranularity();

        void setGranularity(long granularity);
    }

    //gnocchi_aggregation_by_resources_threshold
    public interface GnocchiAggregationByResourcesThresholdRule {
        String getMetric();

        void setMetric(String metric);

        String getResourceType();

        void setResourceType(String resourceType);

        ComparisonOperator getComparisonOperator();

        void setComparisonOperator(ComparisonOperator comparisonOperator);

        AggregationMethod getAggregationMethod();

        void setAggregationMethod(AggregationMethod aggregationMethod);

        int getEvaluationPeriods();

        void setEvaluationPeriods(int evaluationPeriod);

        float getThreshold();

        void setThreshold(float threshold);

        long getGranularity();

        void setGranularity(long granularity);

        Query getQuery();

        void setQuery(CeilometerQuery query);
    }

    //event
    public interface GnocchiEvent {

    }

    public interface MetricOfResourceRule {
        String getMetric();

        void setMetric(String metric);

        String getResourceId();

        void setResourceId(String resourceId);

        String getResourceType();

        void setResourceType(String resourceType);
    }

    public interface AggregationMetricByResourcesLookupRule {
        String getMetric();

        void setMetric(String metric);

        String getResourceType();

        void setResourceType(String resourceType);
    }

    public interface AggregationMetricsByIdLookupRule {
        String getMetric();

        void setMetric(String metric);
    }

    public interface AlarmTimeConstraint {
        String getDescription();

        void setDescription(String description);

        int getDuration();

        void setDuration(int duration);

        String getName();

        void setName(String name);

        String getStart();

        void setStart(String String);

        String getTimezone();

        void setTimezone(String timezone);
    }

    public interface AlarmChange {
        String getAlarmId();

        void setAlarmId(String alarmId);

        String getDetail();

        void setDetail(String detail);

        String getEventId();

        void setEventId(String eventId);

        String getOnBehalfOf();

        void setOnBehalfOf(String onBehalfOf);

        String getProjectId();

        void setProjectId(String projectId);

        String getTimestamp();

        void setTimestamp(String timestamp);

        ChangeType getChangeType();

        void setChangeType(ChangeType changeType);

        String getUserId();

        void setUserId(String userId);

        //The type of change
        public enum ChangeType {
            CREATION, RULE_CHANGE, STATE_TRANSITION, DELETION, UNRECOGNIZED;

            @JsonCreator
            public static ChangeType fromValue(String type) {
                try {
                    return valueOf(type.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return UNRECOGNIZED;
                }
            }

            @JsonValue
            public String value() {
                return this.name().toLowerCase();
            }

            @Override
            public String toString() {
                return value();
            }
        }
    }

}
