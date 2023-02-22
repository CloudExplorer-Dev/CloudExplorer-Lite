package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fit2cloud.controller.response.compliance_scan_result.ComplianceScanResultResponse;
import com.fit2cloud.dao.entity.ComplianceCount;
import com.fit2cloud.dao.entity.ComplianceGroup;
import com.fit2cloud.dao.entity.ComplianceScanResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface ComplianceScanResultMapper extends BaseMapper<ComplianceScanResult> {

    /**
     * 合规扫描列表
     *
     * @param wrapper 查询对象
     * @return 扫描结果列表
     */
    List<ComplianceScanResultResponse> list(@Param(Constants.WRAPPER) Wrapper<ComplianceScanResultResponse> wrapper, @Param("cloudAccountId") String cloudAccountId);

    /**
     * 分页查询
     *
     * @param page    分页对象
     * @param wrapper 查询条件
     * @return 扫描结果对象
     */
    IPage<ComplianceScanResultResponse> page(IPage<ComplianceScanResultResponse> page, @Param(Constants.WRAPPER) Wrapper<ComplianceScanResultResponse> wrapper, @Param("cloudAccountId") String cloudAccountId);

    ComplianceCount count(@Param(Constants.WRAPPER) Wrapper<ComplianceScanResult> wrapper);

    List<ComplianceGroup> group(@Param("groupType") String groupType, @Param(Constants.WRAPPER) Wrapper<ComplianceScanResult> wrapper);

    ComplianceCount ruleCount(@Param(Constants.WRAPPER) Wrapper<ComplianceScanResult> wrapper);
}
