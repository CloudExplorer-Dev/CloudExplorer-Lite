package org.openstack4j.model.gbp.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.gbp.L3Policy;

import java.util.List;

/**
 * A builder which produces a L3Policies object
 *
 * @author vinod borole
 */
public interface L3PolicyBuilder extends Builder<L3PolicyBuilder, L3Policy> {

    L3PolicyBuilder name(String name);

    L3PolicyBuilder description(String description);

    L3PolicyBuilder ipVersion(int ipVersion);

    L3PolicyBuilder ippool(String ippool);

    L3PolicyBuilder subnetPrefixLength(String subnetPrefixLength);

    L3PolicyBuilder isShared(boolean shared);

    L3PolicyBuilder externalSegments(List<String> extSegmentIds);
}
