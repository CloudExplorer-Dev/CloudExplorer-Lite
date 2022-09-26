<script setup lang="ts">
import { ref } from "vue";
import { useI18n } from "vue-i18n";
const { t } = useI18n();
const dialogVisible = ref(false);
const logInfo = ref();
defineExpose({
  dialogVisible,
  logInfo,
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
    title="API详情"
    width="50%"
    destroy-on-close
  >
    <el-form :inline="true" :model="logInfo" class="demo-form-inline">
      <el-form-item label="操作人:">
        <label>{{ logInfo.user }}</label>
      </el-form-item>
      <el-form-item label="操作时间:">
        <label>{{ logInfo.date }}</label>
      </el-form-item>
      <el-form-item label="操作对象:">
        <label>{{ logInfo.resourceId }}</label>
      </el-form-item>
      <el-form-item label="操作类型:">
        <label>{{ logInfo.resourceType }}</label>
      </el-form-item>
      <el-form-item label="API详情:">
        <label class="detail-class">{{ logInfo.msg }}</label>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
      </span>
    </template>
  </el-dialog>
</template>
