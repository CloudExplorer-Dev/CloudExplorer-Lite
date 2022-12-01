package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.collect.Maps;
import org.openstack4j.model.manila.ExtraSpecs;
import org.openstack4j.model.manila.ShareTypeCreate;
import org.openstack4j.model.manila.builder.ShareTypeCreateBuilder;

/**
 * Object used to create new share types.
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("share_type")
public class ManilaShareTypeCreate implements ShareTypeCreate {
    private static final long serialVersionUID = 1L;

    @JsonProperty("extra_specs")
    ExtraSpecs extraSpecs;
    @JsonProperty("os-share-type-access:is_public")
    Boolean osShareTypeAccessIsPublic;
    String name;

    public static ShareTypeCreateBuilder builder() {
        return new ShareTypeCreateConcreteBuilder();
    }

    @Override
    public ExtraSpecs getExtraSpecs() {
        return extraSpecs;
    }

    @Override
    public Boolean getOsShareTypeAccessIsPublic() {
        return osShareTypeAccessIsPublic;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ShareTypeCreateBuilder toBuilder() {
        return new ShareTypeCreateConcreteBuilder(this);
    }

    public static class ShareTypeCreateConcreteBuilder implements ShareTypeCreateBuilder {
        ManilaShareTypeCreate shareTypeCreate;

        public ShareTypeCreateConcreteBuilder() {
            this(new ManilaShareTypeCreate());
        }

        public ShareTypeCreateConcreteBuilder(ManilaShareTypeCreate shareTypeCreate) {
            this.shareTypeCreate = shareTypeCreate;
        }

        @Override
        public ShareTypeCreateBuilder addExtraSpec(String key, String value) {
            if (shareTypeCreate.extraSpecs == null)
                shareTypeCreate.extraSpecs = ExtraSpecs.toExtraSpecs(
                        Maps.<String, String>newHashMap());

            shareTypeCreate.extraSpecs.put(key, value);
            return this;
        }

        @Override
        public ShareTypeCreateBuilder extraSpecs(ExtraSpecs extraSpecs) {
            shareTypeCreate.extraSpecs = extraSpecs;
            return this;
        }

        @Override
        public ShareTypeCreateBuilder osShareTypeAccessIsPublic(boolean osShareTypeAccessIsPublic) {
            shareTypeCreate.osShareTypeAccessIsPublic = osShareTypeAccessIsPublic;
            return this;
        }

        @Override
        public ShareTypeCreateBuilder name(String name) {
            shareTypeCreate.name = name;
            return this;
        }

        @Override
        public ShareTypeCreate build() {
            return shareTypeCreate;
        }

        @Override
        public ShareTypeCreateBuilder from(ShareTypeCreate in) {
            shareTypeCreate = (ManilaShareTypeCreate) in;
            return this;
        }
    }
}
