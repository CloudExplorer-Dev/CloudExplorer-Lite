<template>
  <el-card shadow="always" :body-style="{ padding: '0px', height: '150px' }">
    <div
      class="quantity_describe"
      :style="{
        color: color,
      }"
    >
      {{ format(count) }}
    </div>
    <div class="describe" :style="{ backgroundColor: color }">
      {{ describe }}
    </div></el-card
  >
</template>
<script setup lang="ts">
withDefaults(
  defineProps<{
    color: string;
    count: number;
    describe: string;
    format: (count: number) => string | number;
  }>(),
  {
    color: "#fffeff",
    format: (count: number) => count,
  }
);
</script>
<style lang="scss" scoped>
.quantity_describe {
  font-size: 26px;
  height: 100px;
  display: flex;
  justify-content: center;
  line-height: 100px;
}
.describe {
  height: 50px;
  width: 100%;
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
  display: flex;
  justify-content: center;
  line-height: 50px;
  font-size: 18px;
  color: #fffeff;
}
</style>
