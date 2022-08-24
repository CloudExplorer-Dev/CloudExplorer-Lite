import {Route} from "@commons/index";
import {createWebHistory} from "vue-router";
import baseLayout from "@commons/business/base-layout/index.vue";
import Login from "@commons/business/login/index.vue";
import {servicesStore} from "@commons/stores/services";
import pinia from "@/stores";

const route = new Route(
  createWebHistory(),
  import.meta.glob("@/views/*/*.vue"),
  [
    {
      path: "/",
      name: "home",
      component: baseLayout,
    },
    {
      path: "/login",
      name: "login",
      component: Login,
    },
  ],
  () => {
    const routes = servicesStore(pinia).getRouters();
    return routes;
  },
  undefined,
  async () => {
    // 处理新模块上来后,对模块的import路径进行重写,去掉项目名称
    await window.rootMicroapp.updateModule();
  }
);
export default route;
