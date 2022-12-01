package org.openstack4j.openstack.workflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.workflow.TaskExecution;
import org.openstack4j.model.workflow.builder.TaskExecutionBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * Mistral task execution.
 *
 * @author Renat Akhmerov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MistralTaskExecution extends BaseExecution implements TaskExecution {

    private static final long serialVersionUID = 1L;

    private String name;

    private String type;

    @JsonProperty("workflow_id")
    private String wfDefId;

    @JsonProperty("workflow_execution_id")
    private String wfExecId;

    private Map<String, Object> published;

    @JsonProperty("runtime_context")
    private Map<String, Object> runtimeContext;

    private Object result;

    private Boolean processed;

    private Boolean reset;

    private Map<String, Object> env;


    public static MistralTaskExecutionBuilder builder() {
        return new MistralTaskExecutionBuilder();
    }

    @Override
    public MistralTaskExecutionBuilder toBuilder() {
        return new MistralTaskExecutionBuilder(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getWorkflowDefinitionId() {
        return wfDefId;
    }

    @Override
    public String getWorkflowExecutionId() {
        return wfExecId;
    }

    @Override
    public Map<String, Object> getRuntimeContext() {
        return runtimeContext;
    }

    @Override
    public Object getResult() {
        return result;
    }

    @Override
    public Map<String, Object> getPublished() {
        return published;
    }

    @Override
    public Boolean isProcessed() {
        return processed;
    }

    @Override
    public Boolean isReset() {
        return reset;
    }

    @Override
    public Map<String, Object> getEnvironment() {
        return env;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .add("id", id)
                .add("name", name)
                .toString();
    }

    /**
     * Mistral task execution builder.
     *
     * @author Renat Akhmerov
     */
    public static class MistralTaskExecutionBuilder extends
            BaseExecutionBuilder<MistralTaskExecutionBuilder, MistralTaskExecution>
            implements TaskExecutionBuilder<MistralTaskExecutionBuilder, MistralTaskExecution> {

        public MistralTaskExecutionBuilder() {
            this(new MistralTaskExecution());
        }

        public MistralTaskExecutionBuilder(MistralTaskExecution model) {
            super(model);
        }

        @Override
        public MistralTaskExecutionBuilder from(MistralTaskExecution in) {
            return null;
        }

        @Override
        public MistralTaskExecutionBuilder type(String type) {
            this.model.type = type;

            return this;
        }

        @Override
        public MistralTaskExecutionBuilder workflowDefinitionId(String wfDefId) {
            this.model.wfDefId = wfDefId;

            return this;
        }

        @Override
        public MistralTaskExecutionBuilder workflowExecutionId(String wfExecId) {
            this.model.wfExecId = wfExecId;

            return this;
        }

        @Override
        public MistralTaskExecutionBuilder runtimeContext(Map<String, Object> runtimeContext) {
            this.model.runtimeContext = runtimeContext;

            return this;
        }

        @Override
        public MistralTaskExecutionBuilder result(Object result) {
            this.model.result = result;

            return this;
        }

        @Override
        public MistralTaskExecutionBuilder published(Map<String, Object> published) {
            this.model.published = published;

            return this;
        }

        @Override
        public MistralTaskExecutionBuilder processed(Boolean processed) {
            this.model.processed = processed;

            return this;
        }

        @Override
        public MistralTaskExecutionBuilder reset(Boolean reset) {
            this.model.reset = reset;

            return this;
        }

        @Override
        public MistralTaskExecutionBuilder environment(Map<String, Object> env) {
            this.model.env = env;

            return this;
        }
    }

    public static class MistralTaskExecutions extends ListResult<MistralTaskExecution> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("tasks")
        private List<MistralTaskExecution> list;

        @Override
        protected List<MistralTaskExecution> value() {
            return this.list;
        }
    }
}
