package org.openstack4j.model.compute;

/**
 * The type of reboot to perform when doing a Reboot based action
 *
 * @author Jeremy Unruh
 */
public enum RebootType {

    /**
     * Software-level reboot
     */
    SOFT,
    /**
     * Virtual power cycle hard reboot
     */
    HARD
}
