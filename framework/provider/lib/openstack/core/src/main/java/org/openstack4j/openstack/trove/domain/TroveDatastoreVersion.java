package org.openstack4j.openstack.trove.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.trove.DatastoreVersion;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Model implementation for Datastore version
 *
 * @author sumit gandhi
 */

@JsonRootName("version")
public class TroveDatastoreVersion implements DatastoreVersion {

    private String name;
    private String id;
    @JsonProperty("datastore")
    private String datastoreId;
    @JsonProperty("active")
    private int isActive;
    private String image;
    @JsonProperty("packages")
    private String packageName;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDatastoreId() {
        return datastoreId;
    }

    public int getIsActive() {
        return isActive;
    }

    public String getImage() {
        return image;
    }

    public String getPackageName() {
        return packageName;
    }

    public static class Versions extends ListResult<TroveDatastoreVersion> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("versions")
        private List<TroveDatastoreVersion> troveDatastoreVersionList;

        @Override
        protected List<TroveDatastoreVersion> value() {
            return troveDatastoreVersionList;
        }

    }
}
