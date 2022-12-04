package org.openstack4j.openstack.dns.v2.builder;

import org.openstack4j.model.dns.v2.builder.DNSV2Builders;
import org.openstack4j.model.dns.v2.builder.RecordsetBuilder;
import org.openstack4j.model.dns.v2.builder.ZoneBuilder;
import org.openstack4j.openstack.dns.v2.domain.DesignateRecordset;
import org.openstack4j.openstack.dns.v2.domain.DesignateZone;

/**
 * The Designate V2 Builders
 */
public class DesignateV2Builders implements DNSV2Builders {

    private DesignateV2Builders DesignateV2Builders() {
        return this;
    }

    @Override
    public ZoneBuilder zone() {
        return DesignateZone.builder();
    }

    @Override
    public RecordsetBuilder recordset() {
        return DesignateRecordset.builder();
    }
}
