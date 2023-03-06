import type { Workspace } from "@commons/api/workspace/type";

export interface WorkspaceDetail extends Workspace {
  organizationName?: string;
  userCount?: number;
  cloudMapping?: Array<CloudMapping>;
}

export interface WorkspaceCreate {
  /**
   *主键id
   */
  id?: string;
  /**
   *工作空间名称
   */
  name: string;
  /**
   *工作空间描述
   */
  description: string;

  organizationId?: string;
}

export interface CreateWorkspaceForm {
  organizationId?: string;
  workspaceDetails: Array<WorkspaceCreate>;
}

interface CloudMapping {
  id: number;
  cloud_account_id: string;
  cloud_account_name: string;
  cloud_account_project_uuid: string;
  cloud_account_project_name: string;
  workspace_id: string;
  cloud_account_icon_url: string;
}
export interface ListWorkspaceRequest {
  pageSize: number;
  currentPage: number;
}
