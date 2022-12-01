package org.openstack4j.model.octavia.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.octavia.SessionPersistence;
import org.openstack4j.model.octavia.SessionPersistenceType;

/**
 * A builder to create and update a SessionPersistence
 *
 * @author wei
 */
public interface SessionPersistenceBuilder extends Builder<SessionPersistenceBuilder, SessionPersistence> {

    /**
     * required
     *
     * @param type APP_COOKIE,HTTP_COOKIE,SOURCE_IP
     * @return SessionPersistenceBuilder
     */
    public SessionPersistenceBuilder type(SessionPersistenceType type);

    /**
     * optional
     *
     * @return SessionPersistenceBuilder
     */
    public SessionPersistenceBuilder cookieName(String cookieName);
}
