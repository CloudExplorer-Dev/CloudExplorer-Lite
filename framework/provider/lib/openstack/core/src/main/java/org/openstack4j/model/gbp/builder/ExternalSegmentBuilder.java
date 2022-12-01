package org.openstack4j.model.gbp.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.gbp.ExternalRoutes;
import org.openstack4j.model.gbp.ExternalSegment;

import java.util.List;

/**
 * A builder which produces a External Segments object
 *
 * @author vinod borole
 */
public interface ExternalSegmentBuilder extends Builder<ExternalSegmentBuilder, ExternalSegment> {

    ExternalSegmentBuilder name(String name);

    ExternalSegmentBuilder description(String description);

    ExternalSegmentBuilder externalPolicies(List<String> extPolicyIds);

    ExternalSegmentBuilder ipVersion(int ipVersion);

    ExternalSegmentBuilder cidr(String cidr);

    ExternalSegmentBuilder isShared(boolean shared);

    ExternalSegmentBuilder externalRoutes(List<ExternalRoutes> extRoutes);

    ExternalSegmentBuilder subnetId(String subnetId);

    ExternalSegmentBuilder isPortAddressTranslation(boolean isPortAddressTranslation);
}   
