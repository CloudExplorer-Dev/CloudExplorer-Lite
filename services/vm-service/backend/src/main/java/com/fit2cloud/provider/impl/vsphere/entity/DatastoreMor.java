package com.fit2cloud.provider.impl.vsphere.entity;

import com.vmware.vim25.ManagedObjectReference;
import lombok.Data;
import com.fit2cloud.vm.entity.F2CDatastore;

/**
 * @author jianneng
 * @date 2022/12/19 17:37
 **/
@Data
public class DatastoreMor extends F2CDatastore {

    private ManagedObjectReference mor;

}
