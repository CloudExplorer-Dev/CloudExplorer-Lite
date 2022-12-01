package org.openstack4j.model.sahara;

import org.openstack4j.model.ModelEntity;

import java.util.Date;

/**
 * An Action of Sahara Job Execution
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public interface JobExecutionAction extends ModelEntity {

    String getStatus();

    int getRetries();

    String getTransition();

    String getStats();

    Date getStartTime();

    Date getEndTime();

    String getCred();

    String getErrorMessage();

    String getExternalId();

    String getErrorCode();

    String getConsoleUrl();

    String getToString();

    String getExternalStatus();

    String getConf();

    String getType();

    String getTrackerUri();

    String getExternalChildIds();

    String getData();

    String getId();

    String getName();
}
