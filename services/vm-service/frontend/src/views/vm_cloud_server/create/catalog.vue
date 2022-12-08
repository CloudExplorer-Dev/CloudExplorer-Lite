<template>
  <div class="catalog-container" v-loading="loading">
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
        <div class="main-content">
          <component
            style="width: 100px; height: 100px"
            :is="platformIcon[good.platform]?.component"
            v-bind="platformIcon[good.platform]?.icon"
            :color="platformIcon[good.platform]?.color"
            size="100px"
          ></component>
          <div>
            <div class="right-content">
              <div class="balance-content">
                <h2 style="color: #fa022e">{{ good.balance }}</h2>
                <h4>&nbsp;元</h4>
              </div>
              <h3>账户余额</h3>
            </div>
          </div>
        </div>
        <div class="resource-content">
          <div style="font-weight: bold; margin-bottom: 6px">我的资源</div>
          <div class="resource-main">
            <div class="resource-item">
              <ce-icon class="resource-icon" code="xuniji1" />
              <div class="resource-text">
                <div>云主机</div>
                <div class="count">{{ good.serverCount }}台</div>
              </div>
            </div>
            <div class="resource-item">
              <ce-icon class="resource-icon" code="yuncunchu" />
              <div class="resource-text">
                <div>磁盘</div>
                <div class="count">{{ good.diskCount }}块</div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <el-button
        class="footer el-button--primary"
        @click="openCreatePage(good)"
      >
        立即创建
      </el-button>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import CatalogApi from "@/api/catalog";
import { platformIcon } from "@commons/utils/platform";

import { onMounted, type Ref, ref } from "vue";
import { useRouter } from "vue-router";

import type { Good } from "@/api/catalog/type";

const loading: Ref<boolean> | undefined = ref<boolean>(false);
const useRoute = useRouter();

const goods = ref<Array<Good>>([]);

onMounted(() => {
  CatalogApi.getGoods(loading).then((result) => {
    goods.value = result.data;
  });
});

function openCreatePage(good: Good) {
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
    height: 199px;
    padding: 30px;
    display: flex;
    flex-direction: column;
    flex-wrap: nowrap;
    justify-content: space-between;

    .main-content {
      width: 100%;
      display: flex;
      flex-direction: row;
      flex-wrap: nowrap;
      justify-content: space-between;
      align-items: center;

      .right-content {
        display: flex;
        flex-direction: column;
        flex-wrap: nowrap;
        align-items: flex-end;
      }
      .balance-content {
        display: flex;
        flex-direction: row;
        align-items: baseline;
        flex-wrap: nowrap;
      }
    }
  }
  .resource-content {
    width: 100%;
    .resource-main {
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      flex-wrap: wrap;
      align-items: center;
      .resource-item {
        display: flex;
        .resource-icon {
          height: 48px;
          width: 48px;
          background-color: #b7b7b7;
        }

        .resource-text {
          height: 48px;
          width: 60px;
          font-weight: bolder;
          display: flex;
          flex-direction: column;
          flex-wrap: nowrap;
          align-items: center;
          justify-content: space-evenly;

          .count {
            color: var(--el-color-primary);
          }
        }
      }
    }
  }
  .footer {
    height: 50px;
    width: 100%;
    border-radius: 0;
  }
}
</style>
