package org.openstack4j.model.magnum;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.openstack.common.GenericLink;

import java.util.List;

public interface CertificateBuilder extends Builder<CertificateBuilder, Certificate> {
    /**
     * @see Certificate#getPem
     */
    CertificateBuilder pem(String pem);

    /**
     * @see Certificate#getBayUuid
     */
    CertificateBuilder bayUuid(String bayUuid);

    /**
     * @see Certificate#getLinks
     */
    CertificateBuilder links(List<GenericLink> links);

}
