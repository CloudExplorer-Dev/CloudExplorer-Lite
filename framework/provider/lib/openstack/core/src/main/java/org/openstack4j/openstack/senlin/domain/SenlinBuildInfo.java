package org.openstack4j.openstack.senlin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.senlin.BuildInfo;

import java.util.Map;

/**
 * This is a model of a senlinBuild_info. It uses Jackson annotations for
 * (de)serialization into JSON format
 *
 * @author lion
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("build_info")
public class SenlinBuildInfo implements BuildInfo {
    private static final long serialVersionUID = -7596480972776554810L;

    @JsonProperty("api")
    private Map<String, String> api;
    @JsonProperty("engine")
    private Map<String, String> engine;

    @Override
    public Map<String, String> getApi() {
        return api;
    }

    @Override
    public Map<String, String> getEngine() {
        return engine;
    }

    @Override
    public String toString() {
        return "SenlinBuildInfo{" +
                "api=" + api +
                ", engine=" + engine +
                '}';
    }
}
