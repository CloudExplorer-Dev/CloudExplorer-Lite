package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.User;
import com.fit2cloud.base.mapper.BaseUserMapper;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.user.*;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.dto.UserNotifySettingDTO;
import com.fit2cloud.dto.UserOperateDto;
import com.fit2cloud.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Author: LiuDi
 * Date: 2022/8/26 2:32 PM
 */
@RestController
@RequestMapping("/api/user")
@Api("用户相关接口")
@Validated
public class UserController {
    @Resource
    IUserService userService;

    @ApiOperation(value = "分页查询用户")
    @PreAuthorize("hasAnyCePermission('USER:READ')")
    @GetMapping("/page")
    public ResultHolder<IPage<UserDto>> listUser(PageUserRequest pageUserRequest) {
        return ResultHolder.success(userService.pageUser(pageUserRequest));
    }

    @ApiOperation(value = "管理员/组织管理员获取可管理的用户列表")
    @PreAuthorize("hasAnyCePermission('USER:READ')")
    @GetMapping("/manage/list")
    public ResultHolder<List<User>> listUser() {
        return ResultHolder.success(userService.getManageUserSimpleList(null));
    }

    @ApiOperation(value = "查询用户总数")
    @PreAuthorize("hasAnyCePermission('USER:READ')")
    @GetMapping("/count")
    public ResultHolder<Long> count() {
        return ResultHolder.success(userService.countUser());
    }

    @ApiOperation(value = "添加用户")
    @PreAuthorize("hasAnyCePermission('USER:CREATE')")
    @PostMapping("/add")
    @OperatedLog(resourceType = ResourceTypeEnum.USER, operated = OperatedTypeEnum.ADD,
            content = "'添加了用户['+#request.name+']'",
            param = "#request")
    public ResultHolder<Boolean> createUser(@RequestBody @Validated(ValidationGroup.SAVE.class) CreateUserRequest request) {
        return ResultHolder.success(userService.createUser(request));
    }

    @ApiOperation(value = "更新用户")
    @PreAuthorize("hasAnyCePermission('USER:EDIT')")
    @PostMapping("/update")
    @OperatedLog(resourceType = ResourceTypeEnum.USER, operated = OperatedTypeEnum.MODIFY,
            resourceId = "#request.id",
            content = "'更新了用户['+#request.name+']'",
            param = "#request")
    public ResultHolder<Boolean> updateUser(@RequestBody @Validated(ValidationGroup.UPDATE.class) UpdateUserRequest request) {
        return ResultHolder.success(userService.updateUser(request));
    }

    @ApiOperation(value = "更新密码")
    @PreAuthorize("hasAnyCePermission('USER:EDIT_PASSWORD')")
    @PostMapping("/updatePwd")
    @OperatedLog(resourceType = ResourceTypeEnum.USER, operated = OperatedTypeEnum.MODIFY,
            resourceId = "#user.id",
            content = "'更新了密码'")
    public ResultHolder<Boolean> updatePwd(@RequestBody User user) {
        return ResultHolder.success(userService.updatePwd(user));
    }

    @ApiOperation(value = "查询用户")
    @PreAuthorize("hasAnyCePermission('USER:READ')")
    @GetMapping("/{id}")
    public ResultHolder<UserDto> getUser(@ApiParam("主键 ID")
                                         @NotNull(message = "{i18n.user.id.cannot.be.null}")
                                         @CustomValidated(mapper = BaseUserMapper.class, handler = ExistHandler.class, message = "{i18n.primary.key.not.exist}", exist = false)
                                         @PathVariable("id") String id) {
        return ResultHolder.success(userService.getUser(id));
    }

    @ApiOperation(value = "删除用户")
    @PreAuthorize("hasAnyCePermission('USER:DELETE')")
    @DeleteMapping("/{id}")
    @OperatedLog(resourceType = ResourceTypeEnum.USER, operated = OperatedTypeEnum.DELETE,
            resourceId = "#id",
            content = "'删除用户'",
            param = "#id")
    public ResultHolder<Boolean> delete(@ApiParam("主键 ID")
                                        @NotNull(message = "{i18n.user.id.cannot.be.null}")
                                        @CustomValidated(mapper = BaseUserMapper.class, handler = ExistHandler.class, message = "{i18n.primary.key.not.exist}", exist = false)
                                        @PathVariable("id") String id) {
        return ResultHolder.success(userService.deleteUser(id));
    }

    @ApiOperation(value = "根据用户ID查询用户角色信息")
    @PreAuthorize("hasAnyCePermission('USER:READ')")
    @GetMapping(value = "/role/info/{id}")
    public ResultHolder<UserOperateDto> roleInfo(@ApiParam("主键 ID") @NotNull(message = "{i18n.user.id.cannot.be.null}")
                                                 @NotNull(message = "{i18n.user.id.cannot.be.null}")
                                                 @CustomValidated(mapper = BaseUserMapper.class, handler = ExistHandler.class, message = "{i18n.primary.key.not.exist}", exist = false)
                                                 @PathVariable("id") String id) {
        return ResultHolder.success(userService.userRoleInfo(id));
    }

