package org.openstack4j.openstack.storage.block.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.openstack4j.model.ModelEntity;

/**
 * A request entity used for accepting a volume transfer
 *
 * @author Jeremy Unruh
 */
@JsonRootName("accept")
public class CinderVolumeTransferAccept implements ModelEntity {

    private static final long serialVersionUID = 1L;

    @JsonProperty("auth_key")
    private String authKey;

    public CinderVolumeTransferAccept() {
    }

    private CinderVolumeTransferAccept(String authKey) {
        this.authKey = authKey;
    }

    public static CinderVolumeTransferAccept create(String authKey) {
        return new CinderVolumeTransferAccept(authKey);
    }

}
