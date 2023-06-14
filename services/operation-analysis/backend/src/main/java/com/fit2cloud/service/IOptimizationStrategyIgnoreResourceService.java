package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.controller.request.server.PageServerRequest;
import com.fit2cloud.controller.request.server.ServerRequest;
import com.fit2cloud.dao.entity.OptimizationStrategyIgnoreResource;
import com.fit2cloud.dto.optimization.VmCloudServerDTO;

import java.util.List;

/**
 * <p>
 * 优化策略忽略资源 服务类
 * </p>
 *
 * @author fit2cloud
 */
public interface IOptimizationStrategyIgnoreResourceService extends IService<OptimizationStrategyIgnoreResource> {

    /**
     * 批量插入忽略资源通过优化策略id
     *
     * @param optimizationStrategyId 优化策略id
     * @param resourceIdList         资源id列表
     * @param batchOperate           批量操作
     * @return boolean
     */
    boolean batchInsertIgnoreResourceByOptimizationStrategyId(String optimizationStrategyId, List<String> resourceIdList, boolean batchOperate);


    /**
     * 批量删除优化策略忽略资源
     *
     * @param optimizationStrategyId 优化策略id
     * @param resourceIdList         资源id列表
     * @return boolean
     */
    boolean batchRemoveOptimizationStrategyIgnoreResource(String optimizationStrategyId, List<String> resourceIdList);

    /**
     * 删除忽略资源通过优化策略id
     *
     * @param optimizationStrategyId 优化策略id
     * @return boolean
     */
    boolean deleteIgnoreResourceByOptimizationStrategyId(String optimizationStrategyId);


    /**
     * 分页查询优化策略云主机列表
     *
     * @param request 请求
     * @return {@link IPage }<{@link VmCloudServerDTO }>
     */
    IPage<VmCloudServerDTO> pageVmCloudServerList(PageServerRequest request);

    /**
     * 查询优化策略云主机列表
     *
     * @param request 请求
     * @return {@link IPage }<{@link VmCloudServerDTO }>
     */
    List<VmCloudServerDTO> vmCloudServerList(ServerRequest request);

    /**
     * 得到优化策略忽略资源id列表
     *
     * @param optimizationStrategyId 优化策略id
     * @return {@link List }<{@link String }>
     */
    List<String> getOptimizationStrategyIgnoreResourceIdList(String optimizationStrategyId);

}
