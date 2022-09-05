import type { Role } from "@commons/api/role/type";
import type { SimpleMap } from "@commons/api/base/type";

export class LoginRequest {
  /**
   *用户名
   */
  username: string;
  /**
   * 密码
   */
  password: string;

  constructor(username: string, password: string) {
    this.username = username;
    this.password = password;
  }
}

export class LoginResponse {
  user?: User | null;

  constructor(user: User) {
    this.user = user;
  }
}

export class UserStoreObjectWithLoginStatus {
  userStoreObject?: UserStoreObject | null;
  login: boolean;
  lang: string;

  constructor(
    userStoreObject: UserStoreObject | null,
    login: boolean,
    lang: string
  ) {
    this.userStoreObject = userStoreObject;
    this.login = login;
    this.lang = lang;
  }
}

export class UserStoreObject {
  user?: User | null;
  token?: string | null;
  role?: string | null;
  source?: string | null;

  constructor(
    user?: User,
    token?: string,
    role?: string,
    source?: string | null
  ) {
    this.user = user;
    this.token = token;
    this.role = role;
    this.source = source;
  }
}

export class User {
  id: string;
  username: string;
  name: string;
  email: string;
  phone?: string;
  password?: string;
  roleMap?: SimpleMap<Array<UserRole>>;

  constructor(
    id: string,
    username: string,
    name: string,
    email: string,
    phone?: string,
    password?: string,
    roleMap?: SimpleMap<Array<UserRole>>
  ) {
    this.id = id;
    this.username = username;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.password = password;
    this.roleMap = roleMap;
  }
}

export class UserRole {
  parentRole: string;
  roles: Role[];
  source?: string;
  parentSource?: string;

  constructor(
    parentRole: string,
    roles: Role[],
    source?: string,
    parentSource?: string
  ) {
    this.parentRole = parentRole;
    this.roles = roles;
    this.source = source;
    this.parentSource = parentSource;
  }
}
