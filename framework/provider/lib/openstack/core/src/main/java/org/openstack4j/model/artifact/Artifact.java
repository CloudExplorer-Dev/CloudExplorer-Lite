package org.openstack4j.model.artifact;

import org.openstack4j.model.common.BasicResource;

import java.util.List;

/**
 * A Glare Artifact
 *
 * @author Pavan Vadavi
 */
public interface Artifact extends BasicResource {

    String getDescription();

    List<Object> getTags();

    Metadata getMetadata();

    List<Object> getRelease();

    String getOwner();

    Object getSupportedBy();

    Object getLicenseUrl();

    String getVersion();

    Object getProvidedBy();

    String getVisibility();

    String getUpdatedAt();

    String getActivatedAt();

    String getCreatedAt();

    Object getLicense();

    Object getIcon();

    String getStatus();
}
