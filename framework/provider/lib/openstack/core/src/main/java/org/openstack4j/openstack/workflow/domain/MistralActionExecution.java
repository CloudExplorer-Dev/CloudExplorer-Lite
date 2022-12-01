package org.openstack4j.openstack.workflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.workflow.ActionExecution;
import org.openstack4j.model.workflow.builder.ActionExecutionBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * Mistral action execution.
 *
 * @author Renat Akhmerov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MistralActionExecution extends BaseExecution implements ActionExecution {

    private static final long serialVersionUID = 1L;

    private String name;

    private String type;

    @JsonProperty("task_name")
    private String taskName;

    @JsonProperty("task_execution_id")
    private String taskExecId;

    private Map<String, Object> input;

    private Map<String, Object> output;

    private Boolean accepted;

    public static MistralActionExecutionBuilder builder() {
        return new MistralActionExecutionBuilder();
    }

    @Override
    public MistralActionExecutionBuilder toBuilder() {
        return new MistralActionExecutionBuilder(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTaskName() {
        return taskName;
    }

    @Override
    public String getTaskExecutionId() {
        return taskExecId;
    }


    @Override
    public Boolean isAccepted() {
        return accepted;
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
    public static class MistralActionExecutionBuilder extends
            BaseExecutionBuilder<MistralActionExecutionBuilder, MistralActionExecution>
            implements ActionExecutionBuilder<MistralActionExecutionBuilder, MistralActionExecution> {

        public MistralActionExecutionBuilder() {
            this(new MistralActionExecution());
        }

        public MistralActionExecutionBuilder(MistralActionExecution model) {
            super(model);
        }

        @Override
        public MistralActionExecutionBuilder from(MistralActionExecution in) {
            return null;
        }

        @Override
        public MistralActionExecutionBuilder name(String name) {
            this.model.name = name;

            return this;
        }

        @Override
        public MistralActionExecutionBuilder taskName(String taskName) {
            this.model.taskName = taskName;

            return this;
        }

        @Override
        public MistralActionExecutionBuilder taskExecutionId(String taskExecId) {
            this.model.taskExecId = taskExecId;

            return this;
        }

        @Override
        public MistralActionExecutionBuilder accepted(Boolean accepted) {
            this.model.accepted = accepted;

            return this;
        }

        @Override
        public MistralActionExecutionBuilder input(Map<String, Object> input) {
            this.model.input = input;

            return this;
        }

        @Override
        public MistralActionExecutionBuilder output(Map<String, Object> output) {
            this.model.output = output;

            return this;
        }
    }

    public static class MistralActionExecutions extends ListResult<MistralActionExecution> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("action_executions")
        private List<MistralActionExecution> list;

        @Override
        protected List<MistralActionExecution> value() {
            return this.list;
        }
    }
}
