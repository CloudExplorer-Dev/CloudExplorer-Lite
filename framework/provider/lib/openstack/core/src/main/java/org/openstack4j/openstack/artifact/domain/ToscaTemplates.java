package org.openstack4j.openstack.artifact.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.artifact.Metadata;
import org.openstack4j.model.artifact.Template;
import org.openstack4j.model.artifact.ToscaTemplatesArtifact;
import org.openstack4j.model.artifact.builder.ToscaTemplatesArtifactBuilder;
import org.openstack4j.model.common.builder.BasicResourceBuilder;

import java.util.List;

/**
 * A Glare Tosca Templates Artifact implementation model
 *
 * @author Pavan Vadavi
 */
public class ToscaTemplates extends ArtifactImpl implements ToscaTemplatesArtifact {

    @JsonProperty("template_format")
    private String templateFormat;
    @JsonProperty("template")
    private TemplateImpl template;

    public static ToscaTemplatesArtifactBuilder builder() {
        return new ToscaTemplatesConcreteBuilder();
    }

    @Override
    public TemplateImpl getTemplate() {
        return template;
    }

    public void setTemplate(TemplateImpl template) {
        this.template = template;
    }

    public String getTemplateFormat() {
        return templateFormat;
    }

    public void setTemplateFormat(String templateFormat) {
        this.templateFormat = templateFormat;
    }

    @Override
    public ToscaTemplatesArtifactBuilder toBuilder() {
        return new ToscaTemplatesConcreteBuilder(this);
    }

    public static class ToscaTemplatesConcreteBuilder extends BasicResourceBuilder<ToscaTemplatesArtifact, ToscaTemplatesConcreteBuilder> implements ToscaTemplatesArtifactBuilder {

        private ToscaTemplates artifact;

        ToscaTemplatesConcreteBuilder() {
            this(new ToscaTemplates());
        }

        ToscaTemplatesConcreteBuilder(ToscaTemplates artifact) {
            this.artifact = artifact;
        }

        @Override
        public ToscaTemplatesArtifactBuilder template(Template template) {
            artifact.template = (TemplateImpl) template;
            return this;
        }

        @Override
        public ToscaTemplatesArtifactBuilder templateFormat(String templateFormat) {
            artifact.templateFormat = templateFormat;
            return this;
        }

        @Override
        public ToscaTemplatesArtifact build() {
            return artifact;
        }

        @Override
        public ToscaTemplatesArtifactBuilder from(ToscaTemplatesArtifact in) {
            this.artifact = (ToscaTemplates) in;
            return this;
        }

        @Override
        public ToscaTemplatesArtifactBuilder description(String description) {
            artifact.setDescription(description);
            return this;
        }

        @Override
        public ToscaTemplatesArtifactBuilder tags(List<Object> tags) {
            artifact.setTags(tags);
            return this;
        }

        @Override
        public ToscaTemplatesArtifactBuilder metadata(Metadata metadata) {
            artifact.setMetadata((MetadataImpl) metadata);
            return this;
        }

        @Override
        public ToscaTemplatesArtifactBuilder release(List<Object> release) {
            artifact.setRelease(release);
            return this;
        }

        @Override
        public ToscaTemplatesArtifactBuilder owner(String owner) {
            artifact.setOwner(owner);
            return this;
        }

        @Override
        public ToscaTemplatesArtifactBuilder supportedBy(Object supportedBy) {
            artifact.setSupportedBy(supportedBy);
            return this;
        }

        @Override
        public ToscaTemplatesArtifactBuilder licenseUrl(Object licenseUrl) {
            artifact.setLicenseUrl(licenseUrl);
            return this;
        }

        @Override
        public ToscaTemplatesArtifactBuilder version(String version) {
            artifact.setVersion(version);
            return this;
        }

        @Override
        public ToscaTemplatesArtifactBuilder providedBy(Object providedBy) {
            artifact.setProvidedBy(providedBy);
            return this;
        }

        @Override
        public ToscaTemplatesArtifactBuilder visibility(String visibility) {
            artifact.setVisibility(visibility);
            return this;
        }

        @Override
        public ToscaTemplatesArtifactBuilder updatedAt(String updatedAt) {
            artifact.setUpdatedAt(updatedAt);
            return this;
        }

        @Override
        public ToscaTemplatesArtifactBuilder activatedAt(String activatedAt) {
            artifact.setActivatedAt(activatedAt);
            return this;
        }

        @Override
        public ToscaTemplatesArtifactBuilder createdAt(String createdAt) {
            artifact.setCreatedAt(createdAt);
            return this;
        }

        @Override
        public ToscaTemplatesArtifactBuilder license(Object license) {
            artifact.setLicense(license);
            return this;
        }

        @Override
        public ToscaTemplatesArtifactBuilder icon(Object icon) {
            artifact.setIcon(icon);
            return this;
        }

        @Override
        public ToscaTemplatesArtifactBuilder status(String status) {
            artifact.setStatus(status);
            return this;
        }

        @Override
        protected ToscaTemplatesArtifact reference() {
            return artifact;
        }
    }

}
