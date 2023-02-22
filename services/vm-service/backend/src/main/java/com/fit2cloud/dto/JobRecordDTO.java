package com.fit2cloud.dto;

import com.fit2cloud.base.entity.JobRecord;
import com.fit2cloud.base.entity.VmCloudDisk;
import com.fit2cloud.base.entity.VmCloudServer;
import lombok.Data;

import java.io.Serial;
import java.util.List;

@Data
public class JobRecordDTO extends JobRecord {

    @Serial
    private static final long serialVersionUID = 6572654975684690195L;
    private List<VmCloudServer> servers;
    private List<VmCloudDisk> disks;
    private String resourceId;
    private String resourceName;
    /**
     * 资源类型：VM、DISK等
     */
    private String resourceType;
    private String operateUserName;
    private String workspaceId;
    private String organizationId;
    private String workspaceName;
    private String organizationName;
}
