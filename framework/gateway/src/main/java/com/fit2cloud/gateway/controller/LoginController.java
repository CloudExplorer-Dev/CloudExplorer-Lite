package com.fit2cloud.gateway.controller;

import com.fit2cloud.base.entity.User;
import com.fit2cloud.base.service.IBaseOrganizationService;
import com.fit2cloud.base.service.IBaseUserService;
import com.fit2cloud.common.constants.RoleConstants;
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
import com.fit2cloud.security.filter.JwtTokenAuthFilter;
import com.fit2cloud.service.BasePermissionService;
import com.fit2cloud.service.CommonService;
import com.fit2cloud.service.MenuService;
import com.fit2cloud.service.TokenPoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping
@Tag(name = "基础接口")
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
    @Resource
    private TokenPoolService tokenPoolService;

    @PostMapping("login")
    @Operation(summary = "登录")
    @ApiResponse(responseCode = "200",
            headers = @Header(name = JwtTokenUtils.TOKEN_NAME,
                    description = "认证成功后返回JWT Token",
                    schema = @Schema(type = "string"))
    )
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

    @PostMapping("logout")
    @OperatedLog(resourceType = ResourceTypeEnum.SYSTEM, operated = OperatedTypeEnum.LOGOUT)
    @Parameter(in = ParameterIn.HEADER, name = JwtTokenUtils.TOKEN_NAME, schema = @Schema(type = "string"))
    public Mono<ResponseEntity<ResultHolder<Object>>> logout(@RequestBody Object o, ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String token = request.getHeaders().getFirst(JwtTokenUtils.TOKEN_NAME);

        if (StringUtils.isNotBlank(token)) {
            Map<String, String> map = JwtTokenUtils.renewalToken(token);
            tokenPoolService.removeJwt(map.get("userId"), map.get("jwtId"));
        }

        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(ResultHolder.success(null).message("用户已登出"))
        );
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
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = JwtTokenUtils.TOKEN_NAME, schema = @Schema(type = "string"), required = true),
            @Parameter(in = ParameterIn.HEADER, name = RoleConstants.ROLE_TOKEN, schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.HEADER, name = JwtTokenAuthFilter.CE_SOURCE_TOKEN, schema = @Schema(type = "string"))
    })
    public Mono<ResultHolder<UserDto>> currentUser(ServerWebExchange exchange) {
        UserDto userDto = (UserDto) exchange.getAttributes().get("user");
        User user = userService.getById(userDto.getId());
        BeanUtils.copyProperties(user, userDto);
        userDto.setPassword(null);
        return Mono.just(ResultHolder.success(userDto));
    }

    @PutMapping("api/user/current")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = JwtTokenUtils.TOKEN_NAME, schema = @Schema(type = "string"), required = true),
            @Parameter(in = ParameterIn.HEADER, name = RoleConstants.ROLE_TOKEN, schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.HEADER, name = JwtTokenAuthFilter.CE_SOURCE_TOKEN, schema = @Schema(type = "string"))
    })
    public Mono<ResultHolder<Boolean>> updateCurrentUser(@RequestBody @Validated EditUserRequest request, ServerWebExchange exchange) {
        request.setId(((UserDto) exchange.getAttributes().get("user")).getId());
        return Mono.just(ResultHolder.success(userService.updateUserBasicInfo(request)));
    }

    @PutMapping("api/user/current/pwd")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = JwtTokenUtils.TOKEN_NAME, schema = @Schema(type = "string"), required = true),
            @Parameter(in = ParameterIn.HEADER, name = RoleConstants.ROLE_TOKEN, schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.HEADER, name = JwtTokenAuthFilter.CE_SOURCE_TOKEN, schema = @Schema(type = "string"))
    })
    public Mono<ResultHolder<Boolean>> resetPwd(@RequestBody @Validated ResetPwdRequest request, ServerWebExchange exchange) {
        UserDto userDto = (UserDto) exchange.getAttributes().get("user");
        return Mono.just(ResultHolder.success(userService.resetPwd(request, userDto)));
    }

    @GetMapping("api/modules")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = JwtTokenUtils.TOKEN_NAME, schema = @Schema(type = "string"), required = true),
            @Parameter(in = ParameterIn.HEADER, name = RoleConstants.ROLE_TOKEN, schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.HEADER, name = JwtTokenAuthFilter.CE_SOURCE_TOKEN, schema = @Schema(type = "string"))
    })
    public Mono<ResultHolder<List<Module>>> modules(ServerWebExchange exchange) {
        return Mono.just(ResultHolder.success(commonService.getModules()));
    }

    @GetMapping("api/menus")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = JwtTokenUtils.TOKEN_NAME, schema = @Schema(type = "string"), required = true),
            @Parameter(in = ParameterIn.HEADER, name = RoleConstants.ROLE_TOKEN, schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.HEADER, name = JwtTokenAuthFilter.CE_SOURCE_TOKEN, schema = @Schema(type = "string"))
    })
    public Mono<ResultHolder<Map<String, List<Menu>>>> menus(ServerWebExchange exchange) {
        return Mono.just(ResultHolder.success(menuService.getAvailableMenus()));
    }

    @GetMapping("api/permission/current")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = JwtTokenUtils.TOKEN_NAME, schema = @Schema(type = "string"), required = true),
            @Parameter(in = ParameterIn.HEADER, name = RoleConstants.ROLE_TOKEN, schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.HEADER, name = JwtTokenAuthFilter.CE_SOURCE_TOKEN, schema = @Schema(type = "string"))
    })
    public Mono<ResultHolder<Set<String>>> getCurrentUserPermissionSet(ServerWebExchange exchange) {
        UserDto user = (UserDto) exchange.getAttributes().get("user");
        return Mono.just(ResultHolder.success(basePermissionService.getPlainPermissions(user.getId(), user.getCurrentRole(), user.getCurrentSource())));
    }

    @GetMapping("api/organization/sourceTree")
    @Parameters(value = {
            @Parameter(in = ParameterIn.HEADER, name = JwtTokenUtils.TOKEN_NAME, schema = @Schema(type = "string"), required = true),
            @Parameter(in = ParameterIn.HEADER, name = RoleConstants.ROLE_TOKEN, schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.HEADER, name = JwtTokenAuthFilter.CE_SOURCE_TOKEN, schema = @Schema(type = "string"))
    })
    public Mono<ResultHolder<List<SourceTreeObject>>> sourceTree(ServerWebExchange exchange) {
        UserDto user = (UserDto) exchange.getAttributes().get("user");
        return Mono.just(ResultHolder.success(organizationService.sourceTree(user.getRoleMap())));
    }


}
