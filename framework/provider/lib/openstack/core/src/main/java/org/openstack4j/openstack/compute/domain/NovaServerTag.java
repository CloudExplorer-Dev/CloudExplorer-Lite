package org.openstack4j.openstack.compute.domain;

import com.google.common.base.MoreObjects;
import org.openstack4j.model.ModelEntity;

import java.util.ArrayList;
import java.util.List;

public class NovaServerTag implements ModelEntity {
    private static final long serialVersionUID = 1L;

    private List<String> tags = new ArrayList<String>();

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addTag(String... tags) {
        for (String tag : tags) {
            this.tags.add(tag);
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("tags", tags).toString();
    }

}
