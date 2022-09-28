package com.fit2cloud.common.scheduler.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fit2cloud.common.scheduler.entity.QuzrtzJobDetail;
import org.apache.ibatis.annotations.Mapper;
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
@Mapper
public interface QuzrtzMapper {
    /**
     * 获取当时任务数据
     *
     * @param wrapper 定时任务查询对象
     * @return 定时任务数据
     */
    List<QuzrtzJobDetail> list(@Param(Constants.WRAPPER) Wrapper<QuzrtzJobDetail> wrapper);

    boolean updateJob();
}
