<script setup lang="ts">
import { ref, onMounted, computed, reactive } from "vue";
import { ElMessage } from "element-plus";
import cloudAccountApi from "@commons/api/cloud_account";
import { platformIcon } from "@commons/utils/platform";
import type { Platform, CreateAccount } from "@commons/api/cloud_account/type";
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import type { FormInstance, FormRules } from "element-plus";
import CeIcon from "@commons/components/ce-icon/index.vue";
import _ from "lodash";
import MicroAppRouterUtil from "@commons/router/MicroAppRouterUtil";
import CloudAccountCredentialForm from "@commons/business/cloud-account/CloudAccountCredentialForm.vue";

const props = withDefaults(
  defineProps<{
    showAside?: boolean;
    type?: "dialog" | "model";
  }>(),
  {
    showAside: true,
    type: "model",
  }
);

//自定义close事件暴露给外面
const emit = defineEmits(["close"]);

const router = useRouter();
const { t } = useI18n();
const loading = ref<boolean>(false);
// 所有供应商
const platforms = ref<Array<Platform>>([]);

const credentialFormRef = ref<InstanceType<
  typeof CloudAccountCredentialForm
> | null>();

/**
 * 云账号表单收集
 */
const form = ref<CreateAccount>({
  platform: "",
  name: "",
  credential: {},
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

const platformType = ref("all");

const publicPlatforms = computed(() => {
  return _.filter(platforms.value, (p) => p.publicCloud);
});

const privatePlatforms = computed(() => {
  return _.filter(platforms.value, (p) => !p.publicCloud);
});

const platformGroups = computed(() => {
  const list = [];
  if (publicPlatforms.value.length > 0 && platformType.value !== "private") {
    list.push({
      name: "公有云",
      platforms: publicPlatforms.value,
    });
  }
  if (privatePlatforms.value.length > 0 && platformType.value !== "public") {
    list.push({
      name: "私有云",
      platforms: privatePlatforms.value,
    });
  }
  return list;
});

/**
 * 取消,去列表页面
 */
const cancel = () => {
  if (props.type === "model") {
    router.push({ name: "cloud_account_list" });
  } else {
    emit("close");
  }
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
const submit = () => {
  (credentialFormRef.value?.formRef as FormInstance)?.validate((valid) => {
    if (valid) {
      cloudAccountApi.save(form.value, loading).then((res) => {
        if (props.type === "model") {
          router.push({ name: "cloud_account_list" });
        } else {
          MicroAppRouterUtil.jumpToChildrenPath(
            "management-center",
            "/management-center/cloud_account/list",
            router
          );
        }
        ElMessage.success(t("commons.msg.save_success", "保存成功"));
        emit("close");
      });
    }
  });
};
</script>
<template>
  <el-container
    class="create-cloud-account-form"
    v-loading="loading"
    :class="{
      'in-dialog': type === 'dialog',
    }"
  >
    <el-aside width="288px" class="aside" v-if="showAside">
      <div style="padding: 24px; height: 160px">
        <div
          style="
            font-style: normal;
            font-weight: 500;
            font-size: 20px;
            line-height: 28px;
          "
        >
          添加云账号
        </div>
        <div
          style="
            font-style: normal;
            font-weight: 400;
            font-size: 14px;
            line-height: 22px;
            opacity: 0.8;
            padding-top: 16px;
          "
        >
          添加一个云账号，将您的云资源同步到系统平台中进行统一管理
        </div>
      </div>

      <template v-for="(step, index) in steps" :key="index">
        <div class="step" :class="activeStep === index + 1 ? 'active' : ''">
          <div class="left-active" v-if="activeStep === index + 1"></div>
          STEP{{ index + 1 }}: {{ step }}
          <CeIcon
            style="position: absolute; top: 14.83px; right: 24.83px"
            size="18.33px"
            code="zhengque"
          />
        </div>
      </template>
    </el-aside>
    <el-container>
      <el-header :class="{ model: type === 'model' }">
        <div class="header" :class="{ model: type === 'model' }">
          <span v-if="type === 'model'"> STEP {{ activeStep }}/2: </span>
          {{ steps[activeStep - 1] }}
        </div>
      </el-header>
      <el-main :class="{ model: type === 'model' }">
        <template v-if="activeStep === 1">
          <el-radio-group v-model="platformType" class="platform-type-group">
            <el-radio-button label="all">全部</el-radio-button>
            <el-radio-button label="public">公有云</el-radio-button>
            <el-radio-button label="private">私有云</el-radio-button>
          </el-radio-group>

          <el-radio-group v-model="form.platform" class="platform-group">
            <template v-for="group in platformGroups" :key="group.name">
              <div class="platform-group-name">{{ group.name }}</div>
              <div class="platform-select">
                <template
                  v-for="platform in group.platforms"
                  :key="platform.field"
                >
                  <div
                    class="platform-card"
                    :class="form.platform === platform.field ? 'active' : ''"
                    @click="choosePlatform(platform.field)"
                  >
                    <div class="image-container">
                      <el-image
                        class="image"
                        fit="contain"
                        :src="platformIcon[platform.field].logo"
                      ></el-image>
                    </div>
                    <div class="border-line"></div>
                    <div
                      class="radio-container"
                      :class="form.platform === platform.field ? 'active' : ''"
                    >
                      <el-radio :label="platform.field">
                        {{ platform.label }}
                      </el-radio>
                    </div>
                  </div>
                </template>
              </div>
            </template>
          </el-radio-group>
        </template>
        <template v-if="activeStep === 2">
          <CloudAccountCredentialForm
            v-model="form"
            :type="type"
            :active-platform="activePlatform"
            ref="credentialFormRef"
          />
        </template>
      </el-main>
      <el-footer :class="{ model: type === 'model' }">
        <div class="footer">
          <div></div>
          <div class="footer-btn">
            <el-button @click="cancel()">
              {{ type === "model" ? "取消" : "跳过" }}
            </el-button>
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
              @click="submit()"
            >
              完成
            </el-button>
          </div>
        </div>
      </el-footer>
    </el-container>
  </el-container>
</template>

<style lang="scss">
.in-dialog {
  height: 780px;
  width: 1200px;
}

.create-cloud-account-form {
  height: 100%;

  .el-header {
    --el-header-height: 76px;
    --el-header-padding: 0;
    border-bottom: var(--el-border-color) 1px solid;
    color: #1f2329;
  }

  .el-header.model {
    height: 48px;
  }

  .el-main {
    --el-main-padding: 24px;
  }

  .el-main.model {
    padding: 24px 0;

    .model-form {
      text-align: center;
      margin: 0 auto;
      width: 80%;
      min-width: 300px;
    }
  }

  .el-footer {
    --el-footer-padding: 16px 24px;
    --el-footer-height: 64px;
    border-top: 1px solid var(--el-border-color);
  }

  .el-footer.model {
    height: 48px;
    padding-right: 0;
    padding-bottom: 0;
    padding-top: 16px;
  }

  .aside {
    min-height: 500px;
    background-color: var(--el-color-primary);
    display: flex;
    flex-direction: column;
    flex-wrap: nowrap;
    align-items: stretch;
    padding: 0;
    color: #ffffff;

    .step {
      height: 48px;
      padding: 13px 24px;
      font-style: normal;
      font-size: 14px;
      line-height: 22px;
      box-sizing: border-box;
      position: relative;

      font-weight: 400;

      .left-active {
        height: 48px;
        width: 4px;
        background: #ffffff;
        position: absolute;
        top: 0;
        left: 0;
      }
    }

    .step.active {
      font-weight: 500;
      background: rgba(255, 255, 255, 0.1);
    }
  }

  .header {
    font-style: normal;
    font-weight: 500;
    font-size: 20px;
    line-height: 28px;
    padding: 24px;
  }

  .header.model {
    padding-left: 0;
    padding-top: 0;
    padding-bottom: 16px;
  }

  .platform-type-group {
    margin-bottom: 10px;

    .el-radio-button {
      margin-right: 10px;

      .el-radio-button__inner {
        border: rgba(31, 35, 41, 0.1) 1px solid;
        border-radius: 4px;
        box-shadow: none !important;
        background-color: rgba(31, 35, 41, 0.1);
      }
    }

    .el-radio-button.is-active {
      .el-radio-button__inner {
        color: var(--el-color-primary);
        background-color: rgba(51, 112, 255, 0.2);
        border-color: rgba(51, 112, 255, 0.2);
      }
    }
  }

  .platform-group {
    display: block;

    .platform-group-name {
      font-style: normal;
      font-weight: 500;
      font-size: 16px;
      line-height: 24px;
      padding: 10px 0;
    }

    .platform-select {
      display: flex;
      flex-direction: row;
      flex-wrap: wrap;

      .platform-card {
        width: 204px;
        height: 141px;
        cursor: pointer;
        border: #1f232926 1px solid;
        border-radius: 4px;
        margin-right: 16px;
        margin-bottom: 16px;

        .image-container {
          height: 100px;

          .image {
            width: 160px;
            height: 38px;
            margin: 30px 20px;
          }
        }

        .border-line {
          width: 100%;
          height: 1px;
          background: #1f232926;
        }

        .radio-container {
          height: 40px;
          padding-left: 12px;
          display: flex;
          flex-direction: row;
          align-items: center;
        }

        .radio-container.active {
          background: rgba(51, 112, 255, 0.1);
        }
      }

      .platform-card:hover {
        border: 1px solid #3370ff;
      }

      .platform-card.active {
        border: 1px solid #3370ff;
      }
    }
  }

  .footer {
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
