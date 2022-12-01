package org.openstack4j.model.network;

/**
 * The resource of Neutron
 *
 * @author bboyHan
 */
public enum Resource {

    SECURITY_GROUP("security-groups"),
    NETWORK("networks"),
    SUBNET("subnets"),
    PORT("ports"),
    ROUTER("routers"),
    SUBNET_POOL("subnetpools"),
    FLOATING_IP("floatingips"),
    QOS_POLICY("policies"),
    TRUNK("trunks");

    private final String value;

    Resource(String value) {
        this.value = value;
    }

    public static String forValue(Resource resource) {
        if (resource != null) {
            for (Resource s : Resource.values()) {
                if (s.name().equalsIgnoreCase(resource.name()))
                    return s.value;
            }
        }
        return null;
    }

}
