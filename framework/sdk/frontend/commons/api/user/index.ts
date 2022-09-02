import { get, post } from "@commons/request";
import type { LoginRequest, LoginResponse, User } from "./type";
import type { Result } from "@commons/request/Result";
import type { Ref } from "vue";

export const login = (data: LoginRequest, loading?: Ref<boolean>) => {
  const loginResult: Promise<Result<LoginResponse>> = post(
    "/login",
    null,
    data,
    loading
  );
  return loginResult;
};

export function fetchCurrentUser() {
  const currentUser: Promise<Result<User>> = get("/api/user/current");
  return currentUser;
}

export const getUser = () => {
  return get("/api/user/detail");
};

export const saveUser = (data: User) => {
  return post("/api/user-save", null, data);
};

export const updateUserPwd = (data: any) => {
  return post("/api/user-pwd-update", null, data);
};

export const getApiKeys = () => {
  return get("/api/key");
};

export const createApiKeys = () => {
  return post("/api/key/create");
};

export const deleteApiKeys = (data: any) => {
  return post("/api/key/delete", null, data);
};

export const updateApiKeys = (data: any) => {
  return post("/api/key/update", null, data);
};
