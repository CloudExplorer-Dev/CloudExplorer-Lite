package org.openstack4j.model.manila;

import org.openstack4j.model.ModelEntity;

/**
 * A share type enables you to filter or choose back ends before you create a share.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface ShareType extends ModelEntity {
    /**
     * @return the UUID of the share type
     */
    String getId();

    /**
     * @return the required extra specifications for the share type
     */
    ExtraSpecs getRequiredExtraSpecs();

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
