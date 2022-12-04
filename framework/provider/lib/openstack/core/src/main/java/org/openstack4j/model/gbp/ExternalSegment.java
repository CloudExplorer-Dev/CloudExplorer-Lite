package org.openstack4j.model.gbp;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.gbp.builder.ExternalSegmentBuilder;
import org.openstack4j.openstack.gbp.domain.GbpExternalRoutes;

import java.util.List;

/**
 * External Segment Model Entity
 *
 * @author vinod borole
 */
public interface ExternalSegment extends Resource, Buildable<ExternalSegmentBuilder> {

    /**
     * Gets the external policies
     *
     * @return the external policies
     */
    List<String> getExternalPolicies();

    /**
     * Gets the Ip version
     *
     * @return the Ip version
     */
    int getIpVersion();

    /**
     * Gets the cidr
     *
     * @return the cidr
     */
    String getCidr();

    /**
     * Gets the description
     *
     * @return the description
     */
    String getDescription();

    /**
     * Is external segment shared
     *
     * @return the true if shared and false if not shared
     */
    boolean isShared();

    /**
     * Gets the subnet Id
     *
     * @return the subnet Id
     */
    String getSubnetId();

    /**
     * Gets the L3 policies
     *
     * @return the L3 Policies
     */
    List<String> getL3Policies();

    /**
     * Gets Is Port address Transalation
     *
     * @return True or False
     */
    boolean isPortAddressTranslation();

    /**
     * Gets the list of external routes
     *
     * @return the list of external routes
     */
    List<GbpExternalRoutes> getExternalRoutes();

    /**
     * Gets the list of nat pools
     *
     * @return the list of nat pools
     */
    List<String> getNatpools();

}
