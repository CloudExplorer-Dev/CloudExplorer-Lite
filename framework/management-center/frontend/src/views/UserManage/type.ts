import type { RoleInfo } from "@/api/user/type";

interface UpdateUserForm {
  id: string; // ID
  username: string; // 用户ID
  name: string; // 姓名
  email: string;
  phone: string;
  enabled: true;
  source: string; // 用户来源
  createTime: string;
  roleInfoList: RoleInfo[];
}

export type { UpdateUserForm };
