package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.Organization;
import com.fit2cloud.base.mapper.BaseOrganizationMapper;
import com.fit2cloud.common.event.annotaion.Emit;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.OrganizationBatchRequest;
import com.fit2cloud.controller.request.OrganizationRequest;
import com.fit2cloud.controller.request.PageOrganizationRequest;
import com.fit2cloud.dto.OrganizationDTO;
import com.fit2cloud.service.IOrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:张少虎
 * @Date: 2022/8/30  11:06 AM
 * @Version 1.0
 * @注释:
 */
@RestController
@RequestMapping("/api/organization")
@Tag(name = "组织相关接口")
@Validated
public class OrganizationController {
    @Resource
    private IOrganizationService organizationService;

    @Operation(summary = "分页查询组织", description = "分页查询组织")
    @GetMapping("/page")
    @PreAuthorize("@cepc.hasAnyCePermission('ORGANIZATION:READ')")
    public ResultHolder<IPage<OrganizationDTO>> page(@Validated PageOrganizationRequest pageOrganizationRequest) {
        return ResultHolder.success(organizationService.pageOrganization(pageOrganizationRequest));
    }

    @Operation(summary = "查询组织数量", description = "查询组织数量")
    @GetMapping("/count")
    @PreAuthorize("@cepc.hasAnyCePermission('ORGANIZATION:READ')")
    public ResultHolder<Long> count() {
        return ResultHolder.success(organizationService.countOrganization());
    }

    @GetMapping("/{organizationId}")
    @Operation(summary = "根据组织id查询组织", description = "根据组织id查询组织")
    public ResultHolder<Organization> getOrganization(@Parameter(name = "组织id")
                                                      @NotNull(message = "{i18n.organization.name.is.not.empty}")
                                                      @CustomValidated(mapper = BaseOrganizationMapper.class, handler = ExistHandler.class, message = "{i18n.organization.id.is.not.existent}", exist = false)
                                                      @PathVariable("organizationId") String id) {
        return ResultHolder.success(organizationService.getOne(id, null));
    }

    @Operation(summary = "添加组织", description = "添加组织")
    @PostMapping
    @PreAuthorize("@cepc.hasAnyCePermission('ORGANIZATION:CREATE')")
    @OperatedLog(resourceType = ResourceTypeEnum.ORGANIZATION, operated = OperatedTypeEnum.ADD,
            content = "'添加组织['+#request.name+']'",
            param = "#request")
    @Emit(value = "CREATE::ORGANIZATION", el = "#request.id")
    public ResultHolder<Organization> save(@RequestBody
                                           @Validated(ValidationGroup.SAVE.class) OrganizationRequest request) {
        Organization organization = new Organization();
        BeanUtils.copyProperties(request, organization);
        return ResultHolder.success(organizationService.create(organization));
    }

    @Operation(summary = "批量添加组织", description = "批量添加组织")
    @PostMapping("/batch")
    @PreAuthorize("@cepc.hasAnyCePermission('ORGANIZATION:CREATE')")
    @OperatedLog(resourceType = ResourceTypeEnum.ORGANIZATION, operated = OperatedTypeEnum.BATCH_ADD,
            content = "'批量添加组织['+#request.orgDetails.![name]+']'",
            param = "#request")
    @Emit(value = "CREATE_BATCH::ORGANIZATION", el = "#arrayOf(#result.data).map(\"#root.id\")")
    public ResultHolder<List<Organization>> batch(@RequestBody
                                                  @Validated(ValidationGroup.SAVE.class) OrganizationBatchRequest request) {
        List<Organization> organizations = organizationService.batch(request);
        return ResultHolder.success(organizations);
    }

    @Operation(summary = "编辑组织", description = "编辑组织")
    @PutMapping
    @PreAuthorize("@cepc.hasAnyCePermission('ORGANIZATION:EDIT')")
    @OperatedLog(resourceType = ResourceTypeEnum.ORGANIZATION, operated = OperatedTypeEnum.MODIFY,
            resourceId = "#request.id",
            content = "'编辑组织['+#request.name+']'",
            param = "#request")
    @Emit(value = "UPDATE::ORGANIZATION", el = "#request.id")
    public ResultHolder<Organization> update(@RequestBody
                                             @Validated(ValidationGroup.UPDATE.class) OrganizationRequest request) {
        organizationService.update(request);
        return ResultHolder.success(organizationService.getById(request.getId()));
    }

    @Operation(summary = "删除组织", description = "删除组织")
    @DeleteMapping("/{organizationId}")
    @PreAuthorize("@cepc.hasAnyCePermission('ORGANIZATION:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.ORGANIZATION, operated = OperatedTypeEnum.DELETE,
            resourceId = "#id",
            content = "'删除组织'",
            param = "#id")
    @Emit("DELETE::ORGANIZATION")
    public ResultHolder<Boolean> delete(@Parameter(name = "组织id")
                                        @NotNull(message = "{i18n.organization.name.is.not.empty}")
                                        @CustomValidated(mapper = BaseOrganizationMapper.class, handler = ExistHandler.class, message = "{i18n.organization.id.is.not.existent}", exist = false)
                                        @PathVariable("organizationId") String id) {
        return ResultHolder.success(organizationService.removeTreeById(id));
    }

    @Operation(summary = "批量删除组织", description = "批量删除组织")
    @DeleteMapping
    @PreAuthorize("@cepc.hasAnyCePermission('ORGANIZATION:DELETE')")
    @OperatedLog(resourceType = ResourceTypeEnum.ORGANIZATION, operated = OperatedTypeEnum.BATCH_DELETE,
            resourceId = "#organizationIds.![id]",
            content = "'批量删除了['+#organizationIds.size+']个组织'",
            param = "#organizationIds")
    @Emit(value = "DELETE_BATCH::ORGANIZATION", el = "#arrayOf(#organizationIds).map(\"#root.id\")")
    public ResultHolder<Boolean> deleteBatch(@Parameter(name = "批量删除组织")
                                             @Size(min = 1, message = "{i18n.organization.id.size.gt.one}")
                                             @NotNull(message = "{i18n.organization.id.is.not.empty}")
                                             @RequestBody ArrayList<Organization> organizationIds) {
        return ResultHolder.success(organizationService.removeBatchTreeByIds(organizationIds));
    }
}
