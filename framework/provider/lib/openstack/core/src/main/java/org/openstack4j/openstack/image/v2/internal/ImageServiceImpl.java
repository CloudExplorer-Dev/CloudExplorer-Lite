package org.openstack4j.openstack.image.v2.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonpatch.diff.JsonDiff;
import org.openstack4j.api.Apis;
import org.openstack4j.api.exceptions.ResponseException;
import org.openstack4j.api.image.v2.ImageService;
import org.openstack4j.api.image.v2.TaskService;
import org.openstack4j.core.transport.ExecutionOptions;
import org.openstack4j.core.transport.HttpResponse;
import org.openstack4j.core.transport.propagation.PropagateOnStatus;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.image.v2.CachedImage;
import org.openstack4j.model.image.v2.Image;
import org.openstack4j.model.image.v2.ImageUpdate;
import org.openstack4j.model.image.v2.Member;
import org.openstack4j.openstack.image.v2.domain.CachedGlanceImage.CachedImages;
import org.openstack4j.openstack.image.v2.domain.GlanceImage;
import org.openstack4j.openstack.image.v2.domain.GlanceImageUpdate;
import org.openstack4j.openstack.image.v2.domain.GlanceMember;

import javax.annotation.Nullable;
import java.io.*;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.openstack4j.core.transport.ClientConstants.*;

/**
 * Implementation of Glance V2 Image Service
 *
 * @author emjburns
 */
public class ImageServiceImpl extends BaseImageServices implements ImageService {
    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Image> list() {
        return get(GlanceImage.Images.class, uri("/images")).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Image> list(Map<String, String> filteringParams) {
        return get(GlanceImage.Images.class, uri("/images")).params(filteringParams).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends CachedImage> listCachedImages() {
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
    public Image get(String imageId) {
        checkNotNull(imageId);
        return get(GlanceImage.class, uri("/images/%s", imageId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image create(Image image) {
        checkNotNull(image);
        return post(GlanceImage.class, uri("/images")).entity(image).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image update(Image image) {
        checkNotNull(image);

        ObjectMapper objectMapper = new ObjectMapper();
        Image origImage = get(image.getId());
        ObjectNode origJson;
        ObjectNode newJson;

        try {
            String oImg = objectMapper.writeValueAsString(origImage);
            origJson = (ObjectNode) objectMapper.readTree(oImg);

            String img = objectMapper.writeValueAsString(image);
            newJson = (ObjectNode) objectMapper.readTree(img);

            JsonNode jsonDiff = JsonDiff.asJson(origJson, newJson);
            GlanceImageUpdate giu = new GlanceImageUpdate(jsonDiff);
            return update(image.getId(), giu);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image update(String imageId, ImageUpdate imageUpdate) {
        checkNotNull(imageId);
        checkNotNull(imageUpdate);
        return patch(GlanceImage.class, uri("/images/%s", imageId)).header(HEADER_CONTENT_TYPE, CONTENT_TYPE_IMAGE_V2_PATCH).entity(imageUpdate).execute();
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
    public ActionResponse deactivate(String imageId) {
        checkNotNull(imageId);
        return post(ActionResponse.class, uri("/images/%s/actions/deactivate", imageId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse reactivate(String imageId) {
        checkNotNull(imageId);
        return post(ActionResponse.class, uri("/images/%s/actions/reactivate", imageId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse upload(String imageId, Payload<?> payload, @Nullable Image image) {
        checkNotNull(imageId);
        checkNotNull(payload);
        return put(ActionResponse.class, uri("/images/%s/file", imageId)).header(HEADER_CONTENT_TYPE, CONTENT_TYPE_OCTECT_STREAM).entity(payload).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse download(String imageId, File filename) {
        checkNotNull(imageId);
        checkNotNull(filename);

        HttpResponse response = get(Void.class, uri("/images/%s/file", imageId)).header(HEADER_ACCEPT, CONTENT_TYPE_OCTECT_STREAM).executeWithResponse();
        if (response.getStatus() < 400) {
            InputStream inputStream = response.getInputStream();
            OutputStream outputStream;
            try {
                outputStream = new FileOutputStream(filename);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
                return ActionResponse.actionSuccess();
            } catch (Exception e) {
                e.printStackTrace();
                return ActionResponse.actionFailed("Failed to write to file " + e.getMessage(), 400);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse updateTag(String imageId, String tag) {
        checkNotNull(imageId);
        checkNotNull(tag);
        return put(ActionResponse.class, uri("/images/%s/tags/%s", imageId, tag)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse deleteTag(String imageId, String tag) {
        checkNotNull(imageId);
        checkNotNull(tag);
        return deleteWithResponse(uri("/images/%s/tags/%s", imageId, tag)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Member> listMembers(String imageId) {
        checkNotNull(imageId);
        return get(GlanceMember.Members.class, uri("/images/%s/members", imageId)).execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends Member> listMembers(String imageId, Map<String, String> filteringParams) {
        checkNotNull(imageId);
        Invocation<GlanceMember.Members> req = get(GlanceMember.Members.class, uri("/images/%s/members", imageId));
        if (filteringParams != null) {
            for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
                req = req.param(entry.getKey(), entry.getValue());
            }
        }
        return req.execute().getList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Member getMember(String imageId, String memberId) {
        checkNotNull(imageId);
        checkNotNull(memberId);
        return get(Member.class, uri("/images/%s/members/%s", imageId, memberId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Member createMember(String imageId, String memberId) {
        checkNotNull(imageId);
        checkNotNull(memberId);
        return post(Member.class, uri("/images/%s/members", imageId)).entity(new GlanceMember(memberId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Member updateMember(String imageId, String memberId, Member.MemberStatus memberStatus) {
        checkNotNull(imageId);
        checkNotNull(memberId);
        return put(Member.class, uri("/images/%s/members/%s", imageId, memberId)).entity(new GlanceMember(memberStatus)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResponse deleteMember(String imageId, String memberId) {
        checkNotNull(imageId);
        checkNotNull(memberId);
        return deleteWithResponse(uri("/images/%s/members/%s", imageId, memberId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskService tasks() {
        return Apis.get(TaskService.class);
    }
}
