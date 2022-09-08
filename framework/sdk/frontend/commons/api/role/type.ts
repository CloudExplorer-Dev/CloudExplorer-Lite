import type { OrderObject, PageRequest } from "@commons/api/base/type";

export class Role {
  id: string;
  type: string;
  name: string;
  description?: string;
  parentRoleId: string;
  createTime?: string;
  updateTime?: string;

  constructor(
    id: string,
    type: string,
    name: string,
    parentRoleId: string,
    description?: string,
    createTime?: string,
    updateTime?: string
  ) {
    this.id = id;
    this.type = type;
    this.name = name;
    this.description = description;
    this.parentRoleId = parentRoleId;
    this.createTime = createTime;
    this.updateTime = updateTime;
  }
}

export class RoleRequest {
  id?: string;
  name?: string;
  type?: string;
  parentRoleId?: string;
  order?: OrderObject;

  constructor(
    id?: string,
    name?: string,
    type?: string,
    parentRoleId?: string,
    order?: OrderObject
  ) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.parentRoleId = parentRoleId;
    this.order = order;
  }
}

export class RolePageRequest extends RoleRequest implements PageRequest {
  currentPage?: number;
  pageSize?: number;

  constructor(
    currentPage?: number,
    pageSize?: number,
    id?: string,
    name?: string,
    type?: string,
    parentRoleId?: string,
    order?: OrderObject
  ) {
    super(id, name, type, parentRoleId, order);
    this.currentPage = currentPage;
    this.pageSize = pageSize;
  }

  static assign(source: any): RolePageRequest {
    const result = new RolePageRequest();
    Object.assign(result, source);
    return result;
  }

  setPage(currentPage: number, pageSize: number): RolePageRequest {
    this.currentPage = currentPage;
    this.pageSize = pageSize;
    return this;
  }
}
