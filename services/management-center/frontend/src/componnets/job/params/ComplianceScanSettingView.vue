<template>
  <layout-container :border="border">
    <template #header> <h4>扫描内容设置</h4></template>
    <template #content>
      <el-form :model="from" ref="ruleFormRef">
        <el-form-item
          :rules="{
            message: '扫描内容必须选择一个',
            trigger: 'change',
            required: true,
            type: 'array',
            min: 1,
          }"
          prop="scanContentList"
        >
          <el-checkbox-group v-model="from.scanContentList">
            <el-checkbox
              v-for="item in scanContents"
              :key="item.value"
              :label="item.value"
              >{{ item.key }}</el-checkbox
            ></el-checkbox-group
          ></el-form-item
        >
      </el-form>
    </template>
  </layout-container>
</template>
<script setup lang="ts">
import type { JobDetails } from "@/api/cloud_account/type";
import { ref, watch } from "vue";
import type { FormInstance } from "element-plus";
const props = defineProps<{
  /**
   *定时任务信息
   */
  jobDetails: Array<JobDetails>;
  /**
   * 是否可读
   */
  readOnly: boolean;
  /**
   * 是有有boder
   */
  border: boolean;
}>();

/**
 * 扫描内容
 */
const scanContents = [
  { key: "合规扫描", value: "complianceScan" },
  //   { key: "漏洞扫描", value: "leakHole" },
];
const from = ref<{ scanContentList: Array<string> }>({
  // 选择的扫描内容
  scanContentList: ["complianceScan"],
});
const checkScanContent = ref<Array<string>>([]);

const echoData = () => {
  if (props.jobDetails.length > 0) {
    if (props.jobDetails[0].params["complianceScanContent"]) {
      checkScanContent.value =
        props.jobDetails[0].params["complianceScanContent"];
    }
  }
};

watch(
  () => props.jobDetails,
  () => {
    if (props.jobDetails.length > 0) {
      echoData();
    }
  },
  {
    immediate: true,
  }
);

watch(
  () => from.value.scanContentList,
  () => {
    console.log("xxxx");
    props.jobDetails.forEach((job) => {
      job.params["complianceScanContent"] = from.value.scanContentList;
    });
  },
  {
    immediate: true,
  }
);
/**
 * 校验实例对象
 */
const ruleFormRef = ref<FormInstance>();
const validate = () => {
  return ruleFormRef.value?.validate();
};
defineExpose({ validate });
</script>
<style lang="scss"></style>
