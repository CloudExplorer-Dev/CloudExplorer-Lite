<template>
  <div style="width: 100%" v-loading="loading">
    <OperateBillingPolicy
      @save="save"
      v-if="operate === 'CREATE' || operate === 'EDIT'"
      :cloud-account-list="cloudAccountList"
      v-model="policyForm"
    ></OperateBillingPolicy>
    <ViewBillingPolicy
      v-loading="loading"
      v-else-if="billingPolicy && operate === 'VIEW'"
      :billing-policy="billingPolicy"
      :cloud-account-list="cloudAccountList"
      :model-value="policyForm"
    ></ViewBillingPolicy>
    <EmptyBillingPolicy v-else></EmptyBillingPolicy>
  </div>
</template>
<script setup lang="ts">
import { ref, watch, onMounted, computed } from "vue";
import billingPolicyApi from "@/api/billing_policy/index";
import type {
  BillingPolicy,
  CloudAccountResponse,
  OperateBillingPolicyRequest,
} from "@/api/billing_policy/type";
import ViewBillingPolicy from "@/views/billing_policy/components/ViewBillingPolicy.vue";
import EmptyBillingPolicy from "@/views/billing_policy/components/EmptyBillingPolicy.vue";
import { nanoid } from "nanoid";
import OperateBillingPolicy from "@/views/billing_policy/components/OperateBillingPolicy.vue";
import bus from "@commons/bus";
// 加载器
const loading = ref<boolean>(false);

const cloudAccountList = ref<Array<CloudAccountResponse>>([]);

const policyForm = ref<OperateBillingPolicyRequest>({
  name: "",
  cloudAccountList: [],
  billingPolicyDetailsList: [],
});

const props = defineProps<{
  billingPolicy?: BillingPolicy;
  /**
   * 显示类型
   */
  operate: "VIEW" | "EDIT" | "CREATE";
}>();
/**
 * 刷新详情
 * @param policyId 策略id
 */
const getBillingDetails = (policyId?: string) => {
  loading.value = true;
  const details = billingPolicyApi.details(policyId).then((ok) => {
    ok.data.billingPolicyDetailsList.forEach((item) => {
      item.packagePriceBillingPolicy.forEach((p) => (p.id = nanoid()));
    });
    policyForm.value.billingPolicyDetailsList =
      ok.data.billingPolicyDetailsList;
    policyForm.value.name = ok.data.name;
  });
  const listCloudAccount = getListCloudAccount(policyId);
  Promise.all([listCloudAccount, details]).finally(
    () => (loading.value = false)
  );
};

const billingPolicyId = computed(() => {
  if (props.billingPolicy) {
    return props.billingPolicy.id;
  }
  return undefined;
});
watch(
  billingPolicyId,
  () => {
    if (props.billingPolicy) {
      getBillingDetails(props.billingPolicy.id);
    }
  },
  { immediate: true }
);

/**
 * 请求获取云账号列表
 */
const getListCloudAccount = (policyId?: string) => {
  return billingPolicyApi.listCloudAccount(policyId, loading).then((ok) => {
    cloudAccountList.value = ok.data;
    policyForm.value.cloudAccountList = ok.data
      .filter((c) => c.selected)
      .map((c) => c.id);
  });
};

/**
 * 保存数据
 */
const save = () => {
  if (props.operate === "CREATE") {
    billingPolicyApi.create(policyForm.value, loading).then((ok) => {
      getListCloudAccount(props.billingPolicy?.id);
      bus.emit("update:billing_policy", ok.data.id);
      bus.emit("update:operate", "VIEW");
    });
  } else {
    billingPolicyApi
      .updatePolicy(
        { ...policyForm.value, id: props.billingPolicy?.id },
        loading
      )
      .then(() => {
        getListCloudAccount(props.billingPolicy?.id);
        bus.emit("update:billing_policy", props.billingPolicy?.id);
        bus.emit("update:operate", "VIEW");
      });
  }
};
onMounted(() => {
  bus.on("refrece:cloud_account", (policyId: string) => {
    getListCloudAccount(policyId);
  });
  bus.on("update:operate", ($event: "VIEW" | "EDIT" | "CREATE") => {
    if ($event === "CREATE") {
      getBillingDetails();
      policyForm.value.cloudAccountList = [];
    }
  });
});
</script>
<style lang="scss" scope></style>
