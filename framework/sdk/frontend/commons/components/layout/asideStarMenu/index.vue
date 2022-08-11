<script setup lang="ts">
import StarItem from "./starMenuItem.vue";
import type { StarMenuItem } from "./type";
import CeIcon from "../../ce-icon/index.vue";
import { ref, onMounted } from "vue";
import { Module } from "../../../api/module/type";
import { moduleStore } from "../../../stores/module";
import collectMenu from "../collectMenu/index.vue";
const { getRuningModule } = moduleStore();
const modules = ref<Array<Module>>();
onMounted(() => {
  getRuningModule().then((data: Array<Module>) => {
    modules.value = data;
  });
});

const collapse = ref<boolean>(false);
const root = ref<StarMenuItem>({
  title: "服务列表",
  icon: "yingyongzhongxin",
  order: 0,
});
</script>
<template>
  <div class="startMenu">
    <div class="serviceList">
      <StarItem
        :starMenuItem="root"
        :hoverHander="() => (collapse = true)"
        :leave-hander="() => (collapse = false)"
        :rootItem="true"
      >
        <collectMenu :collapse="collapse" />
      </StarItem>
    </div>
    <div class="starMenuLine"></div>
    <div class="starModuleMenu">
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
  </div>
</template>

<style lang="scss">
.startMenu {
  height: 100%;
  width: var(--ce-star-menu-width);
  background-color: var(--ce-star-menu-bg-color);
  transition: 0.5s;
  z-index: 10;
  border-right: 1px solid var(--ce-star-menu-border-color);
  &:hover {
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
