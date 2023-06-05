<template>
  <el-descriptions direction="vertical" :column="3" v-loading="loading">
    <template v-for="(item, index) in form" :key="index">
      <el-descriptions-item width="33.33%" v-if="item.field === 'syncMode'">
        <template #label>
          <DetailFormLabel label="账单获取方式" />
        </template>
        <DetailFormValue>{{ filterValue(item) }}</DetailFormValue>
      </el-descriptions-item>
      <el-descriptions-item
        width="33.33%"
        v-if="item.field !== 'syncMode' && showRelation(item)"
      >
        <template #label>
          <DetailFormLabel :label="item.label" />
        </template>
        <DetailFormValue>{{ filterValue(item) }}</DetailFormValue>
      </el-descriptions-item>
    </template>
    <template v-for="(item, index) in jobDetails" :key="index">
      <el-descriptions-item width="33.33%">
        <template #label>
          <DetailFormLabel :label="item.description + '频率'" />
        </template>
        <DetailFormValue>
          <IntervalView
            v-if="item.jobType === 'INTERVAL'"
            :unit="item.unit"
            :interval="item.interval"
          />
          <CronView v-else :cron="item.cronExpression" />
        </DetailFormValue>
      </el-descriptions-item>
    </template>
  </el-descriptions>
</template>
<script setup lang="ts">
import type { JobDetails } from "@/api/cloud_account/type";
import cloudAccountApi from "@/api/cloud_account";
import { onMounted, ref } from "vue";
import type { CloudAccount } from "@/api/cloud_account/type";
import type { FormView } from "@commons/components/ce-form/type";
import type { SimpleMap } from "@commons/api/base/type";
import DetailFormValue from "@/componnets/DetailFormValue.vue";
import DetailFormLabel from "@/componnets/DetailFormLabel.vue";
import CronView from "./CronView.vue";
import IntervalView from "./IntervalView.vue";
import formApi from "@commons/api/form_resource_api";
import _ from "lodash";
/**
 * 账单设置form表单
 */
const form = ref<Array<FormView>>([]);
const billData = ref<SimpleMap<any>>({});
/**
 * 表单对象
 */

const props = defineProps<{
  jobDetails: Array<JobDetails>;
  cloudAccount: CloudAccount;
}>();

const loading = ref<boolean>(false);

/**
 *初始化账单设置
 */
const initBillSetting = (platform: string) => {
  cloudAccountApi.getBillFormByPlatform(platform).then((ok) => {
    form.value = ok.data;
    if (
      props.jobDetails.length > 0 &&
      props.jobDetails[0].params["BILL_SETTING"]
    ) {
      //console.log("赋值", props.jobDetails[0].params["BILL_SETTING"]);
      billData.value = props.jobDetails[0].params["BILL_SETTING"];
    }

    _.forEach(form.value, (f) => {
      getOptionList(f);
    });
  });
};

function getOptionList(formItem: FormView) {
  if (
    // 执行类
    formItem.clazz &&
    // 执行函数
    formItem.method &&
    // 获取当前Options时,需要的关联数据已经有值
    formItem.relationTrigger.every((r) => billData.value[r])
  ) {
    formApi
      .getResourceMyMethod(
        formItem.clazz,
        formItem.method,
        {
          ...billData.value,
          ...props.cloudAccount,
        },
        loading
      )
      .then((ok) => {
        formItem.optionList = ok.data;
      });
  }
}

function filterValue(formItem: FormView) {
  if (
    formItem?.field &&
    formItem?.valueField !== undefined &&
    formItem?.textField !== undefined
  ) {
    return _.get(
      _.find(formItem.optionList, (f) => {
        return (
          billData.value[formItem.field] ===
          _.get(f, formItem.valueField as string)
        );
      }),
      formItem.textField
    );
  } else if (formItem?.field) {
    return billData.value[formItem.field];
  } else {
    return undefined;
  }
}

function showRelation(formItem: FormView) {
  if (formItem.relationShows && formItem.relationShows.length > 0) {
    if (formItem.relationShowValues && formItem.relationShowValues.length > 0) {
      return _.every(formItem.relationShows, (r) => {
        //console.log(r, billData.value[r]);
        return formItem.relationShowValues.includes(billData.value[r]);
      });
    } else {
      return _.every(formItem.relationShows, (r) => {
        //console.log(r, billData.value[r]);
        return billData.value[r] !== undefined;
      });
    }
  }
  return true;
}

onMounted(() => {
  initBillSetting(props.cloudAccount.platform);
});
</script>
<style lang="scss"></style>
