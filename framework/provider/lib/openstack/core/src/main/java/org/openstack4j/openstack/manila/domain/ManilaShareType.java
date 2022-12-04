package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.manila.ExtraSpecs;
import org.openstack4j.model.manila.ShareType;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * A share type enables you to filter or choose back ends before you create a share.
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("share_type")
public class ManilaShareType implements ShareType {
    private String id;
    @JsonProperty("required_extra_specs")
    private ExtraSpecs requiredExtraSpecs;
    @JsonProperty("extra_specs")
    private ExtraSpecs extraSpecs;
    @JsonProperty("os-share-type-access:is_public")
    private Boolean osShareTypeAccessIsPublic;
    private String name;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ExtraSpecs getRequiredExtraSpecs() {
        return requiredExtraSpecs;
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

    public static class ShareTypes extends ListResult<ManilaShareType> {
        private static final long serialVersionUID = 1L;

        @JsonProperty("share_types")
        private List<ManilaShareType> shareTypes;

        @Override
        protected List<ManilaShareType> value() {
            return shareTypes;
        }
    }
}
