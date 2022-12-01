package org.openstack4j.model.telemetry;

import java.util.Date;
import java.util.Map;

public interface Resource {

    String getId();

    String getUserId();

    String getSource();

    Date getFirstSampleTimestamp();

    Date getLastSampleTimestamp();

    String getProjectId();

    Map<String, Object> getMeataData();
}
