<template>
  <layout-content
    :style="{
      '--ce-main-top-height': topHeight + 'px',
      '--ce-main-content-padding-bottom': '0px',
      '--ce-main-content-margin-bottom': '0px',
    }"
  >
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <template #top>
      <el-alert
        style="--el-alert-bg-color: rgba(51, 112, 255, 0.15)"
        title="当前计费策略仅对 VMware、OpenStack 等私有云资源进行计费规则设置。具体账单需到 云账单 模块进行查看。"
        type="info"
        show-icon
      />
    </template>

    <div class="content">
      <div class="left_content" v-loading="listLoading">
        <div class="title_content">
          <span class="title_text">私有云计费策略</span>
          <span @click="openAddPolicy" style="cursor: pointer"
            ><AddPolicyIcon></AddPolicyIcon
          ></span>
        </div>
        <div class="line"></div>
        <el-input
          v-model="filterText"
          placeholder="请输入策略名称"
          :prefix-icon="Search"
          class="input"
        />
        <div class="billing_policy_list">
          <div
            @click="selectedPolicy(policy)"
            :class="
              activeBillingPolicy && activeBillingPolicy.id === policy.id
                ? 'active'
                : ''
            "
            class="billing_policy_item"
            v-for="policy in billingPolicyList"
            :key="policy.id"
          >
            <span> {{ policy.name }} </span>
            <el-dropdown trigger="click">
              <span class="el-dropdown-link">
                <el-icon><More /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="openLinkCloudAccount(policy)"
                    >关联云账号</el-dropdown-item
                  >
                  <el-dropdown-item @click="openReName(policy)"
                    >重命名</el-dropdown-item
                  >
                  <el-dropdown-item @click="deletePolicy(policy)"
                    ><span style="color: rgba(245, 74, 69, 1)">
                      删除</span
                    ></el-dropdown-item
                  >
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>
      <div class="right_content" v-loading="loading">
        <div class="header">
          <span class="title"> {{ activeBillingPolicy?.name }}</span>
        </div>
        <div class="line"></div>
        <div style="overflow-y: auto; height: calc(100% - 106px)">
          <base-container style="--ce-base-container-height: 106px">
            <template #header>
              <span>{{ $t("commons.basic_info", "基本信息") }}</span>
            </template>
            <template #content
              ><el-form
                v-if="activeBillingPolicy"
                label-position="top"
                label-width="100px"
              >
                <el-row>
                  <el-col :span="6">
                    <el-form-item>
                      <template #label>
                        计费策略名称
                        <el-icon
                          @click="openReName(activeBillingPolicy)"
                          class="click-icon"
                          ><EditPen /></el-icon
                      ></template>
                      {{ activeBillingPolicy?.name }}</el-form-item
                    ></el-col
                  >
                  <el-col :span="6"
                    ><el-form-item label="创建时间"
                      >{{ activeBillingPolicy?.createTime }}
                    </el-form-item></el-col
                  >
                  <el-col :span="6"
                    ><el-form-item label="更新时间">
                      {{ activeBillingPolicy?.updateTime }}
                    </el-form-item></el-col
                  >
                  <el-col :span="6">
                    <el-form-item>
                      <template #label>
                        关联云账号
                        <el-icon
                          class="click-icon"
                          @click="openLinkCloudAccount(activeBillingPolicy)"
                          ><EditPen /></el-icon
                      ></template>
                      <CloudAccountView :cloud-account-list="linkCloudAccount">
                      </CloudAccountView> </el-form-item
                  ></el-col>
                </el-row>
              </el-form>
            </template>
          </base-container>
          <el-tabs type="border-card">
            <el-tab-pane
              v-for="(policy, index) in billingPolicyDetailsList"
              :label="policy.resourceName"
              :key="index"
            >
              <base-container style="--ce-base-container-height: auto">
                <template #header>
                  <span>按单价计费规则</span>
                </template>
                <template #content>
                  <el-row :gutter="16">
                    <el-col :span="12">
                      <BillingRuleCard
                        billing-mode="ON_DEMAND"
                        :field-list="policy.unitPriceOnDemandBillingPolicy"
                        :field-meta="policy.billingFieldMeta"
                      ></BillingRuleCard
                    ></el-col>
                    <el-col :span="12" style="padding-right: 15px"
                      ><BillingRuleCard
                        billing-mode="MONTHLY"
                        :field-list="policy.unitPriceMonthlyBillingPolicy"
                        :field-meta="policy.billingFieldMeta"
                      ></BillingRuleCard
                    ></el-col>
                  </el-row>
                </template>
              </base-container>
              <base-container style="--ce-base-container-height: auto">
                <template #header>
                  <span>按套餐计费</span>
                </template>
                <template #content>
                  <BillingPackage
                    :resource-type="policy.resourceType"
                    :add-package-billing="addPackageBilling"
                    :edit-package-billing="editPackageBilling"
                    :delete-package-billing="deletePackageBilling"
                    :monthly-billing-policy="
                      policy.unitPriceMonthlyBillingPolicy
                    "
                    :on-demand-billing-policy="
                      policy.unitPriceOnDemandBillingPolicy
                    "
                    :field-meta="policy.billingFieldMeta"
                    :package-price-billing="policy.packagePriceBillingPolicy"
                  ></BillingPackage>
                </template>
              </base-container>
              <base-container
                v-if="policy.globalConfigMetaForms"
                style="--ce-base-container-height: auto"
              >
                <template #header>
                  <span>计费设置</span>
                </template>
                <template #content>
                  <CeForm
                    label-suffix=""
                    require-asterisk-position="right"
                    label-position="top"
                    :form-view-data="policy.globalConfigMetaForms"
                    v-model="policy.globalConfigMeta"
                    :otherParams="{}"
                  ></CeForm>
                </template>
              </base-container>
            </el-tab-pane>
          </el-tabs>
        </div>
        <div class="footer">
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </div>
    </div>
    <LinkCloudAccount ref="linkCloudAccountRef"></LinkCloudAccount>
    <ReName @refrece="refrece" ref="reNameRef"></ReName>
    <AddBillingPolicy
      ref="addBillingPolicyRef"
      @refrece="refrece"
    ></AddBillingPolicy>
  </layout-content>
