package com.fit2cloud.dto;

import com.fit2cloud.base.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserOperateDto extends User {

    @Schema(title = "角色信息列表")
    private List<RoleInfo> roleInfoList;

}
