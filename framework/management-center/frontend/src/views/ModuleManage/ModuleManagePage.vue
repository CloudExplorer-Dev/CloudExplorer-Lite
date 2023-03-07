<template>
  <el-button
    @click="dialogVisible = true"
    class="el-button--primary"
    :disabled="loading"
    >本地安装</el-button
  >

  <el-dialog
    v-model="dialogVisible"
    title="上传模块包"
    width="50%"
    destroy-on-close
  >
    <el-upload
      ref="uploadInstance"
      drag
      :limit="1"
      :headers="header"
      name="file"
      action="management-center/api/module_manage/upload"
      :on-error="onError"
      :on-success="onSuccess"
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">拖拽文件或 <em>点击上传</em></div>
      <template #tip>
        <div class="el-upload__tip">上传tar.gz</div>
      </template>
    </el-upload>
  </el-dialog>

  <el-space wrap class="catalog-container" size="large" v-loading="loading">
    <template v-for="m in list?.modules" :key="m.name">
      <el-card
        :body-style="{
          padding: 0,
          position: 'relative',
        }"
        class="catalog-container-card"
      >
        <div
          class="right-top-tip"
          :class="{
            'm-tip-primary': m.installed && m.version === m.currentVersion,
            'm-tip-secondary': !m.installed,
            'm-tip-warning':
              m.installed && m.currentVersion && m.currentVersion !== m.version,
          }"
        >
          <span v-if="!m.installed">未安装</span>
          <span v-else-if="m.version === m.currentVersion">已安装</span>
          <span v-else>可升级 {{ m.version }}</span>
        </div>
        <div style="height: 110px; display: flex; flex-direction: row">
          <CeIcon
            :code="m.icon"
            size="60px"
            style="height: 66px; width: 66px; padding: 17px"
          />
          <div style="flex: 1; padding: 17px">
            <div style="padding-top: 20px; padding-bottom: 10px">
              <span style="font-weight: bolder; font-size: 1.2em">
                {{ m.display_name }}
              </span>
              <div
                style="
                  margin-left: 6px;
                  line-height: 1.2em;
                  display: inline-block;
                  text-align: center;
                  vertical-align: middle;
                "
              >
                <div
                  :class="{
                    'm-tip-warning': m.status === 'UNHEALTHY',
                    'm-tip-success': m.status === 'HEALTHY',
                    'm-tip-secondary':
                      m.status === 'NOT_RUNNING' || !m.installed,
                  }"
                  style="width: 4px; height: 4px"
                ></div>
              </div>
              <span
                style="
                  padding-left: 16px;
                  font-size: smaller;
                  font-weight: bold;
                  color: var(--el-text-color-secondary);
                "
              >
                {{ m.currentVersion ? m.currentVersion : m.version }}
              </span>
            </div>
            <div
              style="font-size: smaller; color: var(--el-text-color-secondary)"
            >
              {{ m.description }}
            </div>
          </div>
        </div>
        <div style="height: 78px; padding: 0 17px">
          <div style="padding: 6px 0; font-size: 14px; font-weight: bold">
            更新信息
          </div>
          <el-scrollbar
            style="height: 40px"
            height="40px"
            v-if="m.update_infos && m.update_infos.length > 0"
          >
            <div
              v-for="(i, index) in m.update_infos"
              :key="index"
              style="padding: 4px; font-size: smaller"
            >
              {{ i }}
            </div>
          </el-scrollbar>
          <div v-else style="padding: 4px; font-size: smaller">无</div>
        </div>

        <div style="height: 32px; float: right; padding-right: 10px">
          <el-tooltip effect="dark" content="安装" placement="bottom">
            <span>
              <el-button
                text
                size="small"
                v-if="!m.installed"
                @click="install(m)"
              >
                <CeIcon size="20px" code="download" />
              </el-button>
            </span>
          </el-tooltip>
          <el-tooltip effect="dark" content="升级" placement="bottom">
            <span>
              <el-button
                text
                size="small"
                v-if="m.installed"
                :disabled="m.version === m.currentVersion"
                @click="install(m)"
              >
                <CeIcon size="18px" code="update" />
              </el-button>
            </span>
          </el-tooltip>
          <el-tooltip
            effect="dark"
            :content="m.status === 'NOT_RUNNING' ? '启动' : '重启'"
            placement="bottom"
          >
            <span>
              <el-button
                text
                size="small"
                :disabled="!m.installed"
                @click="restart(m)"
              >
                <CeIcon size="20px" code="play" />
              </el-button>
            </span>
          </el-tooltip>
          <el-tooltip effect="dark" content="停止" placement="bottom">
            <span>
              <el-button
                text
                size="small"
                :disabled="!m.installed || m.status === 'NOT_RUNNING'"
                @click="stop(m)"
              >
                <CeIcon size="20px" code="stopcircle" />
              </el-button>
            </span>
          </el-tooltip>
          <el-tooltip effect="dark" content="卸载" placement="bottom">
            <span>
              <el-button
                text
                size="small"
                :disabled="!m.installed"
                @click="uninstall(m)"
              >
                <CeIcon size="20px" code="delete" />
              </el-button>
            </span>
          </el-tooltip>
        </div>
      </el-card>
    </template>
  </el-space>
