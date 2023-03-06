<!--系统日志列表-->
<template>
  <el-form :model="form" ref="formRef" label-width="auto">
    <layout-container>
      <template #header>
        <span>{{
          $t("system_setting.params_setting.recycle_bin.strategy")
        }}</span>
      </template>
      <template #content>
        <el-form-item
          :label="$t('system_setting.params_setting.recycle_bin.open')"
          prop="recycleBinEnable"
        >
          <el-row style="width: 100%">
            <el-switch
              v-model="form.recycleBinEnable"
              :disabled="
                !permissionStore.hasPermission(
                  '[management-center]PARAMS_SETTING:EDIT'
                )
              "
            >
            </el-switch>
          </el-row>

          <el-row style="width: 100%; font-size: 12px; line-height: 16px">
            <el-col :span="1">
              {{ $t("system_setting.params_setting.recycle_bin.tips") }}
            </el-col>
            <el-col :span="23">
              <span>{{
                $t("system_setting.params_setting.recycle_bin.tips_1")
              }}</span
              ><br />
              <span style="color: red">{{
                $t("system_setting.params_setting.recycle_bin.tips_2")
              }}</span>
            </el-col>
          </el-row>
        </el-form-item>
      </template>
    </layout-container>
    <layout-container
      v-hasPermission="'[management-center]PARAMS_SETTING:EDIT'"
    >
      <el-button type="primary" @click="save()">{{
        $t("commons.btn.save")
      }}</el-button>
    </layout-container>
  </el-form>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import type { RecycleParams } from "@/api/system_params/type";
import type { FormInstance } from "element-plus";
import {
  changeRecycleBinStatus,
  getRecycleBinStatus,
} from "@/api/system_params";
import { ElMessage } from "element-plus";
import { useI18n } from "vue-i18n";
import { usePermissionStore } from "@commons/stores/modules/permission";
const permissionStore = usePermissionStore();
const formRef = ref<FormInstance | undefined>();
const form = reactive<RecycleParams>({
  recycleBinEnable: false,
});
const { t } = useI18n();

onMounted(() => {
  initRecycleBinStatus();
});

/**
 * 初始化表单
 * @param row
 */
const initRecycleBinStatus = () => {
  getRecycleBinStatus().then((rsp) => {
    form.recycleBinEnable = rsp.data;
  });
};

const save = () => {
  const param = {
    paramKey: "recycle_bin.enable",
    paramValue: form.recycleBinEnable ? "true" : "false",
  };
  changeRecycleBinStatus(param)
    .then(() => {
      ElMessage.success(t("commons.msg.save_success"));
    })
    .catch((err) => {
      ElMessage.error(err.response.data.message);
    });
};
</script>

<style lang="scss" scoped>
.layout-container {
  .title {
    width: 100%;
    padding-left: 10px;
    color: var(--el-text-color-regular);
    font-weight: bold;
    font-size: 14px;
  }
  .content {
    padding-left: 120px;
  }
}
</style>
