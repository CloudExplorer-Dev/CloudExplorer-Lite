package com.fit2cloud.base.mapper;

import com.fit2cloud.base.entity.AccountJob;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fit2cloud.response.cloud_account.AccountJobRecordResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface BaseAccountJobMapper extends BaseMapper<AccountJob> {
    List<AccountJobRecordResponse> findLastAccountJobRecord(@Param("cloudAccountIds") List<String> cloudAccountIds);
}
