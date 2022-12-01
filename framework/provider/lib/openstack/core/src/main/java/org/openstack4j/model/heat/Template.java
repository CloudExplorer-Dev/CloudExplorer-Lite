package org.openstack4j.model.heat;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.ModelEntity;
import org.openstack4j.model.heat.builder.TemplateBuilder;

/**
 * This interface describes a template object.
 *
 * @author Matthias Reisser
 */
public interface Template extends ModelEntity, Buildable<TemplateBuilder> {

    /**
     * Returns the JSON-representation of the template
     *
     * @return the JSON formatted template
     */
    String getTemplateJson();

    /**
     * The URL of the template to instantiate. This value is ignored if the template is supplied inline.
     *
     * @return the template URL
     */
    String getTemplateURL();


}
