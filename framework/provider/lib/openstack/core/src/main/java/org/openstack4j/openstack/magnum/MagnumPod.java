package org.openstack4j.openstack.magnum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.magnum.Label;
import org.openstack4j.model.magnum.Pod;
import org.openstack4j.model.magnum.PodBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MagnumPod implements Pod {
    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private String id;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("name")
    private String name;
    @JsonProperty("desc")
    private String desc;
    @JsonProperty("bay_uuid")
    private String bayUuid;
    @JsonProperty("images")
    private List<String> images;
    @JsonProperty("labels")
    private Label labels;
    @JsonProperty("status")
    private String status;

    public static PodBuilder builder() {
        return new PodConcreteBuilder();
    }

    @Override
    public PodBuilder toBuilder() {
        return new PodConcreteBuilder(this);
    }

    public String getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getBayUuid() {
        return bayUuid;
    }

    public List<String> getImages() {
        return images;
    }

    public Label getLabels() {
        return labels;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", id).add("uuid", uuid).add("name", name)
                .add("desc", desc).add("bayUuid", bayUuid).add("images", images).add("labels", labels)
                .add("status", status).toString();
    }

    /**
     * Concrete builder containing MagnumPod as model
     */
    public static class PodConcreteBuilder implements PodBuilder {
        MagnumPod model;

        public PodConcreteBuilder() {
            this(new MagnumPod());
        }

        public PodConcreteBuilder(MagnumPod model) {
            this.model = model;
        }

        @Override
        public Pod build() {
            return model;
        }

        @Override
        public PodBuilder from(Pod in) {
            if (in != null)
                this.model = (MagnumPod) in;
            return this;
        }

        @Override
        public PodBuilder id(String id) {
            model.id = id;
            return this;
        }

        @Override
        public PodBuilder uuid(String uuid) {
            model.uuid = uuid;
            return this;
        }

        @Override
        public PodBuilder name(String name) {
            model.name = name;
            return this;
        }

        @Override
        public PodBuilder desc(String desc) {
            model.desc = desc;
            return this;
        }

        @Override
        public PodBuilder bayUuid(String bayUuid) {
            model.bayUuid = bayUuid;
            return this;
        }

        @Override
        public PodBuilder images(List<String> images) {
            model.images = images;
            return this;
        }

        @Override
        public PodBuilder labels(Label labels) {
            model.labels = labels;
            return this;
        }

        @Override
        public PodBuilder status(String status) {
            model.status = status;
            return this;
        }
    }

    public static class Pods extends ListResult<MagnumPod> {
        private static final long serialVersionUID = 1L;
        @JsonProperty("pods")
        private List<MagnumPod> list;

        @Override
        public List<MagnumPod> value() {
            return list;
        }
    }
}
