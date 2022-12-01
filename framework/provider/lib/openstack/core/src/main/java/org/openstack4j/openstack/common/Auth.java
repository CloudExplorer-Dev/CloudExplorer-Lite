package org.openstack4j.openstack.common;

import org.openstack4j.model.ModelEntity;

public interface Auth extends ModelEntity {

    public enum Type {CREDENTIALS, TOKEN, RAX_APIKEY, TOKENLESS}

}
