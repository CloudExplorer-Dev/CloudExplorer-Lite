package org.openstack4j.openstack.telemetry.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.common.builder.BasicResourceBuilder;
import org.openstack4j.model.telemetry.Alarm;
import org.openstack4j.model.telemetry.builder.AlarmBuilder;

import java.util.List;
import java.util.Map;

/**
 * An Alarm is triggered when a specificied rule is satisfied
 *
 * @author Massimiliano Romano
 */
public class CeilometerAlarm implements Alarm {

    private static final long serialVersionUID = 1L;

    @JsonProperty("project_id")
    private String projectId;

    /**
     * {@inheritDoc}
     */

    @JsonProperty("name")
    private String name;


    @JsonProperty("type")
    private Type type;


    @JsonProperty("user_id")
    private String userId;


    @JsonProperty("alarm_actions")
    private List<String> alarmActions;


    @JsonProperty("alarm_id")
    private String alarmId;

    @JsonProperty("description")
    private String description;


    @JsonProperty("enabled")
    private boolean isEnabled;


    @JsonProperty("insufficient_data_actions")
    private List<String> insufficientDataActions;


    @JsonProperty("ok_actions")
    private List<String> okActions;


    @JsonProperty("repeat_actions")
    private boolean repeatActions;


    @JsonProperty("state")
    private String state;

    @JsonProperty("state_timestamp")
    private String stateTimestamp;


    @JsonProperty("threshold_rule")
    private CeilometerThresholdRule thresholdRule;

    @JsonProperty("combination_rule")
    private CeilometerCombinationRule combinationRule;

    @JsonProperty("composite_rule")
    private Map<String, Object> compositeRule;

    @JsonProperty("gnocchi_resources_threshold_rule")
    private CeilometerGnocchiResourcesThresholdRule gnocchiResourcesThresholdRule;

    @JsonProperty("gnocchi_aggregation_by_metrics_threshold_rule")
    private CeilometerGnocchiAggregationByMetricsThresholdRule gnocchiAggregationByMetricsThresholdRule;

    @JsonProperty("gnocchi_aggregation_by_resources_threshold_rule")
    private CeilometerGnocchiAggregationByResourcesThresholdRule gnocchiAggregationByResourcesThresholdRule;

    @JsonProperty("timestamp")
    private String timestamp;

