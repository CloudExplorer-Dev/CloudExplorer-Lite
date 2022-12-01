package org.openstack4j.model.common;

import java.util.Date;

/**
 * A simple entity which supports time data
 *
 * @author bboyHan
 */
public interface TimeEntity {

    /**
     * created time
     *
     * @return created time
     */
    Date getCreatedTime();

    /**
     * updated time
     *
     * @return updated time
     */
    Date getUpdatedTime();

}
