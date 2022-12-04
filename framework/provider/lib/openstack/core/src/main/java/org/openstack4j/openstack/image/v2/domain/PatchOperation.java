package org.openstack4j.openstack.image.v2.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.ModelEntity;

/**
 * Representation of a json patch operation for an openstack image update
 *
 * @author emjburns
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatchOperation implements ModelEntity {
    private OperationType op;
    private String path;
    private Object value;

    public PatchOperation() {
    }

    public PatchOperation(OperationType op, String path, Object value) {
        this.op = op;
        this.path = path;
        this.value = value;
    }

    public PatchOperation(OperationType op, String path) {
        this.op = op;
        this.path = path;
    }

    public OperationType getOp() {
        return op;
    }

    public void setOp(OperationType op) {
        this.op = op;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("op", op)
                .add("path", path)
                .add("value", value)
                .toString();
    }

    public enum OperationType {
        ADD,
        REMOVE,
        REPLACE,
        UNKNOWN;

        @JsonCreator
        public static OperationType value(String v) {
            if (v == null) return UNKNOWN;
            try {
                return valueOf(v.toUpperCase());
            } catch (IllegalArgumentException e) {
                return UNKNOWN;
            }
        }

        @JsonValue
        public String value() {
            return name().toLowerCase();
        }
    }
}
