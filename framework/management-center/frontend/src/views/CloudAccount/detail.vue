<script setup lang="ts">
import { computed, onMounted, onBeforeUnmount, ref } from "vue";
import type { FormInstance } from "element-plus";
import { useI18n } from "vue-i18n";
import cloudAccountApi from "@/api/cloud_account";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import _ from "lodash";
import {
  getStatusIcon,
  getColorByAccountStatus,
} from "@/componnets/StatusIconConstant";

import SyncJobTabView from "@/componnets/job/SyncJobTabView.vue";
import type { AccountJobRecord, ResourceCount } from "@/api/cloud_account/type";
import {
  PaginationConfig,
  TableOperations,
} from "@commons/components/ce-table/type";
import MoreOptionsButton from "@commons/components/ce-table/MoreOptionsButton.vue";
import { usePermissionStore } from "@commons/stores/modules/permission";
import DetailFormLabel from "@/componnets/DetailFormLabel.vue";
import DetailFormValue from "@/componnets/DetailFormValue.vue";
import DetailFormTitle from "@/componnets/DetailFormTitle.vue";
import CurrencyFormat from "@commons/utils/currencyFormat";
import type { CloudAccount } from "@/api/cloud_account/type";
import EditAccount from "./edit.vue";
import SyncAccountDialog from "./SyncAccountDialog.vue";
import PlatformIcon from "@commons/components/platform-icon/index.vue";
import { usePlatformStore } from "@commons/stores/modules/platform";
import type { Platform } from "@commons/api/cloud_account/type";
const platformStore = usePlatformStore();
const props = defineProps<{
  id: string;
}>();

const permissionStore = usePermissionStore();

const syncAccountDialogRef = ref<InstanceType<typeof SyncAccountDialog>>();

function openSyncDialog(row: CloudAccount) {
  syncAccountDialogRef.value?.open(row);
}

const { t } = useI18n();
const router = useRouter();
const loading = ref(false);
const accountBalance = ref<number | string>();

const resourceCountArray = ref<ResourceCount[]>();
const loadingSyncRecord = ref(false); // 同步记录加载标识
const syncRecords = ref<Array<AccountJobRecord>>([]); // 同步记录列表
const syncRecordTotal = ref(0); // 同步记录总数
const selectedSyncRecord = ref<AccountJobRecord>(); // 同步记录详情

const syncRecordConfig = new PaginationConfig(1, 10); // 同步记录列表分页查询配置

// 云账号基本信息表单
const accountForm = ref<CloudAccount>();

const init = () => {
  // 获取云账号信息
  cloudAccountApi.getCloudAccount(props.id, loading).then((ok) => {
    accountForm.value = ok.data;
  });

  // 获取账户余额
  cloudAccountApi.getAccountBalance(props.id, loading).then((ok) => {
    accountBalance.value = ok.data;
  });

  // 获取资源计数
  cloudAccountApi.getResourceCount(props.id, loading).then((ok) => {
    resourceCountArray.value = ok.data;
  });

  initGetRecord();
};

const recordLoading = ref<boolean>(false);

function initGetRecord() {
  syncRecordConfig.currentPage = 1;
  // 分页获取云账号同步日志
  cloudAccountApi
    .pageSyncRecord(
      {
        currentPage: syncRecordConfig.currentPage,
        pageSize: syncRecordConfig.pageSize,
        cloudAccountId: props.id,
        createTime: currentDate.value?.toLocaleDateString(),
        status: searchStatus.value,
        description: searchDescription.value,
      },
      recordLoading
    )
    .then((ok) => {
      syncRecords.value = _.cloneDeep(ok.data.records);
      syncRecordTotal.value = _.cloneDeep(ok.data.total);
      syncRecordConfig.currentPage = syncRecordConfig.currentPage + 1;
      if (syncRecords.value.length > 0) {
        showSyncRecordDetail(syncRecords.value[0]);
      } else {
        selectedSyncRecord.value = undefined;
      }
    });
}

