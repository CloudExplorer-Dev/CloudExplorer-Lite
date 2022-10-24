<template>
  <div class="catalog-container">
    <el-card
      :body-style="{
        padding: 0,
      }"
      class="catalog-container-card"
      v-for="good in goods"
      :key="good.id"
    >
      <div class="header">
        <span class="title">
          <h3>{{ good.name }}</h3>
        </span>
      </div>
      <div class="content">
        <el-image style="width: 40px" :src="platformIcon[good.platform].icon" />
      </div>
      <el-button class="footer el-button--primary" @click="openCreatePage(good)"
        >立即创建</el-button
      >
    </el-card>
  </div>
</template>

<script setup lang="ts">
import BaseCloudAccountApi from "@commons/api/cloud_account";
import { platformIcon } from "@commons/utils/platform";
import { type CloudAccount } from "@commons/api/cloud_account/type";
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";

const useRoute = useRouter();

const goods = ref<Array<CloudAccount>>([]);

onMounted(() => {
  BaseCloudAccountApi.listAll().then((result) => (goods.value = result.data));
});

function openCreatePage(good: CloudAccount) {
  console.log(good);
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace(
      "/catalog",
      `/create/${good.id}`
    ),
  });
}
</script>

<style lang="scss" scoped>
.catalog-container {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
}
.catalog-container-card {
  width: 300px;
  height: 360px;
  margin-bottom: 20px;

  .header {
    width: 100%;
    height: 50px;
    display: flex;
    align-items: center;
    font-size: 15px;
    font-weight: normal;
    border-bottom: 1px solid var(--el-card-border-color);

    .title {
      width: 100%;
      font-weight: bold;
      text-align: center;
      color: var(--el-text-color-regular);
    }
  }
  .content {
    height: 227px;
    padding: 16px;
  }
  .footer {
    height: 50px;
    width: 100%;
    border-radius: 0;
  }
}
</style>
