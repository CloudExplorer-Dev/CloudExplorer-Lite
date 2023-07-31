<template>
  <layout-content
    :style="{
      'background-color': 'transparent',
      padding: 0,
      width:
        'calc(100% - var(--ce-main-content-padding-left, 30px) - var(--ce-main-content-padding-right, 30px))',
      'border-radius': 0,
    }"
  >
    <template #breadcrumb>
      <breadcrumb :auto="true">
        <div
          style="
            height: 100%;
            display: flex;
            align-items: center;
            padding-right: 24px;
          "
        >
          <ce-radio :dataList="typeList" v-model:activeValue="currentType" />
        </div>
      </breadcrumb>
    </template>

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

    <div v-loading="loading">
      <el-row :gutter="16" v-for="(group, i) in groups" :key="i">
        <el-col v-for="(m, j) in group" :key="j" :span="8">
          <div
            class="module-card upload-card"
            v-if="m.upload"
            @click="openDialog"
          >
            <CeIcon code="icon_add_outlined" color="#3370FF" />
            <div class="content">点击上传模块包</div>
          </div>
          <div class="module-card" v-else>
            <div
              class="right-top-tip"
              :class="{
                'm-tip-not-installed': !m.installed,
                'm-tip-running': m.status === 'HEALTHY',
                'm-tip-starting': m.status === 'UNHEALTHY',
                'm-tip-stopped': m.status === 'NOT_RUNNING' && m.installed,
              }"
            >
              <span v-if="!m.installed">未安装</span>
              <span v-else-if="m.status === 'HEALTHY'">运行中</span>
              <span v-else-if="m.status === 'UNHEALTHY'">启动中</span>
              <span v-else-if="m.status === 'NOT_RUNNING' && m.installed">
                已停止
              </span>
            </div>

            <div class="info">
              <div
                style="display: flex; flex-direction: row; flex-wrap: nowrap"
              >
                <div class="icon" :class="{ installed: m.installed }">
                  <CeIcon
                    :color="m.installed ? '#3370FF' : '#767B82'"
                    :code="m.icon"
                    size="38px"
                  />
                </div>
                <div class="title">
                  <div class="title-name">
                    {{ m.display_name }}
                  </div>
                  <div class="version-tip-group">
                    <div class="version-tip">
                      {{ m.currentVersion ? m.currentVersion : m.version }}
                    </div>
                    <div
                      class="version-tip has-update"
                      v-if="
                        m.installed &&
                        m.version &&
                        m.version.length > 0 &&
                        m.version !== m.currentVersion
                      "
                    >
                      可升级至 {{ m.version }}
                    </div>
                  </div>
                </div>
              </div>
              <div class="description">
                {{ m.description }}
              </div>

              <div class="detail-info">
                <p class="detail-text-title">{{ m.display_name }}</p>
                <p class="detail-text">
                  {{ m.description }}
                </p>
                <p class="update-info-title">更新内容:</p>
                <p
                  v-for="(i, index) in m.update_infos"
                  :key="index"
                  class="update-info"
                >
                  {{ i }}
                </p>
              </div>
            </div>

            <div
              style="
                height: 60px;
                padding: 0 16px;
                display: flex;
                flex-direction: row;
                flex-wrap: nowrap;
                align-items: center;
                justify-content: space-between;
              "
            >
              <div style="display: flex">
                <el-button
                  style="color: #3370ff; border-color: #3370ff"
                  @click="install(m)"
                  v-if="
                    m.installed &&
                    m.version &&
                    m.version.length > 0 &&
                    m.version !== m.currentVersion
                  "
                >
                  升级
                </el-button>
                <el-button
                  type="primary"
                  @click="install(m)"
                  v-if="!m.installed"
                >
                  安装
                </el-button>
                <el-button @click="uninstall(m)" v-if="m.installed">
                  卸载
                </el-button>
              </div>
              <el-switch v-model="m.running" @click="runOrStop(m)" />
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </layout-content>
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
import _ from "lodash";

const uploadInstance = ref<UploadInstance>();

const dialogVisible = ref(false);

const userStore = useUserStore();

const { t } = useI18n();

const loading = ref<boolean>(false);

const uploadObj = { upload: true };

const currentType = ref<string>("all");

const typeList = computed(() => {
  const installedCount = _.defaultTo(
    _.filter(list.value.modules, (m) => m.installed)?.length,
    0
  );
  const notInstalledCount = _.defaultTo(
    _.filter(list.value.modules, (m) => !m.installed)?.length,
    0
  );
  const hasUpdateCount = _.defaultTo(
    _.filter(
      list.value.modules,
      (m) =>
        m.installed &&
        m.version &&
        m.version.length > 0 &&
        m.version !== m.currentVersion
    )?.length,
    0
  );

  return [
    { label: "全部", value: "all" },
    { label: `已安装(${installedCount})`, value: "installed" },
    { label: `未安装(${notInstalledCount})`, value: "notInstalled" },
    { label: `待升级(${hasUpdateCount})`, value: "hasUpdate" },
  ];
});

const list = ref<any>({});

