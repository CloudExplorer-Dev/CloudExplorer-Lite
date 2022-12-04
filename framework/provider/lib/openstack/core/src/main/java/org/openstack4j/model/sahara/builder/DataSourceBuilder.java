package org.openstack4j.model.sahara.builder;

import org.openstack4j.common.Buildable.Builder;
import org.openstack4j.model.sahara.DataSource;

/**
 * Builder interface used for {@link DataSource} object.
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
public interface DataSourceBuilder extends Builder<DataSourceBuilder, DataSource> {

    /**
     * See {@link DataSource#getDescription()}
     *
     * @param description the description of the data source
     * @return DataSourceBuilder
     */
    DataSourceBuilder description(String description);

    /**
     * See {@link DataSource#getURL()}
     *
     * @param url the URL of the data source
     * @return DataSourceBuilder
     */
    DataSourceBuilder url(String url);

    /**
     * See {@link DataSource#getType()}
     *
     * @param type the type of the data source
     * @return DataSourceBuilder
     */
    DataSourceBuilder type(String type);

    /**
     * See {@link DataSource#getName()}
     *
     * @param name the name of the data source
     * @return DataSourceBuilder
     */
    DataSourceBuilder name(String name);

    /**
     * See {@link DataSource#getCredentials()}
     *
     * @param user     username of the credentials
     * @param password password of the credentials
     * @return DataSourceBuilder
     */
    DataSourceBuilder credentials(String user, String password);

}
