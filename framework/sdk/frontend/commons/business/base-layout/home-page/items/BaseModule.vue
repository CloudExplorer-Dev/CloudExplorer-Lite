<script lang="ts" setup>
import CeIcon from "@commons/components/ce-icon/index.vue";
import { useRouter } from "vue-router";
import { get } from "@commons/request";
import { onMounted, ref } from "vue";
import Result from "@commons/request/Result";

const router = useRouter();

const props = defineProps<{
  icon: string;
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
  <el-card v-loading="loading">
    <div
      style="
        display: flex;
        flex-direction: row;
        flex-wrap: nowrap;
        justify-content: space-around;
        align-items: center;
      "
    >
      <CeIcon :code="icon" size="40px" style="height: 46px; width: 46px" />
      <div
        style="
          display: flex;
          flex-direction: column;
          flex-wrap: nowrap;
          align-items: center;
        "
      >
        <div style="font-weight: bold; padding: 5px">{{ name }}</div>
        <div
          style="
            font-weight: bold;
            padding: 5px;
            cursor: pointer;
            color: var(--el-color-primary);
          "
          @click="jump"
        >
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
    </div>
  </el-card>
</template>

<style scoped lang="scss"></style>
