package org.openstack4j.api.tacker;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.tacker.Vnf;
import org.openstack4j.model.tacker.VnfUpdate;

import java.util.List;
import java.util.Map;

/**
 * <p>Tacker - OpenStack NFV Orchestration</p>
 *
 * <p>Tacker is an official OpenStack project building a Generic VNF Manager (VNFM) and a NFV Orchestrator (NFVO) to deploy and operate
 * Network Services and Virtual Network Functions (VNFs) on an NFV infrastructure platform like OpenStack.
 * It is based on ETSI MANO Architectural Framework and provides a functional stack to Orchestrate Network Services end-to-end using VNFs.</p>
 *
 * <p>Vnf Stands for Virtual Network Functions. This is the VNF Management API Service which is used to Deploy VNFs..</p>
 *
 * <p>
 * <u>VNFM</u>:
 * <ul>
 * 		<li>Basic life-cycle of VNF (create/update/delete).</li>
 * 		<li>Enhanced platform-aware (EPA) placement of high-performance NFV workloads.</li>
 * 		<li>Health monitoring of deployed VNFs.</li>
 * 		<li>Auto Healing / Auto Scaling VNFs based on Policy.</li>
 * 		<li>Facilitate initial configuration of VNF.</li>
 * </ul>
 * </p>
 *
 * @author Vishvesh Deshmukh
 * @date Aug 11, 2016
 * @see <a href="http://docs.openstack.org/developer/tacker/devref/mano_api.html">Official Tacker Documentation</a>
 * @see <a href="https://github.com/openstack/tacker/blob/master/doc/source/devref/mano_api.rst">Official GitHub Tacker Reference</a>
 */
public interface VnfService extends RestService {

    /**
     * List vnfs - Lists instantiated vnfs in VNF Manager
     *
     * @return list of all Vnf(s)
     */
    List<? extends Vnf> list();

    /**
     * List vnfs - Lists instantiated vnfs in VNF Manager filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     * @return filtered list of Vnf(s)
     */
    List<? extends Vnf> list(Map<String, String> filteringParams);

    /**
     * Show Vnf - Show information for a specified VnfId.
     *
     * @param VnfId the Vnf identifier
     * @return the Vnf or null if not found
     */
    Vnf get(String vnfId);

    /**
     * Delete Vnf - Deletes a specified VnfId from the VNF catalog.
     *
     * @param VnfId the Vnf identifier
     * @return the action response
     */
    ActionResponse delete(String vnfId);

    /**
     * Create Vnf - Create a Vnf entry based on the Vnf template.
     *
     * @return Vnf
     */
    Vnf create(Vnf vnf);

    /**
     * Update vnf - Update a vnf based on user config file or data.
     *
     * @param vnfId     the Vnf identifier
     * @param vnfUpdate VnfUpdate
     * @return Vnf
     */
    Vnf update(String vnfId, VnfUpdate vnfUpdate);

}
