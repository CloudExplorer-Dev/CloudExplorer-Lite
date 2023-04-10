package com.fit2cloud.gateway.controller;

import com.fit2cloud.base.entity.User;
import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.base.service.IBaseUserService;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.utils.FileUtils;
import com.fit2cloud.common.utils.JwtTokenUtils;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.dto.module.Menu;
import com.fit2cloud.dto.module.Module;
import com.fit2cloud.request.LoginRequest;
import com.fit2cloud.request.user.EditUserRequest;
import com.fit2cloud.request.user.ResetPwdRequest;
import com.fit2cloud.response.SourceTreeObject;
import com.fit2cloud.service.BasePermissionService;
import com.fit2cloud.service.CommonService;
import com.fit2cloud.service.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping
public class LoginController {

    @Resource
    private IBaseUserService loginService;
    @Resource
    private CommonService commonService;
    @Resource
    private MenuService menuService;
    @Resource
    private BasePermissionService basePermissionService;
    @Resource
    private IBaseOrganizationService organizationService;
    @Resource
    private IBaseUserService userService;

    @PostMapping("login")
    @OperatedLog(resourceType = ResourceTypeEnum.SYSTEM, operated = OperatedTypeEnum.LOGIN)
    public Mono<ResponseEntity<ResultHolder<Object>>> login(@RequestBody LoginRequest loginRequest, ServerWebExchange exchange) {
        String token = null;
        try {
            ServerHttpRequest request = exchange.getRequest();
            token = loginService.login(request, loginRequest);
        } catch (Exception e) {
            return Mono.just(
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(ResultHolder.error(null).code(HttpStatus.UNAUTHORIZED.value()).message(e.getMessage()))
            );
        }
        if (token != null) {
            return Mono.just(
                    ResponseEntity.ok()
                            .header(JwtTokenUtils.TOKEN_NAME, token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(ResultHolder.success(null).message("登录成功"))
            );
        }
        return Mono.empty();
    }

    @GetMapping("logout")
    @OperatedLog(resourceType = ResourceTypeEnum.SYSTEM, operated = OperatedTypeEnum.LOGOUT)
    public Mono<String> logout() {
        return Mono.just("test");
    }

    @GetMapping("login-hint")
    public Mono<ResponseEntity<ResultHolder<Object>>> getHint() {
        File versionFile = new File("/opt/cloudexplorer/conf/LOGIN_HINT");
        String ceLoginHint = FileUtils.txt2String(versionFile);
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(ResultHolder.success(ceLoginHint)));
    }

    @GetMapping("api/user/current")
    public Mono<ResultHolder<UserDto>> currentUser(ServerWebExchange exchange) {
        UserDto userDto = (UserDto) exchange.getAttributes().get("user");
        User user = userService.getById(userDto.getId());
        BeanUtils.copyProperties(user, userDto);
        return Mono.just(ResultHolder.success(userDto));
    }

    @PutMapping("api/user/current")
    public Mono<ResultHolder<Boolean>> updateCurrentUser(@RequestBody @Validated EditUserRequest request) {
        return Mono.just(ResultHolder.success(userService.updateUserBasicInfo(request)));
    }

    @PutMapping("api/user/current/pwd")
    public Mono<ResultHolder<Boolean>> resetPwd(@RequestBody @Validated ResetPwdRequest request, ServerWebExchange exchange) {
        UserDto userDto = (UserDto) exchange.getAttributes().get("user");
        return Mono.just(ResultHolder.success(userService.resetPwd(request, userDto)));
    }

    @GetMapping("api/modules")
    public Mono<ResultHolder<List<Module>>> modules(ServerWebExchange exchange) {
        return Mono.just(ResultHolder.success(commonService.getModules()));
    }

    @GetMapping("api/menus")
    public Mono<ResultHolder<Map<String, List<Menu>>>> menus(ServerWebExchange exchange) {
        return Mono.just(ResultHolder.success(menuService.getAvailableMenus()));
    }

    @GetMapping("api/permission/current")
    public Mono<ResultHolder<Set<String>>> getCurrentUserPermissionSet(ServerWebExchange exchange) {
        UserDto user = (UserDto) exchange.getAttributes().get("user");
        return Mono.just(ResultHolder.success(basePermissionService.getPlainPermissions(user.getId(), user.getCurrentRole(), user.getCurrentSource())));
    }

    @GetMapping("api/organization/sourceTree")
    public Mono<ResultHolder<List<SourceTreeObject>>> sourceTree(ServerWebExchange exchange) {
        UserDto user = (UserDto) exchange.getAttributes().get("user");
        return Mono.just(ResultHolder.success(organizationService.sourceTree(user.getRoleMap())));
    }


}
