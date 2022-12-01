package org.openstack4j.model.manila;

/**
 * Options used to update a security service.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public class SecurityServiceUpdateOptions {
    private SecurityService.Type type;
    private String name;
    private String description;
    private String dnsIp;
    private String user;
    private String password;
    private String domain;
    private String server;

    public static SecurityServiceUpdateOptions create() {
        return new SecurityServiceUpdateOptions();
    }

    /**
     * Update the security service type.
     *
     * @param type the security service type
     * @return SecurityServiceUpdateOptions
     */
    public SecurityServiceUpdateOptions type(SecurityService.Type type) {
        this.type = type;
        return this;
    }

    /**
     * Update the security service name.
     *
     * @param name the security service name
     * @return SecurityServiceUpdateOptions
     */
    public SecurityServiceUpdateOptions name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Update the security service description.
     *
     * @param description the security service description
     * @return SecurityServiceUpdateOptions
     */
    public SecurityServiceUpdateOptions description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Update the security service DNS IP.
     *
     * @param dnsIp the security service DNS IP
     * @return SecurityServiceUpdateOptions
     */
    public SecurityServiceUpdateOptions dnsIp(String dnsIp) {
        this.dnsIp = dnsIp;
        return this;
    }

    /**
     * Update the security service user.
     *
     * @param user the security service user
     * @return SecurityServiceUpdateOptions
     */
    public SecurityServiceUpdateOptions user(String user) {
        this.user = user;
        return this;
    }

    /**
     * Update the security service password.
     *
     * @param password the security service password
     * @return SecurityServiceUpdateOptions
     */
    public SecurityServiceUpdateOptions password(String password) {
        this.password = password;
        return this;
    }

    /**
     * Update the security service domain.
     *
     * @param domain the security service domain
     * @return SecurityServiceUpdateOptions
     */
    public SecurityServiceUpdateOptions domain(String domain) {
        this.domain = domain;
        return this;
    }

    /**
     * Update the security service server.
     *
     * @param server the security service server
     * @return SecurityServiceUpdateOptions
     */
    public SecurityServiceUpdateOptions server(String server) {
        this.server = server;
        return this;
    }

    public SecurityService.Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDnsIp() {
        return dnsIp;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDomain() {
        return domain;
    }

    public String getServer() {
        return server;
    }
}
