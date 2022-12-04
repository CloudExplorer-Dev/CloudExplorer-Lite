package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.network.TrunkSubport;
import org.openstack4j.openstack.common.ListEntity;

import java.util.List;

/**
 * Models for adding and removing subports
 *
 * @author Kashyap Jha
 */

/*
 * Used to create a SubPortCreate object which only encapsulates allowable fields
 */
@JsonRootName("sub_ports")
public class NeutronTrunkSubportCreate implements ModelEntity {

    private static final long serialVersionUID = 1L;
    @JsonProperty("port_id")
    private String portId;
    @JsonProperty("segmentation_id")
    private int segmentationId;
    @JsonProperty("segmentation_type")
    private String segmentationType;

    public static NeutronTrunkSubportCreate fromTrunkSubport(TrunkSubport trunkSubport) {
        NeutronTrunkSubportCreate toCreate = new NeutronTrunkSubportCreate();
        toCreate.segmentationId = trunkSubport.getSegmentationId();
        toCreate.segmentationType = trunkSubport.getSegmentationType();
        toCreate.portId = trunkSubport.getPortId();
        return toCreate;
    }

    /*
     * Used to create a SubPortDelete object which only encapsulates allowable fields
     */
    public static class NeutronTrunkSubportDelete implements ModelEntity {

        private static final long serialVersionUID = 1L;
        @JsonProperty("port_id")
        private String portId;

        public static NeutronTrunkSubportDelete delete(String portId) {
            NeutronTrunkSubportDelete toDelete = new NeutronTrunkSubportDelete();
            toDelete.portId = portId;
            return toDelete;
        }

        /*
         * Used to create a list of SubPortDelete objects to delete multiple resources at once
         */
        public static class NeutronTrunkSubportsDelete implements ModelEntity {

            private static final long serialVersionUID = 1L;
            @JsonProperty("sub_ports")
            private ListEntity<NeutronTrunkSubportDelete> trunkSubports;

            public NeutronTrunkSubportsDelete() {
                trunkSubports = new ListEntity<>();
            }

            public static NeutronTrunkSubportsDelete delete(List<String> portIds) {
                NeutronTrunkSubportsDelete toDelete = new NeutronTrunkSubportsDelete();

                for (String portId : portIds) {
                    toDelete.trunkSubports.add(NeutronTrunkSubportDelete.delete(portId));
                }
                return toDelete;
            }

        }
    }

    /*
     * Used to create a list of SubPortCreate objects to add multiple resources at once
     */
    public static class NeutronTrunkSubportsCreate implements ModelEntity {

        private static final long serialVersionUID = 1L;
        @JsonProperty("sub_ports")
        private ListEntity<NeutronTrunkSubportCreate> trunkSubports;

        public NeutronTrunkSubportsCreate() {
            trunkSubports = new ListEntity<>();
        }

        public static NeutronTrunkSubportsCreate fromTrunkSubports(List<? extends TrunkSubport> trunkSubports) {
            NeutronTrunkSubportsCreate toCreate = new NeutronTrunkSubportsCreate();
            for (TrunkSubport trunkSubport : trunkSubports) {
                toCreate.trunkSubports.add(NeutronTrunkSubportCreate.fromTrunkSubport(trunkSubport));
            }
            return toCreate;
        }
    }
}
