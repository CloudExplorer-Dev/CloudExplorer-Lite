package org.openstack4j.model.image.v2;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.image.v2.builder.ImageUpdateBuilder;
import org.openstack4j.openstack.image.v2.domain.PatchOperation;

import java.util.List;

/**
 * A class for defining json patch operations.
 * Image is the only API to take json patch and not a changed object.
 * This allows you to define the patch object yourself.
 *
 * @author emjburns
 */
public interface ImageUpdate extends ModelEntity, Buildable<ImageUpdateBuilder> {

    /**
     * A list of Patch operations for updating an image
     * Patch operation description found here:
     * http://specs.openstack.org/openstack/glance-specs/specs/api/v2/http-patch-image-api-v2.html
     *
     * @return List of PatchOperations
     */
    List<PatchOperation> getOps();
}
