package com.fit2cloud.base.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fit2cloud.base.entity.VmCloudServerStatusTiming;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fit2cloud.dto.operation.VmCloudServerStatusTimingDTO;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 云主机运行关机状态计时 Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
public interface BaseVmCloudServerStatusTimingMapper extends MPJBaseMapper<VmCloudServerStatusTimingDTO> {
    /**
     * 云服务器状态时长列表
     * 如果 开关机时间不为空计算开关机时间到当前时间的时间差+已记录的开关机时长=云主机的最终开关机时长
     * @param queryWrapper 查询包装
     * @return {@link List }<{@link VmCloudServerStatusTimingDTO }>
     */
    List<VmCloudServerStatusTimingDTO> listVmCloudServerStatusTiming(@Param("ew") Wrapper queryWrapper);
}
