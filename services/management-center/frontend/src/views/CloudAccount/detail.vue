<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import type { FormRules, FormInstance } from "element-plus";
import { useI18n } from "vue-i18n";
import cloudAccountApi from "@/api/cloud_account";
import { useRouter } from "vue-router";
import { platformIcon } from "@/utils/platform";
import { ElMessage } from "element-plus";
import _ from "lodash";
import Job from "./job.vue";
import type { ResourceCount } from "@/api/cloud_account/type";

const { t } = useI18n();
const router = useRouter();
const basicEditable = ref(false);
const syncEditable = ref(false);
const loading = ref(false);
const accountFormRef = ref<FormInstance>();
const accountBalance = ref<number | string>();
const originAccountName = ref();
const cloudAccountId = ref<string>(
  router.currentRoute.value.params.id as string
);
const resourceCountArray = ref<ResourceCount[]>();
const job = ref<any>(null);

const init = () => {
  if (router.currentRoute.value.params.id) {
    // 获取云账号信息
    cloudAccountApi
      .getCloudAccount(cloudAccountId.value, loading)
      .then((ok) => {
        accountForm.value = _.cloneDeep(ok.data);
        originAccountName.value = _.cloneDeep(ok.data.name);
      });

    // 获取账户余额
    cloudAccountApi
      .getAccountBalance(cloudAccountId.value, loading)
      .then((ok) => {
        accountBalance.value = ok.data;
      });

    // 获取资源计数
    cloudAccountApi
      .getResourceCount(cloudAccountId.value, loading)
      .then((ok) => {
        resourceCountArray.value = ok.data;
      });
  }
};

// 资源分类：基本信息/定时同步设置
const resourceConst = {
  basic: "BASIC",
  sync: "SYNC",
};

// 云账号基本信息表单
const accountForm = ref({
  id: "",
  name: "",
  platform: "",
  state: true,
  createTime: "",
});

// 云账号基本信息校验
const accountFormRules = reactive<FormRules>({
  name: [
    {
      message: t("cloud_account.name_is_not_empty", "云账号名称不为空"),
      trigger: "blur",
      type: "string",
      required: true,
    },
  ],
});

const edit = (resource: string) => {
  if (resource === resourceConst.basic) {
    basicEditable.value = true;
  } else {
    syncEditable.value = true;
  }
};

const cancel = (resource: string) => {
  if (resource === resourceConst.basic) {
    basicEditable.value = false;
    accountForm.value.name = _.cloneDeep(originAccountName.value);
  } else {
    syncEditable.value = false;
    job.value.rollBack();
  }
};

const save = (resource: string, formEl: FormInstance) => {
  // 修改基本信息
  if (resource === resourceConst.basic) {
    if (!formEl) return;
    formEl.validate((valid) => {
      if (valid) {
        const param = _.cloneDeep(accountForm.value);
        cloudAccountApi.updateAccountName(param).then(() => {
          ElMessage.success(t("commons.msg.save_success"));
          basicEditable.value = false;
          originAccountName.value = _.cloneDeep(accountForm.value.name);
        });
      } else {
        return false;
      }
    });
  } else {
    job.value.submitForm(false);
    syncEditable.value = false;
  }
};

onMounted(() => {
  init();
});
</script>

