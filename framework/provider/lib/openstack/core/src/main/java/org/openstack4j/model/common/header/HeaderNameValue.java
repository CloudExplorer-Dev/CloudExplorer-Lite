package org.openstack4j.model.common.header;

/**
 * Simple container which holds a Header name and Header value
 *
 * @author Jeremy Unruh
 */
public final class HeaderNameValue {

    private final String name;
    private final Object value;

    public HeaderNameValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", name, value);
    }
}
