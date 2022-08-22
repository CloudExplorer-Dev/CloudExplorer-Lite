<script setup lang="ts">
import { defineProps, onMounted, ref, watch } from "vue";
import { ModuleMenu, Menu } from "../../../api/menu";
import { Module } from "../../../api/module";
import { flatMenu, hasRolePermission } from "../../../router";
import { Role } from "../../../api/role";
import type { ModulePermission, Permission } from "../../../api/permission";
import { Search } from "@element-plus/icons-vue";
import CeIcon from "../../ce-icon/index.vue";
interface ModuleContainer extends Module {
  /**
   * 子菜单
   */
  childrenMenu: Array<Menu>;
  /**
   *权限
   */
  permissions: Array<Permission>;
}

const props = defineProps<{
  // 是否展开
  collapse: boolean;
  // 运行中的模块菜单
  runingModuleMenus: ModuleMenu;
  // 运行中的所有模块
  runingModules: Array<Module>;
  // 当前角色
  currentRole: Role;
  // 运行模块的权限
  runingModulePermissions: ModulePermission;
}>();
// 搜索内容
const searchText = ref<string>("");
// 模块菜单信息
const moduleMenus = ref<Array<Array<ModuleContainer>>>([[], [], []]);

onMounted(() => {
  moduleMenus.value = resetMenus();
});

/**
 * 将数据重新组合为可用数据
 */
const resetMenus = () => {
  const moduleContainers: Array<ModuleContainer> = flatMapMenus(
    props.runingModuleMenus,
    props.runingModules,
    props.runingModulePermissions
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
 * @param runingModuleMenus       运行中模块菜单
 * @param runingModules           运行中的模块
 * @param runingModulePermissions 运行中模块的权限
 */
const flatMapMenus = (
  runingModuleMenus: ModuleMenu,
  runingModules: Array<Module>,
  runingModulePermissions: ModulePermission
) => {
  return runingModules.map((module) => {
    const newMenu = flatMenu(runingModuleMenus[module.name], [], "");
    const moduleContainer: ModuleContainer = {
      ...module,
      childrenMenu: newMenu,
      permissions: runingModulePermissions[module.name]
        ? runingModulePermissions[module.name]
        : [],
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
  currentRole: Role,
  permissions: Array<Permission>
) => {
  if (!hasRolePermission(menu, currentRole.id, permissions)) {
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
  searchText: string,
  services: Array<ModuleContainer>,
  currentRole: Role
) => {
  return services.filter((service) => {
    service.childrenMenu = service.childrenMenu.filter((serviceMenu) => {
      return hasShowMenu(
        searchText,
        serviceMenu,
        currentRole,
        service.permissions
      );
    });
    return service.childrenMenu.length > 0;
  });
};
/**
 * 搜索框改变事件
 */
const searchChange = () => {
  moduleMenus.value = resetMenus();
};

/**
 * 监控打开状态
 */
watch(
  () => props.collapse,
  (newValue: boolean) => {
    if (newValue) {
      moduleMenus.value = resetMenus();
      console.log(moduleMenus.value);
    }
  }
);
</script>

<template>
  <transition name="collectMenu">
    <div class="collectMenu" v-show="collapse">
      <div class="search">
        <el-input
          v-model="searchText"
          placeholder="请输入需要查询的内容"
          :suffix-icon="Search"
          @input="searchChange"
        />
      </div>
      <div class="menuContainer">
        <div
          class="childMenuContainer"
          v-for="(moduleSplitItem, index) in moduleMenus"
          :key="index"
        >
          <div
            class="moduleItem"
            v-for="moduleItem in moduleSplitItem"
            :key="moduleItem.name"
          >
            <div class="moduleTitle">
              <span>{{ moduleItem.title }}</span>
            </div>
            <div
              class="menuItem"
              v-for="menuItem in moduleItem.childrenMenu"
              :key="menuItem.name"
            >
              <div class="icon">
                <CeIcon size="15px" :code="menuItem.icon"></CeIcon>
              </div>
              <div class="title">
                <span>{{ menuItem.title }}</span>
              </div>
              <div style="flex: auto"></div>
              <div class="star">
                <CeIcon size="15px" :code="'shoucang'"></CeIcon>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<style lang="scss">
.collectMenu {
  height: 100%;
  width: 750px;
  flex-wrap: wrap;
  overflow-y: hidden;
  overflow-x: hidden;
  position: fixed;
  cursor: default;
  top: var(--ce-header-height);
  left: var(--ce-star-menu-hover-width);
  background-color: var(--ce-collect-menu-bg-color);
  color: var(--ce-collect-menu-color);
  z-index: 10000;
  .search {
    height: 10%;
    width: 750px;
    display: flex;
    align-items: center;
    .el-input {
      --el-input-text-color: var(--ce-collect-menu-color);
      --el-input-bg-color: var(--ce-collect-menu-bg-color);
    }
    div {
      width: 80%;
      margin-left: 20px;
    }
  }
  &:hover {
    overflow-y: auto;
  }
  .menuContainer {
    width: 100%;
    display: flex;
    justify-content: space-around;
    .childMenuContainer {
      width: 200px;
      display: flex;
      flex-wrap: wrap;
      .moduleItem {
        width: 100%;
        margin: 10px 0 0 0;
        .moduleTitle {
          font-size: 20px;
          font-weight: 7000;
          margin-left: 10px;
        }
        .menuItem {
          height: 35px;
          display: flex;
          align-items: center;
          align-content: center;
          margin: 0px 0 0px 5px;
          .title {
            margin-left: 8px;
            cursor: pointer;
            height: 100%;
            overflow: hidden;
            &:hover {
              border-bottom: 1px solid;
            }
          }
        }
      }
    }
    margin-bottom: 10%;
  }
}

/** 动画进行时的class **/
.collectMenu-enter-active,
.collectMenu-leave-active {
  transition: 0.3s;
}

.collectMenu-enter-from {
  width: 0;
}

.collectMenu-leave-to {
  width: 0;
}
.collectMenu-enter-form {
  width: 100%;
  height: 100%;
}
.collectMenu-leave-form {
  width: 100%;
  height: 100%;
}
</style>
