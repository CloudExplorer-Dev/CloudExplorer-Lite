<template>
  <el-dialog v-model="visible" :show-close="true" width="840px">
    <template #header>
      <span class="dialog_title">选择扫描资源</span>
    </template>
    <el-form ref="formRef" :model="form" :rules="rule">
      <div class="cloud_account">
        <div class="operate">
          <div class="label">选择云账号</div>
          <el-checkbox
            style="height: 22px"
            v-model="cloudAccountSelected"
            label="全选"
            size="large"
          />
        </div>
        <el-form-item prop="selectedCloudAccountIds">
          <CloudAccountCheckbox
            v-model="form.selectedCloudAccountIds"
            :cloud-account-list="cloudAccountList"
          ></CloudAccountCheckbox>
        </el-form-item>
      </div>

      <div class="rule_group">
        <div class="operate">
          <div class="label">选择规则组</div>
          <el-checkbox
            style="height: 22px"
            v-model="ruleGroupSelected"
            label="全选"
            size="large"
          />
        </div>
        <el-form-item prop="selectedRuleGroupIds">
          <RuleGroupCheckbox
            :rule-group-list="complianceRuleGroupList"
            v-model="form.selectedRuleGroupIds"
          ></RuleGroupCheckbox
        ></el-form-item>
      </div>
    </el-form>

    <div class="scan_operate">
      <el-button @click="close">取消</el-button>
      <el-button class="scan" @click="scan">开始扫描</el-button>
    </div>
  </el-dialog>
</template>
<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import CloudAccountCheckbox from "@/views/scan/complonents/job_scan/CloudAccountCheckbox.vue";
import RuleGroupCheckbox from "@/views/scan/complonents/job_scan/RuleGroupCheckbox.vue";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import type { ComplianceRuleGroup } from "@/api/rule_group/type";
import ruleGroupApi from "@/api/rule_group/index";
import scanApi from "@/api/compliance_scan/index.ts";
import { ElMessage } from "element-plus";
import type { FormInstance, FormRules } from "element-plus";
const visible = ref<boolean>(false);
// 打开模态框
const open = () => {
  visible.value = true;
  form.value.selectedCloudAccountIds = props.cloudAccountList.map(
    (item) => item.id
  );
  form.value.selectedRuleGroupIds = complianceRuleGroupList.value.map(
    (item) => item.id
  );
};
// 关闭模态框
const close = () => {
  visible.value = false;
};
// 表单
const form = ref<{
  //选中的云账号id
  selectedCloudAccountIds: Array<string>;
  //选中的规则组
  selectedRuleGroupIds: Array<string>;
}>({
  selectedCloudAccountIds: [],
  selectedRuleGroupIds: [],
});
// 表单校验规则
const rule = ref<FormRules>({
  selectedCloudAccountIds: {
    type: "array",
    required: true,
    message: "请选择云账号",
    min: 1,
    trigger: "change",
  },

  selectedRuleGroupIds: {
    type: "array",
    required: true,
    message: "请选择规则组",
    min: 1,
    trigger: "change",
  },
});
// 表单对象
const formRef = ref<FormInstance>();
// 规则组列表
const complianceRuleGroupList = ref<Array<ComplianceRuleGroup>>([]);
const props = defineProps<{
  // 云账号列表
  cloudAccountList: Array<CloudAccount>;
}>();

/**
 * 全选选中状态
 */
const cloudAccountSelected = computed({
  get() {
    return (
      form.value.selectedCloudAccountIds.length ===
      props.cloudAccountList.length
    );
  },
  set(active: boolean) {
    if (active) {
      form.value.selectedCloudAccountIds = props.cloudAccountList.map(
        (item) => item.id
      );
    } else {
      form.value.selectedCloudAccountIds = [];
    }
  },
});

/**
 * 规则组全选
 */
const ruleGroupSelected = computed({
  get() {
    return (
      form.value.selectedRuleGroupIds.length ===
      complianceRuleGroupList.value.length
    );
  },
  set(active: boolean) {
    if (active) {
      form.value.selectedRuleGroupIds = complianceRuleGroupList.value.map(
        (item) => item.id
      );
    } else {
      form.value.selectedRuleGroupIds = [];
    }
  },
});

onMounted(() => {
  ruleGroupApi.list().then((ok) => {
    complianceRuleGroupList.value = ok.data;
    form.value.selectedRuleGroupIds = complianceRuleGroupList.value.map(
      (item) => item.id
    );
  });
});

/**
 *扫描函数
 */
const scan = () => {
  formRef.value?.validate((ok) => {
    if (ok) {
      scanApi
        .syncScan({
          cloudAccountIds: form.value.selectedCloudAccountIds,
          ruleGroupIds: form.value.selectedRuleGroupIds,
        })
        .then(() => {
          ElMessage.success("任务发送成功");
          close();
        });
    }
  });
};
defineExpose({ open });
</script>
<style lang="scss" scoped>
@mixin label() {
  color: #1f2329;
  font-weight: 500;
  font-size: 14px;
  line-height: 22px;
  height: 22px;
}
.dialog_title {
  color: rgba(31, 35, 41, 1);
  font-size: 16px;
  line-height: 24px;
  font-weight: 500;
  height: 24px;
}
.cloud_account {
  .operate {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .label {
      @include label;
    }
  }
}
.rule_group {
  margin-top: 24px;
  margin-bottom: 16px;
  .operate {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .label {
      @include label;
    }
  }
}
.scan_operate {
  display: flex;
  justify-content: flex-end;
  margin-top: 29px;
  .scan {
    background: #3370ff;
    color: #fff;
    margin-left: 12px;
  }
}
</style>
