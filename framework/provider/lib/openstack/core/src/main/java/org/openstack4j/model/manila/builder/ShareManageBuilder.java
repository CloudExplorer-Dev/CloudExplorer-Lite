package org.openstack4j.model.manila.builder;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.manila.Share;
import org.openstack4j.model.manila.ShareCreate;
import org.openstack4j.model.manila.ShareManage;

import java.util.Map;

/**
 * Builds a {@link ShareCreate}.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public interface ShareManageBuilder extends Buildable.Builder<ShareManageBuilder, ShareManage> {
    /**
     * Set the Shared File Systems protocol of the share to manage. A valid value is NFS, CIFS, GlusterFS, or HDFS.
     *
     * @param protocol the share protocol
     * @return ShareManageBuilder
     */
    ShareManageBuilder protocol(Share.Protocol protocol);

    /**
     * Set the share name.
     *
     * @param name the share name
     * @return ShareManageBuilder
     */
    ShareManageBuilder name(String name);

    /**
     * Set the share type name.
     *
     * @param shareType the share type
     * @return ShareManageBuilder
     */
    ShareManageBuilder shareType(String shareType);

    /**
     * Add a driver option.
     *
     * @param key   the key of the driver option
     * @param value the value of the driver option
     * @return ShareManageBuilder
     */
    ShareManageBuilder addDriverOption(String key, String value);

    /**
     * Set the driver options.
     *
     * @param driverOptions the driver options
     * @return ShareManageBuilder
     */
    ShareManageBuilder driverOptions(Map<String, String> driverOptions);

    /**
     * Set the share export path.
     *
     * @param exportPath the share export path
     * @return ShareManageBuilder
     */
    ShareManageBuilder exportPath(String exportPath);

    /**
     * Set the manage-share service host in this format: <code>host@backend#POOL</code>.
     *
     * @param serviceHost the service host
     * @return ShareManageBuilder
     */
    ShareManageBuilder serviceHost(String serviceHost);

    /**
     * Set the share description
     *
     * @param desciption the share description
     * @return ShareManageBuilder
     */
    ShareManageBuilder description(String desciption);
}
