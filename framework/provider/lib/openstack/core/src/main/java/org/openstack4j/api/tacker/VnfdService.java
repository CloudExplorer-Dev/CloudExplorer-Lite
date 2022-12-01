package org.openstack4j.api.tacker;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.tacker.Vnfd;

import java.util.List;
import java.util.Map;

/**
 * <p>Tacker - OpenStack NFV Orchestration</p>
 *
 * <p>Tacker is an official OpenStack project building a Generic VNF Manager (VNFM) and a NFV Orchestrator (NFVO) to deploy and operate
 * Network Services and Virtual Network Functions (VNFs) on an NFV infrastructure platform like OpenStack.
 * It is based on ETSI MANO Architectural Framework and provides a functional stack to Orchestrate Network Services end-to-end using VNFs.</p>
 *
 * <p>VNFD Stands for Virtual Network Functions Descriptor. This is the VNF Catalog Management (TOSCA-YAML Template) API Service..</p>
 *
 * <p>
 * <u>NFV Catalog</u>:
 * <ul>
 * 		<li>VNF Descriptors.</li>
 * 		<li>Network Services Decriptors.</li>
 * 		<li>VNF Forwarding Graph Descriptors.</li>
 * </ul>
 * </p>
 *
 * @author Vishvesh Deshmukh
 * @date Aug 11, 2016
 * @see <a href="http://docs.openstack.org/developer/tacker/devref/mano_api.html">Official Tacker Documentation</a>
 * @see <a href="https://github.com/openstack/tacker/blob/master/doc/source/devref/mano_api.rst">Official GitHub Tacker Reference</a>
 */
public interface VnfdService extends RestService {

    /**
     * List vnfs - Lists instantiated vnfs in VNF Manager
     *
     * @return list of all Vnfd(s)
     */
    List<? extends Vnfd> list();

    /**
     * List vnfs - Lists instantiated vnfs in VNF Manager filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     * @return filtered list of Vnfd(s)
     */
    List<? extends Vnfd> list(Map<String, String> filteringParams);

    /**
     * Show vnfd - Show information for a specified vnfdId.
     *
     * @param vnfdId the Vnfd identifier
     * @return the Vnfd or null if not found
     */
    Vnfd get(String vnfdId);

    /**
     * Delete vnfd - Deletes a specified vnfdId from the VNF catalog.
     *
     * @param vnfdId the Vnfd identifier
     * @return the action response
     */
    ActionResponse delete(String vnfdId);

    /**
     * Create vnfd - Create a vnfd entry based on the vnfd template.
     *
     * @return Vnfd
     */
    Vnfd create(Vnfd vnfd);

}
