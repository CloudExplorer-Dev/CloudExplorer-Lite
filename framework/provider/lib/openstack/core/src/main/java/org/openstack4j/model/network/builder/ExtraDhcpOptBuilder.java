package org.openstack4j.model.network.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.ExtraDhcpOptCreate;

/**
 * @author Ales Kemr
 */
public interface ExtraDhcpOptBuilder extends Builder<ExtraDhcpOptBuilder, ExtraDhcpOptCreate> {

    ExtraDhcpOptBuilder optValue(String optValue);

    ExtraDhcpOptBuilder optName(String optName);

}
