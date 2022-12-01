package org.openstack4j.openstack.manila.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.api.manila.ShareTypeService;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.manila.ExtraSpecs;
import org.openstack4j.model.manila.ShareType;
import org.openstack4j.model.manila.ShareTypeAccess;
import org.openstack4j.model.manila.ShareTypeCreate;
import org.openstack4j.openstack.compute.functions.ToActionResponseFunction;
import org.openstack4j.openstack.manila.domain.ManilaShareType;
import org.openstack4j.openstack.manila.domain.actions.ShareTypeAction;
import org.openstack4j.openstack.manila.domain.actions.ShareTypeActions;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Share Types Service for Manila Shared Filesystems.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public class ShareTypeServiceImpl extends BaseShareServices implements ShareTypeService {
    /**
     * {@inheritDoc}
     */
    @Override
    public ShareType create(ShareTypeCreate shareTypeCreate) {
        checkNotNull(shareTypeCreate);
        return post(ShareVolumeTypeWrapper.class, uri("/types"))
                .entity(shareTypeCreate)
                .execute().shareType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ShareType> list() {
        return get(ManilaShareType.ShareTypes.class, uri("/types"))
                .execute()
                .getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShareType listDefaults() {
        return get(ShareVolumeTypeWrapper.class, uri("/types/default")).execute().shareType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String shareTypeId) {
        checkNotNull(shareTypeId);
        return ToActionResponseFunction.INSTANCE.apply(
                delete(Void.class, uri("/types/%s", shareTypeId)).executeWithResponse());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtraSpecs listExtraSpecs(String shareTypeId) {
        checkNotNull(shareTypeId);
        return get(ExtraSpecs.class, uri("/types/%s/extra_specs", shareTypeId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtraSpecs setExtraSpec(String shareTypeId, ExtraSpecs extraSpecs) {
        checkNotNull(shareTypeId);
        checkNotNull(extraSpecs);

        return post(ExtraSpecs.class, uri("/types/%s/extra_specs", shareTypeId))
                .entity(extraSpecs)
                .execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse unsetExtraSpec(String shareTypeId, String extraSpecKey) {
        checkNotNull(shareTypeId);
        checkNotNull(extraSpecKey);

        return ToActionResponseFunction.INSTANCE.apply(
                delete(Void.class, uri("/types/%s/extra_specs/%s", shareTypeId, extraSpecKey)).executeWithResponse());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse addShareTypeAccess(String shareTypeId, String projectId) {
        checkNotNull(shareTypeId);
        checkNotNull(projectId);

        return invokeAction(shareTypeId, ShareTypeActions.addShareTypeAccess(projectId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse removeShareTypeAccess(String shareTypeId, String projectId) {
        checkNotNull(shareTypeId);
        checkNotNull(projectId);

        return invokeAction(shareTypeId, ShareTypeActions.removeShareTypeAccess(projectId));
    }

    /**
     * Invoke the action on the given share type.
     *
     * @param shareTypeId the share type ID
     * @param action      the action to invoke
     * @return the action response of the server
     */
    private ActionResponse invokeAction(String shareTypeId, ShareTypeAction action) {
        return ToActionResponseFunction.INSTANCE.apply(
                post(Void.class, uri("/types/%s/action", shareTypeId))
                        .entity(action)
                        .executeWithResponse());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ShareTypeAccess> shareTypeAccessDetails(String shareTypeId) {
        checkNotNull(shareTypeId);
        return get(ShareTypeAccess.ShareTypeAccessList.class, uri("/types/%s/os-share-type-access", shareTypeId))
                .execute().getList();
    }

    /**
     * Manila API creates both a <code>volume_type</code> object and a <code>share_type</code> object for many of its
     * responses. The use of <code>volume_type</code> is deprecated, but still supported by manila.
     * Therefore this wrapper class is used to unserialize the responses and extract the
     * {@see org.openstack4j.model.manila.ShareType} object.
     */
    private static class ShareVolumeTypeWrapper implements ModelEntity {
        @JsonProperty("volume_type")
        private ManilaShareType volumeType;
        @JsonProperty("share_type")
        private ManilaShareType shareType;
    }
}
