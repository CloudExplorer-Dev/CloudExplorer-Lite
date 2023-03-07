package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fit2cloud.base.entity.Organization;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface OrganizationMapper extends BaseMapper<Organization> {
    /**
     * 分页查询
     *
     * @param wrapper
     * @return
     */
    List<Organization> pageOrganization(@Param(Constants.WRAPPER) Wrapper<Organization> wrapper);

    @Select("SELECT DISTINCT GET_ROOT_ORG_ID (id) AS id FROM organization ${ew.customSqlSegment}")
    List<String> listRootOrganizationIds(@Param(Constants.WRAPPER) Wrapper<Organization> wrapper);
}
