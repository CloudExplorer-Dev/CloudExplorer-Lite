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
.content-class {
  border: 0 !important;
  background-color: transparent !important;
  width: 90%;
}
</style>

<template>
  <el-dialog
    v-model="dialogVisible"
    title="API详情"
    width="50%"
    destroy-on-close
  >
    <el-descriptions :column="1" border size="small">
      <el-descriptions-item
        label-class-name="label-class"
        class-name="content-class"
        label="操作人"
        colon="true"
        min-width="55px"
      >
        {{ logInfo.user }}
      </el-descriptions-item>
      <el-descriptions-item
        label-class-name="label-class"
        class-name="content-class"
        label="时间"
        colon="true"
      >
        {{ logInfo.date }}
      </el-descriptions-item>
      <el-descriptions-item
        label-class-name="label-class"
        class-name="content-class"
        label="描述"
        colon="true"
      >
        [{{ logInfo.operatedName }}]{{ logInfo.content }}
      </el-descriptions-item>
      <el-descriptions-item
        label-class-name="label-class"
        class-name="content-class"
        label="相关资源ID/名称"
        colon="true"
      >
        {{ logInfo.resourceId }}
      </el-descriptions-item>
      <el-descriptions-item
        label-class-name="label-class"
        class-name="content-class"
        label="请求IP"
        colon="true"
      >
        {{ logInfo.sourceIp }}
      </el-descriptions-item>
      <el-descriptions-item
        label-class-name="label-class"
        class-name="content-class"
        label="请求地址"
        colon="true"
      >
        {{ logInfo.url }}
      </el-descriptions-item>
      <el-descriptions-item
        label-class-name="label-class"
        class-name="content-class"
        label="请求方式"
        colon="true"
      >
        {{ logInfo.method }}
      </el-descriptions-item>
      <el-descriptions-item
        label-class-name="label-class"
        class-name="content-class"
        label="请求参数"
        colon="true"
        min-width="115px"
      >
        <div
          :style="{
            maxHeight: '100px',

            overflowX: 'hidden',

            overflowY: 'auto',
          }"
        >
          {{ logInfo.params }}
        </div>
      </el-descriptions-item>
      <el-descriptions-item
        label-class-name="label-class"
        class-name="content-class"
        label="状态码"
        colon="true"
      >
        {{ logInfo.code }}
      </el-descriptions-item>
    </el-descriptions>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
      </span>
    </template>
  </el-dialog>
</template>
