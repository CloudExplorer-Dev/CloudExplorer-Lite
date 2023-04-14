package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.dao.entity.Workspace;
import com.fit2cloud.dto.WorkspaceDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface WorkspaceMapper extends BaseMapper<Workspace> {
    @Select("SELECT " +
            "workspace.*, " +
            "( SELECT count( DISTINCT user_role.user_id ) FROM user_role WHERE user_role._source = workspace.id ) AS userCount, " +
            "organization.NAME AS organizationName  " +
            "FROM " +
            "workspace " +
            "LEFT JOIN organization ON organization.id = workspace.organization_id " +
            "${ew.customSqlSegment}")
    IPage<WorkspaceDTO> pageList(Page page, @Param("ew") Wrapper queryWrapper);

}
