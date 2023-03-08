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
<style scoped lang="scss">
.detail-class {
  height: 300px;
  overflow: auto;
}
.content-class {
  border: 0 !important;
  background-color: transparent !important;
  width: 90%;
}
.el-descriptions__body
  .el-descriptions__table.is-bordered
  .el-descriptions__cell {
  width: 100% !important;
}

.el-descriptions__body .el-descriptions__table .el-descriptions__cell {
  font-size: 12px !important;
  width: 100% !important;
}
</style>

<template>
  <el-dialog
    v-model="dialogVisible"
    title="API详情"
    width="50%"
    destroy-on-close
  >
    <el-descriptions
      :column="1"
      border
      size="small"
      style="word-break: break-all; word-wrap: break-word"
    >
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
        label="操作对象ID/名称"
        colon="true"
      >
        {{ logInfo.resourceName }}
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
      <el-descriptions-item
          v-if="logInfo.code !== 200"
          label-class-name="label-class"
          class-name="content-class"
          label="失败原因"
          colon="true"
      >
        {{ logInfo.response }}
      </el-descriptions-item>
    </el-descriptions>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
      </span>
    </template>
  </el-dialog>
</template>
