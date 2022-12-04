package org.openstack4j.openstack.internal;

import com.google.common.base.Function;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.core.transport.HttpMethod;
import org.openstack4j.model.common.ActionResponse;

/**
 * Base class for OpenStack services which use micro-versions.
 *
 * @author Daniel Gonzalez Nothnagel
 */
public abstract class MicroVersionedOpenStackService extends BaseOpenStackService {
    private final MicroVersion microVersion;

    protected MicroVersionedOpenStackService(MicroVersion microVersion) {
        this.microVersion = microVersion;
    }

    protected MicroVersionedOpenStackService(ServiceType serviceType, MicroVersion microVersion) {
        super(serviceType);
        this.microVersion = microVersion;
    }

    protected MicroVersionedOpenStackService(ServiceType serviceType, MicroVersion microVersion,
                                             Function<String, String> endpointFunc) {
        super(serviceType, endpointFunc);
        this.microVersion = microVersion;
    }

    private MicroVersion getMicroVersion() {
        return microVersion;
    }

    protected abstract String getApiVersionHeader();

    @Override
    protected <R> Invocation<R> get(Class<R> returnType, String... path) {
        return super.get(returnType, path).header(getApiVersionHeader(), getMicroVersion().toString());
    }

    @Override
    protected <R> Invocation<R> post(Class<R> returnType, String... path) {
        return super.post(returnType, path).header(getApiVersionHeader(), getMicroVersion().toString());
    }

    @Override
    protected <R> Invocation<R> put(Class<R> returnType, String... path) {
        return super.put(returnType, path).header(getApiVersionHeader(), getMicroVersion().toString());
    }

    @Override
    protected <R> Invocation<R> patch(Class<R> returnType, String... path) {
        return super.patch(returnType, path).header(getApiVersionHeader(), getMicroVersion().toString());
    }

    @Override
    protected <R> Invocation<R> delete(Class<R> returnType, String... path) {
        return super.delete(returnType, path).header(getApiVersionHeader(), getMicroVersion().toString());
    }

    @Override
    protected Invocation<ActionResponse> deleteWithResponse(String... path) {
        return super.deleteWithResponse(path).header(getApiVersionHeader(), getMicroVersion().toString());
    }

    @Override
    protected <R> Invocation<R> head(Class<R> returnType, String... path) {
        return super.head(returnType, path).header(getApiVersionHeader(), getMicroVersion().toString());
    }

    @Override
    protected <R> Invocation<R> request(HttpMethod method, Class<R> returnType, String path) {
        return super.request(method, returnType, path).header(getApiVersionHeader(), getMicroVersion().toString());
    }
}
