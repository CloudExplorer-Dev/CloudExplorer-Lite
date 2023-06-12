package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.common.exception.Fit2cloudException;
import com.fit2cloud.common.provider.util.CommonUtil;
import com.fit2cloud.common.utils.CurrentUserUtils;
import com.fit2cloud.common.utils.PageUtil;
import com.fit2cloud.constants.ErrorCodeConstants;
import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.controller.request.optimize.CreateOrUpdateOptimizationStrategyRequest;
import com.fit2cloud.controller.request.optimize.PageOptimizationStrategyRequest;
import com.fit2cloud.controller.response.OptimizationRuleFieldResponse;
import com.fit2cloud.dao.entity.OptimizationStrategy;
import com.fit2cloud.dao.entity.OptimizationStrategyIgnoreResource;
import com.fit2cloud.dao.mapper.OptimizationStrategyIgnoreResourceMapper;
import com.fit2cloud.dao.mapper.OptimizationStrategyMapper;
import com.fit2cloud.dto.optimization.*;
import com.fit2cloud.service.IOptimizationStrategyIgnoreResourceService;
import com.fit2cloud.service.IOptimizationStrategyService;
import com.fit2cloud.service.IPermissionService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.common.base.Joiner;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 资源优化策略 服务实现类
 * </p>
 *
 * @author fit2cloud
 */
@Service
public class OptimizationStrategyServiceImpl extends ServiceImpl<OptimizationStrategyMapper, OptimizationStrategy> implements IOptimizationStrategyService {

    @Resource
    private OptimizationStrategyMapper optimizationStrategyMapper;
    @Resource
    private IPermissionService permissionService;
    @Resource
    private IOptimizationStrategyIgnoreResourceService optimizationStrategyIgnoreResourceService;
    @Resource
    private OptimizationStrategyIgnoreResourceMapper optimizationStrategyIgnoreResourceMapper;

    /**
     * 分页列表
     *
     * @param request 请求
     * @return {@link IPage }<{@link OptimizationStrategyDTO }>
     */
    @Override
    public IPage<OptimizationStrategyDTO> pageList(PageOptimizationStrategyRequest request) {
        Page<OptimizationStrategyDTO> page = PageUtil.of(request, OptimizationStrategyDTO.class, null, true);
        // 构建查询参数
        MPJLambdaWrapper<OptimizationStrategy> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(OptimizationStrategy.class);
        wrapper.like(StringUtils.isNotEmpty(request.getName()), OptimizationStrategy::getName, request.getName());
        // 不是管理员就获取当前用户有权限的组织或工作空间下的优化策略
        List<String> sourceId = permissionService.getSourceIds();
        wrapper.in(!CurrentUserUtils.isAdmin(), OptimizationStrategy::getAuthorizeId, sourceId);
        IPage<OptimizationStrategyDTO> result = optimizationStrategyMapper.pageList(page, wrapper);
        result.getRecords().forEach(v -> {
            v.setOptimizationContent(getOptimizationContent(v));
        });
        return result;
    }

    public String getOptimizationContent(OptimizationStrategyDTO optimizationStrategyDTO) {
        OptimizationRule optimizationRule = optimizationStrategyDTO.getOptimizationRules();
        StringBuffer stringBuffer = new StringBuffer();
        if (Objects.nonNull(optimizationRule)) {
            if (Objects.nonNull(optimizationStrategyDTO.getDays())) {
                stringBuffer.append("持续").append(optimizationStrategyDTO.getDays()).append("天,");
            }
            ResourceTypeConstants resourceTypeConstants = ResourceTypeConstants.valueOf(optimizationStrategyDTO.getResourceType());
            List<OptimizationRuleField> list = CommonUtil.exec(OptimizationRuleFieldProviderImpl.class, resourceTypeConstants.getOptimizationRuleFieldList());
            formatOptimizationContentFromOptimizationRule(stringBuffer, optimizationRule, optimizationRule.getChildren(), list);
        }
        return stringBuffer.toString();
    }


