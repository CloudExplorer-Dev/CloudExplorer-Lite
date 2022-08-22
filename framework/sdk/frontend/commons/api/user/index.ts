import { get, post } from "../../request";
import { LoginResponse, LoginRequest, UserInfo } from "./type";
import type { Result } from "../../request/Result";

export const login = (data: LoginRequest) => {
  const loginResult: Promise<Result<LoginResponse>> = post(
    "/api/login",
    null,
    data
  );
  return loginResult;
};

export const getUser = () => {
  return get("/api/user/detail");
};

export const saveUser = (data: UserInfo) => {
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
