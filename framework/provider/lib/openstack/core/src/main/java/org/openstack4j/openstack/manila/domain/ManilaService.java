package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.manila.Service;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Represents a Manila service and their binary.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public class ManilaService implements Service {
    private Integer id;
    private Status status;
    private String binary;
    private String zone;
    private String host;
    private State state;
    @JsonProperty("updated_at")
    private String updatedAt;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public String getBinary() {
        return binary;
    }

    @Override
    public String getZone() {
        return zone;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public String getUpdatedAt() {
        return updatedAt;
    }

    public static class Services extends ListResult<ManilaService> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("services")
        private List<ManilaService> services;

        @Override
        protected List<ManilaService> value() {
            return services;
        }
    }

    /**
     * The response object for enable/disable service actions
     */
    public static class ServiceStatus implements ModelEntity {
        private String binary;
        private String host;
        private Boolean disabled;

        /**
         * @return the name of the enabled/disabled service binary
         */
        public String getBinary() {
            return binary;
        }

        /**
         * @return the host name of enabled/disabled service
         */
        public String getHost() {
            return host;
        }

        /**
         * @return whether the service is disabled or not
         */
        public Boolean getDisabled() {
            return disabled;
        }
    }
}
