package org.openstack4j.model.compute;

/**
 * Options used to Update a Server instance
 *
 * @author Jeremy Unruh
 */
public class ServerUpdateOptions {

    private String name;
    private String accessIPv4;
    private String accessIPv6;

    public static ServerUpdateOptions create() {
        return new ServerUpdateOptions();
    }

    /**
     * The name of the server.
     * <p>
     * If you edit the server name, the server host name does not change.
     * Also, server names are not guaranteed to be unique.
     *
     * @param name the new name of the server
     * @return ServerUpdateOptions
     */
    public ServerUpdateOptions name(String name) {
        this.name = name;
        return this;
    }

    /**
     * The IP Version 4 Address
     *
     * @param accessIPv4 the IP Version 4 Address
     * @return ServerUpdateOptions
     */
    public ServerUpdateOptions accessIPv4(String accessIPv4) {
        this.accessIPv4 = accessIPv4;
        return this;
    }

    /**
     * The IP Version 6 Address
     *
     * @param accessIPv6 the IP Version 6 Address
     * @return ServerUpdateOptions
     */
    public ServerUpdateOptions accessIPv6(String accessIPv6) {
        this.accessIPv6 = accessIPv6;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getAccessIPv4() {
        return accessIPv4;
    }

    public String getAccessIPv6() {
        return accessIPv6;
    }
}
