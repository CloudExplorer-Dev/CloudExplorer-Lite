package org.openstack4j.openstack.octavia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.octavia.MemberV2Update;
import org.openstack4j.model.octavia.builder.MemberV2UpdateBuilder;

/**
 * Entity for updating lbaas v2 members
 *
 * @author wei
 */
@JsonRootName("member")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OctaviaMemberV2Update implements MemberV2Update {

    private static final long serialVersionUID = 1L;

    /**
     * 1~100
     */
    @JsonProperty("weight")
    private Integer weight;

    @JsonProperty("admin_state_up")
    private boolean adminStateUp = true;

    public static MemberV2UpdateBuilder builder() {
        return new MemberV2UpdateConcreteBuilder();
    }

    @Override
    public boolean isAdminStateUp() {
        return adminStateUp;
    }

    @Override
    public Integer getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("weight", weight)
                .add("adminStateUp", adminStateUp)
                .toString();
    }

    @Override
    public MemberV2UpdateBuilder toBuilder() {
        return new MemberV2UpdateConcreteBuilder(this);
    }

    public static class MemberV2UpdateConcreteBuilder implements MemberV2UpdateBuilder {
        private OctaviaMemberV2Update m;

        public MemberV2UpdateConcreteBuilder() {
            this(new OctaviaMemberV2Update());
        }

        public MemberV2UpdateConcreteBuilder(OctaviaMemberV2Update m) {
            this.m = m;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MemberV2Update build() {
            return m;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MemberV2UpdateBuilder from(MemberV2Update in) {
            m = (OctaviaMemberV2Update) in;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MemberV2UpdateBuilder adminStateUp(boolean adminStateUp) {
            m.adminStateUp = adminStateUp;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public MemberV2UpdateBuilder weight(Integer weight) {
            m.weight = weight;
            return this;
        }
    }
}
