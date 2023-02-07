package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fit2cloud.controller.response.BarTreeChartData;
import com.fit2cloud.dao.entity.OrgWorkspace;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
public interface OrgWorkspaceMapper extends MPJBaseMapper<OrgWorkspace> {
    List<BarTreeChartData> analyticsVmCloudDiskByOrgWorkspace(@Param("ew") Wrapper queryWrapper);
    List<BarTreeChartData> analyticsVmCloudDiskByOrgWorkspaceFromSize(@Param("ew") Wrapper queryWrapper);
}
