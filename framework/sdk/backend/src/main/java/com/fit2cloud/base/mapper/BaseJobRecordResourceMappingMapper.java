package com.fit2cloud.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fit2cloud.base.entity.JobRecordResourceMapping;
import com.fit2cloud.response.JobRecordResourceResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since 
 */
public interface BaseJobRecordResourceMappingMapper extends BaseMapper<JobRecordResourceMapping> {

    List<JobRecordResourceResponse> findLastResourceJobRecord(@Param("resourceIds") List<String> resourceIds, String resourceType);

}
