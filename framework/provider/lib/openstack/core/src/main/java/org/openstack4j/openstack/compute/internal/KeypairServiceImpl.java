package org.openstack4j.openstack.compute.internal;

import org.openstack4j.api.compute.KeypairService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.Keypair;
import org.openstack4j.openstack.compute.domain.NovaKeypair;
import org.openstack4j.openstack.compute.domain.NovaKeypair.Keypairs;

import javax.annotation.Nullable;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Keypair Service manages SSH Keys within OpenStack Compute (Nova).
 *
 * @author Jeremy Unruh
 */
public class KeypairServiceImpl extends BaseComputeServices implements KeypairService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Keypair> list() {
        return get(Keypairs.class, uri("/os-keypairs")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Keypair get(String name) {
        checkNotNull(name);
        return get(NovaKeypair.class, uri("/os-keypairs/%s", name)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String name) {
        checkNotNull(name);
        return deleteWithResponse(uri("/os-keypairs/%s", name)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Keypair create(String name, @Nullable String publicKey) {
        checkNotNull(name);
        return post(NovaKeypair.class, uri("/os-keypairs")).entity(NovaKeypair.create(name, publicKey)).execute();
    }

}
