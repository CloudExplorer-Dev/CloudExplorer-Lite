<script setup lang="ts">
import { computed, onMounted, reactive, ref } from "vue";
import type { FormRules, FormInstance } from "element-plus";
import { useI18n } from "vue-i18n";
import cloudAccountApi from "@/api/cloud_account";
import { useRouter } from "vue-router";
import { platformIcon } from "@/utils/platform";
import { ElMessage } from "element-plus";
import _ from "lodash";
import Job from "./job.vue";
import type { AccountJobRecord, ResourceCount } from "@/api/cloud_account/type";
import { PaginationConfig } from "@commons/components/ce-table/type";

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
const loadingSyncRecord = ref(false); // 同步记录加载标识
const syncRecords = ref<Array<AccountJobRecord>>([]); // 同步记录列表
const syncRecordTotal = ref(0); // 同步记录总数
const selectedSyncRecord = ref<AccountJobRecord>(); // 同步记录详情
const syncRecordConfig = new PaginationConfig(1, 10); // 同步记录列表分页查询配置
const recordRegionDescription = ref<number | string>();
const recordResourceDescription = ref<string>();

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

    // 分页获取云账号同步日志
    cloudAccountApi
      .pageSyncRecord({
        currentPage: syncRecordConfig.currentPage,
        pageSize: syncRecordConfig.pageSize,
        cloudAccountId: cloudAccountId.value,
      })
      .then((ok) => {
        syncRecords.value = _.cloneDeep(ok.data.records);
        syncRecordTotal.value = _.cloneDeep(ok.data.total);
        if (syncRecords.value.length > 0) {
          showSyncRecordDetail(syncRecords.value[0]);
        }
      });
  }
};

// 是否还有同步记录需要加载
const noMoreSyncRecord = computed(
    () => syncRecords.value.length >= syncRecordTotal.value
);
// 是否允许继续滚动加载
const scrollDisabled = computed(
    () => loadingSyncRecord.value || noMoreSyncRecord.value
);
const statusFilter = (status: string) => {
  if (status.toUpperCase() === "SUCCESS") {
    return t("cloud_account.native_sync.success","同步成功");
  }
  if (status.toUpperCase() === "FAILED") {
    return t("cloud_account.native_sync.failed","同步失败");
  }
  if (status.toUpperCase() === "SYNCING") {
    return t("cloud_account.native_sync.syncing","同步中");
  }
  return status;
};

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
        cloudAccountId: cloudAccountId.value,
      })
      .then((ok) => {
        syncRecordConfig.currentPage = ok.data.current + 1;
        syncRecords.value = [...syncRecords.value, ...ok.data.records];
        loadingSyncRecord.value = false;
      });
};

/**
 * 展示同步记录详情
 */
