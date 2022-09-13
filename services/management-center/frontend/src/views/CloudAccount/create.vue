<script setup lang="ts">
import { ref, onMounted, computed, watch } from "vue";
import { ElMessage } from "element-plus";
import cloudAccountApi from "@/api/cloud_account";
import type {
  Platform,
  CreateAccount,
  UpdateAccount,
} from "@/api/cloud_account/type";
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
const router = useRouter();
const { t } = useI18n();
const loading = ref<boolean>(false);
// 所有供应商
const platforms = ref<Array<Platform>>([]);
// 选中的供应商
const activePlatform = computed(() => {
  return platforms.value.find(
    (platform) => from.value.platform === platform.field
  );
});

/**
 * 更新云账号
 */
const update = () => {
  cloudAccountApi.updateCloudAccount(from.value as UpdateAccount).then(() => {
    ElMessage.success("更新成功");
  });
};
/**
 * 保存云账号
 */
const create = () => {
  cloudAccountApi.save(from.value).then(() => {
    ElMessage.success("保存成功");
    router.push({ name: "cloud_account_list" });
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
    :model="from"
    :inline="true"
    status-icon
    label-width="120px"
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
            >
              <el-select
                style="width: 100%"
                v-model="from.platform"
                class="m-2"
                placeholder="请选择云平台"
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
          <template #header><h5>账号信息</h5></template>
          <template #content>
            <el-form-item
              style="width: 80%"
              :label="t('cloud_account.name', '云账号名称')"
            >
              <el-input
                v-model="from.name"
                :placeholder="
                  t('cloud_account.name.placeholder', '请输入云账号名称')
                "
              />
            </el-form-item>

            <el-form-item
              style="width: 80%"
              v-for="credential in activePlatform?.credentialFrom"
              :key="credential.field"
              :label="credential.label"
              :role="{
                message: '组织名称不为空',
                trigger: 'blur',
                type: 'string',
                required: credential.required,
              }"
            >
              <el-input v-model="from.credential[credential.field]"></el-input>
            </el-form-item>
          </template>
        </layout-container>
        <layout-container>
          <el-button type="primary" @click="clear">取消</el-button>
          <el-button
            type="primary"
            @click="create"
            v-if="operation === createOperation"
            >创建</el-button
          >
          <el-button
            type="primary"
            @click="update"
            v-if="operation === updateOperation"
            >修改</el-button
          >
        </layout-container>
      </template>
    </layout-container>
  </el-form>
</template>

<style lang="scss"></style>
