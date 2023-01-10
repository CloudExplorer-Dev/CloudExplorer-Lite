package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.dto.AnalyticsDatastoreDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fit2cloud
 */
public interface AnalyticsDatastoreMapper extends BaseMapper<AnalyticsDatastoreDTO> {


    @Select("SELECT vm_cloud_datastore.* " +
            " ,cloud_account.name as account_name"+
            " ,( vm_cloud_datastore.capacity - vm_cloud_datastore.free_space ) AS allocated"+
            " ,TRUNCATE (( vm_cloud_datastore.capacity - vm_cloud_datastore.free_space )* 100 / vm_cloud_datastore.capacity,2 ) AS useRate"+
            " ,TRUNCATE(vm_cloud_datastore.free_space * 100 / vm_cloud_datastore.capacity,2) as free_rate"+
            " ,cloud_account.platform as platform"+
            " FROM vm_cloud_datastore" +
            " INNER JOIN cloud_account on vm_cloud_datastore.account_id=cloud_account.id"+
            " ${ew.customSqlSegment} ")
    IPage<AnalyticsDatastoreDTO> pageList(Page page, @Param("ew") Wrapper queryWrapper);




}
