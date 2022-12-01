package org.openstack4j.openstack.senlin.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import org.openstack4j.model.senlin.ProfileCreate;
import org.openstack4j.model.senlin.builder.ProfileCreateBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains all elements required for the creation of a SenlinProfile. It
 * uses Jackson annotation for (de)serialization into JSON
 *
 * @author lion
 */
public class SenlinProfileCreate implements ProfileCreate {
    private static final long serialVersionUID = -7343560700665811096L;

    @JsonProperty("profile")
    private Map<String, Object> profile;

    /**
     * Returnes a {@link SenlinProfileCreateConcreteBuilder} for configuration and
     * creation of a {@link SenlinProfileCreate} object.
     *
     * @return a {@link SenlinProfileCreateConcreteBuilder}
     */
    public static SenlinProfileCreateConcreteBuilder build() {
        return new SenlinProfileCreateConcreteBuilder();
    }

    @Override
    public ProfileCreateBuilder toBuilder() {
        return new SenlinProfileCreateConcreteBuilder(this);
    }

    /**
     * A Builder to create a SenlinProfile. Use {@link #build()} to receive the
     * {@link ProfileCreate} object.
     *
     * @author lion
     */
    public static class SenlinProfileCreateConcreteBuilder implements
            ProfileCreateBuilder {

        private SenlinProfileCreate model;

        /**
         * Constructor to create a {@link SenlinProfileCreateConcreteBuilder} object
         * with a new, empty {@link SenlinProfileCreate} object.
         */
        public SenlinProfileCreateConcreteBuilder() {

            this(new SenlinProfileCreate());
        }

        /**
         * Constructor for manipulation of an existing {@link SenlinProfileCreate}
         * object.
         *
         * @param model the {@link SenlinProfileCreate} object which is to be
         *              modified.
         */
        public SenlinProfileCreateConcreteBuilder(SenlinProfileCreate model) {
            this.model = model;

            this.model.profile = Maps.newHashMap();
            HashMap<String, Object> metadata = Maps.newHashMap();
            HashMap<String, Object> spec = Maps.newHashMap();
            this.model.profile.put("metadata", metadata);
            this.model.profile.put("spec", spec);
        }

        @Override
        public ProfileCreate build() {
            return model;
        }

        @Override
        public ProfileCreateBuilder from(ProfileCreate in) {
            model = (SenlinProfileCreate) in;
            return this;
        }

        @Override
        public ProfileCreateBuilder name(String name) {
            model.profile.put("name", name);
            return this;
        }

        @Override
        public ProfileCreateBuilder spec(Map<String, Object> spec) {
            model.profile.put("spec", spec);
            return this;
        }

        @Override
        public ProfileCreateBuilder metadata(Map<String, Map> metadata) {
            model.profile.put("metadata", metadata);
            return this;
        }

    }

}
