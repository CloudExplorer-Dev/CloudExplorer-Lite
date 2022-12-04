package org.openstack4j.openstack.networking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;
import org.openstack4j.model.network.Agent;
import org.openstack4j.model.network.builder.AgentBuilder;
import org.openstack4j.openstack.common.ListResult;
import org.openstack4j.openstack.internal.Parser;

import java.util.Date;
import java.util.List;

/**
 * Neutron Agent
 *
 * @author Yin Zhang
 * @author Qin An
 */
@JsonRootName("agent")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeutronAgent implements Agent {

    private static final long serialVersionUID = 1L;
    private String binary;
    private String description;
    @JsonProperty("admin_state_up")
    private boolean adminStateUp;
    @JsonProperty("heartbeat_timestamp")
    private String heartbeatTimeStamp;
    private boolean alive;
    private String topic;
    private String host;
    @JsonProperty("agent_type")
    private Type agentType;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("started_at")
    private String startedAt;
    private String id;

    public static AgentBuilder builder() {
        return new AgentConcreteBuilder();
    }

    @Override
    public boolean getAdminStateUp() {
        return adminStateUp;
    }

    @Override
    public Type getAgentType() {
        return agentType;
    }

    @Override
    public boolean getAlive() {
        return alive;
    }

    @Override
    public String getBinary() {
        return binary;
    }

    @Override
    public Date getCreatedAt() {
        return Parser.parseSimpleDate(createdAt);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Date getHeartbeatTimestamp() {
        return Parser.parseSimpleDate(heartbeatTimeStamp);
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Date getStartedAt() {
        return Parser.parseSimpleDate(startedAt);
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", id).add("binary", binary)
                .add("description", description).add("admin_state_up", adminStateUp)
                .add("heartbeat_timestamp", heartbeatTimeStamp).add("alive", alive).add("topic", topic)
                .add("host", host).add("agent_type", agentType.value()).add("created_at", createdAt)
                .add("started_at", startedAt).toString();
    }

    @Override
    public AgentBuilder toBuilder() {
        return new AgentConcreteBuilder(this);
    }

    public static class Agents extends ListResult<NeutronAgent> {

        private static final long serialVersionUID = 1L;

        @JsonProperty("agents")
        private List<NeutronAgent> agents;

        @Override
        public List<NeutronAgent> value() {
            return agents;
        }
    }

    public static class AgentConcreteBuilder implements AgentBuilder {

        private NeutronAgent model;

        AgentConcreteBuilder() {
            this(new NeutronAgent());
        }

        AgentConcreteBuilder(NeutronAgent model) {
            this.model = model;
        }

        public AgentBuilder name() {
            return this;
        }

        @Override
        public Agent build() {
            return model;
        }

        @Override
        public AgentBuilder from(Agent in) {
            model = (NeutronAgent) in;
            return this;
        }

        @Override
        public AgentBuilder adminStateUp(boolean state) {
            model.adminStateUp = state;
            return this;
        }
    }

}
