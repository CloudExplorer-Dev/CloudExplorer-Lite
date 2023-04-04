package com.fit2cloud.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fit2cloud.constants.AuthorizeTypeConstants;
import com.fit2cloud.controller.request.AuthorizeResourcesRequest;
import com.fit2cloud.controller.request.NotAuthorizeResourcesRequest;
import com.fit2cloud.controller.response.AuthorizeResourcesResponse;
import com.fit2cloud.dao.entity.BillDimensionSetting;
import com.fit2cloud.dao.jentity.BillAuthorizeRule;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author fit2cloud
 * @since
 */
public interface IBillDimensionSettingService extends IService<BillDimensionSetting> {

    /**
     * @param groupField 字段分组
     * @return 获取字段分组
     */
    List<DefaultKeyValue<String, String>> authorizeValues(String groupField);

    /**
     * 所有可以授权的字段
     *
     * @return 返回所有可以授权的字段
     */
    List<DefaultKeyValue<String, String>> authorizeKeys();

    /**
     * 获取账单授权规则
     *
     * @param authorizeId 授权对象id
     * @param type        授权对象类型
     * @return
     */
    BillDimensionSetting getBillDimensionSetting(String authorizeId, String type);


    /**
     * 插入或者修改授权对象
     *
     * @param authorizeId   授权对象id
     * @param type          授权对象类型
     * @param authorizeRule 授权规则
     * @return 插入授权对象
     */
    BillDimensionSetting saveOrUpdate(String authorizeId, String type, BillAuthorizeRule authorizeRule);

    /**
     * 根据授权规则授权数据
     */
    void authorize();

    /**
     * 授权 todo 当前为整体授权 后期需要优化
     *
     * @param billDimensionSetting 授权规则设置对象
     */
    void authorize(BillDimensionSetting billDimensionSetting);

    /**
     * 授权
     *
     * @param billDimensionSettingId 授权规则id
     * @param month                  月份 yyyy-MM
     * @param cloudAccountId         云账号id
     */
    void authorize(String billDimensionSettingId, String month, String cloudAccountId);

    /**
     * 清除授权
     *
     * @param authorizeId 授权id
     * @param type        授权对象类型
     */
    void clearAuthorize(String authorizeId, AuthorizeTypeConstants type);


    /**
     * 获取授权的资源
     *
     * @param page    当前页
     * @param limit   每页显示多少条
     * @param request 请求对象
     * @return 授权资源分页对象
     */
    Page<AuthorizeResourcesResponse> getAuthorizeResources(Integer page, Integer limit, AuthorizeResourcesRequest request);

    /**
     * 获取未授权的资源
     *
     * @param page    当前页
     * @param limit   每页显示多少条
     * @param request 请求对象
     * @return 资源分页对象
     */
    Page<AuthorizeResourcesResponse> getNotAuthorizeResources(Integer page, Integer limit, NotAuthorizeResourcesRequest request);
}
