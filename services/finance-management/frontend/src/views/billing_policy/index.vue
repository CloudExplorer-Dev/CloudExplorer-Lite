<template>
  <layout-content
    :style="{
      '--ce-main-top-height': topHeight + 'px',
      '--ce-main-content-padding-bottom': '0px',
      '--ce-main-content-margin-bottom': '0px',
      '--ce-main-content-padding-left': '0px',
    }"
  >
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <template #top>
      <el-alert
        style="--el-alert-bg-color: rgba(51, 112, 255, 0.15)"
        type="info"
        show-icon
        @close="topHeight = 0"
      >
        <template #title>
          <div>
            1.当前计费策略仅对私有云生效，1 个云账号只能关联 1 个计费策略；
          </div>
          <div>
            2.按规格计费优先级高于按单价计费说明：当实例配置与按规格计费配置匹配时，优先按规格计费，不匹配时将按单价进行计费。
          </div>
        </template>
      </el-alert>
    </template>

    <div class="content">
      <div class="left_content" v-loading="listLoading">
        <div class="title_content">
          <span class="title_text">计费策略</span>
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
        <div
          v-if="billingPolicyList && billingPolicyList.length > 0"
          class="billing_policy_list"
        >
          <div
            @click="selectedPolicy(policy)"
            :class="
              activeBillingPolicy && activeBillingPolicy.id === policy.id
                ? 'active'
                : ''
            "
            class="billing_policy_item"
            v-for="policy in policyList"
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
        <div class="empty_content" v-else>
          没有私有云计费策略，请点击上方蓝色按钮创建
        </div>
      </div>
      <RightContent
        :billing-policy="activeBillingPolicy"
        :operate="operate"
      ></RightContent>
    </div>
    <LinkCloudAccount
      ref="linkCloudAccountRef"
      @refrece="bus.emit('refrece:cloud_account', $event)"
    ></LinkCloudAccount>
    <ReName @refrece="refrece" ref="reNameRef"></ReName>
  </layout-content>
</template>
<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { Search } from "@element-plus/icons-vue";
import billingPolicyApi from "@/api/billing_policy/index";
import type { BillingPolicy } from "@/api/billing_policy/type";
import LinkCloudAccount from "@/views/billing_policy/components/LinkCloudAccount.vue";
import ReName from "@/views/billing_policy/components/ReName.vue";
import AddPolicyIcon from "@/views/billing_policy/components/AddPolicyIcon.vue";
import RightContent from "@/views/billing_policy/components/RightContent.vue";
import { ElMessageBox } from "element-plus";

import bus from "@commons/bus";
const topHeight = ref<number>(40);
const filterText = ref<string>("");
const billingPolicyList = ref<Array<BillingPolicy>>([]);
const activeBillingPolicyId = ref<string>();
const linkCloudAccountRef = ref<InstanceType<typeof LinkCloudAccount>>();
const reNameRef = ref<InstanceType<typeof ReName>>();
const operate = ref<"VIEW" | "EDIT" | "CREATE">("VIEW");

const policyList = computed(() => {
  return billingPolicyList.value.filter((p) =>
    p.name.includes(filterText.value)
  );
});
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

const listLoading = ref<boolean>(false);

// 打开关联云账号页面
const openLinkCloudAccount = (policy: BillingPolicy) => {
  linkCloudAccountRef.value?.open(policy.id);
};

// 打开重命名页面
const openReName = (policy: BillingPolicy) => {
  reNameRef.value?.open(policy);
};

const openAddPolicy = () => {
  bus.emit("update:operate", "CREATE");
};

// 删除策略
const deletePolicy = (policy: BillingPolicy) => {
  ElMessageBox.confirm(`确认删除:  ${policy.name}`, "提示", {
    confirmButtonText: "删除",
    cancelButtonText: "取消",
    type: "warning",
  }).then(() => {
    billingPolicyApi.deleteById(policy.id).then(() => {
      refreceList();
    });
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
 * 刷新
 * @param policyId 策略id
 */
const refrece = (policyId?: string) => {
  refreceList(policyId);
};

const selectedPolicy = (policy: BillingPolicy) => {
  if (operate.value === "EDIT" || operate.value === "CREATE") {
    operate.value = "VIEW";
  }
  activeBillingPolicyId.value = policy.id;
  bus.emit("closePricePreview");
};

onMounted(() => {
  bus.on("update:billing_policy", (policyId: string) => {
    refreceList(policyId);
  });
  bus.on("update:operate", ($event: "VIEW" | "EDIT" | "CREATE") => {
    operate.value = $event;
    if ($event === "CREATE") {
      activeBillingPolicyId.value = undefined;
    }
    if (
      $event === "VIEW" &&
      !activeBillingPolicyId.value &&
      billingPolicyList.value.length > 0
    ) {
      activeBillingPolicyId.value = billingPolicyList.value[0].id;
    }
  });
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
  height: 100%;
  .left_content {
    box-sizing: border-box;
    padding: 0 24px 0 0;
    width: 216px;
    height: 100%;
    .empty_content {
      margin-top: 25px;
      background: #f7f9fc;
      border-radius: 4px;
      color: rgba(143, 149, 158, 1);
      font-weight: 400;
      font-size: 12px;
      padding: 8px;
      box-sizing: border-box;
    }
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
      width: 192px;
      height: auto;
      .billing_policy_item {
        height: 38px;
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
}
</style>
