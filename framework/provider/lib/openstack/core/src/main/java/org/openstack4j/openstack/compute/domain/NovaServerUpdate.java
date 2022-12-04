package org.openstack4j.openstack.compute.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.compute.ServerUpdateOptions;

/**
 * The JSON object used to update an existing server
 *
 * @author Jeremy Unruh
 */
@JsonRootName("server")
public class NovaServerUpdate implements ModelEntity {

    private static final long serialVersionUID = 1L;

    @JsonProperty("name")
    private String name;
    @JsonProperty("accessIPv4")
    private String accessIPv4;
    @JsonProperty("accessIPv6")
    private String accessIPv6;

    /**
     * Creates a NovaServerUpdate from Options
     *
     * @param options the server update options
     * @return NovaServerUpdate instance
     */
    public static NovaServerUpdate fromOptions(ServerUpdateOptions options) {
        NovaServerUpdate su = new NovaServerUpdate();
        su.name = options.getName();
        su.accessIPv4 = options.getAccessIPv4();
        su.accessIPv6 = options.getAccessIPv6();
        return su;
    }

}
