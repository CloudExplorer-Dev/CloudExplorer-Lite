package org.openstack4j.model.identity.v3.builder;

/**
 * The Identity V3 builders
 */
public interface IdentityV3Builders {

    /**
     * The builder to create a Credential.
     *
     * @return the credential builder
     */
    public CredentialBuilder credential();

    /**
     * The builder to create a Domain.
     *
     * @return the domain builder
     */
    public DomainBuilder domain();

    /**
     * The builder to create a Endpoint.
     *
     * @return the endpoint builder
     */
    public EndpointBuilder endpoint();

    /**
     * The builder to create a Group.
     *
     * @return the group builder
     */
    public GroupBuilder group();

    /**
     * The builder to create a Policy.
     *
     * @return the policy builder
     */
    public PolicyBuilder policy();

    /**
     * The builder to create a Project.
     *
     * @return the project builder
     */
    public ProjectBuilder project();

    /**
     * The builder to create a Region.
     *
     * @return the region builder
     */
    public RegionBuilder region();

    /**
     * The builder to create a Role.
     *
     * @return the role builder
     */
    public RoleBuilder role();

    /**
     * The builder to create a Service.
     *
     * @return the service builder
     */
    public ServiceBuilder service();

    /**
     * The builder to create a User.
     *
     * @return the user builder
     */
    public UserBuilder user();

}