// 已同步多少区域
const recordRegionDescription = computed(() => {
  let regionNum = 0;
  if (
    selectedSyncRecord.value &&
    selectedSyncRecord.value.params &&
    selectedSyncRecord.value.params[selectedSyncRecord.value.type]
  ) {
    regionNum =
      selectedSyncRecord.value.params[selectedSyncRecord.value.type].length;
  }
  return (
    t("cloud_account.sync.finishArea", "已同步区域") +
    regionNum +
    t("cloud_account.sync.unit", "个")
  );
});

/**
 * 账单设置详情描述
 */
const recordBillDescriptionTemplate = _.template(
  "已同步" +
    "<%= monthNum %>" +
    "个月账单" +
    "," +
    "共 " +
    "<%= recordCount %> " +
    t("cloud_account.sync.unit", "个") +
    "数据" +
    ";" +
    "累计:" +
    "<%= sum %>" +
    "元" +
    ";"
);
/**
 * 账单设置详情描述
 */
const recordBillDescription = computed(() => {
  if (
    selectedSyncRecord.value &&
    selectedSyncRecord.value.params &&
    selectedSyncRecord.value.params[selectedSyncRecord.value.type]
  ) {
    return recordBillDescriptionTemplate({
      monthNum:
        selectedSyncRecord.value.params[selectedSyncRecord.value.type].length,
      recordCount: _.sumBy(
        selectedSyncRecord.value.params[selectedSyncRecord.value.type],
        "size"
      ),
      sum:
        Math.floor(
          _.sumBy(
            selectedSyncRecord.value.params[selectedSyncRecord.value.type],
            "sum"
          ) * 100
        ) / 100,
    });
  }
  return recordBillDescriptionTemplate({ monthNum: 0, recordCount: 0, sum: 0 });
});

// 已同步多少资源
const recordResourceDescription = computed(() => {
  let resourceCount = 0;
  let description = t("cloud_account.sync.resource", "同步资源");
  if (
    selectedSyncRecord.value &&
    selectedSyncRecord.value.params &&
    selectedSyncRecord.value.params[selectedSyncRecord.value.type]
  ) {
    selectedSyncRecord.value.params[selectedSyncRecord.value.type].forEach(
      (item) => {
        resourceCount = resourceCount + item.size;
      }
    );
    description = selectedSyncRecord.value.description;
  }
  return (
    "已" + description + resourceCount + t("cloud_account.sync.unit", "个")
  );
});

// 是否还有同步记录需要加载
const noMoreSyncRecord = computed(() => {
  return syncRecords.value.length >= syncRecordTotal.value;
});

// 是否允许继续滚动加载
const scrollDisabled = computed(
  () => loadingSyncRecord.value || noMoreSyncRecord.value
);

const currentDate = ref(new Date());
const searchDescription = ref<string>("");
const searchStatus = ref<string>("");

const searchTypes = ref<Array<{ label: string; value: string }>>([
  { label: "全部", value: "" },
]);
const searchStatusList = ref<Array<{ label: string; value: string }>>([
  { label: "全部", value: "" },
  { label: "成功", value: "SUCCESS" },
  { label: "失败", value: "FAILED" },
  { label: "同步中", value: "SYNCING" },
  { label: "超时", value: "TIME_OUT" },
]);

/**
 * 动态加载同步记录
 */
const load = () => {
  loadingSyncRecord.value = true;
  // 分页获取云账号同步日志
  cloudAccountApi
    .pageSyncRecord({
      currentPage: syncRecordConfig.currentPage,
      pageSize: syncRecordConfig.pageSize,
      cloudAccountId: props.id,
      createTime: currentDate.value?.toLocaleDateString(),
      status: searchStatus.value,
      description: searchDescription.value,
    })
    .then((ok) => {
      syncRecordConfig.currentPage = ok.data.current + 1;
      syncRecords.value = [
        ...new Set(
          [...syncRecords.value, ...ok.data.records].map(
            (record) => record.jobRecordId
          )
        ),
      ]
        .map(
          (jobRecordId: string) =>
            [...syncRecords.value, ...ok.data.records].find(
              (record) => record.jobRecordId === jobRecordId
            ) as AccountJobRecord
        )
        .filter((item) => item);

      syncRecords.value.sort((s1, s2) =>
        s2.createTime.localeCompare(s1.createTime)
      );
      loadingSyncRecord.value = false;
    });
};

