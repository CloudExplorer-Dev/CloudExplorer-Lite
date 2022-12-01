package org.openstack4j.model.telemetry;

import java.util.Map;

public interface Capabilities {

    Map<String, Boolean> getAPI();

    Map<String, Boolean> getStorage();

    Map<String, Boolean> getEventStorage();
}
