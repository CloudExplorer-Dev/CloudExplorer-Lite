package org.openstack4j.model.manila;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents extra specifications for share types.
 *
 * @author Daniel Gonzalez Nothnagel
 */
@JsonRootName("extra_specs")
public class ExtraSpecs extends HashMap<String, String> implements ModelEntity {
    private static final long serialVersionUID = 1L;

    /**
     * Creates extra specifications from a map.
     *
     * @param from the base map
     * @return the extra specifications created from the map
     */
    public static ExtraSpecs toExtraSpecs(Map<String, String> from) {
        ExtraSpecs extraSpecs = new ExtraSpecs();
        extraSpecs.putAll(from);
        return extraSpecs;
    }
}
