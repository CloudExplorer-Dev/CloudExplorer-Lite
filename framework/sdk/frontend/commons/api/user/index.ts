import { post } from "../../request";
import { LoginResponse, LoginRequest } from "./type";
import type { Result } from "../../request/Result";
export const login = (url: string, data: LoginRequest) => {
  const loginResult: Promise<Result<LoginResponse>> = post(url, null, data);
  return loginResult;
};
