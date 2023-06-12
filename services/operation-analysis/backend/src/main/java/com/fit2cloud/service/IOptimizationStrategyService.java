package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.controller.request.optimize.CreateOrUpdateOptimizationStrategyRequest;
import com.fit2cloud.controller.request.optimize.PageOptimizationStrategyRequest;
import com.fit2cloud.dao.entity.OptimizationStrategy;
import com.fit2cloud.dto.optimization.OptimizationRuleField;
import com.fit2cloud.dto.optimization.OptimizationStrategyDTO;
import com.fit2cloud.dto.optimization.ResourceTypeDTO;

import java.util.List;


/**
 * 描述：优化策略服务
 *
 * @author jianneng
 */
public interface IOptimizationStrategyService extends IService<OptimizationStrategy> {


    /**
     * 分页查询优化策略列表
     *
     * @param request 请求
     * @return {@link IPage }<{@link OptimizationStrategyDTO }>
     * @author jianneng
     */
    IPage<OptimizationStrategyDTO> pageList(PageOptimizationStrategyRequest request);

    /**
     * 得到优化策略列表
     *
     * @param resourceType 资源类型
     * @return {@link OptimizationStrategy }
     * @author jianneng
     */
    List<OptimizationStrategy> getOptimizationStrategyList(String resourceType);


    /**
     * 得到一个优化策略
     *
     * @param optimizationStrategyId 优化策略id
     * @return {@link OptimizationStrategy }
     */
    OptimizationStrategyDTO getOneOptimizationStrategy(String optimizationStrategyId);


    /**
     * 保存或更新策略
     *
     * @param strategy 策略
     * @return boolean
     */
    boolean saveOrUpdateStrategy(CreateOrUpdateOptimizationStrategyRequest strategy);

    /**
     * 删除一个优化策略
     *
     * @param optimizationStrategyId 优化策略id
     * @return boolean
     */
    boolean deleteOneOptimizationStrategy(String optimizationStrategyId);

    /**
     * 批量删除优化策略
     *
     * @param optimizationStrategyIdList 优化策略id列表
     * @return boolean
     */
    boolean batchDeleteOptimizationStrategy(List<String> optimizationStrategyIdList);

    /**
     * 获取资源类型
     *
     * @return {@link List }<{@link OptimizationRuleField }>
     */
    List<ResourceTypeDTO> getResourceTypeList();

    /**
     * 改变状态
     *
     * @param optimizationStrategyDTO 优化策略dto
     * @return boolean
     */
    boolean changeStatus(OptimizationStrategyDTO optimizationStrategyDTO);


}
