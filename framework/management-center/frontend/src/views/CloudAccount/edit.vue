<script setup lang="ts">
import { ref, computed } from "vue";
import { ElMessage } from "element-plus";
import cloudAccountApi from "@/api/cloud_account";
import type { Platform, UpdateAccount } from "@/api/cloud_account/type";
import { useI18n } from "vue-i18n";
import type { FormInstance } from "element-plus";
import CloudAccountCredentialForm from "@commons/business/cloud-account/CloudAccountCredentialForm.vue";
import CeDrawer from "@commons/components/ce-drawer/index.vue";

const id = ref<string>("");

const { t } = useI18n();
const loading = ref<boolean>(false);

const accountDrawerRef = ref<InstanceType<typeof CeDrawer>>();

const form = ref<UpdateAccount>({
  id: id.value,
  platform: "",
  name: "",
  credential: {},
});

// 所有供应商
const platforms = ref<Array<Platform>>([]);
// 选中的供应商
const activePlatform = computed(() => {
  return platforms.value.find(
    (platform) => form.value.platform === platform.field
  );
});

const emit = defineEmits(["submit"]);

/**
 * 更新云账号
 */
const update = () => {
  (credentialFormRef.value?.formRef as FormInstance)?.validate((valid) => {
    if (valid) {
      cloudAccountApi
        .updateCloudAccount(form.value as UpdateAccount, loading)
        .then(() => {
          ElMessage.success(t("commons.msg.op_success", "操作成功"));
          clear();
          emit("submit");
        });
    }
  });
};

const credentialFormRef = ref<InstanceType<
  typeof CloudAccountCredentialForm
> | null>();

/**
 * 初始化对象
 */
const init = () => {
  // 获取所有供应商数据
  cloudAccountApi.getPlatformAll(loading).then((ok) => {
    platforms.value = ok.data;
  });
  // 云账号id
  cloudAccountApi.getCloudAccount(id.value, loading).then((ok) => {
    form.value.id = id.value;
    form.value.name = ok.data.name;
    form.value.platform = ok.data.platform;
    form.value.credential = JSON.parse(ok.data.credential);
  });
};
/**
 * 取消,去列表页面
 */
const clear = () => {
  accountDrawerRef.value?.close();
  form.value = {
    id: id.value,
    platform: "",
    name: "",
    credential: {},
  };
};

function open(_id: string) {
  id.value = _id;
  accountDrawerRef.value?.open();
  init();
}

defineExpose({ open, clear });
</script>
<template>
  <CeDrawer
    ref="accountDrawerRef"
    title="编辑云账号信息"
    confirm-btn-name="保存"
    @confirm="update"
    @cancel="clear"
    :disable-btn="loading"
  >
    <CloudAccountCredentialForm
      v-if="activePlatform"
      v-model="form"
      :active-platform="activePlatform"
      ref="credentialFormRef"
    />
  </CeDrawer>
</template>

<style lang="scss" scoped></style>
