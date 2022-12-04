package org.openstack4j.api.sahara;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.sahara.ClusterTemplate;
import org.openstack4j.model.sahara.Plugin;

import java.util.List;

/**
 * Sahara Data Processing Operations
 *
 * @author ekasit.kijsipongse@nectec.or.th
 */
public interface SaharaPluginService extends RestService {

    /**
     * List all plugins
     *
     * @return list of plugins registered in Sahara or empty
     */
    List<? extends Plugin> list();

    /**
     * Get a plugin by name
     *
     * @param name the plugin name
     * @return the plugin or null if not found
     */
    Plugin get(String name);

    /**
     * Get a specific plugin with all details by name and version
     *
     * @param name    the plugin name
     * @param version the plugin version
     * @return the plugin or null if not found
     */
    Plugin get(String name, String version);

    /**
     * Convert plugin specific config file into cluster template
     *
     * @param name         the plugin name
     * @param version      the plugin version
     * @param templateName the cluster template name
     * @param payload      the config file for the specific plugin
     * @return the cluster template
     */
    ClusterTemplate convertConfig(String name, String version, String templateName, Payload<?> payload);

}
