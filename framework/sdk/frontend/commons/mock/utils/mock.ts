import Result from "../../request/Result";
export const authMockHander = (headers: any) => {
  const token = headers["authorization"];
  if (!token) {
    return Result.error("未认证请登录", 1001);
  }
  try {
    JSON.parse(token);
    return token;
  } catch (e) {
    return Result.error("token失效", 1001);
  }
};
