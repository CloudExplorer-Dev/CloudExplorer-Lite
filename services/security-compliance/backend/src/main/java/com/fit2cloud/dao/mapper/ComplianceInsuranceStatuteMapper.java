package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fit2cloud.dao.entity.ComplianceInsuranceStatute;
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
public interface ComplianceInsuranceStatuteMapper extends BaseMapper<ComplianceInsuranceStatute> {

    /**
     * 分页查询
     *
     * @param page    分页对象
     * @param wrapper 查询对象
     * @return 等保条例数据
     */
    IPage<ComplianceInsuranceStatute> page(IPage<ComplianceInsuranceStatute> page,
                                           @Param(Constants.WRAPPER) Wrapper<ComplianceInsuranceStatute> wrapper);

    /**
     * 查询等保条例数据
     *
     * @param wrapper 查询对象
     * @return 等保条例数据
     */
    List<ComplianceInsuranceStatute> list(@Param(Constants.WRAPPER) Wrapper<ComplianceInsuranceStatute> wrapper);
}
