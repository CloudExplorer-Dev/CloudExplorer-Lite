<script setup lang="ts">
import { defineProps } from "vue";
import CeIcon from "@commons/components/ce-icon/index.vue";

const props = defineProps({
  collapse: {
    type: Boolean,
    required: true,
  },
  changeCollapse: {
    type: Function,
    required: true,
  },
});
</script>
<template>
  <div class="collapseContainer">
    <div class="openBtn" @click="props.changeCollapse()">
      <CeIcon
        :type="'svg'"
        :code="collapse ? 'xiangzuojiantou' : 'xiangyoujiantou'"
      ></CeIcon>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.collapseContainer {
  z-index: 101;
  height: calc(100vh - var(--ce-header-height));
  display: flex;
  align-items: center;
  .openBtn {
    height: 100px;
    width: 10px;
    display: flex;
    align-items: center;
    position: absolute;
    background-color: var(--ce-menu-collapse-bg-color);
    border-bottom-right-radius: 4px;
    border-top-right-radius: 4px;
    transition: 0.5s;
    cursor: pointer;
    &:hover {
      opacity: 0.8;
    }
  }
}
</style>
