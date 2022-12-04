package org.openstack4j.openstack.identity.v3.internal;

import org.openstack4j.api.identity.v3.UserService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.identity.v3.*;
import org.openstack4j.openstack.common.MapEntity;
import org.openstack4j.openstack.identity.v3.domain.KeystoneDomain;
import org.openstack4j.openstack.identity.v3.domain.KeystoneGroup.Groups;
import org.openstack4j.openstack.identity.v3.domain.KeystoneProject.Projects;
import org.openstack4j.openstack.identity.v3.domain.KeystoneRole.Roles;
import org.openstack4j.openstack.identity.v3.domain.KeystoneUser;
import org.openstack4j.openstack.identity.v3.domain.KeystoneUser.Users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.openstack4j.core.transport.ClientConstants.PATH_DOMAINS;
import static org.openstack4j.core.transport.ClientConstants.PATH_USERS;

/**
 * implementation of v3 user service
 */
public class UserServiceImpl extends BaseIdentityServices implements UserService {

    /**
     * {@inheritDoc}
     */
    @Override
    public User get(String userId) {
        checkNotNull(userId);
        return get(KeystoneUser.class, PATH_USERS, "/", userId).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends User> getByName(String userName) {
        checkNotNull(userName);
        return get(Users.class, uri(PATH_USERS)).param("name", userName).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getByName(String userName, String domainId) {
        checkNotNull(userName);
        checkNotNull(domainId);
        return get(Users.class, uri(PATH_USERS)).param("name", userName).param("domain_id", domainId).execute().first();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Domain getUserDomain(String userId) {
        return get(KeystoneDomain.class, PATH_DOMAINS, "/", get(userId).getDomainId()).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String userId) {
        checkNotNull(userId);
        return deleteWithResponse(PATH_USERS, "/", userId).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User update(User user) {
        checkNotNull(user);
        return patch(KeystoneUser.class, PATH_USERS, "/", user.getId()).entity(user).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User create(User user) {
        checkNotNull(user);
        return post(KeystoneUser.class, uri(PATH_USERS)).entity(user).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User create(String domainId, String name, String password, String email, boolean enabled) {
        checkNotNull(domainId);
        checkNotNull(name);
        checkNotNull(password);
        checkNotNull(email);
        checkNotNull(enabled);
        return create(KeystoneUser.builder().domainId(domainId).name(name).password(password).email(email).enabled(enabled).build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Group> listUserGroups(String userId) {
        checkNotNull(userId);
        return get(Groups.class, uri("/users/%s/groups", userId)).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Project> listUserProjects(String userId) {
        checkNotNull(userId);
        return get(Projects.class, uri("/users/%s/projects", userId)).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends User> list() {
        return get(Users.class, uri(PATH_USERS)).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Role> listProjectUserRoles(String userId, String projectId) {
        checkNotNull(userId);
        checkNotNull(projectId);
        return get(Roles.class, uri("projects/%s/users/%s/roles", projectId, userId)).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Role> listDomainUserRoles(String userId, String domainId) {
        checkNotNull(userId);
        checkNotNull(domainId);
        return get(Roles.class, uri("domains/%s/users/%s/roles", domainId, userId)).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse changePassword(String userId, String originalPassword, String password) {
        checkNotNull(userId);
        checkNotNull(originalPassword);
        checkNotNull(password);
        Map<String, Object> passwordMap = new HashMap<String, Object>();
        passwordMap.put("original_password", originalPassword);
        passwordMap.put("password", password);
        MapEntity mapEntity = MapEntity.create("user", passwordMap);
        return post(ActionResponse.class, uri("/users/%s/password", userId)).entity(mapEntity).execute();
    }

}
