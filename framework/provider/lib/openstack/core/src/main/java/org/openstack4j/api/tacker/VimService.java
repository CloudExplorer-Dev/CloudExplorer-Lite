package org.openstack4j.api.tacker;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.tacker.Vim;

import java.util.List;
import java.util.Map;

/**
 * <p>Tacker - OpenStack NFV Orchestration</p>
 *
 * <p>Tacker is an official OpenStack project building a Generic VIM Manager (VIMM) and a NFV Orchestrator (NFVO) to deploy and operate
 * Network Services and Virtual Network Functions (VIMs) on an NFV infrastructure platform like OpenStack.
 * It is based on ETSI MANO Architectural Framework and provides a functional stack to Orchestrate Network Services end-to-end using VIMs.</p>
 *
 * <p>
 * <u>NFVO</u>:
 * <ul>
 * 		<li>Templatized end-to-end Network Service deployment using decomposed VIMs.</li>
 * 		<li>VIM placement policy – ensure efficient placement of VIMs.</li>
 * 		<li>VIMs connected using an SFC - described in a VIM Forwarding Graph Descriptor.</li>
 * 		<li>VIM Resource Checks and Resource Allocation.</li>
 * 		<li>Ability to orchestrate VIMs across Multiple VIMs and Multiple Sites (POPs).</li>
 * </ul>
 * </p>
 *
 * @author Vishvesh Deshmukh
 * @date Aug 18, 2016
 * @see <a href="http://docs.openstack.org/developer/tacker/devref/mano_api.html">Official Tacker Documentation</a>
 * @see <a href="https://github.com/openstack/tacker/blob/master/doc/source/devref/mano_api.rst">Official GitHub Tacker Reference</a>
 */
public interface VimService extends RestService {

    /**
     * List vims - Lists instantiated vims in VIM Manager
     *
     * @return list of all Vim(s)
     */
    List<? extends Vim> list();

    /**
     * List vims - Lists instantiated vims in VIM Manager filtered by parameters.
     *
     * @param filteringParams map (name, value) of filtering parameters
     * @return filtered list of Vim(s)
     */
    List<? extends Vim> list(Map<String, String> filteringParams);

    /**
     * Show Vim - Show information for a specified vimId.
     *
     * @param vimId the Vim identifier
     * @return the Vim or null if not found
     */
    Vim show(String vimId);

    /**
     * Delete Vim - Deletes a specified vimId from the VIM catalog.
     *
     * @param vimId the Vim identifier
     * @return the action response
     */
    ActionResponse delete(String vimId);

    /**
     * Create Vim - Register a Vim entry based on the Vim template.
     *
     * @return Vim
     */
    Vim register(Vim vim);

    /**
     * Update vim - Update a vim based on user config file or data.
     * @param vimId the Vim identifier
     * @param vimUpdate VimUpdate
     * @return Vim
     */
    //Vim update(String vimId, VimUpdate vimUpdate);

}
