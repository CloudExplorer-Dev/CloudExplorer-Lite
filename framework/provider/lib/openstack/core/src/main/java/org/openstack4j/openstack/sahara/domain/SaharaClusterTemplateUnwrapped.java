package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.sahara.ClusterTemplate;


/**
 * An OpenStack Sahara
 * Unwrap the root name of ClusterTemplate when serialized into json request
 *
 * @author Ekasit Kijsipongse
 */
public class SaharaClusterTemplateUnwrapped implements ModelEntity {

    private static final long serialVersionUID = 1L;

    @JsonUnwrapped
    //@JsonProperty("cluster_template")
    private ClusterTemplate template;

    public SaharaClusterTemplateUnwrapped(ClusterTemplate template) {
        this.template = template;
    }

    public ClusterTemplate getTemplate() { // need for serialization
        return template;
    }
}
