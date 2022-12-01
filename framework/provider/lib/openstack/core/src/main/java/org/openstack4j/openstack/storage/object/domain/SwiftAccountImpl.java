package org.openstack4j.openstack.storage.object.domain;

import com.google.common.base.MoreObjects;
import org.openstack4j.model.storage.object.SwiftAccount;

import java.util.Map;

/**
 * An Account representation for OpenStack Object Storage (Swift)
 *
 * @author Jeremy Unruh
 */
public class SwiftAccountImpl implements SwiftAccount {

    private static final long serialVersionUID = -5316007949264717108L;

    private long containerCount;
    private long objectCount;
    private long bytesUsed;
    private String temporaryUrlKey;
    private Map<String, String> metadata;

    protected SwiftAccountImpl() {
    }

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    @Override
    public long getContainerCount() {
        return containerCount;
    }

    @Override
    public long getObjectCount() {
        return objectCount;
    }

    @Override
    public long getBytesUsed() {
        return bytesUsed;
    }

    @Override
    public String getTemporaryUrlKey() {
        return temporaryUrlKey;
    }

    @Override
    public Map<String, String> getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("containerCount", containerCount).add("objectCount", objectCount)
                .add("bytesUsed", bytesUsed).add("temporaryUrlKey", temporaryUrlKey)
                .add("metadata", metadata)
                .toString();
    }

    public static class AccountBuilder {
        private SwiftAccountImpl account = new SwiftAccountImpl();

        public AccountBuilder containerCount(long containerCount) {
            account.containerCount = containerCount;
            return this;
        }

        public AccountBuilder objectCount(long objectCount) {
            account.objectCount = objectCount;
            return this;
        }

        public AccountBuilder bytesUsed(long bytesUsed) {
            account.bytesUsed = bytesUsed;
            return this;
        }

        public AccountBuilder temporaryUrlKey(String temporaryUrlKey) {
            account.temporaryUrlKey = temporaryUrlKey;
            return this;
        }

        public AccountBuilder metadata(Map<String, String> metadata) {
            account.metadata = metadata;
            return this;
        }

        public SwiftAccountImpl build() {
            return account;
        }
    }

}


