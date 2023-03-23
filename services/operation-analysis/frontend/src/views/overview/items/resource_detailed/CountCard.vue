<script lang="ts" setup>
import { get } from "@commons/request";
import { onMounted, watch, ref } from "vue";
import Result from "@commons/request/Result";

const props = defineProps<{
  name: string;
  func: string;
  cloudAccountId?: string | undefined;
}>();

const loading = ref<boolean>(false);
const count = ref<number>(0);

function getCount(
  func: string,
  params: string | undefined
): Promise<Result<number>> {
  return get(func, { cloudAccountId: params }, loading);
}

watch(
  props,
  (prop) => {
    getCount(
      prop.func,
      prop.cloudAccountId != "all" ? prop.cloudAccountId : ""
    ).then((result) => {
      count.value = result.data;
    });
  },
  { immediate: true }
);
</script>
<template>
  <el-card shadow="never" v-loading="loading" class="card" style="height: 86px">
    <div class="title">{{ name }}</div>
    <div class="value">
      <span>{{ count }}</span>
    </div>
  </el-card>
</template>

<style scoped lang="scss">
.card {
  .title {
    width: 42px;
    height: 22px;
    left: 40px;
    top: 80px;
    font-family: "PingFang SC";
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    line-height: 22px;
    color: #646a73;
  }
  .value {
    left: 3.68%;
    right: 95.22%;
    top: 8.13%;
    bottom: 89.72%;
    font-family: "PingFang SC";
    font-style: normal;
    font-weight: 500;
    font-size: 20px;
    line-height: 28px;
    display: flex;
    align-items: center;
    letter-spacing: -0.01em;
    color: #1f2329;
  }
}
</style>
