const config = {
  TITLE: "CloudExplorer 云服务平台",
  CE_TOKEN_KEY: "ce-token",
  CE_ROLE_KEY: "CE-ROLE",
  CE_SOURCE_KEY: "CE-SOURCE",
  CE_LANG_KEY: "LANG",
  /**
   * microAPP在window对象上的属性名称前缀
   */
  MICRO_APP_PREFIX: "micro-app-",
  /**
   * microAPP 事件在window对象上的属性名称前缀
   */
  MICRO_APP_EVENTCENTER_PREFIX: "eventCenterForApp-",
};

// 角色类型常量
export const roleConst = {
  admin: "ADMIN",
  orgAdmin: "ORGADMIN",
  user: "USER",
};

export default config;
