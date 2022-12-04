package org.openstack4j.openstack.artifact.internal;

import com.google.common.base.Preconditions;
import org.openstack4j.api.artifact.ToscaTemplatesArtifactService;
import org.openstack4j.model.artifact.ArtifactType;
import org.openstack4j.model.artifact.ArtifactUpdate;
import org.openstack4j.model.artifact.ToscaTemplatesArtifact;
import org.openstack4j.model.artifact.ToscaTemplatesArtifacts;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.openstack.artifact.domain.ToscaTemplates;
import org.openstack4j.openstack.artifact.domain.ToscaTemplatesList;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by vadavi on 19-01-2017.
 */
public class ToscaTemplatesArtifactServiceImpl extends BaseArtifactServiceImpl implements ToscaTemplatesArtifactService {

    public ToscaTemplatesArtifactServiceImpl() {
        super(ArtifactType.TOSCA_TEMPLATES);
    }

    @Override
    public ToscaTemplatesArtifacts list() {

        return super.list(ToscaTemplatesList.class);

    }

    @Override
    public ToscaTemplatesArtifact get(String artifactId) {
        Preconditions.checkNotNull(artifactId);
        return super.get(artifactId, ToscaTemplates.class);
    }

    @Override
    public ToscaTemplatesArtifact create(ToscaTemplatesArtifact toscaTemplatesArtifact) {
        Preconditions.checkNotNull(toscaTemplatesArtifact);
        return super.create(toscaTemplatesArtifact, ToscaTemplates.class);
    }

    @Override
    public ToscaTemplatesArtifact upload(String artifactId, File file) {
        Preconditions.checkNotNull(artifactId);
        return super.upload(artifactId, file, ToscaTemplates.class, "template");
    }

    @Override
    public InputStream download(String artifactId) {
        Preconditions.checkNotNull(artifactId);
        return super.download(artifactId, "template");
    }

    @Override
    public ActionResponse delete(String artifactId) {
        Preconditions.checkNotNull(artifactId);
        return super.delete(artifactId);
    }

    @Override
    public ToscaTemplatesArtifact update(String artifactId, List<ArtifactUpdate> artifactUpdates) {
        Preconditions.checkNotNull(artifactId);
        Preconditions.checkNotNull(artifactUpdates);
        return super.update(artifactId, artifactUpdates, ToscaTemplates.class);
    }

    @Override
    public ToscaTemplatesArtifact activate(String artifactId) {
        Preconditions.checkNotNull(artifactId);
        return update(artifactId, "replace", "/status", "active", ToscaTemplates.class);
    }

    @Override
    public ToscaTemplatesArtifact deactivate(String artifactId) {
        Preconditions.checkNotNull(artifactId);
        return update(artifactId, "replace", "/status", "deactivated", ToscaTemplates.class);
    }

    @Override
    public ToscaTemplatesArtifact reactivate(String artifactId) {
        Preconditions.checkNotNull(artifactId);
        return update(artifactId, "replace", "/status", "active", ToscaTemplates.class);
    }

    @Override
    public ToscaTemplatesArtifact publish(String artifactId) {
        Preconditions.checkNotNull(artifactId);
        return update(artifactId, "replace", "/visibility", "public", ToscaTemplates.class);
    }

}
