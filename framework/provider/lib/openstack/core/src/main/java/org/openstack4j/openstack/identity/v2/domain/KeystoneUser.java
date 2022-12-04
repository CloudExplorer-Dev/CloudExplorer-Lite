package org.openstack4j.openstack.identity.v2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.identity.v2.Role;
import org.openstack4j.model.identity.v2.Tenant;
import org.openstack4j.model.identity.v2.User;
import org.openstack4j.model.identity.v2.builder.UserBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("user")
public class KeystoneUser implements User {
    private static final long serialVersionUID = 1L;
    private String id;
    private String username;
    @JsonProperty("password")
    private String password;
    private String tenantId;
    private String name;
    private String email;
    @JsonProperty("enabled")
    private Boolean enabled;
    @JsonProperty("OS-ROLE:roles")
    private List<KeystoneRole> roles;

    public static UserBuilder builder() {
        return new UserConcreteBuilder();
    }

    @Override
    public UserBuilder toBuilder() {
        return new UserConcreteBuilder(this);
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public List<? extends Role> getRoles() {
        return roles;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("name", name)
                .add("id", id)
                .add("username", username)
                .add("password", password)
                .add("tenantId", tenantId)
                .add("email", email)
                .add("enabled", enabled)
                .add("roles", roles)
                .toString();
    }

    public static class Users extends ListResult<KeystoneUser> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("users")
        private List<KeystoneUser> list;

        public List<KeystoneUser> value() {
            return list;
        }
    }

    public static class UserConcreteBuilder implements UserBuilder {

        private KeystoneUser model;

        UserConcreteBuilder() {
            this(new KeystoneUser());
        }

        UserConcreteBuilder(KeystoneUser model) {
            this.model = model;
        }

        public UserBuilder name(String name) {
            model.name = name;
            return this;
        }

        /**
         * ID should only ever be set if the user already exists and this is used for update based actions
         *
         * @param id the user id
         * @return this for method chaining
         */
        public UserBuilder id(String id) {
            model.id = id;
            return this;
        }

        public UserBuilder password(String password) {
            model.password = password;
            return this;
        }

        public UserBuilder email(String email) {
            model.email = email;
            return this;
        }

        public UserBuilder enabled(boolean enabled) {
            model.enabled = enabled;
            return this;
        }

        public UserBuilder tenantId(String tenantId) {
            model.tenantId = tenantId;
            return this;
        }

        public UserBuilder tenant(Tenant tenant) {
            if (tenant != null && tenant.getId() != null)
                model.tenantId = tenant.getId();
            return this;
        }

        @Override
        public User build() {
            return model;
        }

        @Override
        public UserBuilder from(User in) {
            model = (KeystoneUser) in;
            return this;
        }

    }
}
