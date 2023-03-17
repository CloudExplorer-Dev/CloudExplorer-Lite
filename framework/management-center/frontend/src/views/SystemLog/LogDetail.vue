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
.detail-class {
  height: 300px;
  overflow: auto;
}
</style>

<template>
  <el-dialog v-model="dialogVisible" title="详情" width="50%" destroy-on-close class="custom-dialog"
             style="min-width: 600px">
    <el-form :model="logInfo">
      <el-form-item label="详情:">
        <label class="detail-class">{{ logInfo.message }}</label>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer footer-btn">
        <el-button @click="dialogVisible = false">关闭</el-button>
      </span>
    </template>
  </el-dialog>
</template>
