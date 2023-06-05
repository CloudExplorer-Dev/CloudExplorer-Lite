<!--系统日志列表-->
<template>
  <el-container
    direction="vertical"
    style="height: 100%; width: 100%"
    v-loading="loading"
  >
    <el-main>
      <el-form
        :model="form"
        ref="formRef"
        label-width="auto"
        label-position="top"
      >
        <DetailFormTitle
          :title="t('system_setting.params_setting.recycle_bin.strategy')"
          style="margin-bottom: 16px"
        />

        <el-form-item
          :label="$t('system_setting.params_setting.recycle_bin.open')"
          prop="recycleBinEnable"
        >
          <el-row style="width: 100%">
            <el-switch
              v-model="form.recycleBinEnable"
              size="small"
              :disabled="
                !permissionStore.hasPermission(
                  '[management-center]PARAMS_SETTING:EDIT'
                )
              "
            >
            </el-switch>
          </el-row>

          <el-row
            style="
              width: 100%;
              font-size: 12px;
              line-height: 16px;
              margin-top: 12px;
            "
          >
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

        <DetailFormTitle title="日志数据" style="margin-bottom: 16px" />
        <div style="padding-top: 10px; padding-bottom: 10px">登录日志</div>
        <el-form-item prop="loginLogReservedMonth" :rules="rules">
          <el-space>
            <div style="width: 36px">清空</div>
            <LineNumber
              special-step="1"
              v-model.number="form.loginLogReservedMonth"
              :min="1"
              :max="24"
              :step="1"
              required
              @change="
                formatNumber(
                  form.loginLogReservedMonth,
                  'loginLogReservedMonth'
                )
              "
            />

            <div style="width: 180px">个月之前的登录日志</div>
          </el-space>
        </el-form-item>
        <div style="padding-top: 10px; padding-bottom: 10px">平台管理日志</div>
        <el-form-item prop="manageLogReservedMonth" :rules="rules">
          <el-space>
            <div style="width: 36px">清空</div>
            <LineNumber
              special-step="1"
              v-model.number="form.manageLogReservedMonth"
              :min="1"
              :max="24"
              :step="1"
              required
              @change="
                formatNumber(
                  form.manageLogReservedMonth,
                  'manageLogReservedMonth'
                )
              "
            />

            <div style="width: 180px">个月之前的平台管理日志</div>
          </el-space>
        </el-form-item>
        <div style="padding-top: 10px; padding-bottom: 10px">系统日志</div>
        <el-form-item prop="systemLogReservedMonth" :rules="rules">
          <el-space>
            <div style="width: 36px">清空</div>
            <LineNumber
              special-step="1"
              v-model.number="form.systemLogReservedMonth"
              :min="1"
              :max="24"
              :step="1"
              required
              @change="
                formatNumber(
                  form.systemLogReservedMonth,
                  'systemLogReservedMonth'
                )
              "
            />

            <div style="width: 180px">个月之前的系统日志</div>
          </el-space>
        </el-form-item>

        <DetailFormTitle title="监控数据" style="margin-bottom: 16px" />

        <el-form-item prop="metricLogReservedMonth" :rules="rules">
          <el-space>
            <div style="width: 36px">清空</div>
            <LineNumber
              special-step="1"
              v-model.number="form.metricLogReservedMonth"
              :min="1"
              :max="24"
              :step="1"
              required
              @change="
                formatNumber(
                  form.metricLogReservedMonth,
                  'metricLogReservedMonth'
                )
              "
            />

            <div style="width: 180px">个月之前的监控数据</div>
          </el-space>
        </el-form-item>
      </el-form>
    </el-main>

    <el-footer
      style="
        display: flex;
        flex-direction: row;
        justify-content: flex-end;
        align-items: center;
        height: 80px;
        box-shadow: 0 -1px 4px rgba(31, 35, 41, 0.1);
      "
      v-hasPermission="'[management-center]PARAMS_SETTING:EDIT'"
    >
      <el-button type="primary" @click="save"> 保存 </el-button>
    </el-footer>
  </el-container>
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
import DetailFormTitle from "@/componnets/DetailFormTitle.vue";
import OperatedLogApi, { batchSaveLogClearConfig } from "@/api/operated_log";
const permissionStore = usePermissionStore();
const formRef = ref<FormInstance | undefined>();
const form = ref<RecycleParams>({
  recycleBinEnable: false,
  metricLogReservedMonth: 1,
  loginLogReservedMonth: 1,
  manageLogReservedMonth: 1,
  systemLogReservedMonth: 1,
});
const { t } = useI18n();
const loading = ref<boolean>(false);

const rules = [
  {
    message: "保留月数必填",
    type: "number",
    trigger: "blur",
    required: true,
  },
  {
    message: `最小为1，最大为24`,
    trigger: ["change", "blur"],
    type: "number",
    min: 1,
    max: 24,
  },
];

function formatNumber(
  v: number,
  key:
    | "metricLogReservedMonth"
    | "loginLogReservedMonth"
    | "manageLogReservedMonth"
    | "systemLogReservedMonth"
) {
  console.log(v, key);
  form.value[key] = parseInt(`${v}`);
}

onMounted(() => {
  init();
});

/**
 * 初始化表单
 * @param row
 */
const init = () => {
  getRecycleBinStatus(loading).then((rsp) => {
    form.value.recycleBinEnable = rsp.data;
  });
  OperatedLogApi.getLogClearConfig(
    { paramKey: "log.keep.login.months" },
    loading
  ).then((res) => {
    form.value.loginLogReservedMonth = Number(res.data);
  });
  OperatedLogApi.getLogClearConfig(
    { paramKey: "log.keep.api.months" },
    loading
  ).then((res) => {
    form.value.manageLogReservedMonth = Number(res.data);
  });
  OperatedLogApi.getLogClearConfig(
    { paramKey: "log.keep.system.months" },
    loading
  ).then((res) => {
    form.value.systemLogReservedMonth = Number(res.data);
  });
  OperatedLogApi.getLogClearConfig(
    { paramKey: "log.keep.metric.months" },
    loading
  ).then((res) => {
    form.value.metricLogReservedMonth = Number(res.data);
  });
};

function save() {
  formRef.value?.validate((ok) => {
    if (ok) {
      Promise.all([saveRecycleBinStatus(), saveClearSetting()]).then(() => {
        ElMessage.success(t("commons.msg.save_success"));
      });
    }
  });
}

const saveRecycleBinStatus = () => {
  const param = {
    paramKey: "recycle_bin.enable",
    paramValue: form.value.recycleBinEnable ? "true" : "false",
  };
  return changeRecycleBinStatus(param).catch((err) => {
    ElMessage.error(err.response.data.message);
  });
};

function saveClearSetting() {
  const params = [
    {
      paramKey: "log.keep.metric.months",
      paramValue: form.value.metricLogReservedMonth,
    },
    {
      paramKey: "log.keep.login.months",
      paramValue: form.value.loginLogReservedMonth,
    },
    {
      paramKey: "log.keep.api.months",
      paramValue: form.value.manageLogReservedMonth,
    },
    {
      paramKey: "log.keep.system.months",
      paramValue: form.value.systemLogReservedMonth,
    },
  ];
  return OperatedLogApi.batchSaveLogClearConfig(params).catch((err) => {
    ElMessage.error(err);
  });
}
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
