package org.openstack4j.model.manila;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.manila.builder.ShareTypeCreateBuilder;

/**
 * Object used to create new share types.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface ShareTypeCreate extends ModelEntity, Buildable<ShareTypeCreateBuilder> {
    /**
     * @return the extra specifications for the share type
     */
    ExtraSpecs getExtraSpecs();

    /**
     * @return indicates whether a share type is publicly accessible
     */
    Boolean getOsShareTypeAccessIsPublic();

    /**
     * @return the share type name
     */
    String getName();
}
