package com.fit2cloud.base.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface BaseVmCloudDiskMapper extends MPJBaseMapper<VmCloudDisk> {
    @Select("SELECT source_id as `key`,count(*) as `value` FROM  vm_cloud_disk  ${ew.customSqlSegment} GROUP BY source_id")
    List<DefaultKeyValue<String, Integer>> groupSourceId(@Param(Constants.WRAPPER) Wrapper<VmCloudDisk> wrapper);

    @Select("SELECT source_id as `key`,sum(size) as `value` FROM  vm_cloud_disk  ${ew.customSqlSegment} GROUP BY source_id")
    List<DefaultKeyValue<String, Integer>> groupSourceIdBySize(@Param(Constants.WRAPPER) Wrapper<VmCloudDisk> wrapper);
}
