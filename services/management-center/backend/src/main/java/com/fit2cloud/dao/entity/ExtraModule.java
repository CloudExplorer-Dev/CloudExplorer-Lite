package com.fit2cloud.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtraModule {

    private String name;

    @JsonProperty("display_name")
    private String displayName;

    private String version;

    private String icon;

    private int port;

    private String description;

    @JsonProperty("update_infos")
    private List<String> updateInfos;

    @JsonProperty("download_url")
    private String downloadUrl;

    private String currentVersion;

    private boolean installed;

    private String status;

}