<template>
  <el-row>
    <el-col id="basicInfo" :span="20">
      <layout-container style="margin-right: 20px">
        <template #header>
          <span>{{ $t("commons.basic_info", "基本信息") }}</span>
        </template>
        <template #btn>
          <span v-if="!basicEditable" @click="edit(resourceConst.basic)">{{
            $t("commons.btn.edit")
          }}</span>
          <span v-if="basicEditable" @click="cancel(resourceConst.basic)">{{
            $t("commons.btn.cancel")
          }}</span>
          <span
            v-if="basicEditable"
            @click="save(resourceConst.basic, accountFormRef)"
            style="padding-left: 20px"
            >{{ $t("commons.btn.save") }}</span
          >
        </template>
        <template #content>
          <el-form
            :model="accountForm"
            :rules="accountFormRules"
            ref="accountFormRef"
            label-width="100px"
            label-position="right"
            style="height: 30px"
          >
            <el-row>
              <el-col :span="6">
                <el-form-item
                  :label="t('cloud_account.name', '云账号名称') + ':'"
                  prop="name"
                  style="width: 80%"
                >
                  <el-input
                    v-model="accountForm.name"
                    :placeholder="
                      t('cloud_account.name_placeholder', '请输入云账号名称')
                    "
                    v-if="basicEditable"
                    clearable
                  />
                  <span v-if="!basicEditable">{{ accountForm.name }}</span>
                </el-form-item>
              </el-col>

              <el-col :span="6">
                <el-form-item
                  :label="t('cloud_account.platform', '云平台') + ':'"
                  prop="platform"
                >
                  <span v-if="accountForm.platform">
                    {{ platformIcon[accountForm.platform].name }}</span
                  >
                  <el-image
                    style="margin-left: 20px; display: flex"
                    :src="platformIcon[accountForm.platform].icon"
                    v-if="accountForm.platform"
                  ></el-image>
                </el-form-item>
              </el-col>

              <el-col :span="6">
                <el-form-item
                  :label="t('commons.status', '状态') + ':'"
                  prop="state"
                >
                  <div v-if="accountForm.state">
                    <span>{{
                      t("cloud_account.native_state_valid", "有效")
                    }}</span>
                    <el-icon color="green"><SuccessFilled /></el-icon>
                  </div>
                  <div v-if="!accountForm.state">
                    <span>{{
                      t("cloud_account.native_state_invalid", "无效")
                    }}</span>
                    <el-icon color="red"><CircleCloseFilled /></el-icon>
                  </div>
                </el-form-item>
              </el-col>

              <el-col :span="6">
                <el-form-item
                  :label="t('commons.create_time', '创建时间') + ':'"
                  prop="createTime"
                >
                  <span>{{ accountForm.createTime }}</span>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </template>
      </layout-container>
    </el-col>

    <el-col id="accountBalance" :span="4">
      <layout-container>
        <template #header>
          <span>{{ $t("cloud_account.balance.money", "账户余额") }}</span>
        </template>
        <template #content>
          <span style="line-height: 30px;padding-left: 20px; font-size:26px">{{ accountBalance }}</span>
          <span style="padding-left: 10px" v-if="accountBalance != '--'">{{
            $t("cloud_account.balance.unit", "元")
          }}</span>
        </template>
      </layout-container>
    </el-col>
  </el-row>

  <layout-container>
    <template #header>
      <span>{{ $t("cloud_account.resource", "账户资源") }}</span>
    </template>
    <template #content>
      <div class="resourceContainer">
        <div class="item"
          v-for="resourceCount in resourceCountArray"
          :key="resourceCount.icon"
        >
          <div>
            <ce-icon
                class="iconStyle"
              :code="resourceCount.icon"
              size="60px"
            ></ce-icon>
          </div>
          <div class = "right"
          >
            <div id="name" class="content">{{ resourceCount.name }}</div>
            <div id="count" class="content">{{ resourceCount.count }}</div>
          </div>
        </div>
      </div>
    </template>
  </layout-container>

  <layout-container>
    <template #header>
      <span>{{ $t("cloud_account.sync.setting", "定时同步设置") }}</span>
    </template>
    <template #btn>
      <span v-if="!syncEditable" @click="edit(resourceConst.sync)">{{
        $t("commons.btn.edit")
      }}</span>
      <span v-if="syncEditable" @click="cancel(resourceConst.sync)">{{
        $t("commons.btn.cancel")
      }}</span>
      <span
        v-if="syncEditable"
        @click="save(resourceConst.sync, null)"
        style="padding-left: 20px"
        >{{ $t("commons.btn.save") }}</span
      >
    </template>
    <template #content>
      <Job
        :read-only="!syncEditable"
        :border="false"
        ref="job"
        :account-id="cloudAccountId"
        :operation="false"
      ></Job>
    </template>
  </layout-container>
</template>

<style lang="scss">
.resourceContainer{
  display: flex;
  padding: 20px;
  .item {
    width: 200px;
    display: flex;
    .iconStyle{
      cursor: pointer;
      height: 60px;
      width: 50%;
      color: gray
    }
    .right{
      width: 50%;
      display: flex;
      flex-wrap: wrap;
      align-content: center;
      .content{
        width: 100%;
        padding: 5px;
      }
    }
  }
}

</style>
