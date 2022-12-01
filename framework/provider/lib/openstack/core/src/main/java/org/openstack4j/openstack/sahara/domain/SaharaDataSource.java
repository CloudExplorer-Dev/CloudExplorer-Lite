package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.sahara.DataSource;
import org.openstack4j.model.sahara.DataSourceCredentials;
import org.openstack4j.model.sahara.builder.DataSourceBuilder;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;

/**
 * For mapping JSON response to/from java objects
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */
@JsonRootName("data_source")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaharaDataSource implements DataSource {

    private static final long serialVersionUID = 1L;

    @JsonProperty("description")
    private String description;
    @JsonProperty("url")
    private String url;
    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date createdAt;
    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date updatedAt;
    @JsonProperty("type")
    private String type;
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    private SaharaDataSourceCredentials credentials;

    /**
     * @return the data source Builder
     */
    public static DataSourceBuilder builder() {
        return new ConcreteDataSourceBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getURL() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTenantId() {
        return tenantId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSourceCredentials getCredentials() {
        return credentials;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("description", description)
                .add("url", url)
                .add("tenant_id", tenantId)
                .add("created_at", createdAt)
                .add("updated_at", updatedAt)
                .add("type", type)
                .add("id", id)
                .add("name", name)
                .toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataSourceBuilder toBuilder() {
        return new ConcreteDataSourceBuilder(this);
    }

    public static class DataSources extends ListResult<SaharaDataSource> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("data_sources")
        private List<SaharaDataSource> datasources;

        public List<SaharaDataSource> value() {
            return datasources;
        }
    }

    public static class ConcreteDataSourceBuilder implements DataSourceBuilder {

        SaharaDataSource m;

        ConcreteDataSourceBuilder() {
            this(new SaharaDataSource());
        }

        ConcreteDataSourceBuilder(SaharaDataSource m) {
            this.m = m;
        }

        @Override
        public DataSourceBuilder description(String description) {
            m.description = description;
            return this;
        }

        @Override
        public DataSourceBuilder url(String url) {
            m.url = url;
            return this;
        }

        @Override
        public DataSourceBuilder type(String type) {
            m.type = type;
            return this;
        }

        @Override
        public DataSourceBuilder name(String name) {
            m.name = name;
            return this;
        }

        @Override
        public DataSourceBuilder credentials(String user, String password) {
            m.credentials = new SaharaDataSourceCredentials(user, password);
            return this;
        }

        @Override
        public DataSource build() {
            return m;
        }

        @Override
        public DataSourceBuilder from(DataSource in) {
            m = (SaharaDataSource) in;
            return this;
        }

    }

}
