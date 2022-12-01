package org.openstack4j.api.heat;

import org.openstack4j.common.RestService;

/**
 * This interface containts all available HeatServices
 *
 * @author Matthias Reisser
 */
public interface HeatService extends RestService {

    /**
     * Service implementation which provides methods for manipulation of stacks
     *
     * @return StackService
     */
    StackService stacks();

    /**
     * Service implementation which provides methods for validating Templates
     *
     * @return TemplateService
     */
    TemplateService templates();

    /**
     * Service implementation which provides methods for Events
     *
     * @return EventsService
     */
    EventsService events();

    /**
     * Service implementation which provides methods for Resources
     *
     * @return ResourcesService
     */
    ResourcesService resources();

    /**
     * Service implementation which provides methods for Software Configurations
     *
     * @return SoftwareConfigService
     */
    SoftwareConfigService softwareConfig();
}
