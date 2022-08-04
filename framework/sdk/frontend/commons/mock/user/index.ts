import type { MockMethod } from "vite-plugin-mock";
import users from "./data";
import Result from "../../request/Result";
export default [
  {
    url: "/api/login", // 注意，这里只能是string格式
    method: "post",
    response: ({ body: { username, password } }: any) => {
      const u = users.find((user) => {
        return user.userName === username;
      });
      if (!u) {
        return Result.error("用户名不存在", 1001);
      }
      if (u.password !== password) {
        return Result.error("密码错误", 1002);
      }
      return Result.success({ token: "textToken", userInfo: u });
    },
  },
  {
    url: "/api/user",
    method: "post",
    response: ({ body: { username, password, email } }: any) => {
      users.push({
        userName: username,
        password,
        email,
        portrait: "",
      });
    },
  },
] as MockMethod[];
