package org.openstack4j.openstack.manila.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.collect.Maps;
import org.openstack4j.model.manila.Share;
import org.openstack4j.model.manila.ShareManage;
import org.openstack4j.model.manila.builder.ShareManageBuilder;

import java.util.Map;

/**
 * Object to configure Shared File Systems to manage a share.
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("share")
public class ManilaShareManage implements ShareManage {
    private Share.Protocol protocol;
    private String name;
    @JsonProperty("share_type")
    private String shareType;
    @JsonProperty("driver_options")
    private Map<String, String> driverOptions;
    @JsonProperty("export_path")
    private String exportPath;
    @JsonProperty("service_host")
    private String serviceHost;
    private String description;

    public static ShareManageBuilder builder() {
        return new ShareManageConcreteBuilder();
    }

    @Override
    public Share.Protocol getProtocol() {
        return protocol;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getShareType() {
        return shareType;
    }

    @Override
    public Map<String, String> getDriverOptions() {
        return driverOptions;
    }

    @Override
    public String getExportPath() {
        return exportPath;
    }

    @Override
    public String getServiceHost() {
        return serviceHost;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ShareManageBuilder toBuilder() {
        return new ShareManageConcreteBuilder(this);
    }

    public static class ShareManageConcreteBuilder implements ShareManageBuilder {
        ManilaShareManage shareManage;

        public ShareManageConcreteBuilder() {
            this(new ManilaShareManage());
        }

        public ShareManageConcreteBuilder(ManilaShareManage shareManage) {
            this.shareManage = shareManage;
        }

        @Override
        public ShareManageBuilder protocol(Share.Protocol protocol) {
            shareManage.protocol = protocol;
            return this;
        }

        @Override
        public ShareManageBuilder name(String name) {
            shareManage.name = name;
            return this;
        }

        @Override
        public ShareManageBuilder shareType(String shareType) {
            shareManage.shareType = shareType;
            return this;
        }

        @Override
        public ShareManageBuilder addDriverOption(String key, String value) {
            if (shareManage.driverOptions == null)
                shareManage.driverOptions = Maps.newHashMap();

            shareManage.driverOptions.put(key, value);
            return this;
        }

        @Override
        public ShareManageBuilder driverOptions(Map<String, String> driverOptions) {
            shareManage.driverOptions = driverOptions;
            return this;
        }

        @Override
        public ShareManageBuilder exportPath(String exportPath) {
            shareManage.exportPath = exportPath;
            return this;
        }

        @Override
        public ShareManageBuilder serviceHost(String serviceHost) {
            shareManage.serviceHost = serviceHost;
            return this;
        }

        @Override
        public ShareManageBuilder description(String description) {
            shareManage.description = description;
            return this;
        }

        @Override
        public ShareManage build() {
            return shareManage;
        }

        @Override
        public ShareManageBuilder from(ShareManage in) {
            shareManage = (ManilaShareManage) in;
            return this;
        }
    }
}
