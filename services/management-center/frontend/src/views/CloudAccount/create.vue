<script setup lang="ts">
import { ref, onMounted, computed, reactive } from "vue";
import { ElMessage } from "element-plus";
import cloudAccountApi from "@/api/cloud_account";
import type {
  Platform,
  CreateAccount,
  UpdateAccount,
} from "@/api/cloud_account/type";
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import type { FormInstance, FormRules } from "element-plus";
const router = useRouter();
const { t } = useI18n();
const loading = ref<boolean>(false);
// 所有供应商
const platforms = ref<Array<Platform>>([]);
// 校验实例对象
const ruleFormRef = ref<FormInstance>();
// 选中的供应商
const activePlatform = computed(() => {
  return platforms.value.find(
    (platform) => from.value.platform === platform.field
  );
});

/**
 * 更新云账号
 */
const update = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid) => {
    if (valid) {
      cloudAccountApi
        .updateCloudAccount(from.value as UpdateAccount)
        .then(() => {
          router.push({ name: "cloud_account_list" });
          ElMessage.success(t("commons.msg.op_success", "操作成功"));
        });
    }
  });
};
/**
 * 保存云账号
 */
const create = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid) => {
    if (valid) {
      cloudAccountApi.save(from.value).then(() => {
        router.push({ name: "cloud_account_list" });
        ElMessage.success(t("commons.msg.save_success", "保存成功"));
      });
    }
  });
};
/**
 * 云账号表单收集
 */
const from = ref<CreateAccount | UpdateAccount>({
  platform: "",
  name: "",
  credential: {},
});
// 校验对象
const rules = reactive<FormRules>({
  name: [
    {
      message: t("cloud_account.name_is_not_empty", "云账号名称不为空"),
      trigger: "blur",
      type: "string",
      required: true,
    },
  ],
  platform: [
    {
      message: t("cloud_account.platform_is_not_empty", "云平台不能为空"),
      trigger: "blur",
      type: "string",
      required: true,
    },
  ],
});
/**
 * 组建挂载
 */
onMounted(() => {
  init();
});
// 创建操作
const createOperation = "创建";
// 修改操作
const updateOperation = "修改";
// 当前操作
const operation = ref<string>();

/**
 * 初始化对象
 */
const init = () => {
  // 获取所有供应商数据
  cloudAccountApi.getPlatformAll(loading).then((ok) => {
    platforms.value = ok.data;
  });
  if (router.currentRoute.value.params.id) {
    // 云账号id
    const cloudAccountId = router.currentRoute.value.params.id as string;
    cloudAccountApi.getCloudAccount(cloudAccountId, loading).then((ok) => {
      (from.value as UpdateAccount).id = ok.data.id;
      from.value.name = ok.data.name;
      from.value.platform = ok.data.platform;
      from.value.credential = JSON.parse(ok.data.credential);
    });
    operation.value = updateOperation;
  } else {
    operation.value = createOperation;
  }
};
/**
 * 取消,去列表页面
 */
const clear = () => {
  router.push({ name: "cloud_account_list" });
};
</script>
<template>
  <el-form
    v-loading="loading"
    ref="ruleFormRef"
    :model="from"
    :inline="true"
    status-icon
    label-width="130px"
    label-suffix=":"
    label-position="left"
  >
    <layout-container :border="false">
      <template #content>
        <layout-container>
          <template #header
            ><h4>{{ t("cloud_account.platform", "云平台") }}</h4></template
          >
          <template #content>
            <el-form-item
              :label="t('cloud_account.platform', '云平台')"
              style="width: 80%"
              prop="platform"
              :rules="rules.platform"
            >
              <el-select
                style="width: 100%"
                v-model="from.platform"
                class="m-2"
                :placeholder="
                  t(
                    'cloud_account.please_select_platform_message',
                    '请选择云平台'
                  )
                "
              >
                <el-option
                  v-for="item in platforms"
                  :key="item.label"
                  :label="item.label"
                  :value="item.field"
                />
              </el-select>
            </el-form-item>
          </template>
        </layout-container>
        <layout-container>
          <template #header
            ><h5>
              {{ t("cloud_account.account_information_message", "账号信息") }}
            </h5></template
          >
          <template #content>
            <el-form-item
              style="width: 80%"
              :label="t('cloud_account.name', '云账号名称')"
              prop="name"
              :rules="rules.name"
            >
              <el-input
                v-model="from.name"
                :placeholder="
                  t('cloud_account.name_placeholder', '请输入云账号名称')
                "
              />
            </el-form-item>
            <el-form-item
              style="width: 80%"
              v-for="item in activePlatform?.credentialFrom"
              :prop="`credential.${item.field}`"
              :key="item.field"
              :label="item.label"
              :rules="[
                {
                  message:
                    item.label +
                    t('cloud_account.field_is_not_null', '字段不能为null'),
                  trigger: 'blur',
                  required: item.required,
                },
              ]"
            >
              <el-input
                v-model="from.credential[item.field]"
                :type="item.inputType === 'Text' ? 'text' : ''"
                :show-password="item.inputType === 'Password' ? true : false"
                v-if="
                  item.inputType === 'Text' || item.inputType === 'Password'
                "
              ></el-input>

              <el-switch
                v-model="from.credential[item.field]"
                v-if="item.inputType === 'SwitchBtn'"
              >
              </el-switch>
            </el-form-item>
          </template>
        </layout-container>
        <layout-container>
          <el-button @click="clear">{{
            t("commons.btn.cancel", "取消")
          }}</el-button>
          <el-button
            type="primary"
            @click="create(ruleFormRef)"
            v-if="operation === createOperation"
            >{{ t("commons.btn.save", "保存") }}</el-button
          >
          <el-button
            type="primary"
            @click="update(ruleFormRef)"
            v-if="operation === updateOperation"
            >{{ t("commons.btn.submit", "提交") }}</el-button
          >
        </layout-container>
      </template>
    </layout-container>
  </el-form>
</template>

<style lang="scss"></style>
