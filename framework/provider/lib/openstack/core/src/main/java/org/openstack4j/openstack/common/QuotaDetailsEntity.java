package org.openstack4j.openstack.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.common.QuotaDetails;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Quota Details Entity describing quota usage
 *
 * @author Jeremy Unruh
 */
public class QuotaDetailsEntity implements QuotaDetails {

    private static final long serialVersionUID = 1L;

    @JsonProperty("in_use")
    private int inUse;
    @JsonProperty("limit")
    private int limit;
    @JsonProperty("reserved")
    private int reserved;

    @Override
    public int getInUse() {
        return inUse;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public int getReserved() {
        return reserved;
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("in_use", inUse).add("limit", limit).add("reserved", reserved).toString();
    }

}
