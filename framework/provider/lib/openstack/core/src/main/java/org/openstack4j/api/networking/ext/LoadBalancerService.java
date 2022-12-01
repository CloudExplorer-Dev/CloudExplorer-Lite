package org.openstack4j.api.networking.ext;

/**
 * LBaaS (Load Balancer as a Service) support
 */
public interface LoadBalancerService {

    /**
     * @return the Lbaas member Service API
     */
    MemberService member();

    /**
     * @return the Lbaas vip Service API
     */
    VipService vip();

    /**
     * @return the Lbaas healthmonitor Service API
     */
    HealthMonitorService healthMonitor();


    /**
     * @return the Lbaas pool Service API
     */
    LbPoolService lbPool();
}
