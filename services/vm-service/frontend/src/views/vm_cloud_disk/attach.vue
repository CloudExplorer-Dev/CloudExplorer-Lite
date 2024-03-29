<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import VmCloudDiskApi from "@/api/vm_cloud_disk";
import type { FormInstance } from "element-plus";
import type { FormRules } from "element-plus";
import { useI18n } from "vue-i18n";
import type { VmCloudServerVO } from "@/api/vm_cloud_server/type";
import { ElMessage } from "element-plus";
import { Platform } from "@commons/utils/platform";

const props = defineProps<{
  id: string;
  ids: string[];
  accountId: string;
  platform: Platform;
  zone: string;
  visible: boolean;
  isBatch: boolean;
}>();
const emits = defineEmits(["update:visible", "refresh"]);
const { t } = useI18n();
const vmList = ref<VmCloudServerVO[]>([]);
const formRef = ref<FormInstance>();
const loading = ref(false);
const notSupportDeleteWithInstance = [Platform.OPENSTACK];

const form = reactive({
  instanceUuid: "",
  deleteWithInstance: false,
});

const rules = reactive<FormRules>({
  instanceUuid: [
    {
      required: true,
      message: t("vm_cloud_disk.label.select_vm"),
      trigger: "change",
    },
  ],
});

const handleCancel = (formEl: FormInstance) => {
  emits("update:visible", false);
  formEl.resetFields();
};

const handleSave = (formEl: FormInstance) => {
  if (!formEl) return;
  formEl.validate((valid) => {
    if (valid) {
      if (props.isBatch) {
        const data = {
          ids: props.ids,
          instanceUuid: form.instanceUuid,
          deleteWithInstance: form.deleteWithInstance,
        };
        VmCloudDiskApi.batchAttach(data, loading).then(() => {
          ElMessage.success(t("commons.msg.op_success"));
          emits("update:visible", false);
          emits("refresh");
        });
      } else {
        const data = {
          id: props.id,
          instanceUuid: form.instanceUuid,
          deleteWithInstance: form.deleteWithInstance,
        };
        VmCloudDiskApi.attach(data, loading).then(() => {
          ElMessage.success(t("commons.msg.op_success"));
          emits("update:visible", false);
          emits("refresh");
        });
      }
    } else {
      return false;
    }
  });
};

onMounted(() => {
  const params = {
    accountId: props.accountId,
    zone: props.zone,
  };
  VmCloudDiskApi.listVm(params).then((res) => {
    vmList.value = res.data;
  });
});
</script>

<template>
  <el-form :model="form" :rules="rules" ref="formRef" v-loading="loading">
    <div style="margin: 0 50px">
      <el-form-item
        :label="$t('vm_cloud_disk.label.cloudVm')"
        prop="instanceUuid"
      >
        <el-select
          v-model="form.instanceUuid"
          :placeholder="
            $t(
              'commons.validate.select',
              [$t('vm_cloud_disk.label.cloudVm')],
              '请选择云主机'
            )
          "
          filterable
        >
          <el-option
            v-for="item in vmList"
            :key="item.instanceUuid"
            :label="item.instanceName"
            :value="item.instanceUuid"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        v-if="!notSupportDeleteWithInstance.includes(props.platform)"
      >
        <input
          v-model="form.deleteWithInstance"
          type="checkbox"
          style="margin-right: 5px"
        />
        <span>{{ $t("vm_cloud_disk.label.deleteWithVm") }}</span>
      </el-form-item>
    </div>

    <div class="footer">
      <el-button @click="handleCancel(formRef)">{{
        $t("commons.btn.cancel")
      }}</el-button>
      <el-button type="primary" @click="handleSave(formRef)">{{
        $t("commons.btn.ok")
      }}</el-button>
    </div>
  </el-form>
</template>

<style lang="scss">
.footer {
  display: flex;
  justify-content: center;
  padding-top: 30px;
}
</style>
