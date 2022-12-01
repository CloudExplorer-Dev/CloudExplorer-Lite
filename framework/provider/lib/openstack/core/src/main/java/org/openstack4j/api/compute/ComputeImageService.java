package org.openstack4j.api.compute;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.Image;

import java.util.List;
import java.util.Map;

/**
 * Provides access to Compute Images
 *
 * @author Jeremy Unruh
 * @see org.openstack4j.api.image.v2.ImageService
 * @deprecated These APIs are proxy calls to the Image service. Nova has deprecated all the proxy APIs and users should use the native APIs instead. All the Image services proxy APIs except image metadata APIs will fail with a 404 starting from microversion 2.36. The image metadata APIs will fail with a 404 starting from microversion 2.39.
 */
@Deprecated
public interface ComputeImageService extends RestService {

    /**
     * Lists Images on an OpenStack Compute EndPoint.  By default this method lists in "Detail" which means all
     * major fields are populated for each image. This is equivalent to calling {@link #list(boolean)} with {code}true{code}
     *
     * @return List of Image
     */
    List<? extends Image> list();

    /**
     * Lists Images on an OpenStack Compute EndPoint.  If {@code detailed} is false then only {@link Image#getId()}, {@link Image#getName()} and
     *
     * @param detailed true to populate all properties within each Image, false to include Id, Name and Links only (all other properties will be null)
     * @return List of Image
     * {@link Image#getLinks()} will be populated.  True will populate all fields
     */
    List<? extends Image> list(boolean detailed);

    /**
     * Finds an Image by the given {@code imageId}.
     *
     * @param imageId the image identifier
     * @return the specified Image
     */
    Image get(String imageId);

    /**
     * Deletes an Image based on it's identifiers.  Images created by another user typically cannot be deleted
     *
     * @param imageId the image identifier
     * @return the action response
     */
    ActionResponse delete(String imageId);

    /**
     * Gets the metadata for an image
     *
     * @param imageId the image identifier
     * @return the metadata as Map<String, String>
     */
    Map<String, String> getMetaData(String imageId);

    /**
     * Sets the metadata for an image
     *
     * @param imageId  the image identifier
     * @param metadata a Map containing the metadata
     * @return the metadata as Map<String, String>
     */
    Map<String, String> setMetaData(String imageId, Map<String, String> metadata);

    /**
     * Delete a metadata item from an image
     *
     * @param imageId the image identifier
     * @param keys    one or more keys/metadata names to remove
     * @return the action response
     */
    ActionResponse deleteMetaData(String imageId, String... keys);
}
