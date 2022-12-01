package org.openstack4j.model.artifact.builder;

import org.openstack4j.model.artifact.Metadata;

import java.util.List;

/**
 * A Builder which creates a Artifact
 *
 * @author Pavan Vadavi
 */
public interface ArtifactBuilder {

    ArtifactBuilder description(String description);

    ArtifactBuilder tags(List<Object> tags);

    ArtifactBuilder id(String id);

    ArtifactBuilder name(String name);

    ArtifactBuilder metadata(Metadata metadata);

    ArtifactBuilder release(List<Object> release);

    ArtifactBuilder owner(String owner);

    ArtifactBuilder supportedBy(Object supportedBy);

    ArtifactBuilder licenseUrl(Object licenseUrl);

    ArtifactBuilder version(String version);

    ArtifactBuilder providedBy(Object providedBy);

    ArtifactBuilder visibility(String visibility);

    ArtifactBuilder updatedAt(String updatedAt);

    ArtifactBuilder activatedAt(String activatedAt);

    ArtifactBuilder createdAt(String createdAt);

    ArtifactBuilder license(Object license);

    ArtifactBuilder icon(Object icon);

    ArtifactBuilder status(String status);

}
