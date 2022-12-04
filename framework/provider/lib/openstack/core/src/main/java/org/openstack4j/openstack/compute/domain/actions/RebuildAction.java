package org.openstack4j.openstack.compute.domain.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.compute.Personality;
import org.openstack4j.model.compute.actions.RebuildOptions;

import java.util.List;
import java.util.Map;

/**
 * An Action which Rebuilds an existing Server Instance
 *
 * @author Jeremy Unruh
 */
@JsonRootName("rebuild")
public class RebuildAction implements ServerAction {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String imageRef;

    @JsonProperty
    private String name;

    @JsonProperty
    private String adminPass;

    @JsonProperty("user_data")
    private String userData;
    private List<Personality> personality;
    private Map<String, String> metadata;

    public static RebuildAction create(RebuildOptions options) {
        RebuildAction action = new RebuildAction();

        if (options != null) {
            action.name = options.getName();
            action.adminPass = options.getAdminPass();
            action.imageRef = options.getImageRef();
            action.userData = options.getUserData();
            action.personality = options.getPersonality();
            action.metadata = options.getMetadata();
        }
        return action;
    }

    public String getImageRef() {
        return imageRef;
    }

    public String getName() {
        return name;
    }

    public String getAdminPass() {
        return adminPass;
    }

    public String getUserData() {
        return userData;
    }

    public List<Personality> getPersonality() {
        return personality;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }
}