</template>
<script setup lang="ts">
import { ref, onMounted, watch, computed } from "vue";
import { Search } from "@element-plus/icons-vue";
import billingPolicyApi from "@/api/billing_policy/index";
import type {
  BillingPolicy,
  BillingPolicyDetails,
  CloudAccountResponse,
  PackagePriceBillingPolicy,
} from "@/api/billing_policy/type";
import LinkCloudAccount from "@/views/billing_policy/components/LinkCloudAccount.vue";
import ReName from "@/views/billing_policy/components/ReName.vue";
import BillingRuleCard from "@/views/billing_policy/components/BillingRuleCard.vue";
import BillingPackage from "@/views/billing_policy/components/BillingPackage.vue";
import AddBillingPolicy from "@/views/billing_policy/components/AddBillingPolicy.vue";
import CloudAccountView from "@/views/billing_policy/components/CloudAccountView.vue";
import AddPolicyIcon from "@/views/billing_policy/components/AddPolicyIcon.vue";
import CeForm from "@commons/components/ce-form/index.vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { nanoid } from "nanoid";

const topHeight = ref<number>(40);
const filterText = ref<string>();
const billingPolicyList = ref<Array<BillingPolicy>>([]);
const activeBillingPolicyId = ref<string>();
const linkCloudAccountRef = ref<InstanceType<typeof LinkCloudAccount>>();
const reNameRef = ref<InstanceType<typeof ReName>>();
const addBillingPolicyRef = ref<InstanceType<typeof AddBillingPolicy>>();
const billingPolicyDetailsList = ref<Array<BillingPolicyDetails>>([]);
/**
 * 选中的账单策略
 */
const activeBillingPolicy = computed(() => {
  if (activeBillingPolicyId.value) {
    return billingPolicyList.value.find(
      (b) => b.id === activeBillingPolicyId.value
    );
  }
  return undefined;
});

// 加载器
const loading = ref<boolean>(false);
const listLoading = ref<boolean>(false);

// 打开关联云账号页面
const openLinkCloudAccount = (policy: BillingPolicy) => {
  linkCloudAccountRef.value?.open(policy.id);
};

const linkCloudAccount = ref<Array<CloudAccountResponse>>([]);

// 打开重命名页面
const openReName = (policy: BillingPolicy) => {
  reNameRef.value?.open(policy);
};

const openAddPolicy = () => {
  addBillingPolicyRef.value?.open();
};

watch(activeBillingPolicyId, () => {
  if (activeBillingPolicyId.value) {
    refreceDetails(activeBillingPolicyId.value);
    billingPolicyApi
      .listCloudAccount(activeBillingPolicyId.value)
      .then((ok) => {
        linkCloudAccount.value = ok.data.filter((s) => s.selected);
      });
  }
});

