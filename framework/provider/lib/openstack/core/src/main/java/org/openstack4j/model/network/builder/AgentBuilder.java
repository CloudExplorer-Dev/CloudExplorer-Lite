package org.openstack4j.model.network.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.network.Agent;

/**
 * <p>
 * <b>Copyright:</b> Copyright (c) 2016
 * </p>
 * <p>
 * <b>Company:</b> Ericsson
 * </p>
 *
 * @author emagnbr 2016- initial version
 */
public interface AgentBuilder extends Builder<AgentBuilder, Agent> {

    /**
     * Sets the admin_state_up
     *
     * @param state the state of admin_state_up
     * @return the agent builder
     */
    AgentBuilder adminStateUp(boolean state);

}
