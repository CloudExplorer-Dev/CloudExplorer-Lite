package org.openstack4j.openstack.senlin.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.senlin.ClusterActionCreate;
import org.openstack4j.model.senlin.ClusterCreate;
import org.openstack4j.model.senlin.builder.ClusterActionCreateBuilder;

import java.util.ArrayList;
import java.util.Map;

/**
 * This class contains all elements required for the creation of a SenlinClusterAction. It
 * uses Jackson annotation for (de)serialization into JSON
 *
 * @author lion
 */
public class SenlinClusterActionCreate implements ClusterActionCreate {
    private static final long serialVersionUID = 8159175788259631363L;

    @JsonProperty("add_nodes")
    private Map<String, ArrayList<String>> addNodes;
    @JsonProperty("del_nodes")
    private Map<String, ArrayList<String>> delNodes;
    @JsonProperty("scale_out")
    private Map<String, String> scaleOut;
    @JsonProperty("scale_in")
    private Map<String, String> scaleIn;
    @JsonProperty("resize")
    private Map<String, String> resize;
    @JsonProperty("check")
    private Map<String, String> check;
    @JsonProperty("recover")
    private Map<String, String> recover;
    @JsonProperty("policy_attach")
    private Map<String, String> policyAttach;
    @JsonProperty("policy_detach")
    private Map<String, String> policyDetach;
    @JsonProperty("policy_update")
    private Map<String, String> policyUpdate;

    /**
     * Returnes a {@link SenlinClusterActionCreateConcreteBuilder} for configuration and
     * creation of a {@link SenlinClusterActionCreate} object.
     *
     * @return a {@link SenlinClusterActionCreateConcreteBuilder}
     */
    public static SenlinClusterActionCreateConcreteBuilder build() {
        return new SenlinClusterActionCreateConcreteBuilder();
    }

    @Override
    public ClusterActionCreateBuilder toBuilder() {
        return new SenlinClusterActionCreateConcreteBuilder(this);
    }


    /**
     * A Builder to create a SenlinClusterAction. Use {@link #build()} to receive the
     * {@link ClusterCreate} object.
     *
     * @author lion
     */
    public static class SenlinClusterActionCreateConcreteBuilder implements
            ClusterActionCreateBuilder {

        private SenlinClusterActionCreate model;

        /**
         * Constructor to create a {@link SenlinClusterActionCreateConcreteBuilder} object
         * with a new, empty {@link SenlinClusterActionCreate} object.
         */
        public SenlinClusterActionCreateConcreteBuilder() {

            this(new SenlinClusterActionCreate());
        }

        /**
         * Constructor for manipulation of an existing {@link SenlinClusterActionCreate}
         * object.
         *
         * @param model the {@link SenlinClusterActionCreate} object which is to be
         *              modified.
         */
        public SenlinClusterActionCreateConcreteBuilder(SenlinClusterActionCreate model) {
            this.model = model;
        }

        @Override
        public ClusterActionCreate build() {
            return model;
        }

        @Override
        public ClusterActionCreateBuilder from(ClusterActionCreate in) {
            model = (SenlinClusterActionCreate) in;
            return this;
        }

        @Override
        public ClusterActionCreateBuilder addNodes(Map<String, ArrayList<String>> addNodes) {
            model.addNodes = addNodes;
            return this;
        }

        @Override
        public ClusterActionCreateBuilder delNodes(Map<String, ArrayList<String>> delNodes) {
            model.delNodes = delNodes;
            return this;
        }

        @Override
        public ClusterActionCreateBuilder scaleOut(Map<String, String> scaleOut) {
            model.scaleOut = scaleOut;
            return this;
        }

        @Override
        public ClusterActionCreateBuilder scaleIn(Map<String, String> scaleIn) {
            model.scaleIn = scaleIn;
            return this;
        }

        @Override
        public ClusterActionCreateBuilder resize(Map<String, String> resize) {
            model.resize = resize;
            return this;
        }

        @Override
        public ClusterActionCreateBuilder check(Map<String, String> check) {
            model.check = check;
            return this;
        }

        @Override
        public ClusterActionCreateBuilder recover(Map<String, String> recover) {
            model.recover = recover;
            return this;
        }

        @Override
        public ClusterActionCreateBuilder policyAttach(Map<String, String> policyAttach) {
            model.policyAttach = policyAttach;
            return this;
        }

        @Override
        public ClusterActionCreateBuilder policyDetach(Map<String, String> policyDetach) {
            model.policyDetach = policyDetach;
            return this;
        }

        @Override
        public ClusterActionCreateBuilder policyUpdate(Map<String, String> policyUpdate) {
            model.policyUpdate = policyUpdate;
            return this;
        }

    }

}
