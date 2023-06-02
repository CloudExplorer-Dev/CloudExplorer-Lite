<script setup lang="ts">
import { platformIcon } from "@commons/utils/platform";
import { ref, onMounted, onBeforeUnmount } from "vue";
import {
  PaginationConfig,
  SearchConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import cloudAccountApi from "@/api/cloud_account/index";
import type { CloudAccount, AccountJobRecord } from "@/api/cloud_account/type";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import type { SimpleMap } from "@commons/api/base/type";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import MoreOptionsButton from "@commons/components/ce-table/MoreOptionsButton.vue";
import SyncAccountDialog from "./SyncAccountDialog.vue";

import {
  getStatusIcon,
  getColorByAccountStatus,
} from "@/componnets/StatusIconConstant";
import EditAccount from "@/views/CloudAccount/edit.vue";

const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();
const { t } = useI18n();
// 路由实例对象
const router = useRouter();
// 表格数据
const cloudAccountList = ref<Array<CloudAccount>>([]);
// 查询数据
const tableSearch = ref<TableSearch>();
// table组建实例
const table: any = ref(null);
// 选中的云账号id
const multipleSelectionIds = ref<Array<string>>([]);
// 列表字段数据
const columns = ref([]);
// 获得云平台过滤数据
const platformFilters = Object.keys(platformIcon).map((platform: string) => {
  return { text: platformIcon[platform].name, value: platform };
});

const accountDrawerRef = ref<InstanceType<typeof EditAccount>>();
const syncAccountDialogRef = ref<InstanceType<typeof SyncAccountDialog>>();

function openSyncDialog(row: CloudAccount) {
  syncAccountDialogRef.value?.open(row);
}

/**
 * 去修改页面
 * @param row 当前这一行数据
 */
const edit = (row: CloudAccount) => {
  accountDrawerRef.value?.open(row.id);
};
function afterSubmit() {
  search(new TableSearch());
}

/**
 * 展示云账号详情
 * @param row
 */
const showAccountDetail = (row: CloudAccount, go?: string) => {
  if (go) {
    router.push({
      name: "cloud_account_detail",
      params: { id: row.id },
    });
  } else {
    router.push({
      name: "cloud_account_detail",
      params: { id: row.id },
      query: { go: "sync" },
    });
  }
};

/**
 * 删除这一行数据,根据id
 * @param row 当前这一行数据
 */
const deleteItem = (row: CloudAccount) => {
  ElMessageBox.confirm(
    t("commons.message_box.confirm_delete", "确认删除"),
    t("commons.message_box.prompt", "提示"),
    {
      confirmButtonText: t("commons.btn.delete", "删除"),
      cancelButtonText: t("commons.btn.cancel", "取消"),
      type: "warning",
    }
  ).then(() => {
    cloudAccountApi.deleteCloudAccount(row.id).then(() => {
      ElMessage.success("删除成功");
      table.value?.search();
    });
  });
};
/**
 * 校验云账号是否有效
 * @param row 当前这一行数据
 */
const check = (row: CloudAccount) => {
  cloudAccountApi.verificationCloudAccount(row.id, loading).then(() => {
    ElMessage.success(t("native_state_valid_message", "云账号有效"));
    table.value?.search();
  });
};
/**
 * 去数据同步设置页面
 * @param row 当前这一行数据
 */
const updateJob = (row: CloudAccount) => {
  router.push({ name: "cloud_account_sync_job", params: { id: row.id } });
};

/**
 * 全选
 * @param val 全选数据
 */
const handleSelectionChange = (val: CloudAccount[]) => {
  multipleSelectionIds.value = val.map((item) => item.id);
};

/**
 * 批量删除数据
 */
const batchDelete = () => {
  if (!multipleSelectionIds.value) {
    ElMessage.error(
      t("cloud_account.cloud_account_size", "云账号必须选择一条")
    );
    return;
  }
  ElMessageBox.confirm(
    t("commons.message_box.confirm_delete", "确认删除"),
    t("commons.message_box.prompt", "提交"),
    {
      confirmButtonText: t("commons.btn.delete", "删除"),
      cancelButtonText: t("commons.btn.cancel", "取消"),
      type: "warning",
    }
  ).then(() => {
    if (multipleSelectionIds.value) {
      cloudAccountApi
        .batchDeleteCloudAccount(multipleSelectionIds.value)
        .then(() => {
          table.value?.search();
        });
    }
  });
};

const search = (condition: TableSearch) => {
  tableSearch.value = condition;
  const params = TableSearch.toSearchParams(condition);
  cloudAccountApi
    .page(
      {
        currentPage: tableConfig.value.paginationConfig.currentPage,
        pageSize: tableConfig.value.paginationConfig.pageSize,
        ...params,
      },
      loading
    )
    .then((ok) => {
      cloudAccountApi
        .getAccountJobRecord(ok.data.records.map((r) => r.id))
        .then((data) => {
          cloudAccountJobRecord.value = data.data;
        });
      cloudAccountList.value = ok.data.records;
      tableConfig.value.paginationConfig?.setTotal(
        ok.data.total,
        tableConfig.value.paginationConfig
      );
      tableConfig.value.paginationConfig?.setCurrentPage(
        ok.data.current,
        tableConfig.value.paginationConfig
      );
    });
};
let cloudAccountInterval: any;

const cloudAccountJobRecord = ref<SimpleMap<Array<AccountJobRecord>>>({});
onMounted(() => {
  search(new TableSearch());
  cloudAccountInterval = setInterval(() => {
    const cloudAccountIds = cloudAccountList.value.map((r) => r.id);
    if (cloudAccountIds.length === 0) {
      return;
    }
    cloudAccountApi.getAccountJobRecord(cloudAccountIds).then((data) => {
      Object.keys(data.data).forEach((key) => {
        data.data[key].sort((pre: any, next: any) =>
          (pre.type + pre.description).localeCompare(
            next.type + next.description
          )
        );
      });

      cloudAccountJobRecord.value = data.data;
    });
  }, 8000);
});

onBeforeUnmount(() => {
  if (cloudAccountInterval) {
    clearInterval(cloudAccountInterval);
  }
});

// 列表查询 loading
const loading = ref<boolean>(false);

/**
 * 去添加云账号页面
 */
const create = () => {
  router.push({ name: "cloud_account_create" });
};

const getStatusByAccountId = (cloudAccountId: string) => {
  const list = cloudAccountJobRecord.value[cloudAccountId];
  if (list) {
    if (list.some((job) => job.status === "FAILED")) {
      return "FAILED";
    }
    if (list.every((job) => job.status === "SUCCESS")) {
      return "SUCCESS";
    }
    return "SYNCING";
  }
  return "INIT";
};

const mapStatus = (status: string) => {
  return status === "FAILED"
    ? t("cloud_account.native_sync.failed", "同步失败")
    : status === "INIT"
    ? t("cloud_account.native_sync.init", "初始化")
    : status === "SUCCESS"
    ? t("cloud_account.native_sync.success", "同步成功")
    : status === "SYNCING"
    ? t("cloud_account.native_sync.syncing", "同步中")
    : status === "TIME_OUT"
    ? "超时"
    : t("cloud_account.native_sync.unknown", "未知");
};

/**
 * 账单同步是否显示
 * @param row 云账号
 */
const billSyncShow = (row: CloudAccount) => {
  const showPlatforms = [
    "fit2cloud_ali_platform",
    "fit2cloud_huawei_platform",
    "fit2cloud_tencent_platform",
  ];
  return (
    showPlatforms.includes(row.platform) &&
    moduleStore.runningModules?.some(
      (m: any) => m.id === "finance-management"
    ) &&
    permissionStore.hasPermission("[management-center]CLOUD_ACCOUNT:SYNC_BILL")
  );
};

//-------------------------------同步账单    END--------------------

/**
 * 表单配置
 */
const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    // 查询函数
    search: search,
    quickPlaceholder: "搜索",
    components: [
      SearchConfig.buildComponent().DateComponent.newInstance(
        "createTime",
        t("commons.create_time", "创建时间")
      ),
      SearchConfig.buildComponent().DateComponent.newInstance(
        "updateTime",
        t("commons.update_time", "编辑时间")
      ),
    ],
    searchOptions: [{ label: t("commons.name", "名称"), value: "name" }],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([
    TableOperations.buildButtons().newInstance(
      t("commons.btn.edit", "编辑"),
      "primary",
      edit,
      undefined,
      undefined,
      permissionStore.hasPermission("[management-center]CLOUD_ACCOUNT:EDIT")
    ),
    /*TableOperations.buildButtons().newInstance(
      t("cloud_account.verification", "连接校验"),
      "primary",
      check,
      "Search"
    ),*/
    /*TableOperations.buildButtons().newInstance(
      t("cloud_account.sync.syncResource", "同步资源"),
      "primary",
      openSync,
      "Refresh",
      undefined,
      permissionStore.hasPermission(
        "[management-center]CLOUD_ACCOUNT:SYNC_RESOURCE"
      )
    ),
    TableOperations.buildButtons().newInstance(
      t("cloud_account.sync.syncBill", "同步账单"),
      "primary",
      openBillSync,
      "Refresh",
      undefined,
      billSyncShow
    ),*/
    TableOperations.buildButtons().newInstance(
      t("cloud_account.edit_job_message", "数据同步设置"),
      "primary",
      updateJob,
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
  ]),
});

