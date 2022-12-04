package org.openstack4j.openstack.image.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.image.ImageMember;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;

/**
 * Represents a system tenant who has access to another tenants Image
 *
 * @author Jeremy Unruh
 */
@JsonRootName("member")
public class GlanceImageMember implements ImageMember {

    private static final long serialVersionUID = 1L;

    @JsonProperty("member_id")
    private String memberId;
    @JsonProperty("can_share")
    private boolean canShare;

    public GlanceImageMember() {
    }

    public GlanceImageMember(String memberId, boolean canShare) {
        this.memberId = memberId;
        this.canShare = canShare;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMemberId() {
        return memberId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCanShare() {
        return canShare;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("memberId", memberId).add("canShare", canShare).toString();
    }

    public static class Members extends ListResult<GlanceImageMember> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("members")
        private List<GlanceImageMember> members;

        @Override
        protected List<GlanceImageMember> value() {
            return members;
        }
    }

}
