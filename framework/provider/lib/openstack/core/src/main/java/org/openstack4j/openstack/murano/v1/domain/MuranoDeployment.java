package org.openstack4j.openstack.murano.v1.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.murano.v1.domain.Application;
import org.openstack4j.model.murano.v1.domain.Deployment;
import org.openstack4j.model.murano.v1.domain.DeploymentResult;
import org.openstack4j.model.murano.v1.domain.EnvironmentDescription;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * @author Nikolay Mahotkin.
 */
public class MuranoDeployment implements Deployment {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String state;

    @JsonProperty
    private String started;

    @JsonProperty
    private String finished;

    @JsonProperty("environment_id")
    private String environmentId;

    @JsonProperty
    private String id;

    @JsonProperty
    private MuranoEnvironmentDescription description;

    @JsonProperty
    private String created;

    @JsonProperty
    private String updated;

    @JsonProperty
    private MuranoDeploymentResult result;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getState() {
        return this.state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStarted() {
        return this.started;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFinished() {
        return this.finished;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEnvironmentId() {
        return this.environmentId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MuranoEnvironmentDescription getDescription() {
        return this.description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCreated() {
        return this.created;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUpdated() {
        return this.updated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MuranoDeploymentResult getResult() {
        return this.result;
    }

    public static class MuranoDeploymentResult implements DeploymentResult {
        @JsonProperty
        private boolean isException;
        @JsonProperty
        private MuranoInnerResult result;

        /**
         * {@inheritDoc}
         */
        public boolean isException() {
            return this.isException;
        }

        /**
         * {@inheritDoc}
         */
        public MuranoInnerResult getResult() {
            return this.result;
        }

        public static class MuranoInnerResult implements InnerResult {
            @JsonProperty
            private String details;

            @JsonProperty
            private String message;

            /**
             * {@inheritDoc}
             */
            public String getDetails() {
                return this.details;
            }

            /**
             * {@inheritDoc}
             */
            public String getMessage() {
                return this.message;
            }
        }
    }

    public static class MuranoEnvironmentDescription implements EnvironmentDescription {

        private static final long serialVersionUID = 1L;

        @JsonProperty
        private List<MuranoApplication> services;

        @JsonProperty
        private String name;

        @JsonProperty("defaultNetworks")
        private Map<String, Object> defaultNetworks;

        @JsonProperty("?")
        private Map<String, Object> envIdentities;

        /**
         * {@inheritDoc}
         */
        @Override
        public List<? extends Application> getServices() {
            return this.services;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getName() {
            return this.name;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Map<String, Object> getDefaultNetworks() {
            return this.defaultNetworks;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Map<String, Object> getEnvIdentities() {
            return this.envIdentities;
        }
    }

    public static class MuranoDeployments extends ListResult<MuranoDeployment> {
        private static final long serialVersionUID = 1L;

        @JsonProperty
        protected List<MuranoDeployment> deployments;

        protected List<MuranoDeployment> value() {
            return this.deployments;
        }
    }
}
