package org.openstack4j.model.sahara;

import org.openstack4j.model.ModelEntity;

import java.util.List;
import java.util.Map;

/**
 * A Sahara Plugin
 *
 * @author ekasit.kijsipongse@nectec.or.th
 */
public interface Plugin extends ModelEntity {

    /**
     * @return the plugin description
     */
    String getDescription();

    /**
     * @return the available plugin versions
     */
    List<String> getVersions();

    /**
     * @return the name of the plugin
     */
    String getName();

    /**
     * @return the title of the plugin
     */
    String getTitle();

    /**
     * @return the list of processes in a specific service (node_processes in Sahara plugin terminology)
     */
    Map<String, List<String>> getServiceProcesses();


    /**
     * @return the list of required image tags
     */
    List<String> getRequiredImageTags();

    /**
     * @return the list of config information (definitions and default values)
     */
    List<? extends ConfigInfo> getConfigInfos();

}
