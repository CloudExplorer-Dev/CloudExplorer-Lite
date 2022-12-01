package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.openstack.common.ListResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Nova OS Host contains a list of Nova Host Resources
 *
 * @author Qin An
 */
public class NovaHost extends ListResult<NovaHostResource> {

    public static final long serialVersionUID = 1L;

    @JsonProperty("host")
    List<NovaHostResourceBody> hostItems;

    @Override
    protected List<NovaHostResource> value() {
        List<NovaHostResource> hostResources = new ArrayList<NovaHostResource>();
        for (NovaHostResourceBody body : hostItems) {
            hostResources.add((NovaHostResource) body.getHostResource());
        }
        return hostResources;
    }

}
