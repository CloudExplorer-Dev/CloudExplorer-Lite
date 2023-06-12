package com.fit2cloud.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fit2cloud.base.entity.Role;
import com.fit2cloud.base.entity.User;
import com.fit2cloud.common.constants.RoleConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Accessors(chain = true)
@Getter
@Setter
public class UserDto extends User {

    @Serial
    private static final long serialVersionUID = 990142634584088427L;

    private RoleConstants.ROLE currentRole = RoleConstants.ROLE.ANONYMOUS; //不是给前端用的

    private Map<RoleConstants.ROLE, List<UserRoleDto>> roleMap;

    private String currentSource;

    private String ip;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime loginTime;

    @Schema(title = "角色列表")
    private List<Role> roles = new ArrayList<>();

}

