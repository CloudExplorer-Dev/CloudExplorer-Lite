<script setup lang="ts">
import StartMenuItem from "./StartMenuItem.vue";
import CeIcon from "@commons/components/ce-icon/index.vue";
import { onMounted, ref } from "vue";
import { useUserStore } from "@commons/stores/modules/user";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useRouter } from "vue-router";
import CollectMenu from "@commons/components/layout/collect-menu/index.vue";
import type { Module } from "@commons/api/module/type";

const router = useRouter();

const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();
const userStore = useUserStore();

const currentModule = ref(import.meta.env.VITE_APP_NAME);

//const currentRole = ref<Role | any>({});

onMounted(() => {
  /*module.getCurrentRole().then((data: Role) => {
    currentRole.value = data;
  });*/
  //service.init();
});

const collapse = ref<boolean>(false);

const root = ref<Module>({
  id: "",
  name: "服务列表",
  icon: "caidan2",
  order: 0,
});
const hoverHandler = () => {
  collapse.value = true;
};
const test = (moduleName: string) => {
  router.push(moduleName);
};
</script>
<template>
  <div class="startMenu">
    <div class="serviceList">
      <StartMenuItem
        :start-menu-item="root"
        :hover-handler="hoverHandler"
        :leave-handler="() => (collapse = false)"
        :rootItem="true"
      >
        <CollectMenu
          :collapse="collapse"
          :runningModules="moduleStore.runningModules"
          :runningModuleMenus="moduleStore.runningMenus"
          :runningModulePermissions="permissionStore.userPermissions"
          :currentRole="userStore.currentRole"
        />
      </StartMenuItem>
    </div>
    <div class="starMenuLine"></div>
    <div class="runningModule">
      <StartMenuItem
        v-for="item in moduleStore.runningModules"
        :key="item.id"
        @click="test(item.id)"
        :start-menu-item="item"
        :root-item="false"
        v-hasPermission="item.requiredPermissions"
        :class="item.id === currentModule ? 'active' : ''"
      >
        <div class="move">
          <CeIcon
            size="var(--ce-star-menu-icon-width,18px)"
            code="yidongshu"
          ></CeIcon>
        </div>
      </StartMenuItem>
    </div>
  </div>
</template>

<style lang="scss">
.startMenu {
  height: 100%;
  width: var(--ce-star-menu-width);
  background-color: var(--ce-star-menu-bg-color);
  transition: 0.5s;
  border-right: 1px solid var(--ce-star-menu-border-color);
  &:hover {
    overflow: auto;
    position: fixed;
    width: var(--ce-star-menu-hover-width);
    .item {
      .icon {
        width: 30%;
        height: var(--ce-star-menu-item-icon-size);
        .el-icon {
          width: 100%;
        }
      }
      .text {
        text-overflow: ellipsis;
        width: 50%;
        display: block;
      }
      .handle {
        width: 20%;
        display: flex;
        .el-icon {
          width: 100%;
        }
      }
    }
    .startModuleMenu {
      height: 100%;
      width: 100%;
    }
  }

  .active {
    background-color: var(--ce-star-menu-active-bg-color);
    .icon {
      .el-icon {
        color: var(--ce-star-menu-active-icon-color);
      }
    }
  }
  .starMenuLine {
    margin-bottom: 8px;
    height: 1px;
    background-color: var(--ce-star-menu-active-bg-color);
  }
  .starMenuLineMenu {
    margin-top: 8px;
    height: 1px;
    background-color: var(--ce-star-menu-active-bg-color);
  }
}
</style>
