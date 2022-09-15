import type { Role } from "@commons/api/role/type";

export class CreateRoleRequest {
  name?: string;
  description?: string;
  parentRoleId?: string;

  constructor(name?: string, description?: string, parentRoleId?: string) {
    this.name = name;
    this.description = description;
    this.parentRoleId = parentRoleId;
  }
}

export class UpdateRoleRequest {
  id: string;
  name?: string;
  description?: string;

  constructor(id: string, name?: string, description?: string) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  static newInstance(role: Role | UpdateRoleRequest): UpdateRoleRequest {
    return new UpdateRoleRequest(role.id, role.name, role.description);
  }
}