// 删除策略
const deletePolicy = (policy: BillingPolicy) => {
  ElMessageBox.confirm(`确认删除:  ${policy.name}`, "提示", {
    confirmButtonText: "删除",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    billingPolicyApi.deleteById(policy.id).then(() => {
      refreceList(policy.id);
    });
  });
};
const save = () => {
  const billingPolicyId = activeBillingPolicyId.value;
  if (billingPolicyId) {
    billingPolicyApi
      .updatePolicy(billingPolicyId, billingPolicyDetailsList.value)
      .then(() => {
        refreceDetails(billingPolicyId);
        ElMessage.success("保存成功");
      })
      .catch((e) => {
        ElMessage.success("保存失败", e);
      });
  }
};
// 添加套餐计费
const addPackageBilling = (
  resourceType: string,
  packagePriceBillingPolicy: PackagePriceBillingPolicy
) => {
  billingPolicyDetailsList.value.forEach((item) => {
    if (item.resourceType === resourceType) {
      item.packagePriceBillingPolicy.push(packagePriceBillingPolicy);
    }
  });
};

/**
 * 刷新列表
 */
const refreceList = (policyId?: string) => {
  billingPolicyApi.list(listLoading).then((res) => {
    billingPolicyList.value = res.data;
    if (res.data.length > 0) {
      if (policyId) {
        activeBillingPolicyId.value = policyId;
      } else if (!activeBillingPolicy.value) {
        activeBillingPolicyId.value = res.data[0].id;
      }
    }
  });
};

/**
 * 刷新详情
 * @param policyId 策略id
 */
const refreceDetails = (policyId: string) => {
  billingPolicyApi.details(policyId, loading).then((ok) => {
    ok.data.billingPolicyDetailsList.forEach((item) => {
      item.packagePriceBillingPolicy.forEach((p) => (p.id = nanoid()));
    });
    billingPolicyDetailsList.value = ok.data.billingPolicyDetailsList;
  });
};
/**
 * 刷新
 * @param policyId 策略id
 */
const refrece = (policyId?: string) => {
  refreceList(policyId);
};
/**
 * 修改套餐计费
 */
const editPackageBilling = (
  resourceType: string,
  id: string,
  packagePriceBillingPolicy: PackagePriceBillingPolicy
) => {
  billingPolicyDetailsList.value.forEach((item) => {
    if (item.resourceType === resourceType) {
      item.packagePriceBillingPolicy.forEach((policy) => {
        if (policy.id === id) {
          policy.billPolicyFields = packagePriceBillingPolicy.billPolicyFields;
          policy.monthly = packagePriceBillingPolicy.monthly;
          policy.name = packagePriceBillingPolicy.name;
          policy.onDemand = packagePriceBillingPolicy.onDemand;
        }
      });
    }
  });
};

/**
 * 删除套餐计费
 */
const deletePackageBilling = (resourceType: string, id: string) => {
  billingPolicyDetailsList.value.forEach((item) => {
    if (item.resourceType === resourceType) {
      item.packagePriceBillingPolicy = item.packagePriceBillingPolicy.filter(
        (p) => p.id !== id
      );
    }
  });
};

const selectedPolicy = (policy: BillingPolicy) => {
  activeBillingPolicyId.value = policy.id;
};

onMounted(() => {
  refrece();
});
</script>
<style lang="scss" scoped>
.line {
  width: 100%;
  height: 1px;
  margin: 15px 0 16px 0;
  background: #d5d6d8;
}
.content {
  --el-popover-padding: 0;
  display: flex;
  height: calc(100%);
  .left_content {
    box-sizing: border-box;
    padding: 0 24px 0 0;
    width: 240px;
    height: 100%;
    .title_content {
      display: flex;
      align-items: center;
      justify-content: space-between;
      .title_text {
        color: rgba(31, 35, 41, 1);
        font-weight: 500;
        font-size: 14px;
        line-height: 22px;
      }
    }

    .billing_policy_list {
      margin-top: 7px;
      width: 100%;
      .billing_policy_item {
        height: 38px;
        width: 100%;
        border-radius: 4px;
        color: #1f2329;
        font-weight: 400;
        font-size: 14px;
        line-height: 38px;
        padding: 0 8px;
        box-sizing: border-box;
        display: flex;
        align-items: center;
        justify-content: space-between;
        cursor: pointer;
        > span {
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }
      .active {
        background: #ebf1ff;
        color: #3370ff;
        font-weight: 500;
        --color: #3370ff;
      }
    }
  }
  .right_content {
    width: calc(100% - 245px);
    box-sizing: border-box;
    padding-left: 24px;
    height: 100%;
    border-left: 1px solid rgba(31, 35, 41, 0.15);

    .header {
      height: 22px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .title {
        color: #1f2329;
        font-weight: 500;
        font-size: 16px;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
      }
    }
    .content {
      overflow-y: auto;
      height: 100%;
    }
    .footer {
      border-top: 1px solid #d5d6d8;
      width: 100%;
      height: 50px;
      display: flex;
      justify-content: right;
      align-items: center;
    }
  }
}
.click-icon {
  color: #3370ff;
  cursor: pointer;
}
</style>
