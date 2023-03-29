<template>
  <div class="colors">
    <div
      v-for="(item, index) in dataList"
      :key="index"
      class="item"
      :style="{
        width:
          total === 0
            ? 100 / dataList.length + '%'
            : (item.num / total) * 100 + '%',
        backgroundColor: item.color,
      }"
    ></div>
  </div>
</template>
<script setup lang="ts">
import { computed } from "vue";

const props = withDefaults(
  defineProps<{
    dataList: Array<{
      /**
       * 数据
       */
      num: number;
      /**
       * 颜色
       */
      color: string;
    }>;
  }>(),
  {
    dataList: () => [],
  }
);

const total = computed(() => {
  return props.dataList
    .map((item) => item.num)
    .reduce((pre, next) => pre + next, 0);
});
</script>
<style lang="scss">
.colors {
  display: flex;
  height: 8px;

  .item {
    margin: 0 1px;
    &:first-child {
      margin-left: 0px;
      border-top-left-radius: 8px;
      border-bottom-left-radius: 8px;
    }
    &:last-child {
      border-top-right-radius: 8px;
      border-bottom-right-radius: 8px;
      margin-right: 0px;
    }
  }
}
</style>
