package org.openstack4j.model.artifact;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.artifact.builder.ArtifactUpdateBuilder;

/**
 * A Glare ArtifactUpdate
 *
 * @author Pavan Vadavi
 */
public interface ArtifactUpdate extends ModelEntity, Buildable<ArtifactUpdateBuilder> {

    public String getOp();

    public String getPath();

    public String getValue();
}
