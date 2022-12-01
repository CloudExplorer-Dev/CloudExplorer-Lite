package org.openstack4j.model.artifact;

import org.openstack4j.model.ModelEntity;

/**
 * A Glare Artifact Template
 *
 * @author Pavan Vadavi
 */
public interface Template extends ModelEntity {

    public String getMd5();

    public String getSha256();

    public String getContentType();

    public Boolean getExternal();

    public String getUrl();

    public String getSha1();

    public String getStatus();
}
