package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.network.ExtraDhcpOptCreate;
import org.openstack4j.model.network.builder.ExtraDhcpOptBuilder;

/**
 * @author Ales Kemr
 */
public class NeutronExtraDhcpOptCreate implements ExtraDhcpOptCreate {

    // {"opt_value": "testfile.1", "opt_name": "bootfile-name"}

    @JsonProperty("opt_value")
    public String opt_value;
    @JsonProperty("opt_name")
    public String opt_name;

    public static NeutronExtraDhcpOptBuilder builder() {
        return new NeutronExtraDhcpOptBuilder(new NeutronExtraDhcpOptCreate());
    }

    @Override
    public String getOptValue() {
        return opt_value;
    }

    @Override
    public String getOptName() {
        return opt_name;
    }

    @Override
    public ExtraDhcpOptBuilder toBuilder() {
        return new NeutronExtraDhcpOptBuilder(this);
    }

    public static class NeutronExtraDhcpOptBuilder implements ExtraDhcpOptBuilder {

        NeutronExtraDhcpOptCreate create;

        public NeutronExtraDhcpOptBuilder(NeutronExtraDhcpOptCreate create) {
            this.create = create;
        }

        @Override
        public ExtraDhcpOptBuilder optValue(String optValue) {
            create.opt_value = optValue;
            return this;
        }

        @Override
        public ExtraDhcpOptBuilder optName(String optName) {
            create.opt_name = optName;
            return this;
        }

        @Override
        public ExtraDhcpOptCreate build() {
            return create;
        }

        @Override
        public ExtraDhcpOptBuilder from(ExtraDhcpOptCreate in) {
            return new NeutronExtraDhcpOptBuilder((NeutronExtraDhcpOptCreate) in);
        }

    }
}
