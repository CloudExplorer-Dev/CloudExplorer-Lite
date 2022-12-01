package org.openstack4j.model.compute;

/**
 * Basic Actions against a Live Server
 *
 * @author Jeremy Unruh
 */
public enum Action {
    /**
     * Pause the server
     */
    PAUSE,
    /**
     * UnPause the paused server
     */
    UNPAUSE,
    /**
     * Stop the server
     */
    STOP,
    /**
     * Start the server
     */
    START,
    /**
     * Lock the server
     */
    LOCK,
    /**
     * Unlock a locked server
     */
    UNLOCK,
    /**
     * Suspend a server
     */
    SUSPEND,
    /**
     * Resume a suspended server
     */
    RESUME,
    /**
     * Rescue the server
     */
    RESCUE,
    /**
     * Unrescue the server
     */
    UNRESCUE,
    /**
     * Shelve the server
     */
    SHELVE,
    /**
     * Remove a shelved instance from the compute node
     */
    SHELVE_OFFLOAD,
    /**
     * Unshelve the server
     */
    UNSHELVE,
    /**
     * Force delete the server
     */
    FORCEDELETE
}
