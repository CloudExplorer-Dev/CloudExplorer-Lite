package org.openstack4j.openstack.workflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.workflow.WorkbookDefinition;
import org.openstack4j.model.workflow.builder.WorkbookDefinitionBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Mistral workbook definition.
 *
 * @author Renat Akhmerov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MistralWorkbookDefinition extends BaseDefinition implements WorkbookDefinition {

    private static final long serialVersionUID = 1L;

    public static MistralWorkbookDefinitionBuilder builder() {
        return new MistralWorkbookDefinitionBuilder();
    }

    @Override
    public MistralWorkbookDefinitionBuilder toBuilder() {
        return new MistralWorkbookDefinitionBuilder(this);
    }

    /**
     * Mistral workbook definition builder.
     *
     * @author Renat Akhmerov
     */
    public static class MistralWorkbookDefinitionBuilder extends
            BaseDefinitionBuilder<MistralWorkbookDefinitionBuilder, MistralWorkbookDefinition>
            implements WorkbookDefinitionBuilder<MistralWorkbookDefinitionBuilder, MistralWorkbookDefinition> {

        MistralWorkbookDefinitionBuilder() {
            this(new MistralWorkbookDefinition());
        }

        MistralWorkbookDefinitionBuilder(MistralWorkbookDefinition model) {
            super(model);
        }

        @Override
        public MistralWorkbookDefinitionBuilder from(MistralWorkbookDefinition in) {
            return null;
        }
    }

    public static class MistralWorkbookDefinitions extends ListResult<MistralWorkbookDefinition> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("workbooks")
        private List<MistralWorkbookDefinition> list;

        @Override
        protected List<MistralWorkbookDefinition> value() {
            return this.list;
        }
    }
}
