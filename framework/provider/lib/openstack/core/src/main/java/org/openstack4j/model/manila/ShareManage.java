package org.openstack4j.model.manila;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.manila.builder.ShareManageBuilder;

import java.util.Map;

/**
 * Object to configure Shared File Systems to manage a share.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface ShareManage extends ModelEntity, Buildable<ShareManageBuilder> {
    /**
     * @return the Shared File Systems protocol of the share to manage
     */
    Share.Protocol getProtocol();

    /**
     * @return the share name
     */
    String getName();

    /**
     * @return the share type name
     */
    String getShareType();

    /**
     * @return a set of one or more key and value pairs, as a dictionary of strings, that describe driver options
     */
    Map<String, String> getDriverOptions();

    /**
     * @return the share export path in the format appropriate for the protocol
     */
    String getExportPath();

    /**
     * @return the manage-share service host in this format: <code>host@backend#POOL</code>
     */
    String getServiceHost();

    /**
     * @return the share description
     */
    String getDescription();
}
