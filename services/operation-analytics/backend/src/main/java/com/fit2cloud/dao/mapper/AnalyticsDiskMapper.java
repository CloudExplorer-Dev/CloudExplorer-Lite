package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.controller.response.BarTreeChartData;
import com.fit2cloud.dto.AnalyticsDiskDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fit2cloud
 */
public interface AnalyticsDiskMapper extends BaseMapper<AnalyticsDiskDTO> {


    /**
     * TODO 这个地方慢慢累加查询条件
     */
    @Select("SELECT vm_cloud_disk.* " +
            " ,cloud_account.name as account_name"+
            " ,cloud_account.platform as platform"+
            " ,vm_cloud_server.instance_name as vm_instance_name"+
            " FROM vm_cloud_disk" +
            " LEFT JOIN cloud_account on vm_cloud_disk.account_id=cloud_account.id"+
            " LEFT JOIN vm_cloud_server on vm_cloud_disk.instance_uuid=vm_cloud_server.instance_uuid" +
            " and vm_cloud_disk.account_id=vm_cloud_server.account_id"+
            " ${ew.customSqlSegment} ")
    IPage<AnalyticsDiskDTO> pageList(Page page, @Param("ew") Wrapper queryWrapper);

    @Select("SELECT " +
            "vm_cloud_disk.*, " +
            "cloud_account.NAME AS account_name, " +
            "cloud_account.platform AS platform, " +
            "DATE_FORMAT(vm_cloud_disk.create_time,'%Y-%m-%d') as create_month  " +
            "FROM " +
            "vm_cloud_disk " +
            "LEFT JOIN cloud_account ON vm_cloud_disk.account_id = cloud_account.id " +
            " ${ew.customSqlSegment} ")
    List<AnalyticsDiskDTO> list(@Param("ew") Wrapper queryWrapper);

    List<BarTreeChartData> analyticsVmCloudDiskByOrgWorkspace(@Param("ew") Wrapper queryWrapper);
    List<BarTreeChartData> analyticsVmCloudDiskByOrgWorkspaceFromSize(@Param("ew") Wrapper queryWrapper);


}
