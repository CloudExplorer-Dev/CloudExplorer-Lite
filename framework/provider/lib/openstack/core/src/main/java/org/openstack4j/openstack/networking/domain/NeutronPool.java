package org.openstack4j.openstack.networking.domain;

import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.Pool;

/**
 * An IP Address Pool which has a starting network and a ending network which becomes a pool of addresses
 *
 * @author Jeremy Unruh
 */
public class NeutronPool implements Pool {

    private static final long serialVersionUID = 1L;

    private String start;
    private String end;

    public NeutronPool() {
    }

    public NeutronPool(String start, String end) {
        this.start = start;
        this.end = end;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStart() {
        return start;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEnd() {
        return end;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("start", start).add("end", end).toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return java.util.Objects.hash(start, end);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof NeutronPool) {
            NeutronPool that = (NeutronPool) obj;
            if (java.util.Objects.equals(start, that.start) &&
                    java.util.Objects.equals(end, that.end)) {
                return true;
            }
        }
        return false;
    }
}


