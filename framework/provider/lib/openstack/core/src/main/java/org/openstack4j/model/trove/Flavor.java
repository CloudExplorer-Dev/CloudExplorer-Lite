package org.openstack4j.model.trove;

import org.openstack4j.model.ModelEntity;

/**
 * Flavor Model Entity
 *
 * @author sumit gandhi
 */
public interface Flavor extends ModelEntity {

    String getName();

    String getId();

    String getStrId();

    int getRam();

    int getVcpus();

    int getDisk();
}
