package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.dto.VmCloudServerDTO;
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
public interface VmCloudServerMapper extends BaseMapper<VmCloudServerDTO> {


    /**
     * TODO 这个地方慢慢累加查询条件
     * @param page
     * @param queryWrapper
     * @return
     */
    @Select("SELECT vm_cloud_server.* " +
            " ,cloud_account.name as account_name"+
            " ,cloud_account.platform as platform"+
            " FROM vm_cloud_server" +
            " LEFT JOIN cloud_account on vm_cloud_server.account_id=cloud_account.id"+
            " ${ew.customSqlSegment} ")
    IPage<VmCloudServerDTO> pageList(Page page, @Param("ew") Wrapper queryWrapper);

    /**
     * 根据云账号ID查询云主机列表
     * @param accountId
     * @return
     */
    List<VmCloudServerDTO> selectListByAccountId(@Param("accountId") String accountId);

    /**
     * 根据ID查询云主机
     * @param queryWrapper
     * @return
     */
    @Select("SELECT " +
            "vm_cloud_server.*, " +
            "cloud_account.NAME AS account_name, " +
            "cloud_account.platform AS platform " +
            "FROM " +
            "vm_cloud_server " +
            "LEFT JOIN cloud_account ON vm_cloud_server.account_id = cloud_account.id " +
            " ${ew.customSqlSegment} ")
    VmCloudServerDTO getById(@Param("ew") Wrapper queryWrapper);


}
