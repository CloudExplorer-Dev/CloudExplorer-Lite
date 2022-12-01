package org.openstack4j.openstack.artifact.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.artifact.ToscaTemplatesArtifacts;

import java.util.List;

/**
 * Created by vadavi on 18-01-2017.
 */
public class ToscaTemplatesList implements ToscaTemplatesArtifacts {

    @JsonProperty("tosca_templates")
    private List<ToscaTemplates> toscaTemplates = null;
    @JsonProperty("schema")
    private String schema;
    @JsonProperty("first")
    private String first;

    @Override
    public List<ToscaTemplates> getToscaTemplates() {
        return toscaTemplates;
    }

    public void setToscaTemplates(List<ToscaTemplates> toscaTemplates) {
        this.toscaTemplates = toscaTemplates;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }
}
