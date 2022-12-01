package org.openstack4j.openstack.magnum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.magnum.Certificate;
import org.openstack4j.model.magnum.CertificateBuilder;
import org.openstack4j.openstack.common.GenericLink;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MagnumCertificate implements Certificate {
    private static final long serialVersionUID = 1L;
    @JsonProperty("pem")
    private String pem;
    @JsonProperty("bay_uuid")
    private String bayUuid;
    @JsonProperty("links")
    private List<GenericLink> links;

    public static CertificateBuilder builder() {
        return new CertificateConcreteBuilder();
    }

    @Override
    public CertificateBuilder toBuilder() {
        return new CertificateConcreteBuilder(this);
    }

    public String getPem() {
        return pem;
    }

    public String getBayUuid() {
        return bayUuid;
    }

    public List<GenericLink> getLinks() {
        return links;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("pem", pem).add("bayUuid", bayUuid).add("links", links)
                .toString();
    }

    /**
     * Concrete builder containing MagnumCertificate as model
     */
    public static class CertificateConcreteBuilder implements CertificateBuilder {
        MagnumCertificate model;

        public CertificateConcreteBuilder() {
            this(new MagnumCertificate());
        }

        public CertificateConcreteBuilder(MagnumCertificate model) {
            this.model = model;
        }

        @Override
        public Certificate build() {
            return model;
        }

        @Override
        public CertificateBuilder from(Certificate in) {
            if (in != null)
                this.model = (MagnumCertificate) in;
            return this;
        }

        @Override
        public CertificateBuilder pem(String pem) {
            model.pem = pem;
            return this;
        }

        @Override
        public CertificateBuilder bayUuid(String bayUuid) {
            model.bayUuid = bayUuid;
            return this;
        }

        @Override
        public CertificateBuilder links(List<GenericLink> links) {
            model.links = links;
            return this;
        }
    }

    public static class Certificates extends ListResult<MagnumCertificate> {
        private static final long serialVersionUID = 1L;
        @JsonProperty("certificates")
        private List<MagnumCertificate> list;

        @Override
        public List<MagnumCertificate> value() {
            return list;
        }
    }
}
