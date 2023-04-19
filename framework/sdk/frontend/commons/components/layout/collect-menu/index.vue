<script setup lang="ts">
import { ref, computed } from "vue";
import type { Menu } from "@commons/api/menu/type";
import type { Module } from "@commons/api/module/type";
import { flatMenu } from "@commons/router";
import { hasRolePermission } from "@commons/base-directives/hasPermission";
import { Search } from "@element-plus/icons-vue";
import type { SimpleMap } from "@commons/api/base/type";
import _ from "lodash";
import { useRouter } from "vue-router";
import MicroAppRouterUtil from "@commons/router/MicroAppRouterUtil";
const emit = defineEmits(["close"]);
interface ModuleContainer extends Module {
  /**
   * 子菜单
   */
  childrenMenu: Array<Menu>;
  /**
   *权限
   */
  permissions: Array<string>;
}

const props = defineProps<{
  // 运行中的模块菜单
  runningModuleMenus: SimpleMap<Array<Menu>>;
  // 运行中的所有模块
  runningModules: Array<Module>;
  // 当前角色
  currentRole: string;
  // 运行模块的权限
  runningModulePermissions: Array<string>;
}>();
// 搜索内容
const searchText = ref<string>("");

const moduleMenus = computed(() => {
  return resetMenus();
});
const router = useRouter();

/**
 * 将数据重新组合为可用数据
 */
const resetMenus = () => {
  const moduleContainers: Array<ModuleContainer> = flatMapMenus(
    props.runningModuleMenus,
    props.runningModules,
    props.runningModulePermissions
  );
  const filterModuleContainer: Array<ModuleContainer> = filterMenu(
    searchText.value,
    moduleContainers,
    props.currentRole
  );
  return splitMenu(filterModuleContainer, 3);
};

/**
 * 扁平化并映射数据
 * @param runningModuleMenus       运行中模块菜单
 * @param runningModules           运行中的模块
 * @param runningModulePermissions 运行中模块的权限
 */
const flatMapMenus = (
  runningModuleMenus: SimpleMap<Array<Menu>>,
  runningModules: Array<Module>,
  runningModulePermissions: Array<string>
) => {
  return runningModules.map((module) => {
    const newMenu = flatMenu(runningModuleMenus[module.id], [], false);
    const moduleContainer: ModuleContainer = {
      ...module,
      childrenMenu: newMenu,
      permissions: runningModulePermissions ? runningModulePermissions : [],
    };
    return moduleContainer;
  });
};
/**
 * 判断当前菜单是否可以展示
 * @param searchText  搜索内容
 * @param menu        菜单
 * @param currentRole 当前角色
 * @param permissions 当前权限
 */
const hasShowMenu = (
  searchText: string,
  menu: Menu,
  currentRole: string,
  permissions: Array<string>
) => {
  if (!hasRolePermission(currentRole, permissions)) {
    return false;
  }
  if (!menu.componentPath) {
    return false;
  }
  return menu.title.includes(searchText);
};

/**
 * 将菜单分为n份
 * @param moduleContainers 需要分割的对象
 * @param splitNum         分割份数
 */
const splitMenu = (
  moduleContainers: Array<ModuleContainer>,
  splitNum: number
) => {
  const splitMenu: Array<Array<ModuleContainer>> = [];
  for (let i = 0; i < splitNum; i++) {
    splitMenu.push([]);
  }
  moduleContainers.forEach((item: ModuleContainer, index: number) => {
    splitMenu[index % splitNum].push(item);
  });
  return splitMenu;
};

/**
 * 过滤菜单
 * @param searchText  搜索内容
 * @param services    所有的服务
 * @param currentRole 当前角色
 */
const filterMenu = (
  searchText: string | null,
  services: Array<ModuleContainer>,
  currentRole: string
) => {
  return services.filter((service) => {
    service.childrenMenu = service.childrenMenu.filter((serviceMenu) => {
      return hasShowMenu(
        searchText ? searchText : "",
        serviceMenu,
        currentRole,
        service.permissions
      );
    });
    return service.childrenMenu.length > 0;
  });
};

function jumpMenu(menu: Menu) {
  console.debug(menu);
  const module = _.find(props.runningModules, (m) => m.id === menu.module);
  if (!module) {
    return;
  }
  const path = _.replace(module.basePath + menu.path, "//", "/");
  MicroAppRouterUtil.jumpToChildrenPath(module.id, path, router);
  close();
}

const close = () => {
  emit("close");
};
</script>

<template>
  <div class="collectMenu">
    <div class="close" @click.stop="close">
      <el-icon><CloseBold /></el-icon>
    </div>
    <el-input
      v-model="searchText"
      placeholder="请输入功能名称"
      style="height: 32px; width: 676px"
    >
      <template #prepend>
        <el-button :icon="Search" />
      </template>
    </el-input>
    <div class="menuContainer">
      <div
        class="childMenuContainer"
        v-for="(moduleSplitItem, index) in moduleMenus"
        :key="index"
      >
        <div
          class="moduleItem"
          v-for="moduleItem in moduleSplitItem"
          v-hasPermission="moduleItem.requiredPermissions"
          :key="moduleItem.name"
        >
          <div class="moduleTitle">
            <span>{{ moduleItem.name }}</span>
          </div>
          <div
            class="menuItem"
            v-for="menuItem in moduleItem.childrenMenu"
            v-hasPermission="menuItem.requiredPermissions"
            :key="menuItem.name"
          >
            <div class="title" @click="jumpMenu(menuItem)">
              <span>{{ menuItem.title }}</span>
            </div>
            <div style="flex: auto"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scope>
.collectMenu {
  box-sizing: border-box;
  height: 100%;
  width: 100%;
  flex-wrap: wrap;
  background-color: var(--ce-collect-menu-bg-color);
  color: var(--ce-collect-menu-color);
  padding: 24px 32px 24px 32px;

  &:hover {
    overflow-y: auto;
  }
  .close {
    position: absolute;
    right: 20px;
    top: 20px;
    color: #646a73;
    cursor: pointer;
  }

  .menuContainer {
    width: 100%;
    display: flex;
    justify-content: space-between;
    margin-top: -12px;
    .childMenuContainer {
      width: 225px;
      display: flex;
      flex-wrap: wrap;
      .moduleItem {
        margin-top: 36px;
        width: 100%;
        .moduleTitle {
          font-weight: 500;
          font-size: 14px;
          line-height: 24px;
          padding-left: 12px;
        }
        .menuItem {
          font-weight: 400;
          margin-top: 8px;
          color: rgba(100, 106, 115, 1);
          height: 32px;
          display: flex;
          align-items: center;
          align-content: center;
          .title {
            align-content: center;
            padding-left: 12px;
            cursor: pointer;
            height: 100%;
            overflow: hidden;
            &:hover {
              background-color: rgba(239, 240, 241, 1);
              color: rgba(31, 35, 41, 1);
              border-radius: 4px;
            }
          }
        }
      }
    }
  }
}
</style>
