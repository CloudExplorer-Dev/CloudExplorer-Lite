<script lang="ts" setup>
import { useRouter } from "vue-router";
import { get } from "@commons/request";
import CurrencyFormat from "@commons/utils/currencyFormat";
import { onMounted, ref } from "vue";
import Result from "@commons/request/Result";
import MicroAppRouterUtil from "@commons/router/MicroAppRouterUtil";

const router = useRouter();

const props = defineProps<{
  name: string;
  module: string;
  redirect: string;
  func: string;
  unit?: string;
  type?: "currency" | undefined;
}>();

const loading = ref<boolean>(false);
const count = ref<number>(0);

function getCount(func: string): Promise<Result<number>> {
  return get(func, null, loading);
}

function jump() {
  //router.push(props.redirect);
  MicroAppRouterUtil.jumpToChildrenPath(props.module, props.redirect, router);
}

onMounted(() => {
  getCount(props.func).then((result) => {
    count.value = result.data;
  });
});
</script>
<template>
  <div class="base-div" @click="jump" v-loading="loading">
    <div class="label">
      {{ name }}
    </div>
    <div class="value">
      <span v-if="type === 'currency'">{{ CurrencyFormat.format(count) }}</span>
      <span v-else>{{ count }}</span>
      {{ unit }}
    </div>
  </div>
</template>

<style scoped lang="scss">
.base-div {
  cursor: pointer;
  padding: 8px;
  border-radius: 4px;

  .label {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    line-height: 22px;
    color: #646a73;
  }
  .value {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    font-style: normal;
    font-weight: 500;
    font-size: 20px;
    line-height: 28px;
    color: #1f2329;
    width: fit-content;
    min-width: 28px;
  }
}
.base-div:hover {
  background: rgba(31, 35, 41, 0.1);
}
</style>
