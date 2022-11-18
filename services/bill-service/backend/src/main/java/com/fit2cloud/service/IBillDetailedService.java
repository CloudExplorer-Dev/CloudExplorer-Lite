package com.fit2cloud.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.controller.request.PageBillDetailedRequest;
import com.fit2cloud.controller.response.BillDetailResponse;

public interface IBillDetailedService {
    /**
     * 分页查询账单详情
     *
     * @param currentPage 当前页
     * @param pageSize    每页显示多少条
     * @param request     查询参数
     * @return 分页对象
     */
    IPage<BillDetailResponse> page(Integer currentPage, Integer pageSize, PageBillDetailedRequest request);
}
