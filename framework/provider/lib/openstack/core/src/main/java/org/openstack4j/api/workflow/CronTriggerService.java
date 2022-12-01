package org.openstack4j.api.workflow;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.workflow.CronTrigger;

import java.util.List;

/**
 * Service that provides CRUD operations for cron triggers.
 *
 * @author Renat Akhmerov
 */
public interface CronTriggerService extends RestService {
    /**
     * List all cron triggers with details.
     *
     * @return List of cron triggers.
     */
    List<? extends CronTrigger> list();

    /**
     * Create a new cron trigger.
     *
     * @param cronTrigger Cron trigger to create.
     * @return Created cron trigger.
     */
    CronTrigger create(CronTrigger cronTrigger);

    /**
     * Get cron trigger by its ID.
     *
     * @param id Cron trigger ID.
     * @return Cron trigger.
     */
    CronTrigger get(String id);

    /**
     * Delete cron trigger by its ID.
     *
     * @param id Cron trigger ID.
     * @return Action response from the server.
     */
    ActionResponse delete(String id);
}
