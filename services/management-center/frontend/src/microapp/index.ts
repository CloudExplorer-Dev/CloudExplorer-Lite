import type { App } from "vue";
import config from "@commons/utils/constants";
import type { Route } from "@commons/index";

declare global {
  interface Window {
    [index: string]: any;
  }
}
class AppMicroApp {
  appMount: () => App;
  appName: string;
  route: Route;
  app?: App;

  constructor(appMount: () => App, appName: string, route: Route) {
    this.appName = appName;
    this.appMount = appMount;
    this.route = route;
  }
  /**
   * 挂载
   */
  mount() {
    this.app = this.appMount();
    this.addDataListener((data: any) => {
      console.log(`[${import.meta.env.VITE_APP_NAME}]  addDataListener:`, data);
    });
  }
  /**
   * unmount 卸载
   */
  unmount() {
    this.app?.unmount();
    window[
      `${config.MICRO_APP_EVENTCENTER_PREFIX}${this.appName}`
    ]?.clearDataListener();
    this.route.history?.destroy();
  }

  /**
   * 向基座发送数据
   * @param data 数据
   */
  sendBaseData(data: any) {
    window[`${config.MICRO_APP_EVENTCENTER_PREFIX}${this.appName}`]?.dispatch(
      data
    );
  }

  /**
   * 添加数据监听器
   * @param listener 监听器
   */
  addDataListener(listener: (data: Record<string, unknown>) => void) {
    window[
      `${config.MICRO_APP_EVENTCENTER_PREFIX}${this.appName}`
    ]?.addDataListener(listener);
  }

  install(app: App) {
    // 微前端环境下，注册mount和unmount方法
    if (window.__MICRO_APP_BASE_APPLICATION__) {
      window[`micro-app-${import.meta.env.VITE_APP_NAME}`] = {
        mount: this.mount.bind(this),
        unmount: this.unmount.bind(this),
      };
    } else {
      // 非微前端环境直接渲染
      this.mount();
    }
    app.config.globalProperties.$appMicroapp = this;
  }
}

export { AppMicroApp };
