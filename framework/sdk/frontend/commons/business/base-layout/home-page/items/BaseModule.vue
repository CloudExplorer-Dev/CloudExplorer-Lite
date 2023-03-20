<script lang="ts" setup>
import { useRouter } from "vue-router";
import { get } from "@commons/request";
import { onMounted, ref } from "vue";
import Result from "@commons/request/Result";

const router = useRouter();

const props = defineProps<{
  name: string;
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
  router.push(props.redirect);
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
      <span v-if="type === 'currency'">{{
        count?.toLocaleString("zh-CN", {
          style: "currency",
          currency: "CNY",
          minimumFractionDigits: 2,
          maximumFractionDigits: 2,
        })
      }}</span>
      <span v-else>{{ count }}</span>
      {{ unit }}
    </div>
  </div>
</template>

<style scoped lang="scss">
.base-div {
  cursor: pointer;
  padding: 8px;

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
