package org.openstack4j.api.murano.v1;

import org.openstack4j.common.RestService;
import org.openstack4j.model.murano.v1.domain.Deployment;
import org.openstack4j.model.murano.v1.domain.Report;

import java.util.List;

/**
 * @author Nikolay Mahotkin.
 */
public interface MuranoDeploymentService extends RestService {
    /**
     * List all the deployments of the environment.
     *
     * @param environmentId The environment id.
     * @return Deployment list.
     */
    List<? extends Deployment> list(String environmentId);

    /**
     * Get the reports (deployment logs) of the specified deployment.
     *
     * @param environmentId environment id.
     * @param deploymentId  deployment id.
     * @param serviceIds    (optional) list of service ids.
     * @return Report list.
     */
    List<? extends Report> reports(String environmentId, String deploymentId, List<String> serviceIds);

    List<? extends Report> reports(String environmentId, String deploymentId);
}
