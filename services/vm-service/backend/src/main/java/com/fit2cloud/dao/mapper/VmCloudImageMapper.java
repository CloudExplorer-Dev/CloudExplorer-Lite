package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.dto.VmCloudDiskDTO;
import com.fit2cloud.dto.VmCloudImageDTO;
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
public interface VmCloudImageMapper extends BaseMapper<VmCloudImageDTO> {


    /**
     * TODO 这个地方慢慢累加查询条件
     * @param page
     * @param queryWrapper
     * @return
     */
    @Select("SELECT vm_cloud_image.* " +
            " ,cloud_account.name as account_name"+
            " FROM vm_cloud_image" +
            " LEFT JOIN cloud_account on vm_cloud_image.account_id=cloud_account.id"+
            " ${ew.customSqlSegment} ")
    IPage<VmCloudImageDTO> pageList(Page page, @Param("ew") Wrapper queryWrapper);


}
