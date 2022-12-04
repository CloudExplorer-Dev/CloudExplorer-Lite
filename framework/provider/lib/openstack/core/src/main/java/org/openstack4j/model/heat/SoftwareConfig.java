package org.openstack4j.model.heat;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.heat.builder.SoftwareConfigBuilder;

import java.util.List;
import java.util.Map;

/**
 * Software Configuration Model
 *
 * @author Jeremy Unruh
 */
public interface SoftwareConfig extends ModelEntity, Buildable<SoftwareConfigBuilder> {

    /**
     * @return the configuration identifier
     */
    String getId();

    /**
     * The name of this configuration
     *
     * @return the name of this configuration
     */
    String getName();

    /**
     * Namespace that groups this software configuration by when it is delivered to a server.
     * This setting might imply which configuration tool performs the configuration.
     *
     * @return the namespace group
     */
    String getGroup();

    /**
     * Configuration script or manifest that defines which configuration is performed.
     *
     * @return the configuration script
     */
    String getConfig();

    /**
     * List of inputs that this software configuration expects
     *
     * @return list of inputs
     */
    List<? extends Input> getInputs();

    /**
     * List of outputs this software configuration produces
     *
     * @return list of outputs
     */
    List<? extends Output> getOutputs();

    /**
     * Map containing options specific to the configuration management tool used by this resource.
     *
     * @return map of options or null
     */
    Map<String, Object> getOptions();

    public interface Input {

        /**
         * @return the name of this input
         */
        String getName();

        /**
         * @return the description of this input
         */
        String getDescription();

        /**
         * @return default initialized value for this input
         */
        String getDefaultValue();

        /**
         * @return the input type
         */
        String getType();
    }

    public interface Output {

        /**
         * @return the name of this output
         */
        String getName();

        /**
         * @return the description of this output
         */
        String getDescription();

        /**
         * @return the data type of this output
         */
        String getType();

        /**
         * @return true if this is an error related output
         */
        boolean isErrorOutput();
    }

}
