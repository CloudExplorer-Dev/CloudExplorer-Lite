const config = {
  TITLE: "CloudExplorer 云服务平台",
  TOKEN_KEY: "Authorization",
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
const roleConst = {
  admin: "ADMIN",
  orgAdmin: "ORGADMIN",
  user: "USER",
};

export default { config, roleConst };
