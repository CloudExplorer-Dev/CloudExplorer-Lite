package com.fit2cloud.gateway.controller;

import com.fit2cloud.base.service.IBaseUserService;
import com.fit2cloud.common.log.annotation.OperatedLog;
import com.fit2cloud.common.log.constants.OperatedTypeEnum;
import com.fit2cloud.common.log.constants.ResourceTypeEnum;
import com.fit2cloud.common.utils.JwtTokenUtils;
import com.fit2cloud.controller.handler.ResultHolder;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.dto.module.Menu;
import com.fit2cloud.dto.module.Module;
import com.fit2cloud.request.LoginRequest;
import com.fit2cloud.service.BasePermissionService;
import com.fit2cloud.service.CommonService;
import com.fit2cloud.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
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

    @PostMapping("login")
    @OperatedLog(resourceType = ResourceTypeEnum.SYSTEM,operated = OperatedTypeEnum.LOGIN)
    public Mono<ResponseEntity<ResultHolder<Object>>> login(@RequestBody LoginRequest loginRequest) {
        String token = null;
        try {
            token = loginService.login(loginRequest);
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
    @OperatedLog(resourceType = ResourceTypeEnum.SYSTEM,operated = OperatedTypeEnum.LOGOUT)
    public Mono<String> logout() {
        return Mono.just("test");
    }

    @GetMapping("api/user/current")
    public Mono<ResultHolder<UserDto>> currentUser(ServerWebExchange exchange) {
        return Mono.just(ResultHolder.success((UserDto) exchange.getAttributes().get("user")));
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


}
