package org.openstack4j.api.image;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.image.CachedImage;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.image.ImageMember;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * OpenStack (Glance) Image based Operations
 *
 * @author Jeremy Unruh
 */
public interface ImageService extends RestService {

    /**
     * List images currently in the glance image cache.
     *
     * @return list of cached images or empty list if the cache is empty or null if the cache is not enabled.
     */
    List<? extends CachedImage> listChachedImages();

    /**
     * Lists public VM images by the default page size defined by openstack
     *
     * @return list of images or empty
     */
    List<? extends Image> list();

    /**
     * * Returns list of public VM images filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends Image> list(Map<String, String> filteringParams);

    /**
     * Lists all public VM images
     *
     * @return list of images or empty
     */
    List<? extends Image> listAll();

    /**
     * * Returns list of public VM images filtered by parameters when the result greater than the default page size defined by openstack
     *
     * @param filteringParams map (name, value) of filtering parameters
     */
    List<? extends Image> listAll(Map<String, String> filteringParams);

    /**
     * Gets an Image by ID
     *
     * @param imageId the image identifier
     * @return the image or null if not found
     */
    Image get(String imageId);

    /**
     * Deletes an Image by ID
     *
     * @param imageId the image identifier
     * @return the action response
     */
    ActionResponse delete(String imageId);

    /**
     * Updates an Image.  The image must have the id set or a validation exception will be thrown
     *
     * @param image the image to update
     * @return the updated image
     */
    Image update(Image image);

    /**
     * Return the image date for the image by ID
     *
     * @param imageId the image identifier
     * @return the input stream or null if not found
     */
    InputStream getAsStream(String imageId);

    /**
     * Creates a new Image
     *
     * @param image   the image to create
     * @param payload the payload (image data to upload).  Note: if the payload is null then {@link #reserve(Image)} will be called internally
     * @return the updated data from the newly stored image
     */
    Image create(Image image, Payload<?> payload);

    /**
     * Reserves a new image to be uploaded later. See {@link #upload(String, Payload, Image)}
     *
     * @param image the image to reserve
     * @return the updated data from the newly stored image
     */
    Image reserve(Image image);

    /**
     * Upload image data for a previously-reserved image
     * <br>
     * If an image was previously reserved, and thus is in the queued state, then
     * image data can be added using this method. If the image already as data
     * associated with it (e.g. not in the queued state), then you will receive a
     * 409 Conflict exception
     *
     * @param imageId the image identifier of the previously reserved image
     * @param payload the playload to upload
     * @param image   the optional Image which will be used to update meta data during this transaction
     */
    Image upload(String imageId, Payload<?> payload, @Nullable Image image);

    /**
     * List of the other system tenants that may access a given virtual machine image that the Glance server knows about.
     *
     * @param imageId the image identifer
     * @return List of ImageMember or empty
     */
    List<? extends ImageMember> listMembers(String imageId);

    /**
     * Authorize a tenant to access a private image
     *
     * @param imageId  the image identifier
     * @param tenantId the tenant
     * @return true if successful
     */
    boolean addMember(String imageId, String tenantId);

    /**
     * Authorize a tenant to access a private image
     *
     * @param imageId  the image identifier
     * @param tenantId the tenant
     * @param canShare both existing and new memberships will have `can_share` set to the provided value
     * @return true if successful
     */
    boolean addMember(String imageId, String tenantId, boolean canShare);

    /**
     * Revoke a tenant's right to access a private image.
     *
     * @param imageId  the image identifier
     * @param tenantId the tenant to remove (identifier)
     * @return true if successful
     */
    boolean removeMember(String imageId, String tenantId);

}
