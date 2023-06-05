<template>
  <el-main>
    <DetailFormTitle title="账单获取方式" style="margin-bottom: 16px" />
    <CeForm
      require-asterisk-position="right"
      label-position="top"
      :readOnly="readOnly"
      :formViewData="BillFrom"
      ref="ceform"
      :otherParams="cloudAccount"
      v-model="billData"
    ></CeForm>
  </el-main>
</template>
<script setup lang="ts">
import type { JobDetails } from "@/api/cloud_account/type";
import cloudAccountApi from "@/api/cloud_account";
import { ref, watch } from "vue";
import type { CloudAccount } from "@/api/cloud_account/type";
import type { FormView } from "@commons/components/ce-form/type";
import type { SimpleMap } from "@commons/api/base/type";
import DetailFormTitle from "@/componnets/DetailFormTitle.vue";
/**
 * 账单设置form表单
 */
const BillFrom = ref<Array<FormView>>([]);
const billData = ref<SimpleMap<any>>({});
/**
 * 表单对象
 */
const ceform = ref<any>(null);

const props = defineProps<{
  /**
   *定时任务信息
   */
  jobDetails: Array<JobDetails>;
  /**
   * 云账号id
   */
  cloudAccount?: CloudAccount;
  /**
   * 是否可读
   */
  readOnly: boolean;
  /**
   * 是有有boder
   */
  border?: boolean;
}>();

/**
 * 校验参数,并且赋值
 */
const validate = () => {
  props.jobDetails.forEach((item) => {
    item.params["BILL_SETTING"] = billData.value;
  });
  return ceform.value?.validate();
};

/**
 *初始化账单设置
 * @param cloud_account 云账号
 */
const initBillSetting = (cloud_account: CloudAccount) => {
  cloudAccountApi.getBillFormByPlatform(cloud_account.platform).then((ok) => {
    BillFrom.value = ok.data;
    if (
      props.jobDetails.length > 0 &&
      props.jobDetails[0].params["BILL_SETTING"]
    ) {
      console.log("赋值", props.jobDetails[0].params["BILL_SETTING"]);
      billData.value = props.jobDetails[0].params["BILL_SETTING"];
    }
  });
};

/**
 *监听云账号,如果有账号对象有值后 触发
 */
watch(
  () => props.jobDetails,
  () => {
    if (props.cloudAccount) {
      initBillSetting(props.cloudAccount);
    }
  },
  { immediate: true }
);
// 暴露获取当前表单数据函数
defineExpose({
  validate,
});
</script>
<style lang="scss"></style>
