<template>
  <div class="right_content">
    <div class="header">
      <span class="title"> {{ billingPolicy.name }}</span>
      <el-button @click="bus.emit('update:operate', 'EDIT')">编辑</el-button>
    </div>
    <div class="line"></div>
    <div class="policy-content">
      <base-container
        style="--ce-base-container-height: auto; margin-top: 15px"
      >
        <template #header>
          <span>{{ $t("commons.basic_info", "基本信息") }}</span>
        </template>
        <template #content
          ><el-form label-position="top" label-width="100px">
            <el-row>
              <el-col :span="6">
                <el-form-item>
                  <template #label>
                    计费策略名称
                    <el-icon
                      @click="openReName(billingPolicy)"
                      class="click-icon"
                      ><EditPen /></el-icon
                  ></template>
                  <span
                    style="
                      overflow: hidden;
                      margin-right: 20px;
                      text-overflow: ellipsis;
                    "
                  >
                    {{ billingPolicy.name }}
                  </span>
                </el-form-item></el-col
              >
              <el-col :span="6"
                ><el-form-item label="创建时间"
                  >{{ billingPolicy.createTime }}
                </el-form-item></el-col
              >
              <el-col :span="6"
                ><el-form-item label="更新时间">
                  {{ billingPolicy.updateTime }}
                </el-form-item></el-col
              >
              <el-col :span="6">
                <el-form-item>
                  <template #label>
                    关联云账号
                    <el-icon
                      class="click-icon"
                      @click="openLinkCloudAccount(billingPolicy)"
                      ><EditPen /></el-icon
                  ></template>
                  <CloudAccountView :cloud-account-list="linkCloudAccount">
                  </CloudAccountView> </el-form-item
              ></el-col>
            </el-row>
          </el-form>
        </template>
      </base-container>
      <BillingPolicyDetailsVue
        :model-value="modelValue.billingPolicyDetailsList"
        :disabled="true"
      ></BillingPolicyDetailsVue>
    </div>
    <LinkCloudAccount
      ref="linkCloudAccountRef"
      @refrece="bus.emit('refrece:cloud_account', $event)"
    ></LinkCloudAccount>
    <ReName
      ref="reNameRef"
      @refrece="bus.emit('update:billing_policy', $event)"
    ></ReName>
  </div>
</template>
<script setup lang="ts">
import { computed, ref } from "vue";
import type {
  OperateBillingPolicyRequest,
  CloudAccountResponse,
  BillingPolicy,
} from "@/api/billing_policy/type";
import LinkCloudAccount from "@/views/billing_policy/components/LinkCloudAccount.vue";
import ReName from "@/views/billing_policy/components/ReName.vue";
import BillingPolicyDetailsVue from "@/views/billing_policy/components/BillingPolicyDetails.vue";
import CloudAccountView from "@/views/billing_policy/components/CloudAccountView.vue";
import bus from "@commons/bus";

const linkCloudAccountRef = ref<InstanceType<typeof LinkCloudAccount>>();
const reNameRef = ref<InstanceType<typeof ReName>>();
const props = defineProps<{
  /**
   *
   */
  modelValue: OperateBillingPolicyRequest;
  /**
   * 可选的关联云账号列表
   */
  cloudAccountList: Array<CloudAccountResponse>;
  /**
   * 策略信息
   */
  billingPolicy: BillingPolicy;
}>();

const linkCloudAccount = computed(() => {
  return props.cloudAccountList.filter((c) => c.selected);
});

// 打开重命名页面
const openReName = (policy: BillingPolicy) => {
  reNameRef.value?.open(policy);
};
// 打开关联云账号页面
const openLinkCloudAccount = (policy: BillingPolicy) => {
  linkCloudAccountRef.value?.open(policy.id);
};
</script>
<style lang="scss" scoped>
.right_content {
  box-sizing: border-box;
  padding-left: 24px;
  height: 100%;
  border-left: 1px solid rgba(31, 35, 41, 0.15);
}
.line {
  width: calc(100% - 24px);
  height: 1px;
  margin: 15px 0 0 0;
  background: #d5d6d8;
}
.policy-content {
  padding-right: 24px;
  height: calc(100% - 54px);
  overflow-y: auto;
}
.header {
  height: 22px;
  display: flex;
  align-items: center;
  margin-right: 24px;
  justify-content: space-between;
  .title {
    color: #1f2329;
    font-weight: 500;
    max-width: 400px;
    font-size: 16px;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
  }
}

.click-icon {
  color: #3370ff;
  cursor: pointer;
}
</style>