/**
 * 展示同步记录详情
 */
const showSyncRecordDetail = (syncRecord: AccountJobRecord) => {
  selectedSyncRecord.value = syncRecord;
};

function editSyncSetting() {
  router.push({ name: "cloud_account_sync_job", params: { id: props.id } });
}

const simpleFormRef = ref<FormInstance>();
const simpleForm = ref<{ name?: string }>({});
const nameEditable = ref<boolean>(false);
function editName() {
  simpleForm.value.name = accountForm.value?.name;
  nameEditable.value = true;
}

const cancel = () => {
  nameEditable.value = false;
};

const saveName = () => {
  simpleFormRef.value?.validate().then((valid) => {
    if (valid && simpleForm.value?.name) {
      cloudAccountApi
        .updateAccountName(
          { id: props.id, name: simpleForm.value.name },
          loading
        )
        .then((res) => {
          cloudAccountApi.getCloudAccount(props.id, loading).then((ok) => {
            accountForm.value = ok.data;
            cancel();
          });
        });
    }
  });
};

/**
 * 关闭定时任务
 */
const closeInterval = () => {
  if (timer) clearInterval(timer);
};

let timer: any;
onMounted(() => {
  init();
  timer = setInterval(() => {
    if (syncRecords.value.some((s) => s.status === "SYNCING")) {
      // 分页获取云账号同步日志
      cloudAccountApi
        .pageSyncRecord({
          currentPage: 0,
          pageSize: syncRecordConfig.pageSize,
          cloudAccountId: props.id,
          createTime: currentDate.value?.toLocaleDateString(),
          status: searchStatus.value,
          description: searchDescription.value,
        })
        .then((ok) => {
          syncRecords.value.forEach((item) => {
            const find = ok.data.records.find(
              (o) => o.jobRecordId === item.jobRecordId
            );
            if (find) {
              item.status = find.status;
              item.params = find.params;
              item.updateTime = find.updateTime;
            }
          });
        });
    }
  }, 6000);
  if (
    router.currentRoute.value.query.go &&
    router.currentRoute.value.query.go === "sync"
  ) {
    const toElement: any = document.getElementById("record-main");
    //锚点存在跳转
    if (toElement) {
      //toElement.scrollIntoView();
    }
  }

  cloudAccountApi.listRecordTypes().then((res) => {
    _.forEach(res.data, (t) => {
      searchTypes.value.push({
        value: t,
        label: t,
      });
    });
  });
});

const check = () => {
  cloudAccountApi.verificationCloudAccount(props.id, loading).then(() => {
    ElMessage.success(t("native_state_valid_message", "云账号有效"));
    init();
  });
};

const deleteItem = () => {
  ElMessageBox.confirm(
    t("commons.message_box.confirm_delete", "确认删除"),
    t("commons.message_box.prompt", "提示"),
    {
      confirmButtonText: t("commons.btn.delete", "删除"),
      cancelButtonText: t("commons.btn.cancel", "取消"),
      type: "warning",
    }
  ).then(() => {
    cloudAccountApi.deleteCloudAccount(props.id).then(() => {
      ElMessage.success("删除成功");
      router.push({ name: "cloud_account_list" });
    });
  });
};

