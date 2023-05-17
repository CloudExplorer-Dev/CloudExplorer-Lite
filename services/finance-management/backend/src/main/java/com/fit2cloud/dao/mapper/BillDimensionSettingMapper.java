package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fit2cloud.dao.entity.BillDimensionSetting;
import com.fit2cloud.db_convert.entity.OldBillDimensionSetting;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface BillDimensionSettingMapper extends BaseMapper<BillDimensionSetting> {

    List<OldBillDimensionSetting> listOldData();
}
