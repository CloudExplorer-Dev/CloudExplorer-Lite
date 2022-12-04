package org.openstack4j.model.sahara;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.sahara.builder.DataSourceBuilder;

import java.util.Date;

/**
 * An OpenStack Data Source
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public interface DataSource extends ModelEntity, Buildable<DataSourceBuilder> {

    /**
     * @return the description of the data source
     */
    String getDescription();

    /**
     * @return the URL of the data source
     */
    String getURL();

    /**
     * @return the tenant id of the data source
     */
    String getTenantId();

    /**
     * @return the created date of the data source
     */
    Date getCreatedAt();

    /**
     * @return the updated date of the data source
     */
    Date getUpdatedAt();

    /**
     * @return the type of the data source
     */
    String getType();

    /**
     * @return the identifier of the data source
     */
    String getId();

    /**
     * @return the name of the data source
     */
    String getName();

    /**
     * @return the credentials of the data source
     */
    DataSourceCredentials getCredentials();
}
