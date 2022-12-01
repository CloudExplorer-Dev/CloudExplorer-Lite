package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.sahara.NodeGroupTemplate;


/**
 * An OpenStack Sahara
 * Unwrap the root name of NodeGroupTemplate when serialized into json request
 *
 * @author Ekasit Kijsipongse
 */
public class SaharaNodeGroupTemplateUnwrapped implements ModelEntity {

    private static final long serialVersionUID = 1L;

    @JsonUnwrapped
    //@JsonProperty("node_group_template")
    private NodeGroupTemplate template;

    public SaharaNodeGroupTemplateUnwrapped(NodeGroupTemplate template) {
        this.template = template;
    }

    public NodeGroupTemplate getTemplate() { // need for serialization
        return template;
    }
}
