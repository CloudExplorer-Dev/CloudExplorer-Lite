package org.openstack4j.openstack.senlin.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.senlin.NodeActionCreate;
import org.openstack4j.model.senlin.NodeCreate;
import org.openstack4j.model.senlin.builder.NodeActionCreateBuilder;

import java.util.Map;

/**
 * This class contains all elements required for the creation of a SenlinNode. It
 * uses Jackson annotation for (de)serialization into JSON
 *
 * @author lion
 */
public class SenlinNodeActionCreate implements NodeActionCreate {
    private static final long serialVersionUID = 7461515803108018710L;

    @JsonProperty("check")
    private Map<String, String> check;
    @JsonProperty("recover")
    private Map<String, String> recover;

    /**
     * Returnes a {@link SenlinNodeActionCreateConcreteBuilder} for configuration and
     * creation of a {@link SenlinNodeActionCreate} object.
     *
     * @return a {@link SenlinNodeActionCreateConcreteBuilder}
     */
    public static SenlinNodeActionCreateConcreteBuilder build() {
        return new SenlinNodeActionCreateConcreteBuilder();
    }

    @Override
    public NodeActionCreateBuilder toBuilder() {
        return new SenlinNodeActionCreateConcreteBuilder(this);
    }


    /**
     * A Builder to create a SenlinNode. Use {@link #build()} to receive the
     * {@link NodeCreate} object.
     *
     * @author lion
     */
    public static class SenlinNodeActionCreateConcreteBuilder implements
            NodeActionCreateBuilder {

        private SenlinNodeActionCreate model;

        /**
         * Constructor to create a {@link SenlinNodeActionCreateConcreteBuilder} object
         * with a new, empty {@link SenlinNodeActionCreate} object.
         */
        public SenlinNodeActionCreateConcreteBuilder() {

            this(new SenlinNodeActionCreate());
        }

        /**
         * Constructor for manipulation of an existing {@link SenlinNodeActionCreate}
         * object.
         *
         * @param model the {@link SenlinNodeActionCreate} object which is to be
         *              modified.
         */
        public SenlinNodeActionCreateConcreteBuilder(SenlinNodeActionCreate model) {
            this.model = model;
        }

        @Override
        public NodeActionCreate build() {
            return model;
        }

        @Override
        public NodeActionCreateBuilder from(NodeActionCreate in) {
            model = (SenlinNodeActionCreate) in;
            return this;
        }

        @Override
        public NodeActionCreateBuilder check(Map<String, String> check) {
            model.check = check;
            return this;
        }

        @Override
        public NodeActionCreateBuilder recover(Map<String, String> recover) {
            model.recover = recover;
            return this;
        }

    }

}
