import type { Role } from "@commons/api/role/type";
import type { WritableComputedRef } from "vue";

export class CreateRoleRequest {
  name?: string;
  description?: string;
  parentRoleId?: string;

  permissions?: Array<string>;

  constructor(
    name?: string,
    description?: string,
    parentRoleId?: string,
    permissions?: Array<string>
  ) {
    this.name = name;
    this.description = description;
    this.parentRoleId = parentRoleId;
    this.permissions = permissions;
  }

  static newInstance(
    role: Role | CreateRoleRequest,
    permissions?: Array<string>
  ): CreateRoleRequest {
    return new CreateRoleRequest(
      role.name,
      role.description,
      role.parentRoleId,
      permissions
    );
  }
}

export class UpdateRoleRequest {
  id: string;
  name?: string;
  description?: string;
  permissions?: Array<string>;

  constructor(
    id: string,
    name?: string,
    description?: string,
    permissions?: Array<string>
  ) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.permissions = permissions;
  }

  static newInstance(
    role: Role | UpdateRoleRequest,
    permissions?: Array<string>
  ): UpdateRoleRequest {
    return new UpdateRoleRequest(
      role.id,
      role.name,
      role.description,
      permissions
    );
  }
}

export class ModulePermission {
  groups: Array<GroupPermission>;

  constructor(groups: Array<GroupPermission>) {
    this.groups = groups;
  }
}

export class GroupPermission {
  id: string;
  module: string;
  name: string;
  permissions: Array<Permission>;
  checked?: WritableComputedRef<boolean>;

  constructor(
    id: string,
    module: string,
    name: string,
    permissions: Array<Permission>
  ) {
    this.id = id;
    this.module = module;
    this.name = name;
    this.permissions = permissions;
  }
}

export class Permission {
  groupId: string;
  id: string;
  simpleId: string;
  module: string;
  operate: string;
  roles: Array<string>;
  require?: string;

  constructor(
    groupId: string,
    id: string,
    simpleId: string,
    module: string,
    operate: string,
    roles: Array<string>,
    require?: string
  ) {
    this.groupId = groupId;
    this.id = id;
    this.simpleId = simpleId;
    this.module = module;
    this.operate = operate;
    this.roles = roles;
    this.require = require;
  }
}