    /**
     * 从优化规则格式优化内容
     *
     * @param stringBuffer     最终优化内容
     * @param optimizationRule 优化规则
     * @param children         子集
     * @param list             列表
     * @author jianneng
     * @date 2023/05/31
     */
    public void formatOptimizationContentFromOptimizationRule(StringBuffer stringBuffer, OptimizationRule optimizationRule, List<OptimizationRule> children, List<OptimizationRuleField> list) {
        if (CollectionUtils.isNotEmpty(optimizationRule.getConditions())) {
            int conditionsSize = optimizationRule.getConditions().size();
            int count = 0;
            // 比较条件
            for (OptimizationRuleFieldCondition v : optimizationRule.getConditions()) {
                // 从资源类型优化规则字段中获取对应的字段信息
                List<OptimizationRuleField> optimizationRuleFieldList = list.stream().filter(field -> StringUtils.equalsIgnoreCase(field.getField(), v.getField())).toList();
                if (CollectionUtils.isNotEmpty(optimizationRuleFieldList)) {
                    stringBuffer.append(optimizationRuleFieldList.get(0).getLabel());
                    stringBuffer.append(OptimizationRuleFieldCompare.valueOf(v.getCompare()).getMessage());
                    stringBuffer.append(v.getValue());
                    String unit = optimizationRuleFieldList.get(0).getUnit();
                    if (Objects.nonNull(unit)) {
                        stringBuffer.append(unit);
                    }
                    count++;
                    if (count < conditionsSize) {
                        // 比较器
                        stringBuffer.append(optimizationRule.getConditionType().name());
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(children)) {
            // 比较器
            stringBuffer.append(optimizationRule.getConditionType().name());
            for (OptimizationRule child : children) {
                formatOptimizationContentFromOptimizationRule(stringBuffer, child, child.getChildren(), list);
            }
        }
    }

    /**
     * 得到优化策略列表
     *
     * @param resourceType 资源类型
     * @return {@link List }<{@link OptimizationStrategy }>
     */
    @Override
    public List<OptimizationStrategy> getOptimizationStrategyList(String resourceType) {
        MPJLambdaWrapper<OptimizationStrategy> wrapper = new MPJLambdaWrapper<>();
        // 不是管理员就获取当前用户有权限的组织或工作空间下的优化策略
        List<String> sourceId = permissionService.getSourceIds();
        wrapper.in(!CurrentUserUtils.isAdmin(), OptimizationStrategy::getAuthorizeId, sourceId);
        return optimizationStrategyMapper.selectList(wrapper);
    }

    /**
     * 通过策略ID得到一个优化策略
     *
     * @param optimizationStrategyId 优化策略id
     * @return {@link OptimizationStrategy }
     */
    @Override
    public OptimizationStrategyDTO getOneOptimizationStrategy(String optimizationStrategyId) {
        MPJLambdaWrapper<OptimizationStrategy> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(true, OptimizationStrategy::getId, optimizationStrategyId);
        // 不是管理员就获取当前用户有权限的组织或工作空间下的优化策略
        List<String> sourceId = permissionService.getSourceIds();
        wrapper.in(!CurrentUserUtils.isAdmin(), OptimizationStrategy::getAuthorizeId, sourceId);
        OptimizationStrategy optimizationStrategy = optimizationStrategyMapper.selectOne(wrapper);
        if (Objects.isNull(optimizationStrategy)) {
            throw new Fit2cloudException(ErrorCodeConstants.NOT_EXISTS_OPTIMIZE_SUGGEST_STRATEGY.getCode(), ErrorCodeConstants.NOT_EXISTS_OPTIMIZE_SUGGEST_STRATEGY.getMessage());
        }
        OptimizationStrategyDTO optimizationStrategyDTO = new OptimizationStrategyDTO();
        BeanUtils.copyProperties(optimizationStrategy, optimizationStrategyDTO);
        if (!optimizationStrategy.getOptimizationScope()) {
            MPJLambdaWrapper<OptimizationStrategyIgnoreResource> ignoreResourceMPJLambdaWrapper = new MPJLambdaWrapper<>();
            ignoreResourceMPJLambdaWrapper.eq(true, OptimizationStrategyIgnoreResource::getOptimizationStrategyId, optimizationStrategy.getId());
            List<OptimizationStrategyIgnoreResource> ignoreResourceList = optimizationStrategyIgnoreResourceMapper.selectList(ignoreResourceMPJLambdaWrapper);
            if (CollectionUtils.isNotEmpty(ignoreResourceList)) {
                optimizationStrategyDTO.setIgnoreResourceIdList(ignoreResourceList.stream().map(OptimizationStrategyIgnoreResource::getResourceId).toList());
            }
        }
        optimizationStrategyDTO.setOptimizationContent(getOptimizationContent(optimizationStrategyDTO));
        return optimizationStrategyDTO;
    }

    /**
     * 保存或更新策略
     * 如果策略的优化范围不是所有，那么设置优化策略的忽略资源
     *
     * @param strategy 策略
     */
    @Override
    public boolean saveOrUpdateStrategy(CreateOrUpdateOptimizationStrategyRequest strategy) {
        MPJLambdaWrapper<OptimizationStrategy> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(OptimizationStrategy::getId, strategy.getId());
        OptimizationStrategy optimizationStrategy = optimizationStrategyMapper.selectOne(wrapper);
        if (Objects.isNull(optimizationStrategy)) {
            optimizationStrategy = toOptimizationStrategyByCreateOrUpdateOptimizationStrategyRequest(strategy);
        } else {
            BeanUtils.copyProperties(strategy, optimizationStrategy);
            optimizationStrategy.setOptimizationRules(strategy.getOptimizationRules().get(0));
            optimizationStrategy.setUpdateUserId(CurrentUserUtils.getUser().getId());
            optimizationStrategy.setUpdateTime(LocalDateTime.now());
        }
        try {
            boolean result = saveOrUpdate(optimizationStrategy);
            if (result && !strategy.getOptimizationScope() && CollectionUtils.isNotEmpty(strategy.getIgnoreResourceIdList())) {
                return optimizationStrategyIgnoreResourceService.batchInsertIgnoreResourceByOptimizationStrategyId(optimizationStrategy.getId(), strategy.getIgnoreResourceIdList());
            }
            // 改为优化所有资源时，取消所有已忽略的资源
            if(result && strategy.getOptimizationScope()){
                optimizationStrategyIgnoreResourceService.deleteIgnoreResourceByOptimizationStrategyId(optimizationStrategy.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将请求创建或更新的优化策略，转成要持久化的优化策略对象
     * 如果不是管理员，那么授权ID从工作空间或者组织中选一个，如果工作空间为空那么获取组织的ID
     *
     * @param strategy 请求优化策略对象
     * @return {@link OptimizationStrategy }
     */
    private OptimizationStrategy toOptimizationStrategyByCreateOrUpdateOptimizationStrategyRequest(CreateOrUpdateOptimizationStrategyRequest strategy) {
        OptimizationStrategy optimizationStrategy = new OptimizationStrategy();
        BeanUtils.copyProperties(strategy, optimizationStrategy);
        optimizationStrategy.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        optimizationStrategy.setOptimizationRules(strategy.getOptimizationRules().get(0));
        optimizationStrategy.setCreateTime(LocalDateTime.now());
        optimizationStrategy.setUpdateTime(LocalDateTime.now());
        optimizationStrategy.setCreateUserId(CurrentUserUtils.getUser().getId());
        optimizationStrategy.setUpdateUserId(CurrentUserUtils.getUser().getId());
        optimizationStrategy.setStrategyType("MONITORING");
        optimizationStrategy.setEnabled(true);
        optimizationStrategy.setUpdateFlag(true);
        if (!CurrentUserUtils.isAdmin()) {
            String authorizeId = Objects.nonNull(CurrentUserUtils.getWorkspaceId()) ? CurrentUserUtils.getWorkspaceId() : CurrentUserUtils.getOrganizationId();
            optimizationStrategy.setAuthorizeId(authorizeId);
        }
        return optimizationStrategy;
    }

    /**
     * 删除一个优化策略
     * 先判断优化策略是否存在以及是否有权限
     *
     * @param optimizationStrategyId 优化策略id
     */
    @Override
    public boolean deleteOneOptimizationStrategy(String optimizationStrategyId) {
        // 构建查询参数
        MPJLambdaWrapper<OptimizationStrategy> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(true, OptimizationStrategy::getId, optimizationStrategyId);
        // 不是管理员就获取当前用户有权限的组织或工作空间下的优化策略
        List<String> sourceId = permissionService.getSourceIds();
        wrapper.in(!CurrentUserUtils.isAdmin(), OptimizationStrategy::getAuthorizeId, sourceId);
        OptimizationStrategy strategy = optimizationStrategyMapper.selectOne(wrapper);
        if (Objects.isNull(strategy)) {
            throw new RuntimeException("没有权限删除或者找不到ID为[" + optimizationStrategyId + "]的优化策略");
        }
        optimizationStrategyMapper.deleteById(optimizationStrategyId);
        return optimizationStrategyIgnoreResourceService.deleteIgnoreResourceByOptimizationStrategyId(optimizationStrategyId);
    }

    /**
     * 批量删除优化策略
     * 先判断优化策略是否存在以及是否有权限
     *
     * @param optimizationStrategyIdList 优化策略id列表
     */
    @Override
    public boolean batchDeleteOptimizationStrategy(List<String> optimizationStrategyIdList) {
        // 构建查询参数
        MPJLambdaWrapper<OptimizationStrategy> wrapper = new MPJLambdaWrapper<>();
        wrapper.in(true, OptimizationStrategy::getId, optimizationStrategyIdList);
        // 不是管理员就获取当前用户有权限的组织或工作空间下的优化策略
        List<String> sourceId = permissionService.getSourceIds();
        wrapper.in(!CurrentUserUtils.isAdmin(), OptimizationStrategy::getAuthorizeId, sourceId);
        List<OptimizationStrategy> strategyList = optimizationStrategyMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(strategyList)) {
            throw new RuntimeException("没有权限删除或者找不到ID为[" + Joiner.on(",").join(optimizationStrategyIdList) + "]的优化策略");
        }
        optimizationStrategyMapper.deleteBatchIds(strategyList.stream().map(OptimizationStrategy::getId).toList());
        return true;
    }

    /**
     * 获得资源类型列表
     *
     * @return {@link List }<{@link ResourceTypeDTO }>
     */
    @Override
    public List<ResourceTypeDTO> getResourceTypeList() {
        return Arrays.stream(ResourceTypeConstants.values()).map(v -> new ResourceTypeDTO(v.getLabel(), v.name(), toOptimizationRuleFieldResponse(v))).toList();
    }

    /**
     * 格式化资源类型返回对象
     *
     * @param resourceTypeConstants 资源类型常量
     * @return {@link List }<{@link OptimizationRuleFieldResponse }>
     */
    private List<OptimizationRuleFieldResponse> toOptimizationRuleFieldResponse(ResourceTypeConstants resourceTypeConstants) {
        return CommonUtil.exec(OptimizationRuleFieldProviderImpl.class, resourceTypeConstants.getOptimizationRuleFieldList()).stream().map(optimizationRuleField -> {
            OptimizationRuleFieldResponse optimizationRuleFieldResponse = new OptimizationRuleFieldResponse();
            BeanUtils.copyProperties(optimizationRuleField, optimizationRuleFieldResponse);
            optimizationRuleFieldResponse.setCompares(optimizationRuleField.getFieldType().getCompares().stream().map(c -> new DefaultKeyValue<>(c.getMessage(), c.name())).toList());
            return optimizationRuleFieldResponse;
        }).toList();

    }

    /**
     * 改变状态
     *
     * @param optimizationStrategyDTO 优化策略dto
     * @return boolean
     */
    @Override
    public boolean changeStatus(OptimizationStrategyDTO optimizationStrategyDTO) {
        // 构建查询参数
        MPJLambdaWrapper<OptimizationStrategy> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(true, OptimizationStrategy::getId, optimizationStrategyDTO.getId());
        // 不是管理员就获取当前用户有权限的组织或工作空间下的优化策略
        List<String> sourceId = permissionService.getSourceIds();
        wrapper.in(!CurrentUserUtils.isAdmin(), OptimizationStrategy::getAuthorizeId, sourceId);
        OptimizationStrategy strategy = optimizationStrategyMapper.selectOne(wrapper);
        if (Objects.isNull(strategy)) {
            throw new RuntimeException("没有权限改变状态或者找不到ID为[" + optimizationStrategyDTO.getId() + "]的优化策略");
        }
        strategy.setEnabled(optimizationStrategyDTO.getEnabled());
        optimizationStrategyMapper.update(strategy, wrapper);
        return true;
    }

}
