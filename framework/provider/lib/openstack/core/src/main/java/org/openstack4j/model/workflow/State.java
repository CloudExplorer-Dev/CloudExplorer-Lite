package org.openstack4j.model.workflow;

/**
 * Created by Renat Akhmerov.
 */
public enum State {
    IDLE,
    WAITING,
    RUNNING,
    RUNNING_DELAYED,
    PAUSED,
    SUCCESS,
    CANCELLED,
    ERROR;
}
