package org.openstack4j.api.artifact;


import org.openstack4j.common.RestService;
import org.openstack4j.model.artifact.ArtifactUpdate;
import org.openstack4j.model.artifact.ToscaTemplatesArtifact;
import org.openstack4j.model.artifact.ToscaTemplatesArtifacts;
import org.openstack4j.model.common.ActionResponse;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * OpenStack (Glare) Artifact based Operations for Tosca Templates type
 *
 * @author Pavan Vadavi
 */
public interface ToscaTemplatesArtifactService extends RestService {

    /**
     * Lists all artifacts
     *
     * @return Tosca templates artifact list
     */
    ToscaTemplatesArtifacts list();

    /**
     * Get specific artifact
     *
     * @return Tosca template artifact
     */
    ToscaTemplatesArtifact get(String artifactId);

    /**
     * Create artifact
     *
     * @return Tosca template artifact
     */
    ToscaTemplatesArtifact create(ToscaTemplatesArtifact toscaTemplatesArtifact);

    /**
     * Upload template to artifact
     *
     * @return Tosca template artifact
     */
    ToscaTemplatesArtifact upload(String artifactId, File file);

    /**
     * Download template from artifact
     *
     * @return Input stream
     */
    InputStream download(String artifactId);

    /**
     * Delete specific artifact
     *
     * @return Action response
     */
    ActionResponse delete(String artifactId);

    /**
     * Update specific artifact
     *
     * @return Tosca template artifact
     */
    ToscaTemplatesArtifact update(String artifactId, List<ArtifactUpdate> artifactUpdates);

    /**
     * Activate specific artifact
     *
     * @return Tosca template artifact
     */
    ToscaTemplatesArtifact activate(String artifactId);

    /**
     * Deactivate specific artifact
     *
     * @return Tosca template artifact
     */
    ToscaTemplatesArtifact deactivate(String artifactId);

    /**
     * Reactivate specific artifact
     *
     * @return Tosca template artifact
     */
    ToscaTemplatesArtifact reactivate(String artifactId);

    /**
     * Publish specific artifact
     *
     * @return Tosca template artifact
     */
    ToscaTemplatesArtifact publish(String artifactId);

}
