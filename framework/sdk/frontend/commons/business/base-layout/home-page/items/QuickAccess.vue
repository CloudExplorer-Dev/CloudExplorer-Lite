<script lang="ts" setup>
import { useUserStore } from "@commons/stores/modules/user";

const userStore = useUserStore();
</script>
<template>
  <el-card class="info-card">
    <div style="font-weight: bold; font-size: 16px; padding-bottom: 26px">
      快捷服务
    </div>
    xxxx
  </el-card>
</template>

<style scoped lang="scss">
.info-card {
  /*min-width: 340px;*/
  min-height: 160px;
}
</style>
