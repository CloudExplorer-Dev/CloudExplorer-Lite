package org.openstack4j.openstack.trove.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.trove.Flavor;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Model implementation for Database instance flavor
 *
 * @author sumit gandhi
 */

@JsonRootName("flavor")
public class TroveInstanceFlavor implements Flavor {

    private static final long serialVersionUID = 1L;
    private String name;
    private String id;
    @JsonProperty("str_id")
    private String strId;

    private int ram;
    private int vcpus;
    private int disk;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getStrId() {
        return strId;
    }

    public int getRam() {
        return ram;
    }

    public int getVcpus() {
        return vcpus;
    }

    public int getDisk() {
        return disk;
    }

    public static class Flavors extends ListResult<TroveInstanceFlavor> {

        private static final long serialVersionUID = 1L;
        @JsonProperty("flavors")
        private List<TroveInstanceFlavor> troveInstanceFlavorList;

        @Override
        protected List<TroveInstanceFlavor> value() {
            return troveInstanceFlavorList;
        }

    }

}
