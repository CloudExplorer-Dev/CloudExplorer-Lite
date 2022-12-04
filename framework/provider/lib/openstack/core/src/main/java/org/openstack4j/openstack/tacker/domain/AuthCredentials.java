package org.openstack4j.openstack.tacker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;

/**
 * @author Vishvesh Deshmukh
 * @date Aug 18, 2016
 */
@JsonRootName("auth_cred")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthCredentials {

    private String username;

    @JsonProperty("project_name")
    private String projectName;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("user_domain_name")
    private String userDomainName;

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("auth_url")
    private String authUrl;

    private String password;

    @JsonProperty("project_domain_name")
    private String projectDomainName;

    public static AuthCredentials create() {
        return new AuthCredentials();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("username", username).add("projectName", projectName)
                .add("userId", userId).add("userDomainName", userDomainName).add("projectId", projectId)
                .add("authUrl", authUrl).add("password", password).add("projectDomainName", projectDomainName)
                .toString();
    }

    /**
     * Username to Set
     *
     * @param username the username to set
     * @return AuthCredentials
     */
    public AuthCredentials username(String username) {
        this.username = username;
        return this;
    }

    /**
     * password to Set
     *
     * @param password the password to set
     * @return AuthCredentials
     */
    public AuthCredentials password(String password) {
        this.password = password;
        return this;
    }

    /**
     * projectName to Set
     *
     * @param projectName the projectName to set
     * @return AuthCredentials
     */
    public AuthCredentials projectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    /**
     * userId to Set
     *
     * @param userId the userId to set
     * @return AuthCredentials
     */
    public AuthCredentials userId(String userId) {
        this.userId = userId;
        return this;
    }

    /**
     * authUrl to Set
     *
     * @param authUrl the authUrl to set
     * @return AuthCredentials
     */
    public AuthCredentials authUrl(String authUrl) {
        this.authUrl = authUrl;
        return this;
    }

    /**
     * projectId to Set
     *
     * @param projectId the projectId to set
     * @return AuthCredentials
     */
    public AuthCredentials projectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    /**
     * projectDomainName to Set
     *
     * @param projectDomainName the projectDomainName to set
     * @return AuthCredentials
     */
    public AuthCredentials projectDomainName(String projectDomainName) {
        this.projectDomainName = projectDomainName;
        return this;
    }

    /**
     * userDomainName to Set
     *
     * @param userDomainName the userDomainName to set
     * @return AuthCredentials
     */
    public AuthCredentials userDomainName(String userDomainName) {
        this.userDomainName = userDomainName;
        return this;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @return the userDomainName
     */
    public String getUserDomainName() {
        return userDomainName;
    }

    /**
     * @return the projectId
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * @return the authUrl
     */
    public String getAuthUrl() {
        return authUrl;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the projectDomainName
     */
    public String getProjectDomainName() {
        return projectDomainName;
    }
}
