package org.openstack4j.openstack.compute.domain.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * SecurityGroup related Actions
 *
 * @author Jeremy Unruh
 */
public class SecurityGroupActions implements ServerAction {

    private static final long serialVersionUID = 1L;

    @JsonProperty("name")
    private String name;

    protected SecurityGroupActions(String name) {
        this.name = name;
    }

    public static Add add(String name) {
        return new Add(name);
    }

    public static Remove remove(String name) {
        return new Remove(name);
    }

    public String getName() {
        return name;
    }

    @JsonRootName("addSecurityGroup")
    public static class Add extends SecurityGroupActions {

        private static final long serialVersionUID = 1L;

        public Add(String name) {
            super(name);
        }
    }

    @JsonRootName("removeSecurityGroup")
    public static class Remove extends SecurityGroupActions {

        private static final long serialVersionUID = 1L;

        public Remove(String name) {
            super(name);
        }
    }
}
