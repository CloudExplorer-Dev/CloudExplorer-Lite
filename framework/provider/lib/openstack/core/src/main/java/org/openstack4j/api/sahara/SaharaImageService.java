package org.openstack4j.api.sahara;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.sahara.Image;

import java.util.List;

/**
 * Sahara Data Processing Operations
 *
 * @author ekasit.kijsipongse@nectec.or.th
 */
public interface SaharaImageService extends RestService {

    /**
     * List all images
     *
     * @return list of images or empty
     */
    List<? extends Image> list();

    /**
     * List images with specified tag(s)
     *
     * @param tags one or more tags
     * @return list of images or empty
     */
    List<? extends Image> list(String... tags);

    /**
     * Get an image by ID
     *
     * @param imageId the image identifier
     * @return the image or null if not found
     */
    Image get(String imageId);

    /**
     * Register a new image into Sahara image registry
     *
     * @param imageId     the image to register
     * @param username    the username
     * @param description the description
     * @return the registered image
     */
    Image register(String imageId, String username, String description);

    /**
     * Unregister the specified image from Sahara image registry
     *
     * @param imageId the image identifier
     * @return the action response
     */
    ActionResponse unregister(String imageId);

    /**
     * Add Tag(s) to image
     *
     * @param tags one or more tags to add
     * @return the image
     */
    Image tag(String imageId, String... tags);

    /**
     * Remove Tag(s) from image
     *
     * @param tags one or more tags to remove
     * @return the image
     */
    Image untag(String imageId, String... tags);
}
