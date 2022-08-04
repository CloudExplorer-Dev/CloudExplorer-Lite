interface LoginRequest {
  /**
   *用户名
   */
  username: string;
  /**
   * 密码
   */
  password: string;
}

interface UserInfo {
  /**
   * 用户名
   */
  username: string;
  /**
   *密码
   */
  password?: string;
  /**
   *邮箱
   */
  email: string;
  /**
   *头像
   */
  portrait: string;
}
interface LoginResponse {
  /**
   *令牌
   */
  token: string;
  /**
   *用户
   */
  userInfo: UserInfo;
}
export type { LoginRequest, LoginResponse, UserInfo };
