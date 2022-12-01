package org.openstack4j.openstack.image.v2.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.image.v2.ImageUpdate;
import org.openstack4j.model.image.v2.builder.ImageUpdateBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of updating a glance image
 *
 * @author emjburns
 */
public class GlanceImageUpdate implements ImageUpdate {

    List<PatchOperation> ops = new ArrayList<>();

    public GlanceImageUpdate() {
    }

    public GlanceImageUpdate(JsonNode value) {
        if (value.isArray()) {
            for (Iterator<JsonNode> iterator = value.iterator(); iterator.hasNext(); ) {
                JsonNode next = iterator.next();
                iterator.remove();
                PatchOperation p = new PatchOperation(
                        PatchOperation.OperationType.value(next.get("op").textValue()),
                        next.get("path").textValue(),
                        next.get("value")
                );
                ops.add(p);
            }
        }
    }

    public GlanceImageUpdate(List<PatchOperation> ops) {
        this.ops = ops;
    }

    public static ImageUpdateBuilder builder() {
        return new ImageUpdateConcreteBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonValue
    public List<PatchOperation> getOps() {
        return ops;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("ops", ops)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImageUpdateBuilder toBuilder() {
        return new ImageUpdateConcreteBuilder(this);
    }

    public static class ImageUpdateConcreteBuilder implements ImageUpdateBuilder {
        private GlanceImageUpdate m;

        public ImageUpdateConcreteBuilder() {
            this(new GlanceImageUpdate());
        }

        public ImageUpdateConcreteBuilder(GlanceImageUpdate m) {
            this.m = m;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ImageUpdateBuilder ops(List<PatchOperation> ops) {
            m.ops = ops;
            return this;
        }

        @Override
        public ImageUpdateBuilder ops(PatchOperation op) {
            if (m.ops == null) m.ops = new ArrayList<>();
            m.ops.add(op);
            return this;
        }

        @Override
        public ImageUpdate build() {
            return m;
        }

        @Override
        public ImageUpdateBuilder from(ImageUpdate in) {
            m = (GlanceImageUpdate) in;
            return this;
        }
    }
}
