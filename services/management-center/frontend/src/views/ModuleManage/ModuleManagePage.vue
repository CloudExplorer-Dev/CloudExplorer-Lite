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
      action="api/module_manage/upload"
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
          style="
            padding: 8px;
            font-weight: bold;
            font-size: small;
            position: absolute;
            right: 0;
          "
          :style="{
            'background-color': !m.installed
              ? '#ccc6b9'
              : m.version === m.currentVersion
              ? '#59e6ff'
              : '#e7b148',
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
            <h3>{{ m.display_name }}</h3>
            <div
              style="
                font-size: smaller;
                font-weight: bold;
                color: var(--el-text-color-secondary);
              "
            >
              {{ m.currentVersion ? m.currentVersion : m.version }}
              <span style="float: right" v-if="m.installed">
                {{ m.status }}
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
        <el-row style="height: 32px">
          <el-col :span="12">
            <el-button
              style="width: 100%"
              @click="install(m)"
              v-if="m.installed"
              :disabled="m.version === m.currentVersion"
              class="el-button--primary"
            >
              升级
            </el-button>
            <el-button style="width: 100%" @click="install(m)" v-else>
              安装
            </el-button>
          </el-col>
          <el-col :span="12">
            <el-button
              style="width: 100%"
              @click="uninstall(m)"
              :disabled="!m.installed"
            >
              卸载
            </el-button>
          </el-col>
        </el-row>
      </el-card>
    </template>
  </el-space>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import type { UploadInstance } from "element-plus";
import ModuleManageApi from "@/api/module";
import Config from "@commons/utils/constants";
import { ElMessage } from "element-plus";
import { useI18n } from "vue-i18n";
import { useUserStore } from "@commons/stores/modules/user";
import CeIcon from "@commons/components/ce-icon/index.vue";
import { UploadFilled } from "@element-plus/icons-vue";

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

function upload() {}
function install(m: any) {
  console.log(m);
  ModuleManageApi.install(m.download_url, loading)
    .then((res) => {
      getList();
    })
    .catch((err) => {
      ElMessage.error(err.response.data.message);
    });
}
function uninstall(m: any) {
  console.log(m);
  ModuleManageApi.uninstall(m.name, loading)
    .then((res) => {
      getList();
    })
    .catch((err) => {
      ElMessage.error(err.response.data.message);
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

onMounted(() => {
  getList(true);
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
</style>
