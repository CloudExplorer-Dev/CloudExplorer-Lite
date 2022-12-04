package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.sahara.JobBinaryCredentials;

/**
 * For mapping JSON response to/from java objects
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */

@JsonRootName("extra")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaharaJobBinaryCredentials implements JobBinaryCredentials {

    private static final long serialVersionUID = 1L;

    private String password;
    private String user;

    SaharaJobBinaryCredentials(String user, String password) {
        this.user = user;
        this.password = password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUser() {
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("password", password)
                .add("user", user)
                .toString();
    }

}
