package org.openstack4j.model.sahara;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.sahara.builder.ClusterBuilder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * An OpenStack Cluster
 *
 * @author ekasit.kijsipongse@nectec.or.th
 */
public interface Cluster extends ModelEntity, Buildable<ClusterBuilder> {

    /**
     * @return the status of the cluster
     */
    Status getStatus();

    /**
     * @return the information of the cluster
     */
    Map<String, ? extends ServiceInfo> getInfos();

    /**
     * @return the template id of the cluster
     */
    String getClusterTemplateId();

    /**
     * @return the if the cluster is transient
     */
    Boolean isTransient();

    /**
     * @return the description of the cluster
     */
    String getDescription();

    /**
     * @return the configurations of the cluster
     */
    Map<String, ? extends ServiceConfig> getClusterConfigs();

    /**
     * @return the created date of the cluster
     */
    Date getCreatedAt();

    /**
     * @return the default image id of the cluster
     */
    String getDefaultImageId();

    /**
     * @return the user keypair id of the cluster
     */
    String getUserKeypairId();

    /**
     * @return the updated date of the cluster
     */
    Date getUpdatedAt();

    /**
     * @return the plugin name of the cluster
     */
    String getPluginName();

    /**
     * @return the management network of the cluster
     */
    String getManagementNetworkId();

    /**
     * @return the anti-affinity of the cluster
     */
    List<String> getAntiAffinity();

    /**
     * @return the tenant id of the cluster
     */
    String getTenantId();

    /**
     * @return the node groups of the cluster
     */
    List<? extends NodeGroup> getNodeGroups();

    /**
     * @return the management public key of the cluster
     */
    String getManagementPublicKey();

    /**
     * @return the status description of the cluster
     */
    String getStatusDescription();

    /**
     * @return the hadoop version of the cluster
     */
    String getHadoopVersion();

    /**
     * @return the identifier of the cluster
     */
    String getId();

    /**
     * @return the trust id of the cluster
     */
    String getTrustId();

    /**
     * @return the name of the cluster
     */
    String getName();

    enum Status {
        /*
         * Since it is being developed, this list is not stable yet. Note
         *  also that Sahara cluster status may appear in 2 words, e.g.
         *  "Adding Instances", but we match only the first word for simplicity
         *  until the list is stable.
         *  See http://docs.openstack.org/developer/sahara/userdoc/statuses.html for more info.
         */
        UNRECOGNIZED, VALIDATING, INFRAUPDATING, SPAWNING, WAITING, PREPARING, CONFIGURING, STARTING, ACTIVE, SCALING, ADDING, DECOMMISSIONING, DELETING, ERROR;

        @JsonCreator
        public static Status forValue(String value) {
            if (value != null) {
                for (Status s : Status.values()) {
                    if (value.toUpperCase().startsWith(s.name()))
                        return s;
                }
            }
            return Status.UNRECOGNIZED;
        }
    }

}
