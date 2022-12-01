package org.openstack4j.openstack.workflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.workflow.WorkflowExecution;
import org.openstack4j.model.workflow.builder.WorkflowExecutionBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * Mistral workflow execution.
 *
 * @author Renat Akhmerov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MistralWorkflowExecution extends BaseExecution implements WorkflowExecution {

    private static final long serialVersionUID = 1L;

    @JsonProperty("params")
    private Map<String, Object> parameters;

    @JsonProperty("input")
    private Map<String, Object> input;

    private Map<String, Object> output;

    private String taskExecutionId;

    public static MistralWorkflowExecutionBuilder builder() {
        return new MistralWorkflowExecutionBuilder();
    }

    @Override
    public MistralWorkflowExecutionBuilder toBuilder() {
        return new MistralWorkflowExecutionBuilder(this);
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public Map<String, Object> getInput() {
        return input;
    }

    @Override
    public Map<String, Object> getOutput() {
        return output;
    }

    @Override
    public String getTaskExecutionId() {
        return taskExecutionId;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .add("id", id)
                .add("input", input)
                .toString();
    }

    /**
     * Mistral workflow execution builder.
     *
     * @author Renat Akhmerov
     */
    public static class MistralWorkflowExecutionBuilder extends
            BaseExecutionBuilder<MistralWorkflowExecutionBuilder, MistralWorkflowExecution>
            implements WorkflowExecutionBuilder<MistralWorkflowExecutionBuilder, MistralWorkflowExecution> {

        public MistralWorkflowExecutionBuilder() {
            this(new MistralWorkflowExecution());
        }

        public MistralWorkflowExecutionBuilder(MistralWorkflowExecution model) {
            super(model);
        }

        @Override
        public MistralWorkflowExecutionBuilder from(MistralWorkflowExecution in) {
            return null;
        }

        @Override
        public MistralWorkflowExecutionBuilder parameters(Map<String, Object> parameters) {
            this.model.parameters = parameters;

            return this;
        }

        @Override
        public MistralWorkflowExecutionBuilder input(Map<String, Object> input) {
            this.model.input = input;

            return this;
        }

        @Override
        public MistralWorkflowExecutionBuilder output(Map<String, Object> output) {
            this.model.output = output;

            return this;
        }

        @Override
        public MistralWorkflowExecutionBuilder taskExecutionId(String taskExecutionId) {
            this.model.taskExecutionId = taskExecutionId;

            return this;
        }
    }

    public static class MistralWorkflowExecutions extends ListResult<MistralWorkflowExecution> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("executions")
        private List<MistralWorkflowExecution> list;

        @Override
        protected List<MistralWorkflowExecution> value() {
            return this.list;
        }
    }
}
