package org.openstack4j.openstack.networking.domain;

import com.google.common.base.MoreObjects;
import org.openstack4j.model.ModelEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Neutron Resource Tags Entity
 *
 * @author bboyHan
 */
public class NeutronResourceTag implements ModelEntity {

    private static final long serialVersionUID = 1L;

    private List<String> tags = new ArrayList<>();

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addTag(String... tags) {
        this.tags.addAll(Arrays.asList(tags));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("tags", tags).toString();
    }

}
