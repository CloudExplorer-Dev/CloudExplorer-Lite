package org.openstack4j.model.artifact;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.artifact.builder.ToscaTemplatesArtifactBuilder;

/**
 * A Glare Tosca Templates Artifact
 *
 * @author Pavan Vadavi
 */
public interface ToscaTemplatesArtifact extends Artifact, Buildable<ToscaTemplatesArtifactBuilder> {

    Template getTemplate();

    String getTemplateFormat();

}