const showSyncRecordDetail = (syncRecord: AccountJobRecord) => {
  selectedSyncRecord.value = syncRecord;
  let resourceCount = 0;
  selectedSyncRecord.value.params.forEach((item) => {
    resourceCount = resourceCount + item.size;
  });
  recordRegionDescription.value =
      t("cloud_account.sync.finishArea", "已同步区域") +
      selectedSyncRecord.value.params.length +
      t("cloud_account.sync.unit", "个");
  recordResourceDescription.value =
      "已" +
      selectedSyncRecord.value.description +
      resourceCount +
      t("cloud_account.sync.unit", "个");
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
                    style="margin-left: 10px; display: flex"
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
                  <div
                    v-if="accountForm.state"
                    style="display: flex; align-items: center"
                  >
                    <span>{{
                      t("cloud_account.native_state_valid", "有效")
                    }}</span>
                    <el-icon color="green" style="margin-left: 5px"
                      ><SuccessFilled
                    /></el-icon>
                  </div>
                  <div
                    v-if="!accountForm.state"
                    style="display: flex; align-items: center"
                  >
                    <span>{{
                      t("cloud_account.native_state_invalid", "无效")
                    }}</span>
                    <el-icon color="red" style="margin-left: 5px"
                      ><CircleCloseFilled
                    /></el-icon>
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
          <span
            style="line-height: 30px; padding-left: 20px; font-size: 26px"
            >{{ accountBalance }}</span
          >
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
        <div
          class="item"
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
          <div class="right">
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

  <layout-container>
    <template #header>
      <span>{{ $t("cloud_account.sync.record", "同步记录") }}</span>
    </template>
    <template #content>
      <div class="record-main">
        <div class="record-left">
          <div
            class="content"
            v-infinite-scroll="load"
            :infinite-scroll-disabled="scrollDisabled"
            infinite-scroll-immediate="false"
            infinite-scroll-distance="1"
          >
            <div class="container">
              <span class="label">{{
                $t("cloud_account.sync.time", "同步时间")
              }}</span>
              <span class="label">{{
                $t("cloud_account.sync.resource", "同步资源")
              }}</span>
              <span class="label">{{
                $t("cloud_account.sync.status", "同步状态")
              }}</span>
            </div>
            <div
              @click="showSyncRecordDetail(item)"
              :class="
                item.jobRecordId === selectedSyncRecord.jobRecordId
                  ? 'container-details-active'
                  : ''
              "
              class="container container-details"
              v-for="item in syncRecords"
              :key="item"
            >
              <span class="label">{{ item.createTime }}</span>
              <span class="label">{{ item.description }}</span>
              <span class="label">{{ statusFilter(item.status)}}
                <el-icon v-if="item.status === 'SUCCESS' " color="green" style="margin-left: 5px"><SuccessFilled/></el-icon>
                <el-icon v-if="item.status === 'FAILED'" color="red" style="margin-left: 5px"><CircleCloseFilled/></el-icon>
                <el-icon v-if="item.status === 'SYNCING'" color="gray" style="margin-left: 5px"><Loading/></el-icon>
              </span>
            </div>
            <p v-if="loadingSyncRecord" style="text-align: center">
              Loading...
            </p>
            <p v-if="noMoreSyncRecord" style="text-align: center">No more</p>
          </div>
        </div>
        <div class="record-right">
          <div class="header" v-if="selectedSyncRecord">
            <span>{{ selectedSyncRecord.createTime }}</span>
            <span>{{ $t("cloud_account.sync.detail", "详情") }}：</span>
            <span>{{ recordRegionDescription }}；</span>
            <span>{{ recordResourceDescription }}；</span>
          </div>
          <div class="line"></div>
          <div class="content">
            <div class="container" v-if="selectedSyncRecord">
              <div class="start">
                <div class="split-left">
                  {{ selectedSyncRecord.createTime }}：
                </div>
                <div>{{ $t("cloud_account.sync.start", "开始同步") }}</div>
              </div>

              <div
                class="middle"
                v-for="item in selectedSyncRecord.params"
                :key="item"
              >
                <div class="layout">
                  <div class="split-left">
                    {{ $t("cloud_account.sync.area", "数据中心/区域") }}：
                  </div>
                  <div>{{ item.region.name }}</div>
                </div>
                <div class="layout">
                  <div class="split-left">
                    已{{ selectedSyncRecord.description }}：
                  </div>
                  <div>{{ item.size }} 个</div>
                </div>
              </div>

              <div class="end">
                <div class="split-left">
                  {{ selectedSyncRecord.updateTime }}：
                </div>
                <div>{{ $t("cloud_account.sync.end", "结束同步") }}</div>
              </div>
            </div>
            <span v-else>{{
              $t("cloud_account.sync.noDetail", "没有同步信息")
            }}</span>
          </div>
        </div>
      </div>
    </template>
  </layout-container>
</template>

<style lang="scss">
.resourceContainer {
  display: flex;
  padding: 20px;
  .item {
    width: 200px;
    display: flex;
    .iconStyle {
      cursor: pointer;
      height: 60px;
      width: 50%;
      color: gray;
    }
    .right {
      width: 50%;
      display: flex;
      flex-wrap: wrap;
      align-content: center;
      .content {
        width: 100%;
        padding: 5px;
      }
    }
  }
}
.record-main {
  border: 1px solid var(--el-border-color);
  width: 100%;
  height: 100%;
  display: flex;
  .record-left {
    width: 50%;
    border-right: 1px solid var(--el-border-color);
    .content {
      height: 200px;
      padding-top: 20px;
      overflow: auto;
      .container {
        display: flex;
        width: 100%;
        padding: 10px 0 10px 0;
        .label {
          width: 50%;
          float: left;
          text-align: center;
        }
      }
    }
  }
  .record-right {
    width: 50%;
    .header {
      height: 50px;
      align-items: center;
      font-size: 15px;
      display: flex;
      padding-left: 20px;
    }
    .line {
      height: 1px;
      background-color: var(--el-border-color);
    }
    .content {
      height: 150px;
      overflow: auto;
      .container {
        padding: 20px 0px 0px 20px;
        .start {
          display: flex;
          margin-bottom: 20px;
          width: 100%;
        }
        .middle {
          margin-bottom: 20px;
          width: 100%;
          .layout {
            display: flex;
            width: 100%;
            .split-left {
              min-width: 150px;
              text-align: right;
            }
          }
        }
        .end {
          display: flex;
          width: 100%;
        }
      }
    }
  }
}

.container-details {
  &:hover {
    background-color: var(--el-menu-hover-bg-color);
    cursor: pointer;
    color: inherit;
  }
}

.container-details-active {
  background-color: var(--el-menu-hover-bg-color);
}
</style>
