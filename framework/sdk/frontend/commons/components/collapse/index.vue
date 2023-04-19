<script setup lang="ts">
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
        :type="'code'"
        size="var(--ce-menu-collapse-font-size, 9px)"
        color="rgba(31, 35, 41, 1)"
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
    height: var(--ce-menu-collapse-height, 60px);
    width: var(--ce-menu-collapse-width, 16px);
    display: flex;
    align-items: center;
    justify-content: center;
    position: absolute;
    background-color: var(--ce-menu-collapse-bg-color, #fff);
    border-bottom-right-radius: var(
      --ce-menu-collapse-border-bottom-right-radius,
      4px
    );
    font-size: var(--ce-menu-collapse-font-size, 9px);
    border-top-right-radius: var(
      --ce-menu-collapse-border-top-right-radius,
      4px
    );
    transition: 0.5s;
    cursor: pointer;
    &:hover {
      opacity: 0.8;
    }
  }
}
</style>
