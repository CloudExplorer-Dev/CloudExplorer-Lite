package org.openstack4j.model.common;

/**
 * Represents an identifier which can either be an ID or Name
 *
 * @author Jeremy Unruh
 */
public class Identifier {

    private final Type type;
    private final String id;

    private Identifier(Type type, String id) {
        this.type = type;
        this.id = id;
    }

    /**
     * Creates a new identifier which is an ID based value
     *
     * @param id the ID value
     * @return identifier object
     */
    public static Identifier byId(String id) {
        return new Identifier(Type.ID, id);
    }

    /**
     * Creates an identifier which is NAME based identification
     *
     * @param name the name value
     * @return the identifier
     */
    public static Identifier byName(String name) {
        return new Identifier(Type.NAME, name);
    }

    public Type getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public boolean isTypeID() {
        return type == Type.ID;
    }

    public enum Type {ID, NAME}


}
