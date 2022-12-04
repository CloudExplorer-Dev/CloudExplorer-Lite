package org.openstack4j.model.heat;

import org.openstack4j.model.ModelEntity;

import java.util.Map;

/**
 * Base interface for Stack Creation or Update based API Services
 *
 * @author Jeremy Unruh
 */
public interface BaseStackCreateUpdate extends ModelEntity {

    // Future versions: Replace with Template-Object

    /**
     * Returns the Heat template if it was stored in JSON format or YAML format
     *
     * @return the JSON or YAML formatted template out of which the stack is to be
     * created. Returns <code> null </code> if no JSON formatted template has been set.
     */
    String getTemplate();

    /**
     * Returns the parameters which are used for creation of the stack
     *
     * @return Map of parameters. This map is <code> null </code> if no
     * parameter has been set. Returns empty if no parameter has been
     * set.
     */
    Map<String, String> getParameters();
}
