package org.openstack4j.model.artifact;

import org.openstack4j.openstack.artifact.domain.ToscaTemplates;

import java.util.List;

/**
 * A Glare Tosca Templates Artifact
 *
 * @author Pavan Vadavi
 */
public interface ToscaTemplatesArtifacts extends Artifacts {

    List<ToscaTemplates> getToscaTemplates();
}
