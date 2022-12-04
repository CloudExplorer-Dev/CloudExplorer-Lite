package org.openstack4j.openstack.image.internal;

import org.openstack4j.api.client.CloudProvider;
import org.openstack4j.api.exceptions.ResponseException;
import org.openstack4j.api.image.ImageService;
import org.openstack4j.core.transport.ExecutionOptions;
import org.openstack4j.core.transport.HttpResponse;
import org.openstack4j.core.transport.propagation.PropagateOnStatus;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.image.CachedImage;
import org.openstack4j.model.image.Image;
import org.openstack4j.model.image.ImageMember;
import org.openstack4j.openstack.image.domain.CachedGlanceImage.CachedImages;
import org.openstack4j.openstack.image.domain.GlanceImage;
import org.openstack4j.openstack.image.domain.GlanceImage.Images;
import org.openstack4j.openstack.image.domain.GlanceImageMember;
import org.openstack4j.openstack.image.domain.GlanceImageMember.Members;
import org.openstack4j.openstack.image.domain.functions.ImageForUpdateToHeaders;
import org.openstack4j.openstack.image.domain.functions.ImageFromHeadersFunction;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.openstack4j.core.transport.ClientConstants.CONTENT_TYPE_OCTECT_STREAM;
import static org.openstack4j.core.transport.ClientConstants.HEADER_ACCEPT;
import static org.openstack4j.core.transport.HttpEntityHandler.statusAndClose;


/**
 * OpenStack (Glance) Image based Operations
 *
 * @author Jeremy Unruh
 */
public class ImageServiceImpl extends BaseImageServices implements ImageService {

    private static final int DEFAULT_PAGE_SIZE = 25;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends CachedImage> listChachedImages() {
        try {
            return get(CachedImages.class, uri("/cached_images"))
                    .execute(ExecutionOptions.<CachedImages>create(PropagateOnStatus.on(404))).getList();
        } catch (ResponseException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Image> list() {
        String uri = getProvider() == CloudProvider.RACKSPACE ? "/images" : "/images/detail";
        return get(Images.class, uri(uri)).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Image> list(Map<String, String> filteringParams) {
        Invocation<Images> imageInvocation = buildInvocation(filteringParams);
        return imageInvocation.execute().getList();
    }

    public List<? extends Image> listAll(Map<String, String> filteringParams) {
        Invocation<Images> imageInvocation = buildInvocation(filteringParams);

        int limit = DEFAULT_PAGE_SIZE;
        if (filteringParams != null && filteringParams.containsKey("limit")) {
            limit = Integer.parseInt(filteringParams.get("limit"));
        }

        List<GlanceImage> totalList = imageInvocation.execute().getList();
        List<GlanceImage> currList = totalList;
        while (currList.size() == limit) {

            imageInvocation.updateParam("marker", currList.get(limit - 1).getId());
            currList = imageInvocation.execute().getList();
            totalList.addAll(currList);
        }

        return totalList;
    }

    public List<? extends Image> listAll() {
        return listAll(null);
    }

    private Invocation<Images> buildInvocation(Map<String, String> filteringParams) {
        Invocation<Images> imageInvocation = get(Images.class, "/images/detail");
        if (filteringParams == null) {
            return imageInvocation;
        }
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                imageInvocation = imageInvocation.param(entry.getKey(), entry.getValue());
            }
        }
        return imageInvocation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image get(String imageId) {
        checkNotNull(imageId);
        return head(Image.class, uri("/images/%s", imageId)).execute(ExecutionOptions.create(ImageFromHeadersFunction.instance()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse delete(String imageId) {
        checkNotNull(imageId);
        return deleteWithResponse(uri("/images/%s", imageId)).param("format", "json").execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image update(Image image) {
        checkNotNull(image);
        checkNotNull(image.getId());
        return put(GlanceImage.class, uri("/images/%s", image.getId())).headers(ImageForUpdateToHeaders.instance().apply(image)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getAsStream(String imageId) {
        checkNotNull(imageId);
        HttpResponse response = get(Void.class, uri("/images/%s", imageId)).header(HEADER_ACCEPT, CONTENT_TYPE_OCTECT_STREAM).executeWithResponse();
        if (response.getStatus() < 400)
            return response.getInputStream();
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image create(Image image, Payload<?> payload) {
        checkNotNull(image);
        if (payload == null)
            return reserve(image);

        return post(GlanceImage.class, uri("/images")).headers(ImageForUpdateToHeaders.instance().apply(image)).entity(payload).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image reserve(Image image) {
        checkNotNull(image);
        return post(GlanceImage.class, uri("/images")).headers(ImageForUpdateToHeaders.instance().apply(image)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image upload(String imageId, Payload<?> payload, @Nullable Image image) {
        checkNotNull(imageId);
        checkNotNull(payload);
        return put(GlanceImage.class, uri("/images/%s", imageId)).headers(ImageForUpdateToHeaders.instance().apply(image)).entity(payload).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ImageMember> listMembers(String imageId) {
        checkNotNull(imageId);
        return get(Members.class, uri("/images/%s/members", imageId)).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addMember(String imageId, String tenantId) {
        checkNotNull(imageId);
        checkNotNull(tenantId);
        return statusAndClose(put(Void.class, uri("/images/%s/members/%s", imageId, tenantId)).executeWithResponse()) == 204;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addMember(String imageId, String tenantId, boolean canShare) {
        checkNotNull(imageId);
        checkNotNull(tenantId);
        return statusAndClose(put(Void.class, uri("/images/%s/members/%s", imageId, tenantId)).entity(new GlanceImageMember(null, canShare)).executeWithResponse()) == 204;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeMember(String imageId, String tenantId) {
        checkNotNull(imageId);
        checkNotNull(tenantId);
        return statusAndClose(delete(Void.class, uri("/images/%s/members/%s", imageId, tenantId)).executeWithResponse()) == 204;
    }
}
