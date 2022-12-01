package org.openstack4j.openstack.octavia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.octavia.SessionPersistence;
import org.openstack4j.model.octavia.SessionPersistenceType;
import org.openstack4j.model.octavia.builder.SessionPersistenceBuilder;

@JsonRootName("session_persistence")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OctaviaSessionPersistence implements SessionPersistence {

    private static final long serialVersionUID = 1L;
    @JsonProperty("cookie_name")
    private String cookieName;
    private SessionPersistenceType type;

    public static SessionPersistenceBuilder builder() {
        return new SessionPersistenceContreteBuilder();
    }

    /**
     * wrap the SessionPersistence to builder
     *
     * @return SessionPersistenceBuilder
     */
    @Override
    public SessionPersistenceBuilder toBuilder() {
        return new SessionPersistenceContreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCookieName() {
        return cookieName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionPersistenceType getType() {
        return type;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("type", type)
                .add("cookieName", cookieName)
                .toString();
    }

    /**
     * SessionPersistence Builder
     *
     * @author wei
     */
    public static class SessionPersistenceContreteBuilder implements SessionPersistenceBuilder {

        private OctaviaSessionPersistence m;

        public SessionPersistenceContreteBuilder() {
            this(new OctaviaSessionPersistence());
        }

        public SessionPersistenceContreteBuilder(OctaviaSessionPersistence m) {
            this.m = m;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SessionPersistenceBuilder from(SessionPersistence in) {
            m = (OctaviaSessionPersistence) in;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SessionPersistenceBuilder type(SessionPersistenceType type) {
            m.type = type;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SessionPersistenceBuilder cookieName(String cookieName) {
            m.cookieName = cookieName;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public SessionPersistence build() {
            return m;
        }

    }


}
