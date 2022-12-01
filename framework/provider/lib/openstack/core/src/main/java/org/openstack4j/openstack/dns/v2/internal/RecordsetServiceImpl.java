package org.openstack4j.openstack.dns.v2.internal;

import org.openstack4j.api.dns.v2.RecordsetService;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.dns.v2.Recordset;
import org.openstack4j.openstack.dns.v2.domain.DesignateRecordset;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.openstack4j.core.transport.ClientConstants.PATH_RECORDSETS;
import static org.openstack4j.core.transport.ClientConstants.PATH_ZONES;

public class RecordsetServiceImpl extends BaseDNSServices implements RecordsetService {

    @Override
    public Recordset get(String zoneId, String recordsetId) {
        checkNotNull(zoneId);
        checkNotNull(recordsetId);
        return get(DesignateRecordset.class, PATH_ZONES, "/", zoneId, PATH_RECORDSETS, "/", recordsetId).execute();
    }

    @Override
    public ActionResponse delete(String zoneId, String recordsetId) {
        checkNotNull(zoneId);
        checkNotNull(recordsetId);
        return deleteWithResponse(PATH_ZONES, "/", zoneId, PATH_RECORDSETS, "/", recordsetId).execute();
    }

    @Override
    public Recordset update(String zoneId, Recordset recordset) {
        checkNotNull(zoneId);
        checkNotNull(recordset);
        return put(DesignateRecordset.class, PATH_ZONES, "/", zoneId, PATH_RECORDSETS, "/", recordset.getId()).entity(recordset).execute();
    }

    @Override
    public Recordset create(String zoneId, Recordset recordset) {
        checkNotNull(zoneId);
        checkNotNull(recordset);
        return post(DesignateRecordset.class, PATH_ZONES, "/", zoneId, PATH_RECORDSETS).entity(recordset).execute();
    }

    @Override
    public Recordset create(String zoneId, String name, String type, List<String> records) {
        checkNotNull(zoneId);
        checkNotNull(name);
        checkNotNull(type);
        checkNotNull(records);
        return create(zoneId, DesignateRecordset.builder().name(name).type(type).records(records).build());
    }

    @Override
    public List<? extends Recordset> list(String zoneId) {
        checkNotNull(zoneId);
        return get(DesignateRecordset.Recordsets.class, PATH_ZONES, "/", zoneId, PATH_RECORDSETS).execute().getList();
    }

    @Override
    public List<? extends Recordset> list() {
        return get(DesignateRecordset.Recordsets.class, uri(PATH_RECORDSETS)).execute().getList();
    }

}
