package org.openstack4j.openstack.identity.v3.domain;

/**
 * an v3 auth object
 *
 * @see <a href="http://developer.openstack.org/api-ref-identity-v3.html#authenticate">API reference</a>
 */
public abstract class Auth implements org.openstack4j.openstack.common.Auth {

    private static final long serialVersionUID = 1L;

    private String projectId;
    private String projectName;
    private String domainId;
    private String domainName;

    private Type type;

    protected Auth(Type type) {
        this.type = type;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Type getType() {
        return type;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
