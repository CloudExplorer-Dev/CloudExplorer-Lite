package org.openstack4j.model.image;

import org.openstack4j.model.ModelEntity;

import java.util.Date;

/**
 * @author esommar on 5/8/2017.
 * @see https://docs.openstack.org/developer/glance/cache.html
 */
public interface CachedImage extends ModelEntity {

    /**
     * @return the image id of the cached image
     */
    String getImageId();

    /**
     * @return date when this image was last accessed in the cache
     */
    Date getLastAccessed();

    /**
     * @return date when the image was last modified in the cache
     */
    Date getLastModified();

    /**
     * @return nr of cache hits
     */
    Integer getHits();

    /**
     * @return the image size
     */
    Long getSize();
}
