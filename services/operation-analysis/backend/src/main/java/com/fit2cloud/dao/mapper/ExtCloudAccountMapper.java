package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.dto.AnalysisCloudAccountDTO;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
public interface ExtCloudAccountMapper extends MPJBaseMapper<AnalysisCloudAccountDTO> {
    IPage<AnalysisCloudAccountDTO> pageAccountSummary(Page page, @Param("ew") Wrapper queryWrapper);
}
