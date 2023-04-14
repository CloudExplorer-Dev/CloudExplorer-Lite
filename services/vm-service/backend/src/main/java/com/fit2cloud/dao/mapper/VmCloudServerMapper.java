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
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<VmCloudServerDTO> pageVmCloudServer(Page page, @Param("ew") Wrapper queryWrapper);

    /**
     * 根据条件查询云主机列表
     *
     * @param queryWrapper
     * @return
     */
    List<VmCloudServerDTO> selectServerList(@Param("ew") Wrapper queryWrapper);

    /**
     * 根据ID查询云主机
     *
     * @param queryWrapper
     * @return
     */
    VmCloudServerDTO getById(@Param("ew") Wrapper queryWrapper);

    /**
     * 根据IDS查询云主机
     *
     * @param queryWrapper
     * @return
     */
    @Select("SELECT CASE " +
            "WHEN recycle_bin.STATUS = 'ToBeRecycled' and vm_cloud_server.instance_status !='Deleted' THEN" +
            "'ToBeRecycled' ELSE vm_cloud_server.instance_status " +
            "END AS instance_status," +
            "vm_cloud_server.*, " +
            "cloud_account.NAME AS account_name, " +
            "cloud_account.platform AS platform " +
            "FROM " +
            "vm_cloud_server " +
            "LEFT JOIN cloud_account ON vm_cloud_server.account_id = cloud_account.id LEFT JOIN recycle_bin ON vm_cloud_server.id = recycle_bin.resource_id " +
            "AND recycle_bin.resource_type = 'VM' " +
            "AND recycle_bin.STATUS = 'ToBeRecycled' " +
            " ${ew.customSqlSegment} ")
    List<VmCloudServerDTO> getByIds(@Param("ew") Wrapper queryWrapper);


}
