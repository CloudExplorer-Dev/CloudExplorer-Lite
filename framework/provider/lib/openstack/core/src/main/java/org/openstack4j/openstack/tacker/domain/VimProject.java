package org.openstack4j.openstack.tacker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;

/**
 * @author Vishvesh Deshmukh
 * @date Aug 18, 2016
 */
@JsonRootName("vim_project")
@JsonIgnoreProperties(ignoreUnknown = true)
public class VimProject {

    private String id;

    private String name;

    @JsonProperty("project_domain_name")
    private String projectDomainName;

    public static VimProject create() {
        return new VimProject();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("name", name).add("projectDomainName", projectDomainName)
                .toString();
    }

    /**
     * ID to Set
     *
     * @param id the id to set
     * @return VimProject
     */
    public VimProject id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Project Name to Set
     *
     * @param name the name to set
     * @return VimProject
     */
    public VimProject name(String name) {
        this.name = name;
        return this;
    }

    /**
     * ProjectDomainName to Set
     *
     * @param projectDomainName the projectDomainName to set
     * @return VimProject
     */
    public VimProject projectDomainName(String projectDomainName) {
        this.projectDomainName = projectDomainName;
        return this;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the projectDomainName
     */
    public String getProjectDomainName() {
        return projectDomainName;
    }
}
