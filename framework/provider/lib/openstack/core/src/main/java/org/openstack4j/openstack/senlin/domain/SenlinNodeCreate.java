package org.openstack4j.openstack.senlin.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import org.openstack4j.model.senlin.NodeCreate;
import org.openstack4j.model.senlin.builder.NodeCreateBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains all elements required for the creation of a SenlinNode. It
 * uses Jackson annotation for (de)serialization into JSON
 *
 * @author lion
 */
public class SenlinNodeCreate implements NodeCreate {
    private static final long serialVersionUID = -5839890526136001718L;

    @JsonProperty("node")
    private Map<String, Object> node;

    /**
     * Returnes a {@link SenlinNodeCreateConcreteBuilder} for configuration and
     * creation of a {@link SenlinNodeCreate} object.
     *
     * @return a {@link SenlinNodeCreateConcreteBuilder}
     */
    public static SenlinNodeCreateConcreteBuilder build() {
        return new SenlinNodeCreateConcreteBuilder();
    }

    @Override
    public NodeCreateBuilder toBuilder() {
        return new SenlinNodeCreateConcreteBuilder(this);
    }

    /**
     * A Builder to create a SenlinNode. Use {@link #build()} to receive the
     * {@link NodeCreate} object.
     *
     * @author lion
     */
    public static class SenlinNodeCreateConcreteBuilder implements
            NodeCreateBuilder {

        private SenlinNodeCreate model;

        /**
         * Constructor to create a {@link SenlinNodeCreateConcreteBuilder} object
         * with a new, empty {@link SenlinNodeCreate} object.
         */
        public SenlinNodeCreateConcreteBuilder() {

            this(new SenlinNodeCreate());
        }

        /**
         * Constructor for manipulation of an existing {@link SenlinNodeCreate}
         * object.
         *
         * @param model the {@link SenlinNodeCreate} object which is to be
         *              modified.
         */
        public SenlinNodeCreateConcreteBuilder(SenlinNodeCreate model) {
            this.model = model;

            this.model.node = Maps.newHashMap();
            HashMap<String, Object> metadata = Maps.newHashMap();
            this.model.node.put("metadata", metadata);
        }

        @Override
        public NodeCreate build() {
            return model;
        }

        @Override
        public NodeCreateBuilder from(NodeCreate in) {
            model = (SenlinNodeCreate) in;
            return this;
        }

        @Override
        public NodeCreateBuilder clusterID(String clusterID) {
            model.node.put("cluster_id", clusterID);
            return this;
        }

        @Override
        public NodeCreateBuilder metadata(Map<String, String> metadata) {
            model.node.put("metadata", metadata);
            return this;
        }

        @Override
        public NodeCreateBuilder name(String name) {
            model.node.put("name", name);
            return this;
        }

        @Override
        public NodeCreateBuilder profileID(String profileID) {
            model.node.put("profile_id", profileID);
            return this;
        }

        @Override
        public NodeCreateBuilder role(String role) {
            model.node.put("role", role);
            return this;
        }

    }

}
