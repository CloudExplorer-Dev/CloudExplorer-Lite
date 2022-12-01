package org.openstack4j.openstack.identity.v2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.identity.v2.Service;
import org.openstack4j.model.identity.v2.builder.ServiceBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * OpenStack service, such as Compute (Nova), Object Storage (Swift), or Image Service (Glance).
 * A service provides one or more endpoints through which users can access resources and perform
 *
 * @author Jeremy Unruh
 */
@JsonRootName("OS-KSADM:service")
public class KeystoneService implements Service {

    private static final long serialVersionUID = 1L;

    String id;
    String type;
    String name;
    String description;

    public static ServiceBuilder builder() {
        return new ServiceConcreteBuilder();
    }

    @Override
    public ServiceBuilder toBuilder() {
        return new ServiceConcreteBuilder(this);
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("name", name).add("type", type).add("description", description)
                .toString();
    }

    public static class Services extends ListResult<KeystoneService> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("OS-KSADM:services")
        private List<KeystoneService> list;

        public List<KeystoneService> value() {
            return list;
        }
    }

    public static class ServiceConcreteBuilder implements ServiceBuilder {

        private KeystoneService model;

        ServiceConcreteBuilder() {
            this(new KeystoneService());
        }

        ServiceConcreteBuilder(KeystoneService model) {
            this.model = model;
        }

        public ServiceBuilder name(String name) {
            model.name = name;
            return this;
        }

        public ServiceBuilder type(String type) {
            model.type = type;
            return this;
        }

        public ServiceBuilder description(String description) {
            model.description = description;
            return this;
        }

        @Override
        public Service build() {
            return model;
        }

        @Override
        public ServiceBuilder from(Service in) {
            model = (KeystoneService) in;
            return this;
        }

    }

}
