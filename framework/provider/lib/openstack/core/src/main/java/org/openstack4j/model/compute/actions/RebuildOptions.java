package org.openstack4j.model.compute.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.openstack4j.model.compute.Personality;

import java.util.List;
import java.util.Map;

/**
 * Options used to invoke the Rebuild Action on a Server
 *
 * @author Jeremy Unruh
 */
public final class RebuildOptions extends BaseActionOptions {
    private RebuildOptions() {
        super();
    }

    /**
     * @return a new RebuildOptions object
     */
    public static RebuildOptions create() {
        return new RebuildOptions();
    }

    /**
     * Can optionally set a new image to rebuild the server against.  This is the image identifier
     *
     * @param imageId the image id to rebuild the server against
     * @return RebuildOptions
     */
    public RebuildOptions image(String imageId) {
        add(Option.IMAGE, imageId);
        return this;
    }

    /**
     * Can optionally change the name of the instance to a new {@code name}
     *
     * @param name the new name of the server instance
     * @return RebuildOptions
     */
    public RebuildOptions name(String name) {
        add(Option.NAME, name);
        return this;
    }

    public RebuildOptions userData(String userData) {
        add(Option.USER_DATA, userData);
        return this;
    }

    public RebuildOptions personality(List<Personality> personality) {
        add(Option.PERSONALITY, personality);
        return this;
    }

    public RebuildOptions metadata(Map<String, String> metadata) {
        add(Option.METADATA, metadata);
        return this;
    }

    /**
     * Can optionally specify a new admin password to be used during the rebuild
     *
     * @param adminPass the new admin password
     * @return RebuildOptions
     */
    public RebuildOptions adminPass(String adminPass) {
        add(Option.ADMIN_PASS, adminPass);
        return this;
    }

    public String getName() {
        return get(Option.NAME);
    }

    public String getAdminPass() {
        return get(Option.ADMIN_PASS);
    }

    public String getImageRef() {
        return get(Option.IMAGE);
    }

    @JsonProperty("user_data")
    public String getUserData() {
        return get(Option.USER_DATA);
    }

    public List<Personality> getPersonality() {
        return get(Option.PERSONALITY);
    }

    public Map<String, String> getMetadata() {
        return get(Option.METADATA);
    }

    private enum Option implements OptionEnum {
        IMAGE("imageRef"),
        NAME("name"),
        ADMIN_PASS("adminPass"),
        USER_DATA("user_data"),
        PERSONALITY("personality"),
        METADATA("metadata");

        private final String param;

        private Option(String param) {
            this.param = param;
        }

        public String getParam() {
            return param;
        }
    }
}
