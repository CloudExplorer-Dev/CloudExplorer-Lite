package org.openstack4j.model.gbp;

import org.openstack4j.common.Buildable;
import org.openstack4j.model.common.Resource;
import org.openstack4j.model.gbp.builder.ExternalPolicyBuilder;

import java.util.List;
import java.util.Map;

/**
 * External Policy Model Entity
 *
 * @author vinod borole
 */
public interface ExternalPolicyCreate extends Resource, Buildable<ExternalPolicyBuilder> {
    /**
     * Gets the consumed policy rule sets Ids
     *
     * @return the consumed policy rule sets ids
     */
    Map<String, String> getConsumedPolicyRuleSets();

    /**
     * Gets the provided policy rule sets Ids
     *
     * @return the provided policy rule sets ids
     */
    Map<String, String> getProvidedPolicyRuleSets();

    /**
     * Gets the external segments associated with this external policy Ids
     *
     * @return the external segment ids
     */
    List<String> getExternalSegments();

    /**
     * Gets the description
     *
     * @return the description
     */
    String getDescription();

    /**
     * Is external policy shared
     *
     * @return the true if shared and false if not shared
     */
    boolean isShared();

} 
