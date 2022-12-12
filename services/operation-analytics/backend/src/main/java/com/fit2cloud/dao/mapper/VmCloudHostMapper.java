package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.dto.VmCloudHostDTO;
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
public interface VmCloudHostMapper extends BaseMapper<VmCloudHostDTO> {


    /**
     * @param page
     * @param queryWrapper
     * @return
     */
    @Select("SELECT vm_cloud_host.* " +
            " ,cloud_account.name as account_name"+
            " ,cloud_account.platform as platform"+
            " FROM vm_cloud_host" +
            " INNER JOIN cloud_account on vm_cloud_host.account_id=cloud_account.id"+
            " ${ew.customSqlSegment} ")
    IPage<VmCloudHostDTO> pageList(Page page, @Param("ew") Wrapper queryWrapper);




}
