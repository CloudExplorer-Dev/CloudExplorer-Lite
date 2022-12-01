package org.openstack4j.model.heat.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.heat.StackCreate;

import java.util.Map;

/**
 * This interface describes a builder for {@link StackCreate} objects
 *
 * @author Matthias Reisser
 */
public interface StackCreateBuilder extends Buildable.Builder<StackCreateBuilder, StackCreate> {

    StackCreateBuilder name(String name);

    /**
     * Sets the template in YAML/JSON format.  If the template begins with a "{" then JSON is assumed
     *
     * @param template the template
     * @return StackCreateBuilder
     */
    StackCreateBuilder template(String template);

    /**
     * Sets the template URL
     *
     * @param templateURL the template URL
     * @return StackCreateBuilder
     */
    StackCreateBuilder templateURL(String templateURL);

    /**
     * Sets the parameters which are passed to the server. It might contain Information about flavor, image, etc.
     *
     * @param parameters Map of parameters. Key is name, value is the value of the parameters
     * @return the modified StackCreateBuilder
     */
    StackCreateBuilder parameters(Map<String, String> parameters);

    /**
     * Sets the stack creation timeout in minutes
     *
     * @param timeoutMins timeout in minutes
     * @return the modified StackCreateBuilder
     */
    StackCreateBuilder timeoutMins(Long timeoutMins);

    /**
     * sets the boolean for whether or not rollback is enabled or not
     *
     * @param disableRollback boolean value for disabling rollback
     * @return the modified StackCreateBuilder
     */
    StackCreateBuilder disableRollback(boolean disableRollback);

    /**
     * Sets the environment in YAML/JSON format.
     *
     * @param environment the environment
     * @return StackCreateBuilder
     */
    StackCreateBuilder environment(String environment);

    /**
     * Sets the environment in YAML/JSON format.
     *
     * @param environment file location
     * @return StackCreateBuilder
     */
    StackCreateBuilder environmentFromFile(String envFile);

    /**
     * Sets the template in YAML/JSON format.
     *
     * @param tplFile file location
     * @return StackCreateBuilder
     */
    StackCreateBuilder templateFromFile(String tplFile);

    /**
     * Sets the files parameter
     *
     * @param files map
     * @return StackCreateBuilder
     */
    StackCreateBuilder files(Map<String, String> files);

    /**
     * Set the tags for the stack, separated by a comma.
     *
     * @return StackCreateBuilder
     */
    StackCreateBuilder tags(String tags);

}
