package org.openstack4j.openstack.workflow.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.workflow.Execution;
import org.openstack4j.model.workflow.State;
import org.openstack4j.model.workflow.builder.ExecutionBuilder;

import java.util.Date;
import java.util.List;


/**
 * Base class for all execution models.
 *
 * @author Renat Akhmerov
 */
public abstract class BaseExecution implements Execution {
    String id;

    String description;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date updatedAt;

    List<String> tags;

    @JsonProperty("workflow_name")
    String wfName;

    State state;

    @JsonProperty("state_info")
    String stateInfo;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getWorkflowName() {
        return wfName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public String getStateInfo() {
        return stateInfo;
    }

    /**
     * Base execution builder.
     *
     * @author Renat Akhmerov
     */
    @SuppressWarnings("unchecked")
    public static abstract class BaseExecutionBuilder<T extends ExecutionBuilder<T, M>, M extends BaseExecution>
            implements ExecutionBuilder<T, M> {

        protected M model;

        BaseExecutionBuilder(M model) {
            this.model = model;
        }

        public M build() {
            return model;
        }

        public T from(Execution in) {
            return null;
        }

        public T id(String id) {
            model.id = id;

            return (T) this;
        }

        public T description(String description) {
            model.description = description;

            return (T) this;
        }

        public T workflowName(String wfName) {
            model.wfName = wfName;

            return (T) this;
        }

        public T createdAt(Date createdAt) {
            model.createdAt = createdAt;

            return (T) this;
        }

        public T updatedAt(Date updatedAt) {
            model.updatedAt = updatedAt;

            return (T) this;
        }

        public T tags(List<String> tags) {
            model.tags = tags;

            return (T) this;
        }

        public T state(State state) {
            model.state = state;

            return (T) this;
        }

        public T stateInfo(String stateInfo) {
            model.stateInfo = stateInfo;

            return (T) this;
        }
    }
}
