<template>
  <div class="right_content">
    <div
      style="overflow-y: auto; height: calc(100% - 51px); padding-right: 24px"
    >
      <base-container style="--ce-base-container-height: auto">
        <template #header>
          <span>{{ $t("commons.basic_info", "基本信息") }}</span>
        </template>
        <template #content
          ><el-form
            ref="formRef"
            require-asterisk-position="right"
            label-position="top"
            label-width="100px"
            :model="modelValue"
          >
            <el-form-item
              style="width: 100%"
              label="计费策略名称"
              :rules="{
                required: true,
                message: '名称不能为空',
                trigger: 'blur',
              }"
              prop="name"
            >
              <el-input style="width: 75%" v-model="name" />
            </el-form-item>
            <el-form-item label="选择云账号">
              <CloudAccountCheckbox
                :row-number="4"
                :cloud-account-list="linkCloudAccount"
                v-model="selectedCloudAccountList"
              ></CloudAccountCheckbox>
            </el-form-item>
          </el-form>
        </template>
      </base-container>
      <BillingPolicyDetailsVue
        v-model="billingPolicyDetailsList"
        :disabled="false"
      ></BillingPolicyDetailsVue>
    </div>
    <div class="footer">
      <el-button @click="bus.emit('update:operate', 'VIEW')">取消</el-button>
      <el-button type="primary" @click="save">保存</el-button>
    </div>
  </div>
</template>
<script setup lang="ts">
import { computed, ref } from "vue";
import type {
  OperateBillingPolicyRequest,
  BillingPolicyDetails,
  CloudAccountResponse,
} from "@/api/billing_policy/type";
import BillingPolicyDetailsVue from "@/views/billing_policy/components/BillingPolicyDetails.vue";
import CloudAccountCheckbox from "@/views/billing_policy/components/CloudAccountCheckbox.vue";
import type { FormInstance } from "element-plus";
import bus from "@commons/bus";
const formRef = ref<FormInstance>();
const props = defineProps<{
  /**
   *
   */
  modelValue: OperateBillingPolicyRequest;
  /**
   * 可选的关联云账号列表
   */
  cloudAccountList: Array<CloudAccountResponse>;
}>();

const emit = defineEmits(["update:modelValue", "update:operate", "save"]);

const name = computed({
  get: () => {
    return props.modelValue.name;
  },
  set: (event: string) => {
    emit("update:modelValue", { ...props.modelValue, name: event });
  },
});

const billingPolicyDetailsList = computed({
  get: () => {
    return props.modelValue.billingPolicyDetailsList;
  },
  set: (event: Array<BillingPolicyDetails>) => {
    emit("update:modelValue", {
      ...props.modelValue,
      billingPolicyDetailsList: event,
    });
  },
});

const linkCloudAccount = computed(() => {
  return props.cloudAccountList.filter((c) => !c.publicCloud);
});

const selectedCloudAccountList = computed({
  get: () => {
    return props.modelValue.cloudAccountList;
  },
  set: (event: Array<string>) => {
    emit("update:modelValue", {
      ...props.modelValue,
      cloudAccountList: event,
    });
  },
});
const save = () => {
  formRef.value?.validate().then(() => {
    emit("save");
  });
};
</script>
<style lang="scss" scoped>
.right_content {
  box-sizing: border-box;
  padding-left: 24px;
  height: 100%;
  border-left: 1px solid rgba(31, 35, 41, 0.15);
  .footer {
    border-top: 1px solid #d5d6d8;
    height: 50px;
    display: flex;
    justify-content: right;
    align-items: center;
    margin-right: 24px;
  }
}
</style>