const buttonOperations = new TableOperations([
  TableOperations.buildButtons().newInstance(
    t("commons.btn.edit", "编辑"),
    "primary",
    openEditDialog,
    undefined,
    undefined,
    permissionStore.hasPermission("[management-center]CLOUD_ACCOUNT:EDIT")
  ),
  TableOperations.buildButtons().newInstance(
    t("cloud_account.verification", "连接校验"),
    "primary",
    check
  ),
  TableOperations.buildButtons().newInstance(
    "同步资源/账单",
    "primary",
    openSyncDialog,
    undefined,
    undefined,
    permissionStore.hasPermission("[management-center]CLOUD_ACCOUNT:EDIT")
  ),
  TableOperations.buildButtons().newInstance(
    t("cloud_account.edit_job_message", "数据同步设置"),
    "primary",
    editSyncSetting,
    undefined,
    undefined,
    permissionStore.hasPermission(
      "[management-center]CLOUD_ACCOUNT:SYNC_SETTING"
    )
  ),
  TableOperations.buildButtons().newInstance(
    t("commons.btn.delete", "删除"),
    "danger",
    deleteItem,
    undefined,
    undefined,
    permissionStore.hasPermission("[management-center]CLOUD_ACCOUNT:DELETE"),
    "#F54A45"
  ),
]);

const accountDrawerRef = ref<InstanceType<typeof EditAccount>>();

function openEditDialog() {
  accountDrawerRef.value?.open(props.id);
}

function afterSubmit() {
  init();
}

onBeforeUnmount(() => {
  closeInterval();
});
</script>

