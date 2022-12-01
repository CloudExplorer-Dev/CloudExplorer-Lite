package org.openstack4j.openstack.murano.v1.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openstack4j.api.murano.v1.MuranoActionService;
import org.openstack4j.model.murano.v1.domain.ActionInfo;
import org.openstack4j.model.murano.v1.domain.ActionResult;
import org.openstack4j.model.murano.v1.domain.Application;
import org.openstack4j.model.murano.v1.domain.Environment;
import org.openstack4j.openstack.common.MapEntity;
import org.openstack4j.openstack.murano.v1.domain.MuranoActionResult;
import org.openstack4j.openstack.murano.v1.domain.MuranoEnvironment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Nikolay Mahotkin.
 */
public class MuranoActionServiceImpl extends BaseMuranoServices implements MuranoActionService {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ActionInfo> list(String envId, String serviceId) {
        checkNotNull(envId);
        checkNotNull(serviceId);

        Environment env = get(MuranoEnvironment.class, uri("/environments/%s", envId)).execute();

        for (Application app : env.getServices()) {
            if (app.getService().getId().equals(serviceId)) {
                return app.getService().getActions();
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ActionInfo> list(String envId) {
        checkNotNull(envId);

        return getAllActions(envId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionInfo get(String envId, String actionId) {
        checkNotNull(envId);
        checkNotNull(actionId);

        for (ActionInfo action : getAllActions(envId)) {
            if (action.getId().equals(actionId)) {
                return action;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionInfo find(String envId, String actionName) {
        checkNotNull(envId);
        checkNotNull(actionName);

        for (ActionInfo action : getAllActions(envId)) {
            if (action.getName().equals(actionName)) {
                return action;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<? extends ActionInfo> findAll(String envId, String actionName) {
        checkNotNull(envId);
        checkNotNull(actionName);

        List<ActionInfo> actions = new ArrayList<>();

        for (ActionInfo action : getAllActions(envId)) {
            if (action.getName().equals(actionName)) {
                actions.add(action);
            }
        }

        if (actions.size() == 0) {
            return null;
        }

        return actions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String cast(String envId, String actionId, String jsonString) {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> arguments = null;
        try {
            arguments = mapper.readValue(jsonString, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cast(envId, actionId, arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String cast(String envId, String actionId, Map<String, Object> arguments) {
        MapEntity entity = new MapEntity();
        entity.putAll(arguments);

        MapEntity result = post(MapEntity.class, uri("/environments/%s/actions/%s", envId, actionId))
                .entity(entity)
                .execute();

        return (String) result.get("task_id");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String cast(String envId, String actionId) {
        return cast(envId, actionId, "{}");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResult getResult(String envId, String taskId) {
        return get(MuranoActionResult.class, uri("/environments/%s/actions/%s", envId, taskId)).execute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResult run(String envId, String actionId) {
        return run(envId, actionId, "{}");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResult run(String envId, String actionId, String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> arguments = null;

        try {
            arguments = mapper.readValue(jsonString, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return run(envId, actionId, arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActionResult run(String envId, String actionId, Map<String, Object> arguments) {
        String taskId = cast(envId, actionId, arguments);

        ActionResult result = new MuranoActionResult();

        while (result.isException() == null) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            result = getResult(envId, taskId);
        }

        return result;
    }

    protected List<? extends ActionInfo> getAllActions(String envId) {
        Environment env = get(MuranoEnvironment.class, uri("/environments/%s", envId)).execute();

        List<ActionInfo> actions = new ArrayList<>();

        for (Application app : env.getServices()) {
            if (app.getService().getActions() != null) {
                actions.addAll(app.getService().getActions());
            }
        }

        if (actions.size() == 0) {
            return null;
        }

        return actions;
    }
}
