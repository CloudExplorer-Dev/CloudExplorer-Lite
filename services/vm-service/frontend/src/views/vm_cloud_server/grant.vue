<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { useI18n } from "vue-i18n";
import BaseWorkspaceApi from "@commons/api/workspace";
import { grantVmCloudServer } from "@/api/vm_cloud_server";
import { ElMessage } from "element-plus";

const props = defineProps<{
  cloudServerIds: string[];
  dialogVisible: boolean;
}>();
const emits = defineEmits(["update:visible","refresh"]);

const { t } = useI18n();
const loading = ref();
const formRef = ref<FormInstance | undefined>();
const form = reactive({
  grant: "true",
  sourceId: "",
});

const rules: FormRules = {
  selectedSourceId: [
    {
      required: true,
      message: t("commons.validate.required"),
      trigger: "change",
    },
  ],
};

const handleCancel = () => {
  emits("update:visible", false);
  formRef.value?.resetFields();
};

const handleSave = () => {
  if (!formRef.value) return false;
  formRef.value.validate((valid) => {
    if (valid || form.grant === "NO") {
      const param = {
        cloudServerIds: props.cloudServerIds,
        grant: JSON.parse(form.grant),
        sourceId: form.sourceId,
      };
      grantVmCloudServer(param, loading)
        .then(() => {
          emits("update:visible", false);
          emits("refresh");
          ElMessage.success(t("commons.msg.save_success"));
        })
        .catch((err) => {
          ElMessage.error(err.response.data.message);
        });
    } else {
      return false;
    }
  });
};

const workspaceOrgTree = ref();
const getTree = async () => {
  workspaceOrgTree.value = (
    await BaseWorkspaceApi.workspaceOrgTree(loading)
  ).data;
};
onMounted(() => {
  getTree();
});
</script>

<template>
  <div>
    <el-form
      :model="form"
      :rules="rules"
      ref="formRef"
      label-width="auto"
      label-position="right"
    >
      <el-form-item :label="$t('commons.operation','操作')" prop="grant">
        <el-radio-group v-model="form.grant">
          <el-radio-button label="true">{{$t("commons.grant","授权")}}</el-radio-button>
          <el-radio-button label="false">{{$t("commons.cancel_grant","取消授权")}}</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item
        :label="$t('commons.org_workspace','组织或工作空间')"
        prop="sourceId"
        v-show="form.grant === 'true'"
      >
        <el-tree-select
          v-model="form.sourceId"
          node-key="id"
          :props="{ label: 'name' }"
          :data="workspaceOrgTree"
          :render-after-expand="false"
          filterable
          show-checkbox
          check-strictly
          style="width: 100%"
        />
      </el-form-item>
    </el-form>
    <div class="dialog_footer">
      <el-button @click="handleCancel()">{{
        $t("commons.btn.cancel")
      }}</el-button>
      <el-button type="primary" @click="handleSave()">{{
        $t("commons.btn.save")
      }}</el-button>
    </div>
  </div>
</template>

<style lang="scss">
.dialog_footer {
  text-align: right;
  padding-top: 30px;
}
</style>