const groups = computed(() => {
  const _obj = list.value;
  if (_obj.modules === undefined) {
    _obj.modules = [];
  }

  const _list = _.filter(_obj.modules, (m) => {
    if (currentType.value === "installed") {
      return m.installed;
    }
    if (currentType.value === "notInstalled") {
      return !m.installed;
    }
    if (currentType.value === "hasUpdate") {
      return m.installed && m.version !== m.currentVersion;
    }
    return true;
  });

  _list.splice(0, 0, uploadObj);
  const group: Array<Array<any>> = [];
  _.forEach(_list, (o, index: any) => {
    const i = _.floor(index / 3);
    if (group.length === i) {
      group.push([]);
    }
    o.running = o.status === "HEALTHY" || o.status === "UNHEALTHY";
    group[i].push(o);
  });
  return group;
});

function openDialog() {
  if (loading.value) {
    return;
  }
  dialogVisible.value = true;
}

const header = computed(() => {
  const h: Record<string, any> = {};
  h[Config.CE_TOKEN_KEY] = userStore.currentToken();
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
    ModuleManageApi.install(m.name, m.version, loading)
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

function runOrStop(m: any) {
  if (m.status === "HEALTHY" || m.status === "UNHEALTHY") {
    stop(m);
  } else {
    restart(m);
  }
}

function restart(m: any) {
  const action = m.status === "NOT_RUNNING" ? "启动" : "重启";
  ElMessageBox.confirm(`确认${action}:  ${m.display_name}`, "提示", {
    confirmButtonText: action,
    cancelButtonText: "取消",
    type: "warning",
  })
    .then((ok) => {
      ModuleManageApi.start(m.name, loading)
        .then((res) => {
          getList();
        })
        .catch((err) => {
          ElMessage.error(err.response.data.message);
        });
    })
    .finally(() => {
      getList();
    });
}
function stop(m: any) {
  ElMessageBox.confirm(`确认停止:  ${m.display_name}`, "提示", {
    confirmButtonText: "停止",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then((ok) => {
      ModuleManageApi.stop(m.name, loading)
        .then((res) => {
          getList();
        })
        .catch((err) => {
          ElMessage.error(err.response.data.message);
        });
    })
    .finally(() => {
      getList();
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
.radio_content {
  background-color: #ffffff;
}

.module-card {
  height: 184px;
  background-color: #ffffff;
  border-radius: 4px;
  position: relative;

  .info {
    height: 91px;
    border-bottom: #dddedf 1px solid;
    padding: 16px;
    position: relative;

    .detail-info {
      left: 0;
      top: 0;
      padding: 16px 0;
      height: 91px;
      width: 100%;
      background-color: #1f2329;
      opacity: 0.8;
      position: absolute;
      overflow-y: auto;
      display: none;
      border-top-left-radius: 4px;
      border-top-right-radius: 4px;

      .update-info-title {
        font-style: normal;
        font-weight: 500;
        font-size: 13px;
        line-height: 16px;
        color: #ffffff;
        padding: 0 16px;
      }

      .update-info {
        font-style: normal;
        font-weight: 400;
        font-size: 12px;
        line-height: 16px;
        color: #ffffff;
        padding: 0 16px;
      }

      .detail-text-title {
        font-style: normal;
        font-weight: 700;
        font-size: 15px;
        line-height: 16px;
        color: #ffffff;
        padding: 0 16px;
      }

      .detail-text {
        font-style: normal;
        font-weight: 400;
        font-size: 12px;
        line-height: 16px;
        color: #ffffff;
        padding: 0 16px;
      }
    }

    .icon {
      width: 48px;
      height: 48px;
      background-color: #eff0f1;
      display: flex;
      justify-content: center;
      align-items: center;
      margin-right: 12px;
    }

    .icon.installed {
      background-color: #ebf1ff;
    }

    .description {
      height: 32px;
      padding-top: 10px;
      font-style: normal;
      font-weight: 400;
      font-size: 12px;
      line-height: 16px;
      color: #646a73;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .title {
      display: flex;
      flex-direction: column;
      flex-wrap: nowrap;
      justify-content: space-between;
      align-items: flex-start;

      .title-name {
        font-style: normal;
        font-weight: 500;
        font-size: 14px;
        line-height: 22px;
      }
    }

    .version-tip-group {
      display: flex;
      flex-direction: row;
      flex-wrap: nowrap;
    }

    .version-tip {
      background-color: #eff0f1;
      font-style: normal;
      font-weight: 400;
      font-size: 12px;
      line-height: 12px;
      color: #85898f;
      padding: 4px 10px;
    }

    .version-tip.has-update {
      margin-left: 8px;
      background-color: #fff5eb;
      color: #ffa53d;
    }
  }

  .info:hover {
    .detail-info {
      display: block;
    }
  }
}

.upload-card {
  border: 1px dashed #3370ff;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  cursor: pointer;

  .content {
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    line-height: 22px;
    padding: 14px;
  }
}

.el-row {
  margin-bottom: 16px;
}
.el-row:last-child {
  margin-bottom: 0;
}

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
  height: 24px;
  width: 54px;
  position: absolute;
  padding: 1px 6px;
  align-items: center;
  text-align: center;
  line-height: 22px;
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  right: 0;
  border-top-right-radius: 4px;
}

.m-tip-not-installed {
  background-color: #eff0f1;
  color: #767b82;
}
.m-tip-running {
  background-color: #d6f4d3;
  color: #2ea121;
}
.m-tip-starting {
  background-color: #ebf1ff;
  color: #3370ff;
}
.m-tip-stopped {
  background-color: #feecec;
  color: #f54a45;
}
</style>
