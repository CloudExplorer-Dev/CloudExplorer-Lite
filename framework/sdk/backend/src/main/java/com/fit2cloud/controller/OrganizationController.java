package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.base.mapper.OrganizationMapper;
import com.fit2cloud.base.service.IOrganizationService;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.editor.OrderEditor;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.request.OrganizationBatchRequest;
import com.fit2cloud.request.OrganizationRequest;
import com.fit2cloud.request.PageOrganizationRequest;
import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.response.OrganizationTree;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/8/24  1:39 PM
 * @Version 1.0
 * @注释: 组织对应接口
 */
@RestController
@RequestMapping("/organization")
@Api("组织相关接口")
@Validated
public class OrganizationController {
    @Resource
    private IOrganizationService organizationService;

    @GetMapping("/tree")
    @ApiOperation(value="获取组织树",notes = "获取组织树")
    public ResultHolder<List<OrganizationTree>> tree(){
         return ResultHolder.success(organizationService.tree()) ;
    }

    @ApiOperation(value = "分页查询组织", notes = "分页查询组织")
    @GetMapping("/page")
    public ResultHolder<IPage<Organization>> list(@Validated PageOrganizationRequest pageOrganizationRequest) {
        return ResultHolder.success(organizationService.pageOrganization(pageOrganizationRequest));
    }
    @GetMapping
    @ApiOperation(value = "查询到一个组织",notes = "查询到一个组织")
    public ResultHolder<Organization> getOrganization(@ApiParam("组织主键id") @RequestParam("id") String id,@ApiParam("组织名称") @RequestParam("name") String name){
      return ResultHolder.success(organizationService.getOne(id,name));
    }

    @ApiOperation(value = "添加组织", notes = "添加组织")
    @PostMapping
    public ResultHolder<Boolean> save(@RequestBody @Validated(ValidationGroup.SAVE.class) OrganizationRequest request) {
        Organization organization = new Organization();
        BeanUtils.copyProperties(request, organization);
        boolean save = organizationService.save(organization);
        return ResultHolder.success(save);
    }

    @ApiOperation(value = "批量添加组织",notes = "批量添加组织")
    @PostMapping("/batch")
    public ResultHolder<Boolean> batch(@RequestBody @Validated(ValidationGroup.SAVE.class) OrganizationBatchRequest request){
        Boolean batch = organizationService.batch(request);
        return  ResultHolder.success(batch);
    }

    @ApiOperation(value = "修改组织", notes = "修改组织")
    @PutMapping
    public ResultHolder<Boolean> update(@RequestBody @Validated(ValidationGroup.UPDATE.class) OrganizationRequest request) {
        Organization organization = new Organization();
        BeanUtils.copyProperties(request, organization);
        boolean b = organizationService.updateById(organization);
        return ResultHolder.success(b);
    }

    @ApiOperation(value = "删除组织", notes = "删除组织")
    @DeleteMapping("/{organizationId}")
    public ResultHolder<Boolean> delete( @ApiParam("组织id") @NotNull(message = "组织id不能为null") @CustomValidated(mapper = OrganizationMapper.class,handler = ExistHandler.class,message = "组织id不存在",exist = false)  @PathVariable("organizationId")   String id) {
        boolean b = organizationService.removeTreeById(id);
        return ResultHolder.success(b);
    }
    @ApiOperation(value = "批量删除组织",notes = "批量删除组织")
    @DeleteMapping
    public ResultHolder<Boolean> deleteBatch(@ApiParam("批量删除组织") @Size(min = 1,message = "必须传入一个组织id") @RequestBody ArrayList<Organization> organizationIds){
      return  ResultHolder.success(organizationService.removeBatchTreeByIds(organizationIds));
    }

    /**
     * 控制器初始化时调用
     * SpringMVC 使用WebDataBinder处理<请求消息,方法入参>的绑定工作
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(OrderRequest.class, new OrderEditor());
    }

}
