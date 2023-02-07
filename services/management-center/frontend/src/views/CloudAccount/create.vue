<script setup lang="ts">
import { ref, onMounted, computed, reactive, watch } from "vue";
import { ElMessage } from "element-plus";
import cloudAccountApi from "@/api/cloud_account";
import { platformIcon } from "@commons/utils/platform";
import type {
  Platform,
  CreateAccount,
  UpdateAccount,
} from "@/api/cloud_account/type";
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import type { FormInstance, FormRules } from "element-plus";
import _ from "lodash";

const router = useRouter();
const { t } = useI18n();
const loading = ref<boolean>(false);
// 所有供应商
const platforms = ref<Array<Platform>>([]);
// 校验实例对象
const ruleFormRef = ref<FormInstance>();

/**
 * 云账号表单收集
 */
const form = ref<CreateAccount | UpdateAccount>({
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

const steps = ["选择云平台", "输入云账号凭证"];

const activeStep = ref(1);

/**
 * 组建挂载
 */
onMounted(() => {
  // 获取所有供应商数据
  cloudAccountApi.getPlatformAll(loading).then((ok) => {
    platforms.value = ok.data;
  });
});

/**
 * 取消,去列表页面
 */
const cancel = () => {
  router.push({ name: "cloud_account_list" });
};

function before() {
  activeStep.value = 1;
}

const activePlatform = computed<Platform | undefined>(() => {
  return _.find(
    platforms.value,
    (platform) => form.value.platform === platform.field
  );
});

const hasPlatformValue = computed<boolean>(() => {
  return (
    platforms.value.length > 0 &&
    _.some(platforms.value, (p) => p.field === form.value.platform)
  );
});

function next() {
  if (hasPlatformValue.value) {
    activeStep.value = 2;
  }
}

// 改变供应商时,初始化default数据
const changePlatform = () => {
  const p = platforms.value.find(
    (platform) => form.value.platform === platform.field
  );
  form.value.credential = {};
  form.value.name = "";
  p?.credentialForm.forEach((item) => {
    if (item.defaultValue && !form.value.credential[item.field]) {
      try {
        // 设置默认值
        form.value.credential[item.field] = JSON.parse(
          item.defaultValue as string
        );
      } catch (e) {
        console.error(e);
      }
    }
  });
};

function choosePlatform(p: string) {
  form.value.platform = p;
  changePlatform();
}

/**
 * 保存云账号
 */
const submit = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid) => {
    if (valid) {
      cloudAccountApi.save(form.value, loading).then(() => {
        router.push({ name: "cloud_account_list" });
        ElMessage.success(t("commons.msg.save_success", "保存成功"));
      });
    }
  });
};
</script>
<template>
  <el-container class="create-cloud-account-form" v-loading="loading">
    <el-aside width="180px" class="aside">
      <h3>添加云账号</h3>
      <p>将您的云资源同步到系统平台中进行统一管理</p>

      <div
        style="padding: 14px"
        v-for="(step, index) in steps"
        :key="index"
        :style="{
          'font-weight': index + 1 === activeStep ? 'bolder' : 'normal',
        }"
      >
        {{ index + 1 }}. {{ step }}
      </div>
    </el-aside>
    <el-container>
      <el-header>
        <div class="header">
          <h2>{{ steps[activeStep - 1] }}</h2>
        </div>
      </el-header>
      <el-main>
        <template v-if="activeStep === 1">
          <div class="platform-select">
            <template v-for="platform in platforms" :key="platform.field">
              <el-card
                :shadow="form.platform === platform.field ? 'always' : 'hover'"
                class="platform-card"
                @click="choosePlatform(platform.field)"
              >
                <el-image
                  style="width: 180px; height: 90px"
                  fit="contain"
                  :src="platformIcon[platform.field].logo"
                ></el-image>
                <div
                  style="
                    text-align: center;
                    border-top: 1px solid var(--el-border-color);
                    padding-top: 14px;
                  "
                >
                  {{ platformIcon[platform.field].name }}
                </div>
              </el-card>
            </template>
          </div>
        </template>
        <template v-if="activeStep === 2">
          <el-form
            ref="ruleFormRef"
            :model="form"
            :inline="true"
            status-icon
            label-width="130px"
            label-suffix=":"
            label-position="left"
          >
            <el-form-item
              style="width: 80%"
              :label="t('cloud_account.name', '云账号名称')"
              prop="name"
              :rules="rules.name"
            >
              <el-input
                v-model="form.name"
                :placeholder="
                  t('cloud_account.name_placeholder', '请输入云账号名称')
                "
              />
            </el-form-item>
            <el-form-item
              style="width: 80%"
              v-for="item in activePlatform?.credentialForm"
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
                v-model="form.credential[item.field]"
                :type="item.inputType === 'Text' ? 'text' : ''"
                :show-password="item.inputType === 'Password'"
                v-if="
                  item.inputType === 'Text' || item.inputType === 'Password'
                "
              />

              <el-switch
                v-model="form.credential[item.field]"
                v-if="item.inputType === 'SwitchBtn'"
              >
              </el-switch>
            </el-form-item>
          </el-form>
        </template>
      </el-main>
      <el-footer>
        <div class="footer">
          <div></div>
          <div class="footer-btn">
            <el-button @click="cancel()"> 取消 </el-button>
            <el-button v-if="activeStep === 2" @click="before()">
              上一步
            </el-button>
            <el-button
              v-if="activeStep === 1"
              class="el-button--primary"
              :disabled="!hasPlatformValue"
              @click="next()"
            >
              下一步
            </el-button>
            <el-button
              v-if="activeStep === 2"
              class="el-button--primary"
              @click="submit(ruleFormRef)"
            >
              确认
            </el-button>
          </div>
        </div>
      </el-footer>
    </el-container>
  </el-container>
</template>

<style lang="scss">
.create-cloud-account-form {
  height: 100%;

  .aside {
    min-height: 500px;
    background-color: var(--el-color-primary);
    display: flex;
    flex-direction: column;
    flex-wrap: nowrap;
    align-items: stretch;
    padding: 10px;
    color: white;
  }

  .header {
    border-bottom: 1px solid var(--el-border-color);
  }

  .platform-select {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;

    .platform-card {
      margin: 5px;
    }
    .el-card.is-always-shadow {
      box-shadow: 0 0 12px #006eff !important;
    }
  }

  .footer {
    border-top: 1px solid var(--el-border-color);
    padding-top: 10px;
    padding-bottom: 10px;
    display: flex;
    justify-content: space-between;
    flex-direction: row;
    align-items: center;
    flex-wrap: wrap;

    .footer-form {
      min-width: 400px;
    }

    .footer-center {
      display: flex;
      flex-direction: row;
      flex-wrap: wrap;
      justify-content: center;
    }

    .footer-btn {
      display: flex;
      flex-direction: row;
      flex-wrap: wrap;
      justify-content: flex-end;
    }
  }
}
</style>
