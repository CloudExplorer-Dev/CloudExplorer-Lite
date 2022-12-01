package org.openstack4j.openstack.tacker.internal;

import org.openstack4j.api.tacker.VimService;
import org.openstack4j.core.transport.ExecutionOptions;
import org.openstack4j.core.transport.propagation.PropagateOnStatus;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.tacker.Vim;
import org.openstack4j.openstack.compute.functions.ToActionResponseFunction;
import org.openstack4j.openstack.tacker.domain.TackerVim;
import org.openstack4j.openstack.tacker.domain.TackerVim.TackerVims;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Vishvesh Deshmukh
 * @date Aug 18, 2016
 */
public class VimServiceImpl extends BaseTackerServices implements VimService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends TackerVim> list() {
        return get(TackerVims.class, uri("/vims")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends TackerVim> list(Map<String, String> filteringParams) {
        Invocation<TackerVims> req = get(TackerVims.class, uri("/vims"));
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                req = req.param(entry.getKey(), entry.getValue());
            }
        }
        return req.execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TackerVim show(String vimId) {
        checkNotNull(vimId);
        return get(TackerVim.class, uri("/vims/%s", vimId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String vimId) {
        checkNotNull(vimId);
        return ToActionResponseFunction.INSTANCE.apply(delete(Void.class, uri("/vims/%s", vimId)).executeWithResponse());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vim register(Vim vim) {
        return post(TackerVim.class, uri("/vims")).entity(vim).execute(ExecutionOptions.<TackerVim>create(PropagateOnStatus.on(500)));
    }

    /**
     * {@inheritDoc}
     */
	/*@Override
	public Vim update(String vimId, VimUpdate vimUpdate) {
		checkNotNull(vimId);
        checkNotNull(vimUpdate);
        return put(TackerVim.class, uri("/vims/%s", vimId)).entity(vimUpdate).execute();
	}*/
}
