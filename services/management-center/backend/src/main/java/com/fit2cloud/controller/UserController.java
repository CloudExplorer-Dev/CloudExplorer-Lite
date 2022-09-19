package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.User;
import com.fit2cloud.base.mapper.BaseUserMapper;
import com.fit2cloud.common.validator.annnotaion.CustomValidated;
import com.fit2cloud.common.validator.group.ValidationGroup;
import com.fit2cloud.common.validator.handler.ExistHandler;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.controller.request.user.CreateUserRequest;
import com.fit2cloud.controller.request.user.PageUserRequest;
import com.fit2cloud.controller.request.user.UpdateUserRequest;
import com.fit2cloud.controller.request.user.UserBatchAddRoleRequest;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.dto.UserNotifySettingDTO;
import com.fit2cloud.dto.UserOperateDto;
import com.fit2cloud.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

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
    @GetMapping("/page")
    public ResultHolder<IPage<UserDto>> listUser(PageUserRequest pageUserRequest) {
        return ResultHolder.success(userService.pageUser(pageUserRequest));
    }

    @ApiOperation(value = "添加用户")
    @PostMapping("/add")
    public ResultHolder<Boolean> createUser(@RequestBody @Validated(ValidationGroup.SAVE.class) CreateUserRequest request) {
        return ResultHolder.success(userService.createUser(request));
    }

    @ApiOperation(value = "更新用户")
    @PostMapping("/update")
    public ResultHolder<Boolean> updateUser(@RequestBody @Validated(ValidationGroup.UPDATE.class) UpdateUserRequest request) {
        return ResultHolder.success(userService.updateUser(request));
    }

    @ApiOperation(value = "更新密码")
    @PostMapping("/updatePwd")
    public ResultHolder<Boolean> updatePwd(@RequestBody User user) {
        return ResultHolder.success(userService.updatePwd(user));
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/{id}")
    public ResultHolder<Boolean> delete(@ApiParam("主键 ID")
                                        @NotNull(message = "{i18n.user.id.cannot.be.null}")
                                        @CustomValidated(mapper = BaseUserMapper.class, handler = ExistHandler.class, message = "{i18n.primary.key.not.exist}", exist = false)
                                        @PathVariable("id") String id) {
        return ResultHolder.success(userService.deleteUser(id));
    }

    @ApiOperation(value = "根据用户ID查询用户角色信息")
    @GetMapping(value = "/role/info/{id}")
    public ResultHolder<UserOperateDto> roleInfo(@ApiParam("主键 ID") @NotNull(message = "{i18n.user.id.cannot.be.null}")
                                                 @NotNull(message = "{i18n.user.id.cannot.be.null}")
                                                 @CustomValidated(mapper = BaseUserMapper.class, handler = ExistHandler.class, message = "{i18n.primary.key.not.exist}", exist = false)
                                                 @PathVariable("id") String id) {
        return ResultHolder.success(userService.userRoleInfo(id));
    }

    @ApiOperation(value = "启停用户")
    @PostMapping(value = "/changeStatus")
    public ResultHolder<Boolean> changeStatus(@RequestBody UserDto user) {
        return ResultHolder.success(userService.changeUserStatus(user));
    }

    @PostMapping(value = "/notificationSetting")
    public ResultHolder<Boolean> userNotificationSetting(@RequestBody UserNotifySettingDTO userNotificationSetting) {
        return ResultHolder.success(userService.updateUserNotification(userNotificationSetting));
    }

    @GetMapping(value = "/findUserNotification/{userId}")
    public ResultHolder<UserNotifySettingDTO> findUserNotification(@PathVariable String userId) {
        return ResultHolder.success(userService.findUserNotification(userId));
    }

    @ApiOperation(value = "批量添加用户角色")
    @PostMapping(value = "/addRole")
    public ResultHolder<Boolean> addUserRole(@Validated @RequestBody UserBatchAddRoleRequest userBatchAddRoleRequest) {
        return ResultHolder.success(userService.addUserRole(userBatchAddRoleRequest));
    }
}
