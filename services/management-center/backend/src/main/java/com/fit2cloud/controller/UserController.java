package com.fit2cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fit2cloud.base.entity.User;
import com.fit2cloud.controller.editor.OrderEditor;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.dto.UserOperateDto;
import com.fit2cloud.request.CreateUserRequest;
import com.fit2cloud.request.PageUserRequest;
import com.fit2cloud.request.pub.OrderRequest;
import com.fit2cloud.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
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
    public ResultHolder<Boolean> createUser(@RequestBody CreateUserRequest request) {
        return ResultHolder.success(userService.createUser(request));
    }

    @ApiOperation(value = "更新用户")
    @PostMapping("/update")
    public ResultHolder<Boolean> updateUser(@RequestBody CreateUserRequest request) {
        return ResultHolder.success(userService.updateUser(request));
    }

    @ApiOperation(value = "更新密码")
    @PostMapping("/updatePwd")
    public ResultHolder<Boolean> updatePwd(@RequestBody User user) {
        return ResultHolder.success(userService.updatePwd(user));
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/{userId}")
    public ResultHolder<Boolean> delete(@ApiParam("用户id") @NotNull(message = "用户id不能为null") @PathVariable("userId") String userId) {
        return ResultHolder.success(userService.deleteUser(userId));
    }

    @ApiOperation(value = "根据用户ID查询用户角色信息")
    @GetMapping(value = "/role/info/{userId}")
    public ResultHolder<UserOperateDto> roleInfo(@PathVariable String userId) {
        return ResultHolder.success(userService.userRoleInfo(userId));
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