<template>
  <el-container style="padding: 24px; height: 100%">
    <el-header>
      <div class="header">
        {{ accountForm?.name }} - 详情
        <MoreOptionsButton
          style="float: right"
          name="操作"
          border
          :buttons="buttonOperations.buttons"
          :row="accountForm"
        />
      </div>
    </el-header>

    <el-scrollbar style="height: calc(100% - var(--el-header-height))">
      <el-main>
        <el-descriptions direction="vertical" :column="3">
          <template #title>
            <DetailFormTitle :title="t('commons.basic_info', '基本信息')" />
          </template>
          <el-descriptions-item width="33.33%">
            <template #label>
              <DetailFormLabel :label="t('cloud_account.name', '云账号名称')" />
            </template>
            <DetailFormValue v-if="!nameEditable">
              <div
                style="display: flex; flex-direction: row; align-items: center"
              >
                {{ accountForm?.name }}
                <el-icon
                  style="cursor: pointer; color: #3370ff; margin-left: 10px"
                  @click="editName"
                >
                  <EditPen />
                </el-icon>
              </div>
            </DetailFormValue>

            <div v-if="nameEditable">
              <div
                style="display: flex; flex-direction: row; align-items: center"
              >
                <el-form :inline="true" :model="simpleForm" ref="simpleFormRef">
                  <el-form-item
                    :rules="{
                      message: '云账号名称不能为空',
                      trigger: 'blur',
                      required: true,
                    }"
                    prop="name"
                  >
                    <el-input v-model="simpleForm.name" />
                  </el-form-item>
                  <el-form-item>
                    <el-icon
                      style="cursor: pointer; color: #34c724"
                      @click="saveName"
                    >
                      <Check />
                    </el-icon>

                    <el-icon
                      style="cursor: pointer; margin-left: 10px"
                      @click="cancel"
                    >
                      <Close />
                    </el-icon>
                  </el-form-item>
                </el-form>
              </div>
            </div>
          </el-descriptions-item>
          <el-descriptions-item width="33.33%">
            <template #label>
              <DetailFormLabel :label="t('cloud_account.platform', '云平台')" />
            </template>
            <DetailFormValue>
              <div
                style="
                  display: flex;
                  flex-direction: row;
                  flex-wrap: nowrap;
                  align-items: center;
                "
                v-if="accountForm?.platform"
              >
                <PlatformIcon :platform="accountForm?.platform"></PlatformIcon>

                {{
                  platformStore.platforms.find(
                    (p: Platform) => p.field === accountForm?.platform
                  ).label
                }}
              </div>
            </DetailFormValue>
          </el-descriptions-item>
          <el-descriptions-item width="33.33%">
            <template #label>
              <DetailFormLabel :label="t('commons.status', '状态')" />
            </template>
            <DetailFormValue>
              <div
                v-if="accountForm?.state"
                style="display: flex; align-items: center"
              >
                <el-icon color="green" style="margin-right: 8px">
                  <SuccessFilled />
                </el-icon>
                {{ t("cloud_account.native_state_valid", "有效") }}
              </div>
              <div v-else style="display: flex; align-items: center">
                <el-icon color="red" style="margin-right: 8px">
                  <CircleCloseFilled />
                </el-icon>
                {{ t("cloud_account.native_state_invalid", "无效") }}
              </div>
            </DetailFormValue>
          </el-descriptions-item>
          <el-descriptions-item width="33.33%">
            <template #label>
              <DetailFormLabel
                :label="t('cloud_account.balance.money', '账户余额')"
              />
            </template>
            <DetailFormValue>
              <span v-if="accountBalance != '--'">
                {{ CurrencyFormat.format(Number(accountBalance)) }}
              </span>
              <span v-else>
                {{ accountBalance }}
              </span>
            </DetailFormValue>
          </el-descriptions-item>
          <el-descriptions-item width="33.33%">
            <template #label>
              <DetailFormLabel :label="t('commons.create_time', '创建时间')" />
            </template>
            <DetailFormValue>{{ accountForm?.createTime }}</DetailFormValue>
          </el-descriptions-item>
        </el-descriptions>

        <el-descriptions direction="vertical" :column="3">
          <template #title>
            <DetailFormTitle :title="t('cloud_account.resource', '账户资源')" />
          </template>
          <el-descriptions-item
            width="33.33%"
            v-for="resourceCount in resourceCountArray"
            :key="resourceCount.icon"
          >
            <template #label>
              <DetailFormLabel>
                {{ resourceCount.name }}
                <span v-if="resourceCount.unit">
                  ({{ resourceCount.unit }})
                </span>
              </DetailFormLabel>
            </template>
            <DetailFormValue :value="resourceCount.count" />
          </el-descriptions-item>
        </el-descriptions>

        <DetailFormTitle
          style="margin-bottom: 16px"
          :title="t('cloud_account.sync.setting', '数据同步设置')"
        />

        <SyncJobTabView :id="id" />

        <DetailFormTitle
          style="margin-bottom: 16px"
          :title="t('cloud_account.sync.record', '同步记录')"
        />

        <div style="margin-bottom: 8px">
          <el-date-picker
            v-model="currentDate"
            type="date"
            :default-value="new Date()"
            format="YYYY-MM-DD"
            @change="initGetRecord"
          />

          <el-select
            v-model="searchDescription"
            @change="initGetRecord"
            style="margin-left: 6px; width: 180px"
          >
            <template #prefix>资源类型</template>
            <el-option
              v-for="(t, i) in searchTypes"
              :key="i"
              :label="t.label"
              :value="t.value"
            />
          </el-select>
          <el-select
            v-model="searchStatus"
            @change="initGetRecord"
            style="margin-left: 6px; width: 130px"
          >
            <template #prefix>状态</template>
            <el-option
              v-for="(t, i) in searchStatusList"
              :key="i"
              :label="t.label"
              :value="t.value"
            />
          </el-select>
        </div>

        <el-container style="height: 440px" v-loading="recordLoading">
          <el-aside width="210px">
            <div style="height: 10px"></div>
            <el-timeline
              class="infinite-list"
              v-infinite-scroll="load"
              :infinite-scroll-disabled="scrollDisabled"
              infinite-scroll-immediate="false"
              infinite-scroll-distance="1"
            >
              <el-timeline-item
                v-for="(item, index) in syncRecords"
                :key="index"
              >
                <div
                  class="infinite-list-item"
                  :class="
                    item.jobRecordId === selectedSyncRecord?.jobRecordId
                      ? item.status === 'SUCCESS'
                        ? 'success'
                        : item.status === 'FAILED'
                        ? 'failed'
                        : item.status === 'SYNCING'
                        ? 'syncing'
                        : 'failed'
                      : undefined
                  "
                  @click="showSyncRecordDetail(item)"
                >
                  <div class="timestamp">
                    {{ item.createTime }}
                  </div>
                  <div class="description">
                    <el-icon
                      size="14.67px"
                      :color="getColorByAccountStatus(item.status)"
                      :class="item.status === 'SYNCING' ? 'is-loading' : ''"
                    >
                      <component :is="getStatusIcon(item.status)" />
                    </el-icon>

                    {{ item.description }}
                  </div>
                </div>
              </el-timeline-item>
              <el-timeline-item
                v-if="loadingSyncRecord"
                style="text-align: center"
              >
                Loading...
              </el-timeline-item>
              <el-timeline-item
                v-if="noMoreSyncRecord"
                style="text-align: center"
              >
                No more
              </el-timeline-item>
            </el-timeline>
          </el-aside>
          <el-main style="padding: 0 24px">
            <el-scrollbar
              v-if="selectedSyncRecord"
              style="background: #f7f9fc; border-radius: 2px; padding: 0 16px"
            >
              <div style="height: 16px"></div>
              <div
                class="header"
                v-if="
                  selectedSyncRecord &&
                  selectedSyncRecord.type === 'CLOUD_ACCOUNT_SYNC_JOB'
                "
              >
                <span>{{ recordRegionDescription }}</span>
                &nbsp;&nbsp;&nbsp;
                <span>{{ recordResourceDescription }}</span>
              </div>
              <div
                class="header"
                v-if="
                  selectedSyncRecord &&
                  selectedSyncRecord.type === 'CLOUD_ACCOUNT_SYNC_BILL_JOB'
                "
              >
                <span>{{ recordBillDescription }}</span>
              </div>
              <div class="container" v-if="selectedSyncRecord">
                <table>
                  <tr>
                    <td align="right">
                      <DetailFormLabel :label="selectedSyncRecord.createTime" />
                    </td>
                    <td width="20px"></td>
                    <td>
                      <DetailFormValue padding-bottom="0">
                        {{ t("cloud_account.sync.start", "开始同步") }}
                      </DetailFormValue>
                    </td>
                  </tr>
                  <tr>
                    <td height="16px"></td>
                  </tr>

                  <template
                    v-for="(item, index) in selectedSyncRecord.params[
                      selectedSyncRecord.type
                    ]"
                    :key="index"
                  >
                    <template
                      v-if="
                        selectedSyncRecord.type === 'CLOUD_ACCOUNT_SYNC_JOB'
                      "
                    >
                      <tr>
                        <td align="right">
                          <DetailFormLabel
                            :label="
                              t('cloud_account.sync.area', '数据中心/区域')
                            "
                          />
                        </td>
                        <td width="20px"></td>
                        <td>
                          <DetailFormValue
                            padding-bottom="0"
                            :value="item.region.name"
                          />
                        </td>
                      </tr>

                      <tr>
                        <td align="right">
                          <DetailFormLabel>
                            已{{ selectedSyncRecord.description }}
                          </DetailFormLabel>
                        </td>
                        <td width="20px"></td>
                        <td>
                          <DetailFormValue padding-bottom="0">
                            {{ item.size }} 个
                          </DetailFormValue>
                        </td>
                      </tr>
                      <tr>
                        <td height="16px"></td>
                      </tr>
                    </template>

                    <template
                      v-if="
                        selectedSyncRecord.type ===
                        'CLOUD_ACCOUNT_SYNC_BILL_JOB'
                      "
                    >
                      <tr>
                        <td align="right">
                          <DetailFormLabel label="月份" />
                        </td>
                        <td width="20px"></td>
                        <td>
                          <DetailFormValue
                            padding-bottom="0"
                            :value="JSON.parse(item.month)"
                          />
                        </td>
                      </tr>

                      <tr>
                        <td align="right">
                          <DetailFormLabel>
                            已{{ selectedSyncRecord.description }}
                          </DetailFormLabel>
                        </td>
                        <td width="20px"></td>
                        <td>
                          <DetailFormValue padding-bottom="0">
                            {{ item.size }} 个
                          </DetailFormValue>
                        </td>
                      </tr>

                      <tr>
                        <td align="right">
                          <DetailFormLabel> 累计 </DetailFormLabel>
                        </td>
                        <td width="20px"></td>
                        <td>
                          <DetailFormValue padding-bottom="0">
                            {{ item.sum }}元
                          </DetailFormValue>
                        </td>
                      </tr>

                      <tr>
                        <td height="16px"></td>
                      </tr>
                    </template>
                  </template>

                  <template v-if="selectedSyncRecord.status === 'SYNCING'">
                    <tr>
                      <td align="right">
                        <DetailFormLabel>
                          {{ t("cloud_account.sync.synchronizing", "同步中") }}
                        </DetailFormLabel>
                      </td>
                      <td width="20px"></td>
                      <td>
                        <el-icon
                          size="14.67px"
                          :color="
                            getColorByAccountStatus(selectedSyncRecord.status)
                          "
                          :class="
                            selectedSyncRecord.status === 'SYNCING'
                              ? 'is-loading'
                              : ''
                          "
                        >
                          <component
                            :is="getStatusIcon(selectedSyncRecord.status)"
                          />
                        </el-icon>
                      </td>
                    </tr>
                  </template>

                  <template v-else-if="selectedSyncRecord.status !== 'SYNCING'">
                    <tr>
                      <td align="right">
                        <DetailFormLabel>
                          {{ selectedSyncRecord.updateTime }}
                        </DetailFormLabel>
                      </td>
                      <td width="20px"></td>
                      <td>
                        {{ t("cloud_account.sync.end", "结束同步") }}
                      </td>
                    </tr>
                  </template>
                </table>
              </div>

              <span v-else>
                {{ t("cloud_account.sync.noDetail", "没有同步信息") }}
              </span>

              <div style="height: 16px"></div>
            </el-scrollbar>
          </el-main>
        </el-container>
      </el-main>
    </el-scrollbar>

    <SyncAccountDialog ref="syncAccountDialogRef" />

    <EditAccount ref="accountDrawerRef" @submit="afterSubmit" />
  </el-container>
