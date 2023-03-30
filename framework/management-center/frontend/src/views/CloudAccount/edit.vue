<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { ElMessage } from "element-plus";
import cloudAccountApi from "@/api/cloud_account";
import type { Platform, UpdateAccount } from "@/api/cloud_account/type";
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import type { FormInstance } from "element-plus";
import CloudAccountCredentialForm from "@commons/business/cloud-account/CloudAccountCredentialForm.vue";

const props = defineProps<{
  id: string;
}>();

const router = useRouter();
const { t } = useI18n();
const loading = ref<boolean>(false);

const form = ref<UpdateAccount>({
  id: props.id,
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

/**
 * 更新云账号
 */
const update = () => {
  (credentialFormRef.value?.formRef as FormInstance)?.validate((valid) => {
    if (valid) {
      cloudAccountApi
        .updateCloudAccount(form.value as UpdateAccount, loading)
        .then(() => {
          router.push({ name: "cloud_account_list" });
          ElMessage.success(t("commons.msg.op_success", "操作成功"));
        });
    }
  });
};

const credentialFormRef = ref<InstanceType<
  typeof CloudAccountCredentialForm
> | null>();

/**
 * 组建挂载
 */
onMounted(() => {
  init();
});

/**
 * 初始化对象
 */
const init = () => {
  // 获取所有供应商数据
  cloudAccountApi.getPlatformAll(loading).then((ok) => {
    platforms.value = ok.data;
  });
  // 云账号id
  cloudAccountApi.getCloudAccount(props.id, loading).then((ok) => {
    form.value.name = ok.data.name;
    form.value.platform = ok.data.platform;
    form.value.credential = JSON.parse(ok.data.credential);
  });
};
/**
 * 取消,去列表页面
 */
const clear = () => {
  router.push({ name: "cloud_account_list" });
};
</script>
<template>
  <el-container class="create-catalog-container" v-loading="loading">
    <el-main ref="create-catalog-container">
      <div class="form-div">
        <CloudAccountCredentialForm
          v-model="form"
          :active-platform="activePlatform"
          ref="credentialFormRef"
        />
      </div>
    </el-main>
    <el-footer>
      <div class="footer">
        <div class="form-div">
          <div class="footer-btn">
            <el-button class="cancel-btn" @click="clear">{{
              $t("commons.btn.cancel")
            }}</el-button>
            <el-button class="save-btn" type="primary" @click="update()">{{
              $t("commons.btn.save")
            }}</el-button>
          </div>
        </div>
      </div>
    </el-footer>
  </el-container>
</template>

<style lang="scss"></style>
