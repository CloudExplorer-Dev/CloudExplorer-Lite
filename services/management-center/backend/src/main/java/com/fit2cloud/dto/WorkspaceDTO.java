package com.fit2cloud.dto;

import com.fit2cloud.dao.entity.Workspace;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author jianneng
 * @date 2022/8/30 22:52
 **/
@Accessors(chain = true)
@Getter
@Setter
public class WorkspaceDTO extends Workspace {
    private String userCount;
    private String organizationName;
}