</template>

<script setup lang="ts">
import { computed, onBeforeUnmount, onMounted, ref } from "vue";
import type { UploadInstance } from "element-plus";
import ModuleManageApi from "@/api/module";
import Config from "@commons/utils/constants";
import { ElMessage, ElMessageBox } from "element-plus";
import { useI18n } from "vue-i18n";
import { useUserStore } from "@commons/stores/modules/user";
import { UploadFilled } from "@element-plus/icons-vue";
import CeIcon from "@commons/components/ce-icon/index.vue";

const uploadInstance = ref<UploadInstance>();

const dialogVisible = ref(false);

const userStore = useUserStore();

const { t } = useI18n();

const loading = ref<boolean>(false);

const list = ref<any>({});

const header = computed(() => {
  const h: Record<string, any> = {};
  h[Config.CE_TOKEN_KEY] = userStore.currentToken;
  h[Config.CE_ROLE_KEY] = userStore.currentRole;
  h[Config.CE_SOURCE_KEY] = userStore.currentSource;
  return h;
});

function install(m: any) {
  ElMessageBox.confirm(`确认安装:  ${m.display_name} ${m.version}`, "提示", {
    confirmButtonText: "安装",
    cancelButtonText: "取消",
    type: "warning",
  }).then((ok) => {
    ModuleManageApi.install(m.download_url, loading)
      .then((res) => {
        getList();
      })
      .catch((err) => {
        ElMessage.error(err.response.data.message);
      });
  });
}
function uninstall(m: any) {
  ElMessageBox.confirm(`确认卸载:  ${m.display_name}`, "提示", {
    confirmButtonText: "卸载",
    cancelButtonText: "取消",
    type: "warning",
  }).then((ok) => {
    ModuleManageApi.uninstall(m.name, loading)
      .then((res) => {
        getList();
      })
      .catch((err) => {
        ElMessage.error(err.response.data.message);
      });
  });
}
function restart(m: any) {
  const action = m.status === "NOT_RUNNING" ? "启动" : "重启";
  ElMessageBox.confirm(`确认${action}:  ${m.display_name}`, "提示", {
    confirmButtonText: action,
    cancelButtonText: "取消",
    type: "warning",
  }).then((ok) => {
    ModuleManageApi.start(m.name, loading)
      .then((res) => {
        getList();
      })
      .catch((err) => {
        ElMessage.error(err.response.data.message);
      });
  });
}
function stop(m: any) {
  ElMessageBox.confirm(`确认停止:  ${m.display_name}`, "提示", {
    confirmButtonText: "停止",
    cancelButtonText: "取消",
    type: "warning",
  }).then((ok) => {
    ModuleManageApi.stop(m.name, loading)
      .then((res) => {
        getList();
      })
      .catch((err) => {
        ElMessage.error(err.response.data.message);
      });
  });
}

function getList(load?: boolean) {
  ModuleManageApi.list(load ? loading : undefined)
    .then((res) => {
      list.value = res.data;
    })
    .catch((err) => {
      ElMessage.error(err.response.data.message);
    });
}

function onError(error: Error) {
  ElMessage.error(JSON.parse(error.message)?.message);
}

function onSuccess(response: any) {
  uploadInstance.value!.clearFiles();
  dialogVisible.value = false;
  ElMessage.info("成功");
  getList(true);
}

let timer: any = null;

onMounted(() => {
  getList(true);

  if (timer == null) {
    timer = setInterval(() => {
      getList();
    }, 10000);
  }
});
onBeforeUnmount(() => {
  if (timer != null) {
    clearInterval(timer);
    timer = null;
  }
});
</script>

<style lang="scss" scoped>
.catalog-container {
  margin-top: 20px;
  width: 100%;
  .catalog-container-card {
    width: 300px;
    height: 220px;
    margin-bottom: 20px;
  }
}
.right-top-tip {
  padding: 8px;
  font-weight: bold;
  font-size: small;
  position: absolute;
  right: 0;
}

.m-tip-primary {
  background-color: var(--el-color-primary);
  color: var(--el-color-white);
}
.m-tip-secondary {
  background-color: var(--el-color-info);
  color: var(--el-color-white);
}
.m-tip-warning {
  background-color: var(--el-color-warning);
  color: var(--el-color-white);
}
.m-tip-success {
  background-color: var(--el-color-success);
  color: var(--el-color-white);
}
</style>
