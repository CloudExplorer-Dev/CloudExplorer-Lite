package org.openstack4j.openstack.murano.v1.domain;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openstack4j.model.murano.v1.domain.ServiceInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Nikolay Mahotkin.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MuranoServiceInfo implements ServiceInfo {
    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String type;

    private List<MuranoActionInfo> actions;

    @JsonProperty
    private String status;

    @JsonAnySetter
    public void setAction(String key, Object value) {
        if (key.equals("_actions")) {
            if (this.actions == null) {
                this.actions = new ArrayList<>();
            }

            ObjectMapper mapper = new ObjectMapper();

            for (Map.Entry<String, Object> entry : ((Map<String, Object>) value).entrySet()) {
                MuranoActionInfo action = mapper.convertValue(entry.getValue(), MuranoActionInfo.class);
                action.setId(entry.getKey());

                this.actions.add(action);
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MuranoActionInfo> getActions() {
        return this.actions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStatus() {
        return this.status;
    }
}
