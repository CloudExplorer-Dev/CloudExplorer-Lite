package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fit2cloud.common.page.PageImpl;
import com.fit2cloud.common.utils.ColumnNameUtil;
import com.fit2cloud.controller.request.compliance_insurance_statute.ComplianceInsuranceStatuteRequest;
import com.fit2cloud.controller.response.compliance_insurance_statute.ComplianceInsuranceStatuteResponse;
import com.fit2cloud.dao.entity.ComplianceInsuranceStatute;
import com.fit2cloud.dao.entity.ComplianceRule;
import com.fit2cloud.dao.mapper.ComplianceInsuranceStatuteMapper;
import com.fit2cloud.service.IComplianceInsuranceStatuteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class ComplianceInsuranceStatuteServiceImpl extends ServiceImpl<ComplianceInsuranceStatuteMapper, ComplianceInsuranceStatute> implements IComplianceInsuranceStatuteService {
    @Override
    public IPage<ComplianceInsuranceStatuteResponse> page(Integer currentPage, Integer limit, ComplianceInsuranceStatuteRequest request) {
        IPage<ComplianceInsuranceStatute> page = baseMapper.page(PageImpl.of(currentPage, limit, ComplianceInsuranceStatute.class, Objects.nonNull(request.getOrder()) ? request.getOrder() : new OrderItem("id", true)), getQueryWrapper(request));
        Page<ComplianceInsuranceStatuteResponse> res = Page.of(currentPage, limit);
        BeanUtils.copyProperties(page, res);
        res.setRecords(
                page.getRecords()
                        .stream()
                        .map(this::toComplianceStatuteResponse)
                        .toList()
        );
        return res;
    }

    @Override
    public List<ComplianceInsuranceStatuteResponse> list(ComplianceInsuranceStatuteRequest request) {
        return baseMapper.list(getQueryWrapper(request))
                .stream()
                .map(this::toComplianceStatuteResponse)
                .sorted(Comparator.comparing(ComplianceInsuranceStatuteResponse::getId))
                .toList();
    }


    /**
     * 根据请求对象获取查询Wrapper
     *
     * @param request 请求过滤对象
     * @return 查询Wrapper
     */

    private Wrapper<ComplianceInsuranceStatute> getQueryWrapper(ComplianceInsuranceStatuteRequest request) {
        return new QueryWrapper<ComplianceInsuranceStatute>()
                .like(StringUtils.isNotEmpty(request.getBaseClause()), ColumnNameUtil.getColumnName(ComplianceInsuranceStatute::getBaseClause, "cis"), request.getBaseClause())
                .like(StringUtils.isNotEmpty(request.getControlPoint()), ColumnNameUtil.getColumnName(ComplianceInsuranceStatute::getControlPoint, "cis"), request.getControlPoint())
                .like(StringUtils.isNotEmpty(request.getSecurityLevel()), ColumnNameUtil.getColumnName(ComplianceInsuranceStatute::getSecurityLevel, "cis"), request.getSecurityLevel())
                .like(StringUtils.isNotEmpty(request.getImprovementProposal()), ColumnNameUtil.getColumnName(ComplianceInsuranceStatute::getImprovementProposal, "cis"), request.getImprovementProposal())
                .eq(StringUtils.isNotEmpty(request.getComplianceRuleId()), ColumnNameUtil.getColumnName(ComplianceRule::getId, "cr"), request.getComplianceRuleId());
    }

    /**
     * 转换对象
     *
     * @param complianceInsuranceStatute 等保条例对象
     * @return 等保条例返回对象
     */
    private ComplianceInsuranceStatuteResponse toComplianceStatuteResponse(ComplianceInsuranceStatute complianceInsuranceStatute) {
        ComplianceInsuranceStatuteResponse complianceStatuteResponse = new ComplianceInsuranceStatuteResponse();
        BeanUtils.copyProperties(complianceInsuranceStatute, complianceStatuteResponse);
        return complianceStatuteResponse;
    }
}
