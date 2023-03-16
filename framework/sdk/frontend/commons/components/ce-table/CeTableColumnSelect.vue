<script setup lang="ts">
const props = defineProps<{ columns: [] }>();
const gridColumnSelectTitle = "请选择列表中要展示的信息";
</script>

<template>
  <fu-table-column-select
    type="icon"
    :columns="columns"
    :title="gridColumnSelectTitle"
    popper-class="popper-class"
    :show-arrow="true"
    placement="bottom-end"
    trigger="hover"
  />
</template>

<style lang="scss">
.popper-class {
  width: 240px !important;
  max-height: 400px;
  padding-right: 0px !important;
  padding-left: 11px !important;
  translatey: 0px !important;
  H3 {
    font-family: PingFang SC;
    font-style: normal;
    font-size: 14px;
    font-weight: 400;
    line-height: 22px;
    display: flex;
    align-items: center;
    height: 22px;
    width: 216px;
    margin: 0px;
    color: #8f959e;
  }
  .fu-table-column-select-popper__body {
    max-height: 350px;
  }
  .fu-table-column-select-popper__item {
    margin-top: 0px;
  }
}
</style>
