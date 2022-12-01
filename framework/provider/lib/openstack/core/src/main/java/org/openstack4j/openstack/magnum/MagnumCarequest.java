package org.openstack4j.openstack.magnum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.magnum.Carequest;
import org.openstack4j.model.magnum.CarequestBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MagnumCarequest implements Carequest {
    private static final long serialVersionUID = 1L;
    @JsonProperty("bay_uuid")
    private String bayUuid;
    @JsonProperty("csr")
    private String csr;

    public static CarequestBuilder builder() {
        return new CarequestConcreteBuilder();
    }

    @Override
    public CarequestBuilder toBuilder() {
        return new CarequestConcreteBuilder(this);
    }

    public String getBayUuid() {
        return bayUuid;
    }

    public String getCsr() {
        return csr;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("bayUuid", bayUuid).add("csr", csr).toString();
    }

    /**
     * Concrete builder containing MagnumCarequest as model
     */
    public static class CarequestConcreteBuilder implements CarequestBuilder {
        MagnumCarequest model;

        public CarequestConcreteBuilder() {
            this(new MagnumCarequest());
        }

        public CarequestConcreteBuilder(MagnumCarequest model) {
            this.model = model;
        }

        @Override
        public Carequest build() {
            return model;
        }

        @Override
        public CarequestBuilder from(Carequest in) {
            if (in != null)
                this.model = (MagnumCarequest) in;
            return this;
        }

        @Override
        public CarequestBuilder bayUuid(String bayUuid) {
            model.bayUuid = bayUuid;
            return this;
        }

        @Override
        public CarequestBuilder csr(String csr) {
            model.csr = csr;
            return this;
        }
    }

    public static class Carequests extends ListResult<MagnumCarequest> {
        private static final long serialVersionUID = 1L;
        @JsonProperty("carequests")
        private List<MagnumCarequest> list;

        @Override
        public List<MagnumCarequest> value() {
            return list;
        }
    }
}
