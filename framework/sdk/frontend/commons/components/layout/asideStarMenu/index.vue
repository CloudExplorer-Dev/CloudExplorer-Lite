<script setup lang="ts">
import { storeToRefs } from "pinia";
import StarItem from "./starMenuItem.vue";
import type { StarMenuItem } from "./type";
import CeIcon from "../../ce-icon/index.vue";
import { ref, onMounted } from "vue";
import { Role } from "../../../api/role";
import { servicesStore } from "../../../stores/services";
import { moduleStore } from "../../../stores/module";
import collectMenu from "../collectMenu/index.vue";
import { useRouter } from "vue-router";
const router = useRouter();
const service = servicesStore();
const module = moduleStore();
const { runingModules, runingModuleMenus, runingModulePermissions } =
  storeToRefs(service);
const { currentModule } = storeToRefs(module);
const currentRole = ref<Role | any>({});

onMounted(() => {
  module.getCurrentRole().then((data) => {
    currentRole.value = data;
  });
  service.init();
});

const collapse = ref<boolean>(false);
const root = ref<StarMenuItem>({
  title: "服务列表",
  icon: "caidan2",
  order: 0,
});
const hoverHander = () => {
  collapse.value = true;
};
const test = (moduleName: string) => {
  router.push(moduleName);
};
</script>
<template>
  <div class="startMenu">
    <div class="serviceList">
      <StarItem
        :starMenuItem="root"
        :hoverHander="hoverHander"
        :leave-hander="() => (collapse = false)"
        :rootItem="true"
      >
        <collectMenu
          :collapse="collapse"
          :runingModules="runingModules"
          :runingModuleMenus="runingModuleMenus"
          :runingModulePermissions="runingModulePermissions"
          :currentRole="currentRole"
        />
      </StarItem>
    </div>
    <div class="starMenuLine"></div>
    <div class="runingModule">
      <StarItem
        @click="test(item.name)"
        :star-menu-item="item"
        :root-item="false"
        v-for="item in runingModules"
        :key="item.name"
        :class="item.name === currentModule.name ? 'active' : ''"
      >
        <div class="move">
          <CeIcon size="20px" code="yidongshu"></CeIcon>
        </div>
      </StarItem>
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
    margin-top: 8px 0;
    height: 1px;
    background-color: var(--ce-star-menu-active-bg-color);
  }
}
</style>
