package org.openstack4j.model.trove;

import java.util.Date;
import java.util.List;

public interface Instance {

    int getVolumeSize();

    String getVolumeType();

    Flavor getFlavor();

    String getName();

    Date getCreated();

    Date getUpdated();

    String getHostname();

    List<String> getIp();

    String getId();

    String getStatus();

    String getDatastoreType();

    String getDatastoreVersion();

}
