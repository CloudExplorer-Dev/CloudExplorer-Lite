interface Workspace {
  id: string;
  name: string;
  description?: string;
  createTime: string;
  organization_id: string;
  organization_name: string;
  user_count: number;
  cloud_mapping?: Array<CloudMapping>;
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
interface ListWorkspaceRequest {
  pageSize: number;
  currentPage: number;
}

export type { Workspace, ListWorkspaceRequest, CloudMapping };
