package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.openstack4j.openstack.common.ListResult;

import java.util.Collections;
import java.util.List;

/**
 * Maps to the OpenStack Floating IP Pool specification and used internally to retrieve the list of pool names
 *
 * @author Jeremy Unruh
 */
public class NovaFloatingIPPools extends ListResult<String> {

    private static final long serialVersionUID = 1L;

    @JsonProperty("floating_ip_pools")
    private List<Wrapper> values;

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> value() {
        if (values != null)
            return Lists.transform(values, WrapperToStringFunc.instance);
        return Collections.emptyList();
    }

    static final class Wrapper {
        @JsonProperty("name")
        String name;
    }

    private static class WrapperToStringFunc implements Function<Wrapper, String> {

        static final WrapperToStringFunc instance = new WrapperToStringFunc();

        @Override
        public String apply(Wrapper input) {
            return input.name;
        }

    }

}
