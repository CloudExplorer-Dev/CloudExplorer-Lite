interface User {
  id: string;
  name: string;
  email: string;
  createTime: number;
  source: string;
  password: string;
  active: boolean;
  phone: string;
}
interface ListUserRequest {
  pageSize: number;
  currentPage: number;
}

export type { User, ListUserRequest };
