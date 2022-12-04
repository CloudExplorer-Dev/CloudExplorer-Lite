package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.manila.Access;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Represents the access to a a share.
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("access")
public class ManilaAccess implements Access {
    private static final long serialVersionUID = 1L;

    private String id;
    @JsonProperty("share_id")
    private String shareId;
    private State state;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("access_type")
    private Type accessType;
    @JsonProperty("access_to")
    private String accessTo;
    @JsonProperty("access_level")
    private Level accessLevel;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getShareId() {
        return shareId;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public Type getAccessType() {
        return accessType;
    }

    @Override
    public String getAccessTo() {
        return accessTo;
    }

    @Override
    public Level getAccessLevel() {
        return accessLevel;
    }

    public static class AccessList extends ListResult<ManilaAccess> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("access_list")
        private List<ManilaAccess> accessList;

        @Override
        protected List<ManilaAccess> value() {
            return accessList;
        }
    }
}
