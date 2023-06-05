package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fit2cloud.base.entity.JobRecord;
import com.fit2cloud.controller.response.cloud_account.CloudAccountResponse;
import com.fit2cloud.dao.entity.CloudAccount;
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
public interface CloudAccountMapper extends BaseMapper<CloudAccount> {

    IPage<CloudAccountResponse> pageCloudAccount(IPage<CloudAccountResponse> cloudAccountPage, @Param(Constants.WRAPPER) Wrapper<CloudAccountResponse> wrapper);

    IPage<JobRecordResourceResponse> pageSyncRecord(IPage<JobRecordResourceResponse> syncRecordPage, @Param(Constants.WRAPPER) Wrapper wrapper);

    List<String> listTypes(@Param(Constants.WRAPPER) Wrapper<JobRecord> wrapper);

}
