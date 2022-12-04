package org.openstack4j.model.sahara;

import org.openstack4j.model.ModelEntity;

import java.util.Date;
import java.util.List;

/**
 * An Info of Sahara Job Execution
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public interface JobExecutionInfo extends ModelEntity {

    String getStatus();

    String getExternalId();

    int getRun();

    Date getStartTime();

    Date getEndTime();

    String getAppName();

    Date getLastModTime();

    List<? extends JobExecutionAction> getActions();

    String getAcl();

    String getConsoleUrl();

    String getAppPath();

    String getToString();

    String getUser();

    String getConf();

    String getParentId();

    Date getCreatedTime();

    String getGroup();

    String getId();
}
