package org.openstack4j.openstack.compute.domain.ext;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.compute.ext.Service;
import org.openstack4j.openstack.common.ListResult;

import java.util.Date;
import java.util.List;

@JsonRootName("service")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtService implements Service {

    private static final long serialVersionUID = 1L;

    private String binary;

    @JsonProperty("disabled_reason")
    private String disabledReason;

    private String host;

    private String id;

    private State state;

    private Status status;

    @JsonProperty("updated_at")
    private Date updatedAt;

    private String zone;

    @JsonProperty("forced_down")
    private String forcedDown;

    @Override
    public String getBinary() {
        return binary;
    }

    @Override
    public String getDisabledReason() {
        return disabledReason;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String getZone() {
        return zone;
    }

    /**
     * <p>Author:Wang Ting</p>
     *
     * @Title: getForcedDown
     * @see org.openstack4j.model.compute.Service#getDorcedDown()
     */
    @Override
    public String getForcedDown() {
        return forcedDown;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", id).add("binary", binary).add("host", host)
                .add("zone", zone).add("status", status).add("state", state).add("updated_at", updatedAt)
                .add("disabled_reason", disabledReason).add("forced_down", forcedDown).toString();
    }

    public static class Services extends ListResult<ExtService> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("services")
        private List<ExtService> services;

        @Override
        public List<ExtService> value() {
            return services;
        }
    }

}
