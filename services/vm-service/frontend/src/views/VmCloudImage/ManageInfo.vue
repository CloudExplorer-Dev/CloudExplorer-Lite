<script setup lang="ts">
import { ref } from "vue";
import { useI18n } from "vue-i18n";
const { t } = useI18n();
const dialogVisible = ref(false);
const manageInfo = ref();
defineExpose({
  dialogVisible,
  manageInfo,
});
</script>
<style scoped>
.detail-class{
  height: 300px;
  overflow: auto;
}
</style>

<template>
  <el-dialog
    v-model="dialogVisible"
    title="设置管理信息"
    width="50%"
    destroy-on-close
  >
    <el-form :inline="true" :model="manageInfo" class="demo-form-inline">
      <el-form-item label="操作系统:">
        <el-input></el-input>
      </el-form-item>
      <el-form-item label="操作系统版本:">
        <el-input></el-input>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
      </span>
    </template>
  </el-dialog>
</template>
