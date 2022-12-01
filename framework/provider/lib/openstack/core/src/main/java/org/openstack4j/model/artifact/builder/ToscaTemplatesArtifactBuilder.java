package org.openstack4j.model.artifact.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.artifact.Template;
import org.openstack4j.model.artifact.ToscaTemplatesArtifact;

/**
 * A Builder which creates a Tosca Template Artifact Builder
 *
 * @author Pavan Vadavi
 */
public interface ToscaTemplatesArtifactBuilder extends ArtifactBuilder, Builder<ToscaTemplatesArtifactBuilder, ToscaTemplatesArtifact> {

    ToscaTemplatesArtifactBuilder template(Template template);

    ToscaTemplatesArtifactBuilder templateFormat(String templateFormat);
}
