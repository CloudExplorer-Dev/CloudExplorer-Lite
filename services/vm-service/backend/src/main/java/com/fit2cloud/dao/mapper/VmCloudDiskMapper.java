package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.dto.VmCloudDiskDTO;
import com.fit2cloud.dto.VmCloudServerDTO;
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
public interface VmCloudDiskMapper extends BaseMapper<VmCloudDiskDTO> {


    /**
     * TODO 这个地方慢慢累加查询条件
     * @param page
     * @param queryWrapper
     * @return
     */
    @Select("SELECT vm_cloud_disk.* " +
            " ,cloud_account.name as account_name"+
            " ,vm_cloud_server.instance_name as vm_instance_name"+
            " FROM vm_cloud_disk" +
            " LEFT JOIN cloud_account on vm_cloud_disk.account_id=cloud_account.id"+
            " LEFT JOIN vm_cloud_server on vm_cloud_disk.instance_uuid=vm_cloud_server.instance_uuid"+
            " ${ew.customSqlSegment} ")
    IPage<VmCloudDiskDTO> pageList(Page page, @Param("ew") Wrapper queryWrapper);


}
