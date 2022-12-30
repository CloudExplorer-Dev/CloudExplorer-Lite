package com.fit2cloud.service;

import com.fit2cloud.dao.entity.ComplianceRuleInsuranceStatuteMapping;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IComplianceRuleInsuranceStatuteMappingService extends IService<ComplianceRuleInsuranceStatuteMapping> {

    /**
     * 插入规则于等保条例关联表
     *
     * @param complianceRuleId    合规规则id
     * @param insuranceStatuteIds 等保条例id
     */
    void save(String complianceRuleId, List<Integer> insuranceStatuteIds);

    void update(String complianceRuleId, List<Integer> insuranceStatuteIds);
}
