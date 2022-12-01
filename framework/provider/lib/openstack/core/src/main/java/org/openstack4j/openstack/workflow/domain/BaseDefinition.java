package org.openstack4j.openstack.workflow.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.workflow.Definition;
import org.openstack4j.model.workflow.Scope;
import org.openstack4j.model.workflow.builder.DefinitionBuilder;

import java.util.Date;
import java.util.List;


/**
 * Base class for all definition models.
 *
 * @author Renat Akhmerov
 */
public abstract class BaseDefinition implements Definition {
    String id;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date updatedAt;

    List<String> tags;

    String name;

    String definition;

    @JsonProperty("project_id")
    String projectId;

    Scope scope;

    Boolean system;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDefinition() {
        return definition;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public Boolean isSystem() {
        return system;
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    @Override
    public Scope getScope() {
        return scope;
    }

    @Override
    public String getProjectId() {
        return projectId;
    }

    /**
     * Base definition builder.
     *
     * @author Renat Akhmerov
     */
    @SuppressWarnings("unchecked")
    public static abstract class BaseDefinitionBuilder<T extends DefinitionBuilder<T, M>, M extends BaseDefinition>
            implements DefinitionBuilder<T, M> {

        protected M model;

        BaseDefinitionBuilder(M model) {
            this.model = model;
        }

        public M build() {
            return model;
        }

        public T from(Definition in) {
            return null;
        }

        public T id(String id) {
            model.id = id;

            return (T) this;
        }

        public T name(String name) {
            model.name = name;

            return (T) this;
        }

        public T definition(String definition) {
            model.definition = definition;

            return (T) this;
        }

        public T created(Date create) {
            model.createdAt = create;

            return (T) this;
        }

        public T updated(Date updated) {
            model.updatedAt = updated;

            return (T) this;
        }

        public T system(Boolean system) {
            model.system = system;

            return (T) this;
        }

        public T tags(List<String> tags) {
            model.tags = tags;

            return (T) this;
        }

        public T scope(Scope scope) {
            model.scope = scope;

            return (T) this;
        }

        public T projectId(String projectId) {
            model.projectId = projectId;

            return (T) this;
        }
    }

}
