package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.openstack.common.GenericLink;

import java.util.List;

public interface Certificate extends ModelEntity, Buildable<CertificateBuilder> {
    /**
     * Gets pem
     *
     * @return pem
     */
    String getPem();

    /**
     * Gets bayUuid
     *
     * @return bayUuid
     */
    String getBayUuid();

    /**
     * Gets links
     *
     * @return links
     */
    List<GenericLink> getLinks();

}
