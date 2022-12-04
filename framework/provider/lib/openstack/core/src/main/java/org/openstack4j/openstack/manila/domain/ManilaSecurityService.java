package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.manila.SecurityService;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * A security service stores configuration information for clients for authentication and authorization (AuthN/AuthZ).
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("security_service")
public class ManilaSecurityService implements SecurityService {
    private static final long serialVersionUID = 1L;

    private String status;
    private String id;
    @JsonProperty("project_id")
    private String projectId;
    private Type type;
    private String name;
    private String description;
    @JsonProperty("dns_ip")
    private String dnsIp;
    private String user;
    private String password;
    private String domain;
    private String server;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("share_networks")
    private List<String> shareNetworks;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProjectId() {
        return projectId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDnsIp() {
        return dnsIp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUser() {
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDomain() {
        return domain;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getServer() {
        return server;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getShareNetworks() {
        return shareNetworks;
    }

    public static class SecurityServices extends ListResult<ManilaSecurityService> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("security_services")
        private List<ManilaSecurityService> securityServices;

        @Override
        protected List<ManilaSecurityService> value() {
            return securityServices;
        }
    }
}