    public static AlarmBuilder builder() {
        return new AlarmConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProjectId() {
        return projectId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", alarmId).add("name", name).add("enabled", isEnabled)
                .add("project_id", projectId).add("type", type)
                .add("user_id", userId)
                .toString();
    }

    @Override
    public List<String> getAlarmActions() {
        return alarmActions;

    }

    @Override
    public void setAlarmActions(List<String> alarmActions) {
        this.alarmActions = alarmActions;

    }

    @Override
    public String getAlarmId() {
        return alarmId;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void isEnabled(boolean newValue) {
        isEnabled = newValue;
    }

    @Override
    public List<String> getInsufficientDataActions() {
        return insufficientDataActions;
    }

    @Override
    public void setInsufficientDataActions(List<String> insufficientDataActions) {
        this.insufficientDataActions = insufficientDataActions;
    }

    @Override
    public List<String> getOkActions() {
        return okActions;
    }

    @Override
    public void setOkActions(List<String> okActions) {
        this.okActions = okActions;
    }

    @Override
    public boolean getRepeatActions() {
        return repeatActions;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public String getStateTimestamp() {
        return stateTimestamp;
    }

    @Override
    public ThresholdRule getThresholdRule() {
        return thresholdRule;
    }

    @Override
    public void setThresholdRule(CeilometerThresholdRule tr) {
        this.thresholdRule = (CeilometerThresholdRule) tr;
    }

    @Override
    public CombinationRule getCombinationRule() {
        return combinationRule;
    }

    @Override
    public Map<String, Object> getCompositeRule() {
        return compositeRule;
    }

    @Override
    public GnocchiResourcesThresholdRule getGnocchiResourcesThresholdRule() {
        return gnocchiResourcesThresholdRule;
    }

    @Override
    public void setGnocchiResourcesThresholdRule(CeilometerGnocchiResourcesThresholdRule ceilometerGnocchiResourcesThresholdRule) {
        this.gnocchiResourcesThresholdRule = ceilometerGnocchiResourcesThresholdRule;
    }

    @Override
    public GnocchiAggregationByMetricsThresholdRule getGnocchiAggregationByMetricsThresholdRule() {
        return gnocchiAggregationByMetricsThresholdRule;
    }

    @Override
    public void setGnocchiAggregationByMetricsThresholdRule(CeilometerGnocchiAggregationByMetricsThresholdRule ceilometerGnocchiAggregationByMetricsThresholdRule) {
        this.gnocchiAggregationByMetricsThresholdRule = ceilometerGnocchiAggregationByMetricsThresholdRule;
    }

    @Override
    public GnocchiAggregationByResourcesThresholdRule getGnocchiAggregationByResourcesThresholdRule() {
        return gnocchiAggregationByResourcesThresholdRule;
    }

    @Override
    public void setGnocchiAggregationByResourcesThresholdRule(CeilometerGnocchiAggregationByResourcesThresholdRule ceilometerGnocchiAggregationByResourcesThresholdRule) {
        this.gnocchiAggregationByResourcesThresholdRule = ceilometerGnocchiAggregationByResourcesThresholdRule;
    }

    @Override
    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public void setRepeateActions(Boolean repeatActions) {
        this.repeatActions = repeatActions;
    }

    @Override
    public String getId() {
        return this.alarmId;
    }

    @Override
    public void setId(String id) {
        this.alarmId = id;
    }

    @Override
    public AlarmBuilder toBuilder() {
        return new AlarmConcreteBuilder(this);
    }

    public static class CeilometerCombinationRule implements CombinationRule {

        @JsonProperty("alarm_ids")
        List<String> alarmIds;

        @JsonProperty("operator")
        Operator operator;

        @Override
        public List<String> getAlarmIds() {
            return alarmIds;
        }

        @Override
        public void setAlarmIds(List<String> alarmIds) {
            this.alarmIds = alarmIds;
        }

        @Override
        public Operator getOperator() {
            return operator;
        }

        @Override
        public void setOperator(Operator operator) {
            this.operator = operator;
        }

    }

    public static class CeilometerThresholdRule implements ThresholdRule {

        @JsonProperty("meter_name")
        String meterName;

        @JsonProperty("evaluation_periods")
        int evaluationPeriods;

        @JsonProperty("statistic")
        Statistic statistic;

        @JsonProperty("period")
        int period;

        @JsonProperty("threshold")
        float threshold;

        @JsonProperty("query")
        List<CeilometerQuery> query;

        @JsonProperty("comparison_operator")
        ComparisonOperator comparisonOperator;

        @JsonProperty("exclude_outliers")
        boolean excludeOutliers;

        @Override
        public String getMeterName() {
            return meterName;
        }

        @Override
        public void setMeterName(String meterName) {
            this.meterName = meterName;
        }

        @Override
        public int getEvaluationPeriods() {
            return evaluationPeriods;
        }

        @Override
        public void setEvaluationPeriods(int evaluationPeriod) {
            this.evaluationPeriods = evaluationPeriod;
        }

        @Override
        public Statistic getStatistic() {
            return statistic;
        }

        @Override
        public void setStatistic(Statistic statistic) {
            this.statistic = statistic;
        }

        @Override
        public int getPeriod() {
            return period;
        }

        @Override
        public void setPeriod(int period) {
            this.period = period;
        }

        @Override
        public float getThreshold() {
            return threshold;
        }

        @Override
        public void setThreshold(float threshold) {
            this.threshold = threshold;
        }

        @Override
        public List<? extends Query> getQuery() {
            return query;
        }

        @Override
        public void setQuery(List<CeilometerQuery> query) {
            this.query = query;
        }

        @Override
        public ComparisonOperator getComparisonOperator() {
            return comparisonOperator;
        }

        @Override
        public void setComparisonOperator(ComparisonOperator comparisonOperator) {
            this.comparisonOperator = comparisonOperator;
        }

        @Override
        public boolean getExcludeOutliers() {
            return excludeOutliers;
        }

        @Override
        public void setExcludeOutliers(boolean excludeOutliers) {
            this.excludeOutliers = excludeOutliers;
        }
    }

    public static class CeilometerQuery implements Query {
        @JsonProperty("field")
        String field;
        @JsonProperty("value")
        String value;
        @JsonProperty("op")
        ComparisonOperator op;

        public CeilometerQuery() {
        }

        @Override
        public String getField() {
            return field;
        }

        @Override
        public void setField(String field) {
            this.field = field;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public ComparisonOperator getOp() {
            return op;
        }

        @Override
        public void setOp(ComparisonOperator comparisonOperator) {
            this.op = comparisonOperator;
        }
    }

    public static class CeilometerGnocchiResourcesThresholdRule implements GnocchiResourcesThresholdRule {
        @JsonProperty("metric")
        String metric;
        @JsonProperty("comparison_operator")
        ComparisonOperator comparisonOperator;
        @JsonProperty("aggregation_method")
        AggregationMethod aggregationMethod;
        @JsonProperty("evaluation_periods")
        int evaluationPeriods;
        @JsonProperty("granularity")
        long granularity;
        @JsonProperty("resource_type")
        String resourceType;
        @JsonProperty("resource_id")
        String resourceId;
        @JsonProperty("threshold")
        float threshold;

        @Override
        public String getMetric() {
            return metric;
        }

        @Override
        public void setMetric(String metric) {
            this.metric = metric;
        }

        @Override
        public String getResourceId() {
            return resourceId;
        }

        @Override
        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }

        @Override
        public String getResourceType() {
            return resourceType;
        }

        @Override
        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }

        @Override
        public ComparisonOperator getComparisonOperator() {
            return comparisonOperator;
        }

        @Override
        public void setComparisonOperator(ComparisonOperator comparisonOperator) {
            this.comparisonOperator = comparisonOperator;
        }

        @Override
        public AggregationMethod getAggregationMethod() {
            return aggregationMethod;
        }

        @Override
        public void setAggregationMethod(AggregationMethod aggregationMethod) {
            this.aggregationMethod = aggregationMethod;
        }

        @Override
        public int getEvaluationPeriods() {
            return evaluationPeriods;
        }

        @Override
        public void setEvaluationPeriods(int evaluationPeriod) {
            this.evaluationPeriods = evaluationPeriod;
        }

        @Override
        public float getThreshold() {
            return threshold;
        }

        @Override
        public void setThreshold(float threshold) {
            this.threshold = threshold;
        }

        @Override
        public long getGranularity() {
            return granularity;
        }

        @Override
        public void setGranularity(long granularity) {
            this.granularity = granularity;
        }
    }

    public static class CeilometerGnocchiAggregationByMetricsThresholdRule implements GnocchiAggregationByMetricsThresholdRule {
        @JsonProperty("metrics")
        List<String> metrics;
        @JsonProperty("comparison_operator")
        ComparisonOperator comparisonOperator;
        @JsonProperty("aggregation_method")
        AggregationMethod aggregationMethod;
        @JsonProperty("threshold")
        float threshold;
        @JsonProperty("evaluation_periods")
        int evaluationPeriods;
        @JsonProperty("granularity")
        long granularity;

        @Override
        public List<String> getMetrics() {
            return metrics;
        }

        @Override
        public void setMetrics(List<String> metrics) {
            this.metrics = metrics;
        }

        @Override
        public ComparisonOperator getComparisonOperator() {
            return comparisonOperator;
        }

        @Override
        public void setComparisonOperator(ComparisonOperator comparisonOperator) {
            this.comparisonOperator = comparisonOperator;
        }

        @Override
        public AggregationMethod getAggregationMethod() {
            return aggregationMethod;
        }

        @Override
        public void setAggregationMethod(AggregationMethod aggregationMethod) {
            this.aggregationMethod = aggregationMethod;
        }

        @Override
        public float getThreshold() {
            return threshold;
        }

        @Override
        public void setThreshold(float threshold) {
            this.threshold = threshold;
        }

        @Override
        public int getEvaluationPeriods() {
            return evaluationPeriods;
        }

        @Override
        public void setEvaluationPeriods(int evaluationPeriod) {
            this.evaluationPeriods = evaluationPeriod;
        }

        @Override
        public long getGranularity() {
            return granularity;
        }

        @Override
        public void setGranularity(long granularity) {
            this.granularity = granularity;
        }
    }

    public static class CeilometerGnocchiAggregationByResourcesThresholdRule implements GnocchiAggregationByResourcesThresholdRule {
        @JsonProperty("metric")
        String metric;
        @JsonProperty("resource_type")
        String resourceType;
        @JsonProperty("comparison_operator")
        ComparisonOperator comparisonOperator;
        @JsonProperty("aggregation_method")
        AggregationMethod aggregationMethod;
        @JsonProperty("evaluation_periods")
        int evaluationPeriods;
        @JsonProperty("threshold")
        float threshold;
        @JsonProperty("granularity")
        long granularity;
        @JsonProperty("query")
        CeilometerQuery query;

        @Override
        public String getMetric() {
            return metric;
        }

        @Override
        public void setMetric(String metric) {
            this.metric = metric;
        }

        @Override
        public String getResourceType() {
            return resourceType;
        }

        @Override
        public void setResourceType(String resourceType) {
            this.resourceType = resourceType;
        }

        @Override
        public ComparisonOperator getComparisonOperator() {
            return comparisonOperator;
        }

        @Override
        public void setComparisonOperator(ComparisonOperator comparisonOperator) {
            this.comparisonOperator = comparisonOperator;
        }

        @Override
        public AggregationMethod getAggregationMethod() {
            return aggregationMethod;
        }

        @Override
        public void setAggregationMethod(AggregationMethod aggregationMethod) {
            this.aggregationMethod = aggregationMethod;
        }

        @Override
        public int getEvaluationPeriods() {
            return evaluationPeriods;
        }

        @Override
        public void setEvaluationPeriods(int evaluationPeriod) {
            this.evaluationPeriods = evaluationPeriod;
        }

        @Override
        public float getThreshold() {
            return threshold;
        }

        @Override
        public void setThreshold(float threshold) {
            this.threshold = threshold;
        }

        @Override
        public long getGranularity() {
            return granularity;
        }

        @Override
        public void setGranularity(long granularity) {
            this.granularity = granularity;
        }

        @Override
        public Query getQuery() {
            return query;
        }

        @Override
        public void setQuery(CeilometerQuery query) {
            this.query = query;
        }
    }

    public static class AlarmConcreteBuilder extends BasicResourceBuilder<Alarm, AlarmConcreteBuilder> implements AlarmBuilder {

        private CeilometerAlarm m;

        AlarmConcreteBuilder() {
            this(new CeilometerAlarm());
        }

        AlarmConcreteBuilder(CeilometerAlarm m) {
            this.m = m;
        }

        @Override
        public Alarm build() {
            return m;
        }

        @Override
        public AlarmBuilder from(Alarm in) {
            this.m = (CeilometerAlarm) in;
            return this;
        }

        @Override
        public AlarmBuilder okActions(List<String> okActions) {
            this.m.okActions = okActions;
            return this;
        }

        @Override
        public AlarmBuilder insufficientDataActions(List<String> insufficientDataActions) {
            this.m.insufficientDataActions = insufficientDataActions;
            return this;
        }

        @Override
        protected Alarm reference() {
            return m;
        }

        @Override
        public AlarmBuilder type(Type type) {
            this.m.type = type;
            return this;
        }

        @Override
        public AlarmBuilder thresholeRule(CeilometerThresholdRule tr) {
            this.m.thresholdRule = tr;
            return this;
        }

        @Override
        public AlarmBuilder repeatActions(boolean repeatActions) {
            m.repeatActions = repeatActions;
            return this;
        }

        @Override
        public AlarmBuilder description(String description) {
            m.description = description;
            return this;
        }

        @Override
        public AlarmBuilder alarmActions(List<String> alarmActions) {
            this.m.alarmActions = alarmActions;
            return this;
        }

        @Override
        public AlarmBuilder isEnabled(boolean isEnabled) {
            this.m.isEnabled = isEnabled;
            return this;
        }

        @Override
        public AlarmBuilder combinationRule(CeilometerCombinationRule ce) {
            this.m.combinationRule = ce;
            return this;
        }

        @Override
        public AlarmBuilder compositeRule(Map<String, Object> cr) {
            this.m.compositeRule = cr;
            return this;
        }

        @Override
        public AlarmBuilder gnocchiResourcesThresholdRule(CeilometerGnocchiResourcesThresholdRule ceilometerGnocchiResourcesThresholdRule) {
            this.m.gnocchiResourcesThresholdRule = ceilometerGnocchiResourcesThresholdRule;
            return this;
        }

        @Override
        public AlarmBuilder gnocchiAggregationByMetricsThresholdRule(CeilometerGnocchiAggregationByMetricsThresholdRule ceilometerGnocchiAggregationByMetricsThresholdRule) {
            this.m.gnocchiAggregationByMetricsThresholdRule = ceilometerGnocchiAggregationByMetricsThresholdRule;
            return this;
        }

        @Override
        public AlarmBuilder gnocchiAggregationByResourcesThresholdRule(CeilometerGnocchiAggregationByResourcesThresholdRule ceilometerGnocchiAggregationByResourcesThresholdRule) {
            this.m.gnocchiAggregationByResourcesThresholdRule = ceilometerGnocchiAggregationByResourcesThresholdRule;
            return this;
        }

    }
}
