package com.fit2cloud.service;

import com.fit2cloud.constants.ResourceTypeConstants;
import com.fit2cloud.dao.entity.ComplianceScanResourceResult;
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
public interface IComplianceScanResourceResultService extends IService<ComplianceScanResourceResult> {
    /**
     * 插入并且更新数据
     *
     * @param complianceScanResourceResults 合规资源扫描数据
     */
    void saveOrUpdate(List<ComplianceScanResourceResult> complianceScanResourceResults);
}
