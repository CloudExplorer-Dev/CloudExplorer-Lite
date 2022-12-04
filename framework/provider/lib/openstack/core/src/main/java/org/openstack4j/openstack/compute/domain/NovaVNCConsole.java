package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.compute.VNCConsole;

/**
 * Represents a VNC Console which provides VNC connection information for a remote server
 *
 * @author Jeremy Unruh
 */
@JsonRootName("console")
public class NovaVNCConsole implements VNCConsole {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private Type type;

    @JsonProperty
    private String url;

    public static NovaConsole getConsoleForType(Type type) {
        if (type == Type.SPICE)
            return new NovaConsoleSPICE(type);

        return new NovaConsoleVNC(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getURL() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("type", type).add("url", url).toString();
    }

    public static class NovaConsole implements ModelEntity {

        private static final long serialVersionUID = 1L;

        @JsonProperty("type")
        protected String type;

        public NovaConsole(Type type) {
            this.type = type.value();
        }
    }

    @JsonRootName("os-getVNCConsole")
    public static class NovaConsoleVNC extends NovaConsole {

        private static final long serialVersionUID = 1L;

        public NovaConsoleVNC(Type type) {
            super(type);
        }
    }

    @JsonRootName("os-getSPICEConsole")
    public static class NovaConsoleSPICE extends NovaConsole {

        private static final long serialVersionUID = 1L;

        public NovaConsoleSPICE(Type type) {
            super(type);
        }
    }
}
