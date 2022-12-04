package org.openstack4j.openstack.artifact.internal;

import org.openstack4j.api.types.ServiceType;
import org.openstack4j.core.transport.HttpResponse;
import org.openstack4j.model.artifact.ArtifactType;
import org.openstack4j.model.artifact.ArtifactUpdate;
import org.openstack4j.model.artifact.ToscaTemplatesArtifact;
import org.openstack4j.model.artifact.builder.ArtifactUpdateBuilder;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.common.payloads.FilePayload;
import org.openstack4j.openstack.artifact.domain.ArtifactUpdateModel;
import org.openstack4j.openstack.common.ListEntity;
import org.openstack4j.openstack.common.functions.EnforceVersionToURL;
import org.openstack4j.openstack.internal.BaseOpenStackService;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.openstack4j.core.transport.ClientConstants.*;

/**
 * Created by vadavi on 18-01-2017.
 */
public class BaseArtifactServiceImpl extends BaseOpenStackService {
    private ArtifactType artifactType;

    protected BaseArtifactServiceImpl(ArtifactType artifactType) {

        super(ServiceType.ARTIFACT, EnforceVersionToURL.instance(""));
        this.artifactType = artifactType;

    }

    protected <T> T list(Class<T> clazz) {

        return get(clazz, uri(PATH_ARTIFACTS + URI_SEP + artifactType.value())).execute();

    }

    protected <T> T get(String artifactId, Class<T> clazz) {
        return get(clazz, uri(PATH_ARTIFACTS + URI_SEP + artifactType.value() + "/%s", artifactId)).execute();
    }

    protected <T> T create(ToscaTemplatesArtifact toscaTemplatesArtifact, Class<T> clazz) {
        return post(clazz, uri(PATH_ARTIFACTS + URI_SEP + artifactType.value())).entity(toscaTemplatesArtifact).execute();
    }

    protected <T> T upload(String artifactId, File file, Class<T> clazz, String blobName) {
        Payload<?> payload = new FilePayload(file);
        Invocation<T> invocation = put(clazz, uri(PATH_ARTIFACTS + URI_SEP + artifactType.value() + "/%s/%s", artifactId, blobName));
        invocation.entity(payload);
        return invocation.execute();
    }

    protected InputStream download(String artifactId, String blobName) {
        Invocation<Void> invocation = get(Void.class, uri(PATH_ARTIFACTS + URI_SEP + artifactType.value() + "/%s/%s", artifactId, blobName));
        invocation.header(HEADER_ACCEPT, CONTENT_TYPE_OCTECT_STREAM);
        HttpResponse response = invocation.executeWithResponse();
        if (response.getStatus() < 400) {
            return response.getInputStream();
        }
        return null;
    }

    protected ActionResponse delete(String artifactId) {
        return deleteWithResponse(uri(PATH_ARTIFACTS + URI_SEP + artifactType.value() + "/%s", artifactId)).execute();
    }

    protected <T> T update(String artifactId, List<ArtifactUpdate> artifactUpdates, Class<T> clazz) {
        Invocation<T> invocation = patch(clazz, uri(PATH_ARTIFACTS + URI_SEP + artifactType.value() + "/%s", artifactId));
        invocation.entity(new ListEntity<ArtifactUpdate>(artifactUpdates));
        invocation.contentType(CONTENT_TYPE_ARTIFACT_PATCH);
        return invocation.execute();
    }

    protected <T> T update(String artifactId, String op, String path, String value, Class<T> clazz) {
        ArtifactUpdateBuilder updateBuilder = ArtifactUpdateModel.builder();
        updateBuilder.op(op);
        updateBuilder.path(path);
        updateBuilder.value(value);

        List<ArtifactUpdate> artifactUpdates = new ArrayList<>();
        artifactUpdates.add(updateBuilder.build());

        Invocation<T> invocation = patch(clazz, uri(PATH_ARTIFACTS + URI_SEP + artifactType.value() + "/%s", artifactId));
        invocation.entity(new ListEntity<ArtifactUpdate>(artifactUpdates));
        invocation.contentType(CONTENT_TYPE_ARTIFACT_PATCH);
        return invocation.execute();
    }

}
