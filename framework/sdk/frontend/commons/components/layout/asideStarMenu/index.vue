<script setup lang="ts">
import StarItem from "./starMenuItem.vue";
import type { StarMenuItem } from "./type";
import CeIcon from "../../ce-icon/index.vue";
import { ref, onMounted } from "vue";
import { listRuningModules } from "../../../api/module";
import { Module } from "@/api/module/type";
const loading = ref(true);
onMounted(() => {
  listRuningModules(loading).then((data) => {
    modules.value = data.data;
  });
});
const modules = ref<Array<Module>>();
const hoverHander = () => {
  console.log("sx");
};
const root = ref<StarMenuItem>({
  title: "服务列表",
  icon: "yingyongzhongxin",
  order: 0,
});
</script>
<template>
  <div class="startMenu" v-loading="loading">
    <StarItem :starMenuItem="root" :hoverHander="hoverHander" :rootItem="true">
    </StarItem>
    <div class="starMenuLine"></div>
    <StarItem
      :star-menu-item="item"
      :root-item="false"
      v-for="item in modules"
      :key="item.name"
    >
      <div class="move">
        <CeIcon code="yidongshu"></CeIcon>
      </div>
    </StarItem>
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
    position: fixed;
    z-index: 1000;
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
    height: 1px;
    background-color: black;
  }
}
</style>
