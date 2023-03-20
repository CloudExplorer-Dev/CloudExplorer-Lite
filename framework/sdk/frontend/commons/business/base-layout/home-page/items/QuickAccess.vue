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
import type { RecentAccessRoute } from "@commons/router/type";

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

const accessibleModules = computed(() => {
  return _.filter(moduleStore.runningModules, (module: Module) =>
    hasRolePermission(
      userStore.currentRole,
      permissionStore.userPermissions,
      module.requiredPermissions
    )
  ) as Array<Module>;
});

const menus = computed(() => {
  return _.filter(
    _.map(accessibleModules.value, (module: Module) => {
      const newMenu = flatMenu(moduleStore.runningMenus[module.id], [], false);
      const filteredMenu = _.filter(
        newMenu,
        (m: Menu) =>
          !!m.componentPath &&
          (queryString.value && queryString.value.length > 0
            ? _.includes(m.title, queryString.value)
            : true) &&
          hasRolePermission(
            userStore.currentRole,
            permissionStore.userPermissions,
            m.requiredPermissions
          )
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
    }),
    (g) => g.childrenMenu.length > 0
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

  //console.log(path);
  router.push(path);
}

const quickAccessMenus = computed(() => {
  return _.flatMap(accessibleModules.value, (module: Module) => {
    const newMenu = flatMenu(
      moduleStore.runningMenus[module.id],
      [],
      false,
      undefined,
      true
    );

    return _.filter(newMenu, (m) => {
      return (
        m.quickAccess &&
        hasRolePermission(
          userStore.currentRole,
          permissionStore.userPermissions,
          m.requiredPermissions
        )
      );
    }) as Menu[];
  });
});

const quickAccessGroup = computed<Array<Array<Menu>>>(() => {
  const group: Array<Array<Menu>> = [];
  _.forEach(quickAccessMenus.value, (o, index) => {
    const i = _.floor(index / 4);
    if (group.length === i) {
      group.push([]);
    }
    group[i].push(o);
  });
  return group;
});

function goQuickAccess(menu: Menu) {
  const module = _.find(
    menus.value,
    (g: MenuObject) => g.id === menu.module
  ) as MenuObject;
  const path = _.replace(module.basePath + menu.path, "//", "#/");

  //console.log(path);
  router.push(path);
}

const key = "RecentAccess-" + userStore.currentUser.id;
const str = localStorage.getItem(key);
const recentAccessRoutes: Array<RecentAccessRoute> = str ? JSON.parse(str) : [];

interface RecentAccess extends RecentAccessRoute {
  title: string;
  parentTitle: string;
  quickAccessName: string;

  moduleName: string;
}

const recentAccess = computed<Array<RecentAccess>>(() => {
  const accessibleMenus = _.flatMap(
    accessibleModules.value,
    (module: Module) => {
      const newMenu = flatMenu(
        moduleStore.runningMenus[module.id],
        [],
        false,
        undefined,
        true
      );
      return _.filter(newMenu, (m) => {
        return (
          _.some(
            recentAccessRoutes,
            (r) => r.module === m.module && r.name === m.name
          ) &&
          hasRolePermission(
            userStore.currentRole,
            permissionStore.userPermissions,
            m.requiredPermissions
          )
        );
      }) as Menu[];
    }
  );

  return _.slice(
    _.orderBy(
      _.map(accessibleMenus, (m) => {
        const r = _.find(
          recentAccessRoutes,
          (r) => r.module === m.module && r.name === m.name
        );
        return {
          ...r,
          title: m.title,
          parentTitle: m.parentTitle,
          quickAccessName: m.quickAccessName,
          moduleName: _.find(
            accessibleModules.value,
            (module) => module.id === m.module
          )?.name,
        };
      }),
      ["time"],
      ["desc"]
    ),
    0,
    8 //只展示8个
  ) as Array<RecentAccess>;
});

const recentAccessGroup = computed<Array<Array<RecentAccess>>>(() => {
  const group: Array<Array<RecentAccess>> = [];
  _.forEach(recentAccess.value, (o, index) => {
    const i = _.floor(index / 4);
    if (group.length === i) {
      group.push([]);
    }
    group[i].push(o);
  });
  return group;
});

function goRecentAccess(menu: RecentAccessRoute) {
  const module = _.find(
    menus.value,
    (g: MenuObject) => g.id === menu.module
  ) as MenuObject;
  const path = _.replace(module.basePath + menu.fullPath, "//", "#/");

  //console.log(path);
  router.push(path);
}
</script>
<template>
  <div class="info-card">
    <div class="menu-div">
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
    </div>

    <div class="menu-div" v-if="recentAccess.length > 0">
      <div class="title">最近访问</div>
      <div>
        <template v-for="(array, _i) in recentAccessGroup" :key="_i">
          <el-row class="group-row" :gutter="8">
            <template v-for="(o, _j) in array" :key="_j">
              <el-col :span="6">
                <div
                  class="recent-access-btn"
                  @click="goRecentAccess(o)"
                  :title="
                    o.moduleName +
                    '-' +
                    (o.quickAccessName ? o.quickAccessName : o.parentTitle)
                  "
                >
                  {{
                    o.moduleName +
                    "-" +
                    (o.quickAccessName ? o.quickAccessName : o.parentTitle)
                  }}
                </div>
              </el-col>
            </template>
          </el-row>
        </template>
      </div>
    </div>

    <div class="menu-div">
      <div class="title">快捷入口</div>
      <div>
        <template v-for="(array, _i) in quickAccessGroup" :key="_i">
          <el-row class="group-row" :gutter="8">
            <template v-for="(o, _j) in array" :key="_j">
              <el-col :span="6">
                <div
                  class="quick-access-btn"
                  @click="goQuickAccess(o)"
                  :title="o.quickAccessName"
                >
                  <CeIcon
                    :code="o.quickAccessIcon"
                    v-if="o.quickAccessIcon"
                    size="16px"
                    style="margin-right: 8px"
                  />
                  {{ o.quickAccessName }}
                </div>
              </el-col>
            </template>
          </el-row>
        </template>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.info-card {
  border-radius: 4px;
  background-color: #ffffff;
  padding: 24px;

  .menu-div {
    margin-bottom: 24px;
  }

  .menu-div:last-child {
    margin-bottom: 0;
  }

  .title {
    font-style: normal;
    font-weight: 500;
    font-size: 16px;
    line-height: 24px;
    margin-bottom: 10px;
  }

  .group-row {
    margin-bottom: 8px;

    .recent-access-btn {
      padding: 2px 6px;
      cursor: pointer;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      font-style: normal;
      font-weight: 400;
      font-size: 14px;
      line-height: 22px;
      color: #646a73;
    }

    .recent-access-btn:hover {
      color: var(--el-color-primary);
    }

    .recent-access-btn:before {
      display: inline-block;
      content: "";
      height: 4px;
      width: 4px;
      border-radius: 2px;
      background-color: var(--el-color-primary);
      margin-right: 10px;
      line-height: 22px;
      vertical-align: middle;

      margin-top: auto;
      margin-bottom: auto;
    }

    .quick-access-btn {
      padding: 8px 16px;
      box-sizing: border-box;
      background: #ffffff;
      border: 1px solid #dee0e3;
      border-radius: 4px;
      cursor: pointer;
      font-style: normal;
      font-weight: 400;
      font-size: 14px;
      line-height: 22px;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      color: #1f2329;
    }
    .quick-access-btn:hover {
      box-shadow: 0 6px 24px rgba(31, 35, 41, 0.08);
    }
  }
  .group-row:last-child {
    margin-bottom: 0;
  }
}
</style>
