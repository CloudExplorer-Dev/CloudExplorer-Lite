package com.fit2cloud.provider.impl.vsphere.entity;

import com.fit2cloud.provider.entity.F2CDatastore;
import com.vmware.vim25.ManagedObjectReference;
import lombok.Data;

/**
 * @author jianneng
 * @date 2022/12/19 17:37
 **/
@Data
public class DatastoreMor extends F2CDatastore {

    private ManagedObjectReference mor;

}
