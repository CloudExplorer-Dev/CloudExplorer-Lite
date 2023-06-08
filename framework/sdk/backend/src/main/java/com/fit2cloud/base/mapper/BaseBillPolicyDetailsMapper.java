package com.fit2cloud.base.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fit2cloud.base.entity.BillPolicyDetails;
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
public interface BaseBillPolicyDetailsMapper extends BaseMapper<BillPolicyDetails> {
    List<BillPolicyDetails> listLast(@Param(Constants.WRAPPER) Wrapper<BillPolicyDetails> wrapper);

}
