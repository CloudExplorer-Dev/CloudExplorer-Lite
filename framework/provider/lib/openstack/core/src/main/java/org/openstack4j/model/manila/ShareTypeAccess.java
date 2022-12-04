package org.openstack4j.model.manila;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Access details for a share type.
 *
 * @author Daniel Gonazalez Nothnagel
 */
public class ShareTypeAccess implements ModelEntity {
    private static final long serialVersionUID = 1L;

    @JsonProperty("share_type_id")
    private String shareTypeId;
    @JsonProperty("project_id")
    private String projectId;

    public String getShareTypeId() {
        return shareTypeId;
    }

    public String getProjectId() {
        return projectId;
    }

    public static class ShareTypeAccessList extends ListResult<ShareTypeAccess> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("share_type_access")
        List<ShareTypeAccess> shareTypeAccessList;

        @Override
        protected List<ShareTypeAccess> value() {
            return shareTypeAccessList;
        }
    }
}
