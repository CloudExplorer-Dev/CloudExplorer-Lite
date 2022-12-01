package org.openstack4j.model.compute;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.compute.builder.ServerActionEventBuilder;

/**
 * Model for the generic events
 *
 * @author sujit sah
 */
public interface ServerActionEvent extends ModelEntity, Buildable<ServerActionEventBuilder> {
    /**
     * @return event
     */
    public String getEvent();

    /**
     * @return finish_time or null
     */
    public String getFinishTime();

    /**
     * @return result
     */
    public String getResult();

    /**
     * @return start_time or null
     */
    public String getStartTime();

    /**
     * @return traceback or null
     */
    public String getTraceback();
}
