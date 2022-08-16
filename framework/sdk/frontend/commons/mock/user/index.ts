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
      return Result.success({
        token: JSON.stringify({ username: u.userName }),
        userInfo: u,
      });
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
  {
    url: "/api/user/detail",
    method: "get",
    response: () => {
      const { username } = JSON.parse(
        localStorage.getItem("Authorization") + ""
      );
      const user: any = users.find((user) => {
        return user.userName === username;
      });
      return Result.success({
        userInfo: user,
      });
    },
  },
  {
    url: "/api/user-save",
    method: "post",
    response: ({ body: { userName, password, email, portrait } }: any) => {
      const u = users.find((user) => {
        return user.userName === userName;
      });
      if (!u) {
        return Result.error("用户名不存在", 1001);
      }
      users.find((user) => {
        if (user.userName === userName) {
          user.email = email;
        }
      });
      return Result.success({});
    },
  },
  {
    url: "/api/user-pwd-update",
    method: "post",
    response: ({ body: { password, newPassword } }: any) => {
      const currentUser = "admin";
      const u = users.find((user) => {
        return user.userName === currentUser;
      });
      if (!u) {
        return Result.error("用户名不存在", 1001);
      }
      if (u.password !== password) {
        return Result.error("旧密码错误", 1001);
      } else {
        u.password = newPassword;
        return Result.success({});
      }
    },
  },
] as MockMethod[];
