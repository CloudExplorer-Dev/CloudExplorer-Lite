<script setup lang="ts">
import { onBeforeRouteUpdate, useRouter } from "vue-router";
import { onMounted, ref } from "vue";

const router = useRouter();
const showReturnBtnPath = "/user_tenant/user/detail";
const currentRoutePath = router.currentRoute.value.path;
const isShowReturnBtn = ref(false);

const handleReturn = () => {
  router.push({ name: "user" });
  isShowReturnBtn.value = false;
};

onMounted(() => {
  if (showReturnBtnPath === currentRoutePath) {
    isShowReturnBtn.value = true;
  }
});

onBeforeRouteUpdate((to) => {
  if (showReturnBtnPath === to.path) {
    isShowReturnBtn.value = true;
  }
});
</script>

<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
      <div style="flex: 1 1 auto"></div>
      <el-affix
        style="padding-top: 10px; padding-right: 60px"
        v-if="isShowReturnBtn"
        @click="handleReturn"
      >
        <el-button type="info"> {{ $t("commons.btn.return") }} </el-button>
      </el-affix>
    </template>
    <router-view></router-view>
  </layout-content>
</template>

<style lang="scss" scoped></style>
