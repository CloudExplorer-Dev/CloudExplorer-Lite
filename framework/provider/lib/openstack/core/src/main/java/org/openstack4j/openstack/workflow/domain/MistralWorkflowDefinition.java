package org.openstack4j.openstack.workflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.workflow.WorkflowDefinition;
import org.openstack4j.model.workflow.builder.WorkflowDefinitionBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Mistral workflow definition.
 *
 * @author Renat Akhmerov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MistralWorkflowDefinition extends BaseDefinition implements WorkflowDefinition {

    private static final long serialVersionUID = 1L;

    private String input;

    public static MistralWorkflowDefinitionBuilder builder() {
        return new MistralWorkflowDefinitionBuilder();
    }

    @Override
    public MistralWorkflowDefinitionBuilder toBuilder() {
        return new MistralWorkflowDefinitionBuilder(this);
    }

    @Override
    public String getInput() {
        return input;
    }

    /**
     * Mistral workflow definition builder.
     *
     * @author Renat Akhmerov
     */
    public static class MistralWorkflowDefinitionBuilder extends
            BaseDefinitionBuilder<MistralWorkflowDefinitionBuilder, MistralWorkflowDefinition>
            implements WorkflowDefinitionBuilder<MistralWorkflowDefinitionBuilder, MistralWorkflowDefinition> {

        MistralWorkflowDefinitionBuilder() {
            this(new MistralWorkflowDefinition());
        }

        MistralWorkflowDefinitionBuilder(MistralWorkflowDefinition model) {
            super(model);
        }

        @Override
        public MistralWorkflowDefinitionBuilder from(MistralWorkflowDefinition in) {
            return null;
        }

        @Override
        public MistralWorkflowDefinitionBuilder input(String input) {
            this.model.input = input;

            return this;
        }
    }

    public static class MistralWorkflowDefinitions extends ListResult<MistralWorkflowDefinition> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("workflows")
        private List<MistralWorkflowDefinition> list;

        @Override
        protected List<MistralWorkflowDefinition> value() {
            return this.list;
        }
    }
}