const syncAll = () => {
  const _list = cloudAccountList.value.filter((a) =>
    multipleSelectionIds.value.includes(a.id)
  );
  if (_list && _list.length > 0 && _list.every((a) => a.state)) {
    cloudAccountApi.syncAll(multipleSelectionIds.value).then((ok) => {
      ElMessage.success("发送同步任务成功");
    });
  } else {
    ElMessage.error("请选择有效的云账号");
  }
};
</script>
<template>
  <ce-table
    localKey="cloudAccountTable"
    height="100%"
    ref="table"
    :columns="columns"
    :data="cloudAccountList"
    :tableConfig="tableConfig"
    show-selected-count
    v-loading="loading"
    @selection-change="handleSelectionChange"
    row-key="id"
  >
    <template #toolbar>
      <el-button
        type="primary"
        @click="create"
        v-hasPermission="'[management-center]CLOUD_ACCOUNT:CREATE'"
      >
        {{ t("commons.btn.add", "添加") }}
      </el-button>
      <el-button
        @click="syncAll"
        v-hasPermission="'[management-center]CLOUD_ACCOUNT:SYNC_RESOURCE'"
      >
        {{ t("commons.btn.full_sync", "全量同步") }}
      </el-button>
      <el-button
        @click="batchDelete"
        v-hasPermission="'[management-center]CLOUD_ACCOUNT:DELETE'"
      >
        {{ t("commons.btn.delete", "删除") }}
      </el-button>
    </template>
    <el-table-column type="selection" />
    <el-table-column
      min-width="150"
      :label="t('commons.name', '名称')"
      column-key="platform"
      :filters="platformFilters"
      prop="platform"
      fixed
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <component
            style="margin-right: 8px"
            :is="platformIcon[scope.row.platform]?.component"
            v-bind="platformIcon[scope.row.platform]?.icon"
            :color="platformIcon[scope.row.platform]?.color"
            size="16px"
          ></component>
          <span
            style="cursor: pointer; color: var(--el-color-primary)"
            @click="showAccountDetail(scope.row, 'detail')"
          >
            {{ scope.row.name }}
          </span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      min-width="150"
      prop="state"
      :label="t('cloud_account.native_state', '连接状态')"
      column-key="state"
      sortable
      :filters="[
        { text: '连接成功', value: true },
        { text: '连接失败', value: false },
      ]"
    >
      <template #default="scope">
        <div class="status-tip" :class="scope.row.state ? 'valid' : 'invalid'">
          {{ scope.row.state ? "连接成功" : "连接失败" }}
        </div>
      </template>
    </el-table-column>
    <el-table-column
      min-width="150"
      column-key="status"
      prop="status"
      :label="t('cloud_account.native_sync_status', '同步状态')"
      :filters="[
        { text: t('cloud_account.native_sync.init', '初始化'), value: 'INIT' },
        {
          text: t('cloud_account.native_sync.success', '同步成功'),
          value: 'SUCCESS',
        },
        {
          text: t('cloud_account.native_sync.failed', '同步失败'),
          value: 'FAILED',
        },
        {
          text: t('cloud_account.native_sync.syncing', '同步中'),
          value: 'SYNCING',
        },
      ]"
      sortable
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <el-tooltip class="box-item" effect="dark" placement="top-start">
            <template #content>
              <div
                v-for="j in cloudAccountJobRecord[scope.row.id] || []"
                :key="j.jobRecordId"
                style="
                  display: flex;
                  justify-content: space-between;
                  width: 300px;
                "
              >
                <div style="width: 120px">{{ j.description }}</div>
                <div>{{ mapStatus(j.status) }}</div>
                <div>{{ j.createTime }}</div>
              </div>
            </template>
            <div
              @click="showAccountDetail(scope.row)"
              style="
                display: flex;
                cursor: pointer;
                flex-flow: row nowrap;
                align-items: center;
              "
            >
              <el-icon
                size="14.67px"
                :color="
                  getColorByAccountStatus(getStatusByAccountId(scope.row.id))
                "
                :class="
                  getStatusByAccountId(scope.row.id) === 'SYNCING'
                    ? 'is-loading'
                    : ''
                "
              >
                <component
                  :is="getStatusIcon(getStatusByAccountId(scope.row.id))"
                />
              </el-icon>
              <span style="margin-left: 6px">
                {{ mapStatus(getStatusByAccountId(scope.row.id)) }}
              </span>
            </div>
          </el-tooltip>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      min-width="155"
      prop="updateTime"
      :label="t('cloud_account.last_sync_time', '最近同步时间')"
      sortable
    />
    <el-table-column
      min-width="155"
      prop="createTime"
      :label="t('commons.create_time', '创建时间')"
      sortable
    />

    <el-table-column min-width="165" label="操作" fixed="right">
      <template #default="scope">
        <div
          style="
            padding: 0 9px;
            width: auto;
            display: inline-flex;
            flex-direction: row;
            flex-wrap: nowrap;
            align-items: center;
          "
        >
          <el-button
            link
            @click="openSyncDialog(scope.row)"
            type="primary"
            v-if="
              permissionStore.hasPermission(
                '[management-center]CLOUD_ACCOUNT:SYNC_RESOURCE'
              ) || billSyncShow(scope.row)
            "
          >
            {{ t("cloud_account.sync.sync", "同步") }}
          </el-button>
          <el-button link @click="check(scope.row)" type="primary">
            {{ t("cloud_account.verification", "连接校验") }}
          </el-button>
          <MoreOptionsButton
            style="margin-left: 5px"
            :buttons="tableConfig.tableOperations.buttons"
            :row="scope.row"
          />
        </div>
      </template>
    </el-table-column>

    <template #buttons>
      <CeTableColumnSelect :columns="columns" />
    </template>
  </ce-table>

  <SyncAccountDialog ref="syncAccountDialogRef" />

  <EditAccount ref="accountDrawerRef" @submit="afterSubmit" />
</template>

<style lang="scss" scoped>
.status-tip {
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  padding: 1px 6px;
  width: 68px;
  height: 24px;
  border-radius: 2px;
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 22px;

  &.valid {
    background: rgba(52, 199, 36, 0.2);
    color: #2ea121;
  }
  &.invalid {
    background: rgba(245, 74, 69, 0.2);
    color: #d83931;
  }
}
</style>
