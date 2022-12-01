package org.openstack4j.openstack.sahara.internal;

import org.openstack4j.api.sahara.ClusterTemplateService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.sahara.ClusterTemplate;
import org.openstack4j.openstack.sahara.domain.SaharaClusterTemplate;
import org.openstack4j.openstack.sahara.domain.SaharaClusterTemplate.ClusterTemplates;
import org.openstack4j.openstack.sahara.domain.SaharaClusterTemplateUnwrapped;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Sahara Data Processing Operations
 *
 * @author Ekasit Kijsipongse
 */
public class ClusterTemplateServiceImpl extends BaseSaharaServices implements ClusterTemplateService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ClusterTemplate> list() {
        return get(ClusterTemplates.class, uri("/cluster-templates")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClusterTemplate get(String templateId) {
        checkNotNull(templateId);
        return get(SaharaClusterTemplate.class, uri("/cluster-templates/%s", templateId)).execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ClusterTemplate create(ClusterTemplate template) {
        checkNotNull(template);
        SaharaClusterTemplateUnwrapped unwrapped = new SaharaClusterTemplateUnwrapped(template);
        return post(SaharaClusterTemplate.class, uri("/cluster-templates"))
                .entity(unwrapped)  // setup request
                .execute();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String templateId) {
        checkNotNull(templateId);
        return deleteWithResponse(uri("/cluster-templates/%s", templateId)).execute();
    }

}
