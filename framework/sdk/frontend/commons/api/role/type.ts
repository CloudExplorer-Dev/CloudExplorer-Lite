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
