package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.controller.request.compliance_insurance_statute.ComplianceInsuranceStatuteRequest;
import com.fit2cloud.controller.response.compliance_insurance_statute.ComplianceInsuranceStatuteResponse;
import com.fit2cloud.dao.entity.ComplianceInsuranceStatute;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IComplianceInsuranceStatuteService extends IService<ComplianceInsuranceStatute> {
    /**
     * 分页查询等保条例
     *
     * @param currentPage 当前页
     * @param limit       每页显示多少条
     * @param request     请求过滤参数
     * @return 等保条例
     */
    IPage<ComplianceInsuranceStatuteResponse> page(Integer currentPage, Integer limit, ComplianceInsuranceStatuteRequest request);

    /**
     * 列举所有的等保条例
     *
     * @param request 过滤条件
     * @return 等保条例数据
     */
    List<ComplianceInsuranceStatuteResponse> list(ComplianceInsuranceStatuteRequest request);


}
