package org.openstack4j.openstack.image.v2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.image.v2.Member;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;

/**
 * Implementation of Glance V2 member
 *
 * @author emjburns
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GlanceMember implements Member {

    @JsonProperty("image_id")
    private String imageId;

    @JsonProperty("member_id")
    private String memberId;

    @JsonProperty("created_at")
    private Date createdAt;

    @JsonProperty("updated_at")
    private Date updatedAt;

    private MemberStatus status = null;

    private String schema;

    @JsonProperty("member")
    private String member;

    public GlanceMember() {
    }

    /**
     * Constructor used in createMember
     */
    public GlanceMember(String memberId) {
        this.member = memberId;
    }

    /**
     * Constructor used in updateMember
     */
    public GlanceMember(MemberStatus status) {
        this.status = status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getImageId() {
        return imageId;
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
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberStatus getStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSchema() {
        return schema;
    }

    /**
     * {@inheritDoc}
     */


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("imageId", imageId)
                .add("memberId", memberId)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .add("status", status)
                .add("schema", schema)
                .toString();
    }

    public static class Members extends ListResult<GlanceMember> {
        @JsonProperty("members")
        List<GlanceMember> members;

        /**
         * {@inheritDoc}
         */
        @Override
        protected List<GlanceMember> value() {
            return members;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("members", members)
                    .toString();
        }
    }
}
