<script lang="ts" setup>
import { useUserStore } from "@commons/stores/modules/user";
import { computed, ref } from "vue";
import { useModuleStore } from "@commons/stores/modules/module";
import _ from "lodash";
import { flatMenu } from "@commons/router";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { hasRolePermission } from "@commons/base-directives/hasPermission";
import type { Menu } from "@commons/api/menu/type";
import CeIcon from "@commons/components/ce-icon/index.vue";
import type { Module } from "@commons/api/module/type";
import { useRouter } from "vue-router";

const router = useRouter();
const userStore = useUserStore();
const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();

const value = ref<string>();
const loading = ref<boolean>(false);

const queryString = ref<string | undefined>(undefined);

interface MenuObject extends Module {
  childrenMenu: Array<Menu>;
  permissions: Array<string>;
}

const menus = computed(() => {
  return _.map(
    _.filter(moduleStore.runningModules, (module: Module) =>
      hasRolePermission(
        userStore.currentRole,
        permissionStore.userPermissions,
        module.requiredPermissions
      )
    ) as Array<Module>,
    (module: Module) => {
      const newMenu = flatMenu(moduleStore.runningMenus[module.id], [], false);
      const filteredMenu = _.filter(
        newMenu,
        (m: Menu) =>
          !!m.componentPath &&
          (queryString.value && queryString.value.length > 0
            ? _.includes(m.title, queryString.value)
            : true)
      ) as Menu[];
      const moduleBaseMenu: Menu = {
        componentPath: "module",
        title: module.name,
        name: module.id,
        path: _.defaultTo(module.basePath, ""),
        icon: module.icon,
        order: module.order,
        requiredPermissions: _.defaultTo(module.requiredPermissions, []),
      };

      const hasModule =
        (queryString.value && queryString.value.length > 0
          ? _.includes(moduleBaseMenu.title, queryString.value)
          : true) || filteredMenu.length > 0;

      return {
        ...module,
        childrenMenu: hasModule
          ? _.concat(moduleBaseMenu, filteredMenu)
          : filteredMenu,
        permissions: permissionStore.userPermissions,
      } as MenuObject;
    }
  );
});

function filterMenus(query?: string) {
  queryString.value = query;
}

function changeMenu() {
  const menuList = _.flatMap(menus.value, (group: MenuObject) => {
    return group.childrenMenu;
  }) as Menu[];
  const menu = _.find(
    menuList,
    (obj: Menu) => obj.name === value.value
  ) as Menu;

  let path = "";
  //跳转模块
  if (!menu?.module && menu.componentPath === "module") {
    path = menu.path;
  } else {
    const module = _.find(
      menus.value,
      (g: MenuObject) => g.id === menu.module
    ) as MenuObject;
    path = _.replace(module.basePath + menu.path, "//", "#/");
  }

  console.log(path);
  router.push(path);
}
</script>
<template>
  <el-card class="info-card">
    <div style="font-weight: bold; font-size: 16px; padding-bottom: 26px">
      快捷服务
    </div>
    <el-select
      style="width: 100%"
      v-model="value"
      filterable
      :filter-method="filterMenus"
      reserve-keyword
      placeholder="服务菜单名称"
      :loading="loading"
      @change="changeMenu"
    >
      <el-option-group v-for="group in menus" :key="group.id">
        <el-option
          v-for="item in group.childrenMenu"
          :key="item.name"
          :label="item.title"
          :value="item.name"
        >
          <span
            v-if="item.componentPath === 'module'"
            style="font-weight: bold"
          >
            <CeIcon
              size="15px"
              :code="item.icon"
              style="margin-right: 10px"
            />{{ item.title }}
          </span>
          <span v-else style="padding-left: 40px">
            <CeIcon
              size="15px"
              :code="item.icon"
              style="margin-right: 10px"
            />{{ item.title }}
          </span>
        </el-option>
      </el-option-group>
    </el-select>
  </el-card>
</template>

<style scoped lang="scss">
.info-card {
  /*min-width: 340px;*/
  min-height: 160px;
}
</style>
