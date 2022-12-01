package org.openstack4j.openstack.storage.object.internal;

import org.openstack4j.api.storage.ObjectStorageAccountService;
import org.openstack4j.model.storage.object.SwiftAccount;
import org.openstack4j.openstack.storage.object.functions.MetadataToHeadersFunction;
import org.openstack4j.openstack.storage.object.functions.ParseAccountFunction;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.openstack4j.model.storage.object.SwiftHeaders.*;

/**
 * The Object Storage Account based services
 *
 * @author Jeremy Unruh
 */
public class ObjectStorageAccountServiceImpl extends BaseObjectStorageService implements ObjectStorageAccountService {

    /**
     * {@inheritDoc}
     */
    @Override
    public SwiftAccount get() {
        return ParseAccountFunction.INSTANCE.apply(head(Void.class).executeWithResponse());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateMetadata(Map<String, String> metadata) {
        checkNotNull(metadata);
        return invokeMetadata(ACCOUNT_METADATA_PREFIX, metadata);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteMetadata(Map<String, String> metadata) {
        checkNotNull(metadata);
        return invokeMetadata(ACCOUNT_REMOVE_METADATA_PREFIX, metadata);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateTemporaryUrlKey(String temporaryUrlKey) {
        checkNotNull(temporaryUrlKey);
        return isResponseSuccess(post(Void.class).header(ACCOUNT_TEMPORARY_URL_KEY, temporaryUrlKey).executeWithResponse(), 204);
    }

    private boolean invokeMetadata(String prefix, Map<String, String> metadata) {
        return isResponseSuccess(post(Void.class)
                .headers(MetadataToHeadersFunction.create(prefix).apply(metadata))
                .executeWithResponse(), 204);
    }
}
