package org.openstack4j.model.placement.ext;

/**
 * Provider inventories body contains resource provider inventories
 *
 * @author Jyothi Saroja
 */
public interface ProviderInventoriesBody {

    /**
     * @return the resource provider generation contained in the unnamed body
     */
    String getGeneration();

    /**
     * @return the resource provider inventories contained in the unnamed body
     */
    ResourceProviderInventories getInventories();
}
