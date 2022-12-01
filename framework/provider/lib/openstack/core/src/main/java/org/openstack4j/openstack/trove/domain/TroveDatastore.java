package org.openstack4j.openstack.trove.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.trove.Datastore;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Model implementation for Datastore
 *
 * @author sumit gandhi
 */

@JsonRootName("datastore")
public class TroveDatastore implements Datastore {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String default_version;
    @JsonProperty("versions")
    private List<TroveDatastoreVersion> troveDatastoreVersionList;

    public String getDefault_version() {
        return default_version;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<TroveDatastoreVersion> getTroveDatastoreVersionList() {
        return troveDatastoreVersionList;
    }

    public static class Datastores extends ListResult<TroveDatastore> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("datastores")
        private List<TroveDatastore> troveDatastoreList;

        @Override
        protected List<TroveDatastore> value() {
            return troveDatastoreList;
        }

    }

}
