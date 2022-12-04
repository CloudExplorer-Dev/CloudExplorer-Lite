package org.openstack4j.api.networking.ext;

import org.openstack4j.common.RestService;

/**
 * Service Function Chain Operations API
 *
 * @author Dmitry Gerenrot
 */
public interface ServiceFunctionChainService extends RestService {

    /**
     * @return the Flow Classifier Service API
     */
    FlowClassifierService flowclassifiers();

    /**
     * @return the Port Pair Service API
     */
    PortPairService portpairs();

    /**
     * @return the Port Pair Service API
     */
    PortPairGroupService portpairgroups();

    /**
     * @return the Port Chain Service API
     */
    PortChainService portchains();
}
