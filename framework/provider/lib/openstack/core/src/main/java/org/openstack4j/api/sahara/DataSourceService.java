package org.openstack4j.api.sahara;

import org.openstack4j.common.RestService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.sahara.DataSource;

import java.util.List;

/**
 * Sahara Data Processing Operations
 *
 * @author ekasit.kijsipongse@nectec.or.th
 */
public interface DataSourceService extends RestService {

    /**
     * List all data sources
     *
     * @return list of data sources or empty
     */
    List<? extends DataSource> list();

    /**
     * Get a data source by ID
     *
     * @param datasourceId the data source identifier
     * @return the data source or null if not found
     */
    DataSource get(String datasourceId);

    /**
     * Create a new data source
     *
     * @param datasource the data source to create
     * @return the created data source
     */
    DataSource create(DataSource datasource);

    /**
     * Delete the specified data source
     *
     * @param datasourceId the data source identifier
     * @return the action response
     */
    ActionResponse delete(String datasourceId);

}
