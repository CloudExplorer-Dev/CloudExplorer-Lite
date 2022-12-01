package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.sahara.ConfigInfo;
import org.openstack4j.model.sahara.Plugin;
import org.openstack4j.openstack.common.ListResult;

import java.util.List;
import java.util.Map;

/**
 * For mapping JSON response to/from java objects
 *
 * @author ekasit.kijsipongse@nectec.or.th
 */

@JsonRootName("plugin")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaharaPlugin implements Plugin {

    private static final long serialVersionUID = 1L;

    private String description;
    private List<String> versions;
    private String name;
    private String title;

    @JsonProperty("node_processes")
    private Map<String, List<String>> serviceProcesses;
    @JsonProperty("required_image_tags")
    private List<String> requiredImageTags;
    @JsonProperty("configs")
    private List<SaharaConfigInfo> configInfos;

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
    public List<String> getVersions() {
        return versions;
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
    public String getTitle() {
        return title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<String>> getServiceProcesses() {
        return serviceProcesses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getRequiredImageTags() {
        return requiredImageTags;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ConfigInfo> getConfigInfos() {
        return configInfos;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("name", name)
                .add("title", title)
                .add("description", description)
                .add("versions", versions)
                .add("required_image_tags", requiredImageTags)
                .add("node_processes", serviceProcesses)
                .add("configs", configInfos)
                .toString();
    }

    public static class SaharaPlugins extends ListResult<SaharaPlugin> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("plugins")
        private List<SaharaPlugin> plugins;

        public List<SaharaPlugin> value() {
            return plugins;
        }
    }

}
