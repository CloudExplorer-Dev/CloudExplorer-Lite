package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.dao.entity.OptimizationStrategy;
import com.fit2cloud.dto.optimization.OptimizationStrategyDTO;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 资源优化策略 Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
public interface OptimizationStrategyMapper extends MPJBaseMapper<OptimizationStrategy> {

    /**
     * 分页查询优化策略
     *
     * @param page
     * @param queryWrapper 查询包装
     * @return {@link IPage }<{@link OptimizationStrategyDTO }>
     */
    IPage<OptimizationStrategyDTO> pageList(Page page, @Param("ew") Wrapper queryWrapper);

}
