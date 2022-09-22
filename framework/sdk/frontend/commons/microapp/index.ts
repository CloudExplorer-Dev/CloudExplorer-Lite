import type { App } from "vue";
import config from "@commons/utils/constants";
import route from "@commons/router";

export class AppMicroApp {
  appMount: () => Promise<App>;
  appName: string;
  app?: App;

  constructor(appMount: () => Promise<App>, appName: string) {
    this.appName = appName;
    this.appMount = appMount;
  }
  /**
   * 挂载
   */
  mount() {
    this.appMount().then((app) => (this.app = app));
    this.addDataListener((data: any) => {
      console.log(`[${import.meta.env.VITE_APP_NAME}]  addDataListener:`, data);
    });
  }
  /**
   * unmount 卸载
   */
  unmount() {
    this.app?.unmount();
    // 卸载所有数据监听函数
    window[
      `${config.MICRO_APP_EVENTCENTER_PREFIX}${this.appName}`
    ]?.clearDataListener();
    route.getRoute()?.history?.destroy();

    this.app = undefined;
    route.clearRoute();

    console.log(`微应用 [${import.meta.env.VITE_APP_NAME}] 卸载了`);
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

  install() {
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
    if (this.app) {
      this.app.config.globalProperties.$appMicroapp = this;
    }
  }
}
