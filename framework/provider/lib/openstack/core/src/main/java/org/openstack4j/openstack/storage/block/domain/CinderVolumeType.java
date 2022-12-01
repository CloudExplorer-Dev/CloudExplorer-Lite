package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.storage.block.VolumeType;
import org.openstack4j.model.storage.block.builder.VolumeTypeBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * The volume type defines the characteristics of a volume. It usually maps to a set of capabilities
 * of the storage back-end driver to be used for this volume.
 * Examples: "Performance", "SSD", "Backup", etc.
 *
 * @author Jeremy Unruh
 */
@JsonRootName("volume_type")
public class CinderVolumeType implements VolumeType {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;

    @JsonProperty("extra_specs")
    private Map<String, String> extraSpecs;

    /**
     * @return the Volume Type Builder
     */
    public static VolumeTypeBuilder builder() {
        return new ConcreteVolumeTypeBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getExtraSpecs() {
        return extraSpecs;
    }

    @Override
    public VolumeTypeBuilder toBuilder() {
        return new ConcreteVolumeTypeBuilder(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", id).add("name", name).add("extras", extraSpecs)
                .toString();
    }

    public static class VolumeTypes extends ListResult<CinderVolumeType> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("volume_types")
        private List<CinderVolumeType> types;

        @Override
        protected List<CinderVolumeType> value() {
            return types;
        }

    }

    public static class ConcreteVolumeTypeBuilder implements VolumeTypeBuilder {

        private CinderVolumeType m;

        ConcreteVolumeTypeBuilder() {
            this(new CinderVolumeType());
        }

        ConcreteVolumeTypeBuilder(CinderVolumeType volumeType) {
            this.m = volumeType;
        }

        @Override
        public VolumeType build() {
            return m;
        }

        @Override
        public VolumeTypeBuilder from(VolumeType in) {
            m = (CinderVolumeType) in;
            return this;
        }

        @Override
        public VolumeTypeBuilder name(String name) {
            m.name = name;
            return this;
        }

        @Override
        public VolumeTypeBuilder extraSpecs(Map<String, String> extraSpecs) {
            m.extraSpecs = extraSpecs;
            return this;
        }
    }

}
