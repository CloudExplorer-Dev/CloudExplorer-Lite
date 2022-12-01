package org.openstack4j.openstack;

import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.client.CloudProvider;
import org.openstack4j.api.client.IOSClientBuilder;
import org.openstack4j.api.types.Facing;
import org.openstack4j.core.transport.Config;
import org.openstack4j.core.transport.internal.HttpLoggingFilter;
import org.openstack4j.model.identity.v2.Access;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.openstack.client.OSClientBuilder;
import org.openstack4j.openstack.internal.OSClientSession.OSClientSessionV2;
import org.openstack4j.openstack.internal.OSClientSession.OSClientSessionV3;

/**
 * A Factory which sets up the APIs to be used a previously non-expired authorization or new authorization.
 *
 * @author Jeremy Unruh
 */
public abstract class OSFactory<T extends OSFactory<T>> {

    private OSFactory() {
    }

    /**
     * Skips Authentication and created the API around a previously cached Token object.  This can be useful in multi-threaded environments
     * or scenarios where a client should not be re-authenticated due to a token lasting 24 hours.
     *
     * @param token an authorized token entity which is to be used to create the API
     * @return the OSClient
     */
    public static OSClientV3 clientFromToken(Token token) {
        return OSClientSessionV3.createSession(token);
    }

    /**
     * Skips Authentication and created the API around a previously cached Token object.  This can be useful in multi-threaded environments
     * or scenarios where a client should not be re-authenticated due to a token lasting 24 hours
     *
     * @param token       an authorized token entity which is to be used to create the API
     * @param perspective the current endpoint perspective to use
     * @return the OSClient
     */
    public static OSClientV3 clientFromToken(Token token, Facing perspective) {
        return OSClientSessionV3.createSession(token, perspective, null, null);
    }

    /**
     * Skips Authentication and created the API around a previously cached Token object.  This can be useful in multi-threaded environments
     * or scenarios where a client should not be re-authenticated due to a token lasting 24 hours
     *
     * @param token  an authorized token entity which is to be used to create the API
     * @param config OpenStack4j configuration options
     * @return the OSClient
     */
    public static OSClientV3 clientFromToken(Token token, Config config) {
        return OSClientSessionV3.createSession(token, null, null, config);
    }

    /**
     * Skips Authentication and created the API around a previously cached Token object.  This can be useful in multi-threaded environments
     * or scenarios where a client should not be re-authenticated due to a token lasting 24 hours
     *
     * @param token       an authorized token entity which is to be used to create the API
     * @param perspective the current endpoint perspective to use
     * @param config      OpenStack4j configuration options
     * @return the OSClient
     */
    public static OSClientV3 clientFromToken(Token token, Facing perspective, Config config) {
        return OSClientSessionV3.createSession(token, perspective, null, config);
    }

    /**
     * Skips Authentication and created the API around a previously cached Token object.  This can be useful in multi-threaded environments
     * or scenarios where a client should not be re-authenticated due to a token lasting 24 hours
     *
     * @param token       an authorized token entity which is to be used to create the API
     * @param perspective the current endpoint perspective to use
     * @param provider    the cloud provider
     * @param config      OpenStack4j configuration options
     * @return the OSClient
     */
    public static OSClientV3 clientFromToken(Token token, Facing perspective, CloudProvider provider, Config config) {
        return OSClientSessionV3.createSession(token, perspective, provider, config);
    }

    /**
     * Skips Authentication and created the API around a previously cached Access object.  This can be useful in multi-threaded environments
     * or scenarios where a client should not be re-authenticated due to a token lasting 24 hours
     *
     * @param access an authorized access entity which is to be used to create the API
     * @return the OSCLient
     */
    public static OSClientV2 clientFromAccess(Access access) {
        return OSClientSessionV2.createSession(access);
    }

    /**
     * Skips Authentication and created the API around a previously cached Access object.  This can be useful in multi-threaded environments
     * or scenarios where a client should not be re-authenticated due to a token lasting 24 hours
     *
     * @param access      an authorized access entity which is to be used to create the API
     * @param perspective the current endpoint perspective to use
     * @return the OSCLient
     */
    public static OSClientV2 clientFromAccess(Access access, Facing perspective) {
        return OSClientSessionV2.createSession(access, perspective, null, null);
    }

    /**
     * Skips Authentication and created the API around a previously cached Access object.  This can be useful in multi-threaded environments
     * or scenarios where a client should not be re-authenticated due to a token lasting 24 hours
     *
     * @param access an authorized access entity which is to be used to create the API
     * @param config OpenStack4j configuration options
     * @return the OSCLient
     */
    public static OSClientV2 clientFromAccess(Access access, Config config) {
        return OSClientSessionV2.createSession(access, null, null, config);
    }

    /**
     * Skips Authentication and created the API around a previously cached Access object.  This can be useful in multi-threaded environments
     * or scenarios where a client should not be re-authenticated due to a token lasting 24 hours
     *
     * @param access      an authorized access entity which is to be used to create the API
     * @param perspective the current endpoint perspective to use
     * @param config      OpenStack4j configuration options
     * @return the OSCLient
     */
    public static OSClientV2 clientFromAccess(Access access, Facing perspective, Config config) {
        return OSClientSessionV2.createSession(access, perspective, null, config);
    }

    /**
     * Skips Authentication and created the API around a previously cached Access object.  This can be useful in multi-threaded environments
     * or scenarios where a client should not be re-authenticated due to a token lasting 24 hours
     *
     * @param access      an authorized access entity which is to be used to create the API
     * @param perspective the current endpoint perspective to use
     * @param provider    the cloud provider
     * @param config      OpenStack4j configuration options
     * @return the OSCLient
     */
    public static OSClientV2 clientFromAccess(Access access, Facing perspective, CloudProvider provider, Config config) {
        return OSClientSessionV2.createSession(access, perspective, provider, config);
    }

    /**
     * Globally enables or disables verbose HTTP Request and Response logging useful for debugging
     *
     * @param enabled true to enable, false to enable
     */
    public static void enableHttpLoggingFilter(boolean enabled) {
        HttpLoggingFilter.toggleLogging(enabled);
    }

    /**
     * Creates builder for OpenStack V2 based authentication
     *
     * @return V2 Authentication builder
     */
    public static IOSClientBuilder.V2 builderV2() {
        return new OSClientBuilder.ClientV2();
    }

    /**
     * Creates builder for OpenStack V3 based authentication
     *
     * @return V3 Authentication builder
     */
    public static IOSClientBuilder.V3 builderV3() {
        return new OSClientBuilder.ClientV3();
    }
}
