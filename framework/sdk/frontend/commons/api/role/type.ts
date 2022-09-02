export class Role {
  id: string;
  type: string;
  name: string;
  description?: string;
  parentRoleId?: string;

  constructor(
    id: string,
    type: string,
    name: string,
    description: string,
    parentRoleId: string
  ) {
    this.id = id;
    this.type = type;
    this.name = name;
    this.description = description;
    this.parentRoleId = parentRoleId;
  }
}
