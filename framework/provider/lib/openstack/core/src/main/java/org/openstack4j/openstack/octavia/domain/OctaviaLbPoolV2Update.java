package org.openstack4j.openstack.octavia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.octavia.LbMethod;
import org.openstack4j.model.octavia.LbPoolV2Update;
import org.openstack4j.model.octavia.SessionPersistence;
import org.openstack4j.model.octavia.builder.LbPoolV2UpdateBuilder;

/**
 * Used to update lbaas V2 lb pool
 *
 * @author wei
 */
@JsonRootName("pool")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OctaviaLbPoolV2Update implements LbPoolV2Update {
    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    @JsonProperty("admin_state_up")
    private boolean adminStateUp = true;

    @JsonProperty("lb_algorithm")
    private LbMethod lbMethod;

    @JsonProperty("session_persistence")
    private SessionPersistence sessionPersistence;

    public static LbPoolV2UpdateBuilder builder() {
        return new LbPoolV2UpdateContreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LbPoolV2UpdateBuilder toBuilder() {
        return new OctaviaLbPoolV2Update.LbPoolV2UpdateContreteBuilder(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isAdminStateUp() {
        return adminStateUp;
    }

    @Override
    public LbMethod getLbMethod() {
        return lbMethod;
    }

    @Override
    public SessionPersistence getSessionPersistence() {
        return sessionPersistence;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("adminStateUp", adminStateUp)
                .add("description", description)
                .add("lbMethod", lbMethod)
                .add("name", name)
                .toString();
    }

    public static class LbPoolV2UpdateContreteBuilder implements LbPoolV2UpdateBuilder {

        private OctaviaLbPoolV2Update m;

        public LbPoolV2UpdateContreteBuilder() {
            this(new OctaviaLbPoolV2Update());
        }

        public LbPoolV2UpdateContreteBuilder(OctaviaLbPoolV2Update m) {
            this.m = m;
        }

        @Override
        public LbPoolV2Update build() {

            return m;
        }

        @Override
        public LbPoolV2UpdateContreteBuilder from(LbPoolV2Update in) {
            m = (OctaviaLbPoolV2Update) in;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolV2UpdateContreteBuilder lbMethod(LbMethod lbMethod) {
            m.lbMethod = lbMethod;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolV2UpdateContreteBuilder name(String name) {
            m.name = name;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolV2UpdateContreteBuilder description(String description) {
            m.description = description;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public LbPoolV2UpdateBuilder sessionPersistence(SessionPersistence sessionPersistence) {
            m.sessionPersistence = sessionPersistence;
            return this;
        }

        @Override
        public LbPoolV2UpdateContreteBuilder adminStateUp(boolean adminStateUp) {
            m.adminStateUp = adminStateUp;
            return this;
        }
    }

}
