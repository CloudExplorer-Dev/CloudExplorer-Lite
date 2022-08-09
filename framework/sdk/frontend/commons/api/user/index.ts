import { post } from "../../request";
import { LoginResponse, LoginRequest } from "./type";
import type { Result } from "../../request/Result";

export const login = (data: LoginRequest) => {
  const loginResult: Promise<Result<LoginResponse>> = post(
    "/api/login",
    null,
    data
  );
  return loginResult;
};