    @ApiOperation(value = "启停用户")
    @PreAuthorize("hasAnyCePermission('USER:EDIT')")
    @PostMapping(value = "/changeStatus")
    @OperatedLog(resourceType = ResourceTypeEnum.USER, operated = OperatedTypeEnum.MODIFY,
            resourceId = "#user.id",
            content = "#user.enabled?'启用['+#user.name+']':'停用['+#user.name+']'",
            param = "#user")
    public ResultHolder<Boolean> changeStatus(@RequestBody UserDto user) {
        return ResultHolder.success(userService.changeUserStatus(user));
    }

    @PreAuthorize("hasAnyCePermission('USER:NOTIFICATION_SETTING')")
    @PostMapping(value = "/notificationSetting")
    @OperatedLog(resourceType = ResourceTypeEnum.USER, operated = OperatedTypeEnum.MODIFY,
            resourceId = "#userNotificationSetting.id",
            content = "'修改用户通知设置'",
            param = "#userNotificationSetting")
    public ResultHolder<Boolean> userNotificationSetting(@RequestBody UserNotifySettingDTO userNotificationSetting) {
        return ResultHolder.success(userService.updateUserNotification(userNotificationSetting));
    }

    @PreAuthorize("hasAnyCePermission('USER:NOTIFICATION_SETTING')")
    @GetMapping(value = "/findUserNotification/{userId}")
    public ResultHolder<UserNotifySettingDTO> findUserNotification(@PathVariable String userId) {
        return ResultHolder.success(userService.findUserNotification(userId));
    }

    @ApiOperation(value = "批量添加用户角色")
    @PreAuthorize("hasAnyCePermission('USER:EDIT')")
    @PostMapping(value = "/addRole")
    @OperatedLog(resourceType = ResourceTypeEnum.USER_ROLE, operated = OperatedTypeEnum.BATCH_ADD,
            resourceId = "#userBatchAddRoleRequest.userIdList",
            content = "'批量添加用户角色['+#userBatchAddRoleRequest.roleInfoList.![roleId]+']'",
            param = "#userBatchAddRoleRequest")
    public ResultHolder<Integer> addUserRole(@Validated @RequestBody UserBatchAddRoleRequest userBatchAddRoleRequest) {
        return ResultHolder.success(userService.addUserRole(userBatchAddRoleRequest));
    }

    @ApiOperation(value = "批量添加用户角色V2")
    @PreAuthorize("hasAnyCePermission('USER:EDIT')")
    @PostMapping(value = "/addRole/v2")
    @OperatedLog(resourceType = ResourceTypeEnum.USER_ROLE, operated = OperatedTypeEnum.BATCH_ADD,
            resourceId = "#userBatchAddRoleRequest.userIdList.userIds",
            content = "'批量添加用户角色['+#userBatchAddRoleRequest.roleId+']'",
            param = "#userBatchAddRoleRequest")
    public ResultHolder<Integer> addUserRoleV2(@Validated @RequestBody UserBatchAddRoleRequestV2 userBatchAddRoleRequest) {
        return ResultHolder.success(userService.addUserRoleV2(userBatchAddRoleRequest));
    }

    @ApiOperation(value = "批量添加用户角色V3")
    @PreAuthorize("hasAnyCePermission('USER:EDIT')")
    @PostMapping(value = "/addRole/v3")
    @OperatedLog(resourceType = ResourceTypeEnum.USER_ROLE, operated = OperatedTypeEnum.BATCH_ADD,
            resourceId = "#userBatchAddRoleRequest.userIdList.userIds",
            content = "'批量添加用户角色'",
            param = "#userBatchAddRoleRequest")
    public ResultHolder<Integer> addUserRoleV3(@Validated @RequestBody UserBatchAddRoleRequestV3 userBatchAddRoleRequest) {
        return ResultHolder.success(userService.addUserRoleV3(userBatchAddRoleRequest));
    }

    @ApiOperation(value = "移除用户角色")
    @PreAuthorize("hasAnyCePermission('USER:EDIT')")
    @DeleteMapping(value = "/removeRole")
    @OperatedLog(resourceType = ResourceTypeEnum.USER_ROLE, operated = OperatedTypeEnum.DELETE,
            resourceId = "#request.userId",
            content = "'移除用户角色'",
            param = "#request")
    public ResultHolder<Boolean> removeUserRole(@Validated @RequestBody RemoveUserRoleRequest request) {
        return ResultHolder.success(userService.removeUserRole(request.getUserId(), request.getRoleId(), request.getSourceId()));
    }


}
