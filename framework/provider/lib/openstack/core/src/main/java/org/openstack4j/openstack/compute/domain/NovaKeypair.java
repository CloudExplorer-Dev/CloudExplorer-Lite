package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.openstack4j.model.compute.Keypair;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;

/**
 * An OpenStack Keypair is an SSH Key
 *
 * @author Jeremy Unruh
 */
@JsonRootName("keypair")
public class NovaKeypair implements Keypair {

    private static final long serialVersionUID = 1L;

    private String name;
    @JsonProperty("public_key")
    private String publicKey;
    @JsonProperty("private_key")
    private String privateKey;
    private String fingerprint;

    @JsonProperty("user_id")
    private String userId;
    private Boolean deleted;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonProperty("deleted_at")
    private Date deletedAt;
    private Integer id;

    /**
     * Used internally by the domain side of the API to create a new Keypair on an OpenStack server
     *
     * @param name      the name of the keypair
     * @param publicKey the public key or null to have OS generated one
     * @return NovaKeypair
     */
    public static NovaKeypair create(String name, String publicKey) {
        NovaKeypair kp = new NovaKeypair();
        kp.name = name;
        kp.publicKey = publicKey;
        return kp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFingerprint() {
        return fingerprint;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserId() {
        return this.userId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getDeleted() {
        return this.deleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getCreatedAt() {
        return this.createdAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDeletedAt() {
        return this.deletedAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("name", name).add("public_key", publicKey).add("private_key", privateKey).add("fingerprint", fingerprint)
                .add("user_id", userId).add("deleted", deleted).add("created_at", createdAt).add("updated_at", updatedAt)
                .add("deleted_at", deletedAt).add("id", id).toString();
    }

    public static class Keypairs extends ListResult<NovaKeypair> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("keypairs")
        private List<KeyPairWrapper> wrapped;
        private transient List<NovaKeypair> unwrapped;

        @Override
        protected List<NovaKeypair> value() {
            if (wrapped != null && unwrapped == null) {
                unwrapped = Lists.newArrayList();
                for (KeyPairWrapper kp : wrapped)
                    unwrapped.add(kp.keypair);
            }
            return unwrapped;
        }

        static final class KeyPairWrapper {
            @JsonProperty
            private NovaKeypair keypair;

        }

    }
}
