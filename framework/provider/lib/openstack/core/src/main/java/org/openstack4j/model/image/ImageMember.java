package org.openstack4j.model.image;

import org.openstack4j.model.ModelEntity;

/**
 * Represents a system tenant who has access to another tenants Image
 *
 * @author Jeremy Unruh
 */
public interface ImageMember extends ModelEntity {

    /**
     * The Member/Tenant
     *
     * @return the member identifier
     */
    String getMemberId();

    /**
     * If true the current member can share the image with another tenant
     *
     * @return true, if the current member/tenant can share the image
     */
    boolean isCanShare();

}

