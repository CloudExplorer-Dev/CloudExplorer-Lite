package org.openstack4j.openstack.trove.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.trove.Datastore;
import org.openstack4j.model.trove.InstanceCreate;
import org.openstack4j.model.trove.builder.InstanceCreateBuilder;

/**
 * Model implementation for Database instance creation
 *
 * @author Shital Patil
 */

@JsonRootName("instance")
public class TroveInstanceCreate implements InstanceCreate {

    private static final long serialVersionUID = 1L;

    @JsonProperty("volume")
    private Volume volume;
    @JsonProperty("flavorRef")
    private String flavorRef;
    @JsonProperty("name")
    private String name;
    @JsonProperty("datastore")
    private Datastore datastore;
    @JsonProperty("availability_zone")
    private String availabilityZone;

    public static InstanceCreateBuilder builder() {
        return new ConcereteInstanceBuilder();
    }

    /**
     * @param volume The volume
     */
    public void setVolume(Volume volume) {
        this.volume = volume;
    }

    /**
     * @param flavorRef The flavorRef
     */
    @Override
    public void setFlavor(String flavorRef) {
        this.flavorRef = flavorRef;
    }

    /**
     * @param name The name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     */
    @Override
    public void setVolumetype(String volumeType) {
        this.volume.setType(volumeType);
    }

    /**
     *
     */
    @Override
    public void setvolumeSize(int size) {
        this.volume.setSize(size);
    }

    /**
     *
     */
    @Override
    public void setDatastore(Datastore datastore) {
        this.datastore = datastore;
    }

    /**
     *
     */
    @Override
    public void setAvailabilityZone(String availabilityZone) {
        this.availabilityZone = availabilityZone;
    }

    @Override
    public InstanceCreateBuilder toBuilder() {
        return new ConcereteInstanceBuilder(this);
    }

    public static class ConcereteInstanceBuilder implements InstanceCreateBuilder {
        private TroveInstanceCreate instance;

        public ConcereteInstanceBuilder() {
            this(new TroveInstanceCreate());
        }

        public ConcereteInstanceBuilder(TroveInstanceCreate instance) {
            this.instance = instance;
            this.instance.setVolume(instance.new Volume());
        }

        @Override
        public InstanceCreate build() {
            return instance;
        }

        @Override
        public InstanceCreateBuilder from(InstanceCreate in) {
            instance = (TroveInstanceCreate) in;
            return this;
        }

        @Override
        public InstanceCreateBuilder flavor(String flavorRef) {
            instance.setFlavor(flavorRef);
            return this;
        }

        @Override
        public InstanceCreateBuilder name(String name) {
            instance.setName(name);
            return this;
        }

        @Override
        public InstanceCreateBuilder datastore(Datastore datastore) {
            instance.setDatastore(datastore);
            return this;
        }

        @Override
        public InstanceCreateBuilder volumeType(String volumeType) {
            instance.setVolumetype(volumeType);
            return this;
        }

        @Override
        public InstanceCreateBuilder volumeSize(int size) {
            instance.setvolumeSize(size);
            return this;
        }

        @Override
        public InstanceCreateBuilder availabilityZone(String availabilityZone) {
            instance.setAvailabilityZone(availabilityZone);
            return this;
        }

    }

    public class Volume {

        @JsonProperty("type")
        private String type;
        @JsonProperty("size")
        private Integer size;

        /**
         * @return The type
         */

        public String getType() {
            return type;
        }

        /**
         * @param type The type
         */

        public void setType(String type) {
            this.type = type;
        }

        /**
         * @param size The size
         */

        public int getSize() {
            return size;
        }

        /**
         * @param size The size
         */

        public void setSize(Integer size) {
            this.size = size;
        }

    }

}
