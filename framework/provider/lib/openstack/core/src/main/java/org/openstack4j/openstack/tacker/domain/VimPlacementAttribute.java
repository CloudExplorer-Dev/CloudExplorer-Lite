package org.openstack4j.openstack.tacker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;

import java.util.List;

/**
 * @author Vishvesh Deshmukh
 * @date Aug 16, 2016
 */
@JsonRootName("placement_attr")
@JsonIgnoreProperties(ignoreUnknown = true)
public class VimPlacementAttribute {

    private List<String> regions;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("regions", regions)
                .toString();
    }

    /**
     * @return the regions
     */
    public List<String> getRegions() {
        return regions;
    }
}
