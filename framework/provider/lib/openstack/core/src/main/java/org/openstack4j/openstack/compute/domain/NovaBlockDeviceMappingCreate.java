package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.compute.BDMDestType;
import org.openstack4j.model.compute.BDMSourceType;
import org.openstack4j.model.compute.BlockDeviceMappingCreate;
import org.openstack4j.model.compute.builder.BlockDeviceMappingBuilder;

/**
 * @author jaroslav.sovicka@oracle.com
 */
public class NovaBlockDeviceMappingCreate implements BlockDeviceMappingCreate {

    public String device_name;
    public BDMSourceType source_type = BDMSourceType.VOLUME;
    public BDMDestType destination_type = BDMDestType.VOLUME;
    public String uuid;
    public Integer boot_index;
    public Integer volume_size;
    public boolean delete_on_termination = false;

    @JsonProperty("snapshot_id")
    public String snapshotId;

    @JsonProperty("volume_id")
    public String volumeId;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("disk_bus")
    public String diskBus;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty("device_type")
    public String deviceType;

    public static NovaBlockDeviceMappingBuilder builder() {
        return new NovaBlockDeviceMappingBuilder(new NovaBlockDeviceMappingCreate());
    }


    @Override
    public NovaBlockDeviceMappingBuilder toBuilder() {
        return new NovaBlockDeviceMappingBuilder(this);
    }

    public static class NovaBlockDeviceMappingBuilder implements BlockDeviceMappingBuilder {

        NovaBlockDeviceMappingCreate create;

        public NovaBlockDeviceMappingBuilder(NovaBlockDeviceMappingCreate create) {
            this.create = create;
        }

        @Override
        public BlockDeviceMappingBuilder deviceName(String deviceName) {
            create.device_name = deviceName;
            return this;
        }

        @Override
        public BlockDeviceMappingBuilder uuid(String id) {
            create.uuid = id;
            return this;
        }

        @Override
        public BlockDeviceMappingBuilder bootIndex(int i) {
            create.boot_index = i;
            return this;
        }

        @Override
        public BlockDeviceMappingBuilder sourceType(BDMSourceType type) {
            create.source_type = type;
            return this;
        }

        @Override
        public BlockDeviceMappingBuilder destinationType(BDMDestType type) {
            create.destination_type = type;
            return this;
        }

        @Override
        public BlockDeviceMappingBuilder deleteOnTermination(boolean deleteOnTermination) {
            create.delete_on_termination = deleteOnTermination;
            return this;
        }

        @Override
        public BlockDeviceMappingBuilder snapshotId(String snapshotId) {
            create.snapshotId = snapshotId;
            return this;
        }

        @Override
        public BlockDeviceMappingBuilder volumeId(String volumeId) {
            create.volumeId = volumeId;
            return this;
        }

        @Override
        public BlockDeviceMappingBuilder volumeSize(Integer volumeSize) {
            create.volume_size = volumeSize;
            return this;
        }

        @Override
        public BlockDeviceMappingBuilder diskBus(String diskBus) {
            create.diskBus = diskBus;
            return this;
        }

        @Override
        public BlockDeviceMappingBuilder deviceType(String deviceType) {
            create.deviceType = deviceType;
            return this;
        }

        @Override
        public BlockDeviceMappingCreate build() {
            return create;
        }

        @Override
        public BlockDeviceMappingBuilder from(BlockDeviceMappingCreate in) {
            return new NovaBlockDeviceMappingBuilder((NovaBlockDeviceMappingCreate) in);
        }
    }
}
