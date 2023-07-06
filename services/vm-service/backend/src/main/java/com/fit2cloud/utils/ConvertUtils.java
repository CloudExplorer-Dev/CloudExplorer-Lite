package com.fit2cloud.utils;

import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.common.log.utils.LogUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import com.fit2cloud.vm.entity.F2CDisk;

import java.util.function.Function;

/**
 * @author : LiuDi
 * @date : 2023/2/27 16:42
 */
public class ConvertUtils {

    private static <T, R> R convert(Function<T, R> function, T t) {
        return function.apply(t);
    }

    public static VmCloudDisk disk(F2CDisk disk) {
        return convert(f2cDisk -> {
            VmCloudDisk vmCloudDisk = new VmCloudDisk();
            try {
                BeanUtils.copyProperties(disk, vmCloudDisk);
                vmCloudDisk.setDatastoreId(disk.getDatastoreUniqueId());
            } catch (Exception e) {
                LogUtil.error("vm_cloud_disk transfer error. {}", ExceptionUtils.getStackTrace(e));
            }
            return vmCloudDisk;
        }, disk);
    }
}
