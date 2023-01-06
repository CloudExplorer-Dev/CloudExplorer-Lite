package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.dto.VmCloudDiskDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface VmCloudDiskMapper extends BaseMapper<VmCloudDiskDTO> {


    /**
     * TODO 这个地方慢慢累加查询条件
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<VmCloudDiskDTO> pageList(Page page, @Param("ew") Wrapper queryWrapper);

    VmCloudDiskDTO selectDiskDetailById(@Param("id") String id);
}
