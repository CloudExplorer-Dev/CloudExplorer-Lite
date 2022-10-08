<template>
  <layout-container :border="false">
    <template #content>
      <layout-container v-loading="loading">
        <template #header>
          <h4>基本信息</h4>
        </template>

        <template #content>

        </template>
      </layout-container>
      <layout-container >
        <el-button @click="useRoute.back">返回</el-button>
      </layout-container>
    </template>
  </layout-container>
</template>
<script setup lang="ts">
import {useRouter} from "vue-router";
import {ref} from "vue";
const loading = ref<boolean>(false);
const loadingPermission = ref<boolean>(false);
const useRoute = useRouter();
</script>
<style lang="scss">
.edit-button-container {
  text-align: center;
  line-height: 50px;
  align-items: center;
}

.permission-container {
  width: 100%;
  min-height: 100px;
  .module-selector {
  }
}
</style>
