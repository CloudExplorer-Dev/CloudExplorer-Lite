import type { Router } from "vue-router";
import microApp from "@micro-zoe/micro-app";

/**
 * 通过主应用下发路由跳转
 * @param module
 * @param path
 * @param router Router对象，基座应用必需传
 */
function jumpToChildrenPath(module: string, path: string, router?: Router) {
  const _encodePath =
    import.meta.env.VITE_APP_NAME === "base"
      ? microApp.router.encode(path)
      : window.microApp.router.encode(path);
  const _path = "/" + module + "?" + module + "=" + _encodePath;

  if (import.meta.env.VITE_APP_NAME === "base") {
    router?.push(_path);
  } else {
    (window.microApp.router.getBaseAppRouter() as Router)?.push(_path);
  }
}

const utils = {
  jumpToChildrenPath,
};

export default utils;
