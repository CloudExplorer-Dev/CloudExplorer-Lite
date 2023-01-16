export interface BaseModuleInfo {
  icon: string;
  name: string;
  hasPermission: boolean;
  roles: string[];
  module: string;
  path: string;
  redirect: string;
  unit?: string;
  type?: "currency" | undefined;
}
