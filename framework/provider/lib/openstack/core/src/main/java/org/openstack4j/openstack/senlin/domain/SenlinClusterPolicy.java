package org.openstack4j.openstack.senlin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.senlin.ClusterPolicy;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * This is a model of a senlinCluster_policy. It uses Jackson annotations for
 * (de)serialization into JSON format
 *
 * @author lion
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("cluster_policy")
public class SenlinClusterPolicy implements ClusterPolicy {
    private static final long serialVersionUID = 5451213592273429432L;

    @JsonProperty("cluster_id")
    private String clusterID;
    @JsonProperty("cluster_name")
    private String clusterName;
    @JsonProperty("enabled")
    private Boolean enabled;
    @JsonProperty("id")
    private String id;
    @JsonProperty("policy_id")
    private String policyID;
    @JsonProperty("policy_name")
    private String policyName;
    @JsonProperty("policy_type")
    private String policyType;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPolicyType() {
        return policyType;
    }

    @Override
    public String getClusterID() {
        return clusterID;
    }

    @Override
    public String getClusterName() {
        return clusterName;
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }

    @Override
    public String getPolicyID() {
        return policyID;
    }

    @Override
    public String getPolicyName() {
        return policyName;
    }

    @Override
    public String toString() {
        return "SenlinClusterPolicy{" +
                "cluster_id='" + clusterID + '\'' +
                ", cluster_name='" + clusterName + '\'' +
                ", enabled='" + enabled + '\'' +
                ", id='" + id + '\'' +
                ", policy_id='" + policyID + '\'' +
                ", policy_name='" + policyName + '\'' +
                ", policy_type='" + policyType + '\'' +
                '}';
    }

    /**
     * An inner class for representing lists of senlinCluster_policy
     *
     * @author lion
     */
    public static class ClusterPolicy extends ListResult<SenlinClusterPolicy> {
        private static final long serialVersionUID = 600661296207420793L;

        @JsonProperty("cluster_policies")
        private List<SenlinClusterPolicy> list;

        protected List<SenlinClusterPolicy> value() {
            return list;
        }
    }
}
