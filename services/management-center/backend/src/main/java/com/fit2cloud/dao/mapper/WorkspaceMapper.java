package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.dao.entity.Workspace;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fit2cloud.dto.WorkspaceDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
public interface WorkspaceMapper extends BaseMapper<Workspace> {

    String querySql = "SELECT " +
            "a.*, " +
            "( SELECT count( DISTINCT user_role.user_id ) FROM user_role WHERE user_role._source = a.id ) AS userCount, " +
            "b.NAME AS organizationName  " +
            "FROM " +
            "workspace AS a " +
            "LEFT JOIN organization AS b ON b.id = a.organization_id ";
    String wrapperSql = "SELECT * from ( " + querySql + " ) AS q ${ew.customSqlSegment}";

    @Select(wrapperSql)
    IPage<WorkspaceDTO> pageList(Page page, @Param("ew") Wrapper queryWrapper);

}
