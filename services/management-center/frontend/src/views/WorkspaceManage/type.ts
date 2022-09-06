interface WorkspaceDetails {
  id?: string;
  name: string;
  organizationId?: string;
  description: string;
}
interface CreateWorkspaceForm {
  organizationId?: string;
  workspaceDetails: Array<WorkspaceDetails>;
}
export type { CreateWorkspaceForm, WorkspaceDetails };
