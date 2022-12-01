package org.openstack4j.model.network;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.network.builder.ExtraDhcpOptBuilder;

/**
 * @author Ales Kemr
 */
public interface ExtraDhcpOptCreate extends Buildable<ExtraDhcpOptBuilder> {

    String getOptValue();

    String getOptName();
}
