package org.openstack4j.openstack.image.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.image.v2.Task;
import org.openstack4j.model.image.v2.builder.TaskBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A Glance v2 task model implementation
 *
 * @author emjburns
 */
public class GlanceTask implements Task {
    @JsonProperty("created_at")
    Date createdAt;

    @JsonProperty("expires_at")
    Date expiresAt;

    @JsonProperty("updated_at")
    Date updatedAt;

    String id;

    Map<String, Object> input;

    String message;

    String owner;

    Map<String, String> result;

    String schema;

    TaskStatus status;

    String type;

    String self;

    public static TaskBuilder builder() {
        return new TaskConcreteBuilder();
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public Date getExpiresAt() {
        return expiresAt;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String, Object> getInput() {
        return input;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public Map<String, String> getResult() {
        return result;
    }

    @Override
    public String getSchema() {
        return schema;
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getSelf() {
        return self;
    }

    @Override
    public TaskBuilder toBuilder() {
        return new TaskConcreteBuilder(this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("createdAt", createdAt)
                .add("expiresAt", expiresAt)
                .add("updatedAt", updatedAt)
                .add("id", id)
                .add("input", input)
                .add("message", message)
                .add("owner", owner)
                .add("result", result)
                .add("schema", schema)
                .add("status", status)
                .add("type", type)
                .add("self", self)
                .toString();
    }

    public static class Tasks extends ListResult<GlanceTask> {
        @JsonProperty("tasks")
        private List<GlanceTask> tasks;

        @Override
        protected List<GlanceTask> value() {
            return tasks;
        }
    }

    public static class TaskConcreteBuilder implements TaskBuilder {
        private GlanceTask m;

        TaskConcreteBuilder() {
            this(new GlanceTask());
        }

        TaskConcreteBuilder(GlanceTask m) {
            this.m = m;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TaskBuilder type(String type) {
            m.type = type;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public TaskBuilder input(Map<String, Object> input) {
            m.input = input;
            return this;
        }

        @Override
        public Task build() {
            return m;
        }

        @Override
        public TaskBuilder from(Task in) {
            m = (GlanceTask) in;
            return this;
        }
    }
}
