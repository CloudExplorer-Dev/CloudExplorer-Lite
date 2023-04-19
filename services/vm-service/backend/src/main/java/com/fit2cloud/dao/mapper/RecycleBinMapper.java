package com.fit2cloud.dao.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.dto.RecycleBinDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface RecycleBinMapper extends BaseMapper<RecycleBinDTO> {

    /**
     * @param page
     * @return
     */
    IPage<RecycleBinDTO> pageRecycleBin(Page page, @Param("ew") Wrapper queryWrapper);

}
