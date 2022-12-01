package org.openstack4j.api.types;

import java.util.regex.Pattern;

public enum ServiceType {

    IDENTITY("keystone", "identity"),
    APP_CATALOG("murano", "application-catalog"),
    COMPUTE("nova", "compute"),
    IMAGE("glance", "image"),
    BLOCK_STORAGE("cinder", "volume"),
    OBJECT_STORAGE("object-store", "object-store"),
    NETWORK("neutron", "network"),
    OCTAVIA("octavia", "load-balancer"),
    PLACEMENT("placement", "placement"),
    EC2("ec2", "ec2"),
    TELEMETRY("ceilometer", "metering"),
    TELEMETRY_AODH("aodh", "alarming"),
    ORCHESTRATION("heat", "orchestration"),
    CLUSTERING("senlin", "clustering"),
    SAHARA("sahara", "data_processing"),
    SHARE("manila", "share"),
    DATABASE("trove", "database"),
    BARBICAN("barbican", "key-manager"),
    TACKER("tacker", "nfv-orchestration"),
    ARTIFACT("glare", "artifact"),
    MAGNUM("magnum", "container"),
    DNS("designate", "dns"),
    WORKFLOW("mistral", "workflow"),
    UNKNOWN("NA", "NA");

    private static final String SERVICE_PATTERN_SUFFIX = "[v|\\d|\\.]*";
    private final String serviceName;
    private final String type;
    private final Pattern servicePattern;

    ServiceType(String serviceName, String type) {
        this.serviceName = serviceName;
        this.type = type;
        this.servicePattern = Pattern.compile(Pattern.quote(serviceName) + SERVICE_PATTERN_SUFFIX +
                        "|" + Pattern.quote(type) + SERVICE_PATTERN_SUFFIX +
                        "|" + Pattern.quote(this.name()) + SERVICE_PATTERN_SUFFIX
                , Pattern.CASE_INSENSITIVE);
    }

    public static ServiceType forName(String name) {
        if (name == null || name.isEmpty()) {
            return ServiceType.UNKNOWN;
        }
        for (ServiceType s : ServiceType.values()) {
            if (s.getServicePattern().matcher(name).matches())
                return s;
        }
        return ServiceType.UNKNOWN;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public String getType() {
        return this.type;
    }

    private Pattern getServicePattern() {
        return this.servicePattern;
    }
}
