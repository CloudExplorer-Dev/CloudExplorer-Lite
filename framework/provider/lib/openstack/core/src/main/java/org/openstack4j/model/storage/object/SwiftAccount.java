package org.openstack4j.model.storage.object;

import org.openstack4j.model.ModelEntity;

import java.util.Map;

/**
 * An Account representation for OpenStack Object Storage (Swift)
 *
 * @author Jeremy Unruh
 */
public interface SwiftAccount extends ModelEntity {

    long getContainerCount();

    /**
     * The total number of objects that are stored in storage for the account.
     *
     * @return total number of objects
     */
    long getObjectCount();

    /**
     * The total number of bytes that are stored in storage for the account.
     *
     * @return total number of bytes
     */
    long getBytesUsed();

    /**
     * The secret key value for temporary URLs. If not set (null), this header is not returned by this operation.
     *
     * @return the secret key value or null
     */
    String getTemporaryUrlKey();

    /**
     * The custom account metadata map
     *
     * @return map of name to value of metadata
     */
    Map<String, String> getMetadata();

}
