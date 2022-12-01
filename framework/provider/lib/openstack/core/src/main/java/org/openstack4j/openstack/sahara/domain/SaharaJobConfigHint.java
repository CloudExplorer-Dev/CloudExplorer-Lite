package org.openstack4j.openstack.sahara.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.sahara.JobConfigHint;
import org.openstack4j.model.sahara.JobConfigHintConfig;

import java.util.List;

/**
 * For mapping JSON response to/from java objects
 *
 * @author ekasit.kijsipongse@nectec.or.th
 * @author siwat.pru@outlook.com
 */

@JsonRootName("job_config")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaharaJobConfigHint implements JobConfigHint {

    private static final long serialVersionUID = 1L;
    @JsonProperty("configs")
    private List<SaharaJobConfigHintConfig> configs;
    @JsonProperty("args")
    private List<Object> args;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends JobConfigHintConfig> getConfigs() {
        return configs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Object> getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("configs", configs)
                .add("args", args)
                .toString();
    }

}
