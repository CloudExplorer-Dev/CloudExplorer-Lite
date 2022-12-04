package org.openstack4j.model.senlin;

/**
 * Basic cluster status
 *
 * @author lion
 */
public enum ClusterStatus {
    /**
     * Initialization of the server
     */
    INIT,
    /**
     * Server is running
     */
    ACTIVE,
    /**
     * Server is creating
     */
    CREATING,
    /**
     * Server is updating
     */
    UPDATING,
    /**
     * Server is resizing
     */
    RESIZING,
    /**
     * Server is deleting
     */
    DELETING,
    /**
     * Server is checking
     */
    CHECKING,
    /**
     * Server is recovering
     */
    RECOVERING,
    /**
     * Server is critical
     */
    CRITICAL,
    /**
     * Failed in some conditions,eg.
     * failed to create
     */
    ERROR,
    /**
     * Warning
     */
    WARNING
}
