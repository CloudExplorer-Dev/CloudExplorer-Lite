package org.openstack4j.model.network.ext;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.ext.builder.SessionPersistenceBuilder;

/**
 * Session persistence parameters for the VIP. Omit the session_persistence
 * parameter to prevent session persistence. When no session persistence is
 * used, the session_persistence parameter does not appear in the API response.
 * To clear session persistence for the VIP, set the session_persistence
 * parameter to null in a VIP update request.
 *
 * @author liujunpeng
 */
public interface SessionPersistence extends ModelEntity, Buildable<SessionPersistenceBuilder> {

    /**
     * optional
     *
     * @return cookie name
     */
    public String getCookieName();

    /**
     * required
     *
     * @return type. APP_COOKIE,HTTP_COOKIE,SOURCE_IP
     */
    public SessionPersistenceType getType();

}