</template>

<style lang="scss" scoped>
.el-header {
  --el-header-padding: 0;
  --el-header-height: 44px;
  border-bottom: var(--el-border-color) 1px solid;
  color: #1f2329;

  .header {
    font-style: normal;
    font-weight: 500;
    font-size: 16px;
    line-height: 24px;
    padding: 0 0 20px 0;
  }
}

.el-main {
  --el-main-padding: 20px 0;
}
.el-aside {
  border-right: 1px solid #dee0e3;
}

.infinite-list {
  width: 100%;

  .infinite-list-item {
    width: 148px;
    height: 58px;
    border-radius: 4px;
    cursor: pointer;
    padding: 8px;

    &.success {
      background: #effbed;
    }
    &.failed {
      background: #fef1f0;
    }
    &.syncing {
      background: #ebf1ff;
    }

    .timestamp {
      font-style: normal;
      font-weight: 400;
      font-size: 14px;
      line-height: 22px;
      height: 22px;
      color: #8f959e;
    }

    .description {
      font-style: normal;
      font-weight: 400;
      font-size: 14px;
      line-height: 22px;
      height: 22px;
      color: #1f2329;
      margin-top: 4px;
    }
  }
}

ul {
  padding-inline-start: 0;
}

.header {
  font-style: normal;
  font-weight: 500;
  font-size: 12px;
  line-height: 22px;

  color: #1f2329;

  padding-bottom: 16px;
}
</style>
