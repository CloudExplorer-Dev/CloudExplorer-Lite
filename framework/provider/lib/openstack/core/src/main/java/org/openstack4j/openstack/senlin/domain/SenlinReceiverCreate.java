package org.openstack4j.openstack.senlin.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import org.openstack4j.model.senlin.ReceiverCreate;
import org.openstack4j.model.senlin.builder.ReceiverCreateBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains all elements required for the creation of a SenlinReceiver. It
 * uses Jackson annotation for (de)serialization into JSON
 *
 * @author lion
 */
public class SenlinReceiverCreate implements ReceiverCreate {
    private static final long serialVersionUID = 280106309296409150L;

    @JsonProperty("receiver")
    private Map<String, Object> receiver;

    /**
     * Returnes a {@link SenlinReceiverCreateConcreteBuilder} for configuration and
     * creation of a {@link SenlinReceiverCreate} object.
     *
     * @return a {@link SenlinReceiverCreateConcreteBuilder}
     */
    public static SenlinReceiverCreateConcreteBuilder build() {
        return new SenlinReceiverCreateConcreteBuilder();
    }

    @Override
    public ReceiverCreateBuilder toBuilder() {
        return new SenlinReceiverCreateConcreteBuilder(this);
    }

    /**
     * A Builder to create a SenlinReceiver. Use {@link #build()} to receive the
     * {@link ReceiverCreate} object.
     *
     * @author lion
     */
    public static class SenlinReceiverCreateConcreteBuilder implements
            ReceiverCreateBuilder {

        private SenlinReceiverCreate model;

        /**
         * Constructor to create a {@link SenlinReceiverCreateConcreteBuilder} object
         * with a new, empty {@link SenlinReceiverCreate} object.
         */
        public SenlinReceiverCreateConcreteBuilder() {

            this(new SenlinReceiverCreate());
        }

        /**
         * Constructor for manipulation of an existing {@link SenlinReceiverCreate}
         * object.
         *
         * @param model the {@link SenlinReceiverCreate} object which is to be
         *              modified.
         */
        public SenlinReceiverCreateConcreteBuilder(SenlinReceiverCreate model) {
            this.model = model;

            this.model.receiver = Maps.newHashMap();
            HashMap<String, Object> params = Maps.newHashMap();
            this.model.receiver.put("params", params);
        }

        @Override
        public ReceiverCreate build() {
            return model;
        }

        @Override
        public ReceiverCreateBuilder from(ReceiverCreate in) {
            model = (SenlinReceiverCreate) in;
            return this;
        }

        @Override
        public ReceiverCreateBuilder action(String action) {
            model.receiver.put("action", action);
            return this;
        }

        @Override
        public ReceiverCreateBuilder clusterID(String clusterID) {
            model.receiver.put("cluster_id", clusterID);
            return this;
        }

        @Override
        public ReceiverCreateBuilder name(String name) {
            model.receiver.put("name", name);
            return this;
        }

        @Override
        public ReceiverCreateBuilder type(String type) {
            model.receiver.put("type", type);
            return this;
        }

        @Override
        public ReceiverCreateBuilder params(Map<String, String> params) {
            model.receiver.put("params", params);
            return this;
        }

    }

}
