package com.fit2cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fit2cloud.controller.request.BillRuleRequest;
import com.fit2cloud.dao.entity.BillRule;
import com.fit2cloud.dao.mapper.BillRuleMapper;
import com.fit2cloud.service.IBillRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jodd.util.StringUtil;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
@Service
public class BillRuleServiceImpl extends ServiceImpl<BillRuleMapper, BillRule> implements IBillRuleService {

    @Override
    public Page<BillRule> page(Integer currentPage, Integer pageSize, BillRuleRequest request) {
        LambdaQueryWrapper<BillRule> wrapper = new LambdaQueryWrapper<BillRule>().like(StringUtil.isNotEmpty(request.getName()),     BillRule::getName, request.getName());
        Page<BillRule> page = Page.of(currentPage, pageSize);
        return page(page, wrapper);
    }
}
