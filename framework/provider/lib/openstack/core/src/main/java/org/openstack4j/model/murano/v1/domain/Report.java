package org.openstack4j.model.murano.v1.domain;

import org.openstack4j.model.ModelEntity;

/**
 * @author Nikolay Mahotkin.
 */
public interface Report extends ModelEntity {
    /**
     * @return entity.
     */
    String getEntity();

    /**
     * @return entity id.
     */
    String getEntityId();

    /**
     * @return report level.
     */
    String getLevel();

    /**
     * @return Created date.
     */
    String getCreated();

    /**
     * @return Updated date.
     */
    String getUpdated();

    /**
     * @return report text.
     */
    String getText();

    /**
     * @return task id which deployment belongs to.
     */
    String getTaskId();

    /**
     * @return id of the report.
     */
    String getId();

    /**
     * @return report details.
     */
    String getDetails();
}
