package com.fit2cloud.dto;

import com.fit2cloud.base.entity.Organization;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;

@Getter
@Setter
@Accessors(chain = true)
public class OrganizationDTO extends Organization {

    @Serial
    private static final long serialVersionUID = -1031352778213602491L;

    private int workspaceCount;

    private int userCount;

}
