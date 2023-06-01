<script setup lang="ts">
import { platformIcon } from "@commons/utils/platform";
import { ref, onMounted, computed, onBeforeUnmount } from "vue";
import {
  PaginationConfig,
  SearchConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import cloudAccountApi from "@/api/cloud_account/index";
import type {
  CloudAccount,
  Region,
  ResourceSync,
  SyncRequest,
  AccountJobRecord,
} from "@/api/cloud_account/type";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import type { FormInstance, FormRules } from "element-plus";
import type { SimpleMap } from "@commons/api/base/type";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import MoreOptionsButton from "@commons/components/ce-table/MoreOptionsButton.vue";

import {
  getStatusIcon,
  getColorByAccountStatus,
} from "@/componnets/StatusIconConstant";

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

/**
 * 去修改页面
 * @param row 当前这一行数据
 */
const edit = (row: CloudAccount) => {
  router.push({ name: "cloud_account_update", params: { id: row.id } });
};

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
const resources = ref<Array<ResourceSync>>([]);

/**
 * 同步云账号
 */
const syncCloudAccountId = ref<string>("");
/**
 * 区域加载器
 */
const regionsLoading = ref<boolean>(false);
/**
 * 同步资源加载器
 */
const resourceLoading = ref<boolean>(false);
/**
 * 打开同步弹出框
 */

function openSyncDialog(row: CloudAccount) {}

const openSync = (row: CloudAccount) => {
  // 打开弹出框
  syncVisible.value = true;
  // 同步云账号
  syncCloudAccountId.value = row.id;
  // 获取当前云账号区域
  cloudAccountApi.getRegions(row.id, regionsLoading).then((ok) => {
    regions.value = ok.data;
    syncForm.value.checkedRegions = ok.data.map((r) => r.regionId);
  });
  // 获取同步的资源
  cloudAccountApi.getResourceSync(row.id, resourceLoading).then((ok) => {
    resources.value = ok.data;
    syncForm.value.checkedResources = ok.data.map((r: any) => r.jobName);
  });
};

//------------------------ 点击按钮同步 START-------------------
const syncVisible = ref<boolean>(false);
/**
 * 区域
 */
const regions = ref<Array<Region>>([]);

/**
 * 去添加云账号页面
 */
const create = () => {
  router.push({ name: "cloud_account_create" });
};

/**
 * 资源是否全选
 */
const resourcesCheckedAll = computed(() => {
  return syncForm.value.checkedResources.length === resources.value.length;
});

/**
 * 同步任务发布
 */
const sync = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.validate((valid) => {
    if (valid) {
      // 关闭弹出框
      syncVisible.value = false;
      // 构建同步对象
      const syncSubmit: SyncRequest = {
        cloudAccountId: syncCloudAccountId.value,
        params: {
          REGIONS: regions.value.filter((r) =>
            syncForm.value.checkedRegions.includes(r.regionId)
          ),
        },
        syncJob: resources.value
          .filter((r) => syncForm.value.checkedResources.includes(r.jobName))
          .map((source) => {
            return {
              module: source.module,
              jobName: source.jobName,
              jobGroup: source.jobGroup,
            };
          }),
      };
      // 发送同步任务
      cloudAccountApi.syncJob(syncSubmit).then((ok) => {
        ElMessage.success("发送同步任务成功");
      });
    }
  });
};
/**
 * 是否全选
 */
const checkAll = computed(() => {
  return syncForm.value.checkedRegions.length === regions.value.length;
});

/**
 * 全选资源
 * @param val true 全选,false 全不选
 */
const handleCheckAllResource = (val: boolean) => {
  if (val) {
    syncForm.value.checkedResources = resources.value.map(
      (region) => region.jobName
    );
  } else {
    syncForm.value.checkedResources = [];
  }
};

/**
 *
 * @param selectResources 资源
 */
const changeResource = (selectResources: Array<string>) => {
  syncForm.value.checkedResources = selectResources;
};
/**
 * 同步表单
 */
const syncForm = ref<{
  /**
   * 选中的资源
   */
  checkedResources: Array<string>;
  /**
   * 选中的区域
   */
  checkedRegions: Array<string>;
}>({
  checkedResources: [],
  checkedRegions: [],
});
// 校验规则
const ruleFormRef = ref<FormInstance>();
// 校验规则
const syncRules = ref<FormRules>({
  checkedResources: [
    {
      required: true,
      message: "同步资源必须选择",
      trigger: "change",
    },
  ],
  checkedRegions: [
    {
      required: true,
      message: "同步区域必须选择",
      trigger: "change",
    },
  ],
});
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
 * 全选改变触发函数
 * @param val 改变的值
 */
const handleCheckAllChange = (val: boolean) => {
  if (val) {
    syncForm.value.checkedRegions = regions.value.map(
      (region) => region.regionId
    );
  } else {
    syncForm.value.checkedRegions = [];
  }
};

/**
 * 区域选中和取消的时候触发
 * @param selectRegion
 */
const change = (selectRegion: Array<string>) => {
  syncForm.value.checkedRegions = selectRegion;
};
//-------------------------------点击按钮同步 END----------------

//-------------------------------同步账单    START--------------

/**
 *当前时间
 */
const currentDate = new Date();

/**
 *获取12个月之前到现在的月份
 */
const months = ref<Array<string>>(
  Array.from({ length: 12 })
    .map((item, index) => {
      return index;
    })
    .map((num) => {
      const date = new Date(
        currentDate.getFullYear(),
        currentDate.getMonth() - (num as number)
      );
      const month = date.getMonth() + 1;
      return (
        date.getFullYear().toString() +
        "-" +
        (month.toString().length === 1
          ? "0" + month.toString()
          : month.toString())
      );
    })
);

// 校验规则
const ruleFormSyncBill = ref<FormInstance>();
/**
 * 账单同步表单
 */
const billSyncForm = ref<{
  months: Array<string>;
  syncBillType: string;
  bucketSyncCycle: "current" | "all";
}>({
  months: months.value,
  syncBillType: "api",
  bucketSyncCycle: "current",
});

/**
 *账单是否展示
 */
const billSyncView = ref<boolean>(false);
const billSyncLoading = ref<boolean>(false);
/**
 *打开账单同步
 */
const openBillSync = (row: CloudAccount) => {
  cloudAccountApi.getJobs(row.id, billSyncLoading).then((ok) => {
    const financeManagement = ok.data.cloudAccountModuleJobs.find(
      (m) => m.module === "finance-management"
    );
    if (financeManagement) {
      if (
        financeManagement.jobDetailsList &&
        financeManagement.jobDetailsList.length > 0
      ) {
        billSyncForm.value.syncBillType =
          financeManagement.jobDetailsList[0].params["BILL_SETTING"][
            "syncMode"
          ];
      }
    }
  });
  billSyncView.value = true;
  syncCloudAccountId.value = row.id;
  billSyncForm.value.months = months.value;
  billMonthAll.value = months.value.length === billSyncForm.value.months.length;
};

/**
 * 校验规则
 */
const billSyncRules = ref<FormRules>({
  months: [
    {
      required: true,
      message: "同步月份必须选择",
      trigger: "change",
    },
  ],
});

/**
 * 全选选中或者取消触发函数
 * @param selected 选中还是取消
 */
const handleCheckAllBillMonthChange = (selected: boolean) => {
  billSyncForm.value.months = selected ? months.value : [];
};

/**
 * 账单月份全选
 */
const billMonthAll = ref<boolean>(
  months.value.length === billSyncForm.value.months.length
);

/**
 * 月份改变调用函数
 * @param checkedMonths 选中的月份
 */
const changeMonths = (checkedMonths: Array<string>) => {
  billSyncForm.value.months = checkedMonths;
  billMonthAll.value = months.value.length === checkedMonths.length;
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

/**
 * 同步
 * @param formEl
 */
const syncBill = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.validate((v) => {
    if (v) {
      // 构建同步对象
      const syncSubmit: SyncRequest = {
        cloudAccountId: syncCloudAccountId.value,
        params: {
          MONTHS: billSyncForm.value.months,
        },
        syncJob: [
          {
            jobName: "SYNC_BILL",
            jobGroup: "CLOUD_ACCOUNT_BILL_SYNC_GROUP",
            module: "finance-management",
          },
        ],
      };
      if (billSyncForm.value.syncBillType === "api") {
        syncSubmit.params = {
          MONTHS: billSyncForm.value.months,
        };
      } else {
        syncSubmit.params = {
          BUCKET_CYCLE: billSyncForm.value.bucketSyncCycle,
        };
      }

      // 发送同步任务
      cloudAccountApi.syncJob(syncSubmit).then(() => {
        ElMessage.success("发送同步任务成功");
        billSyncView.value = false;
      });
    }
  });
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
  <!-- 同步资源  START -->
  <el-dialog v-model="syncVisible" title="同步" width="50%">
    <el-form ref="ruleFormRef" :rules="syncRules" :model="syncForm">
      <layout-container :border="false">
        <template #header
          ><h4>
            {{ t("cloud_account.sync.range", "同步范围") }}
          </h4></template
        ><template #content>
          <el-checkbox
            style="margin-bottom: 10px"
            v-model="checkAll"
            @change="handleCheckAllChange"
            >全选</el-checkbox
          >
          <el-form-item prop="checkedRegions">
            <el-checkbox-group
              v-loading="regionsLoading"
              @change="change"
              v-model="syncForm.checkedRegions"
            >
              <el-checkbox
                :title="region.name"
                v-for="region in regions"
                :key="region.regionId"
                :label="region.regionId"
                size="large"
              >
                <span
                  style="
                    display: inline-block;
                    width: 120px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                  "
                >
                  {{ region.name }}
                </span>
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </template>
      </layout-container>
      <layout-container :border="false">
        <template #header>资源</template>
        <template #content>
          <el-checkbox
            style="margin-bottom: 10px"
            v-model="resourcesCheckedAll"
            @change="handleCheckAllResource"
          >
            全选
          </el-checkbox>
          <el-form-item prop="checkedResources">
            <el-checkbox-group
              v-model="syncForm.checkedResources"
              v-loading="resourceLoading"
              @change="changeResource"
            >
              <el-checkbox
                v-for="resource in resources"
                :key="resource.jobName"
                :label="resource.jobName"
                :title="resource.resourceDesc"
                size="large"
              >
                <span
                  style="
                    display: inline-block;
                    width: 120px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                  "
                >
                  {{ resource.resourceDesc }}
                </span>
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </template>
      </layout-container>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="syncVisible = false">取消</el-button>
        <el-button type="primary" @click="sync(ruleFormRef)">同步</el-button>
      </span>
    </template>
  </el-dialog>
  <!-- 同步资源 END -->
  <el-dialog v-model="billSyncView" title="同步账单" width="40%">
    <el-form
      v-loading="billSyncLoading"
      ref="ruleFormSyncBill"
      :rules="billSyncRules"
      :model="billSyncForm"
    >
      <layout-container :border="false">
        <template #content>
          <el-form-item
            label="账单周期:"
            v-if="billSyncForm.syncBillType === 'api'"
          >
            <el-checkbox
              style="margin-bottom: 10px"
              v-model="billMonthAll"
              @change="handleCheckAllBillMonthChange"
              >全选</el-checkbox
            >
            <el-form-item prop="months">
              <el-checkbox-group
                @change="changeMonths"
                v-model="billSyncForm.months"
              >
                <el-checkbox
                  :title="month"
                  v-for="month in months"
                  :key="month"
                  :label="month"
                  size="large"
                >
                  <span
                    style="
                      display: inline-block;
                      width: 120px;
                      overflow: hidden;
                      text-overflow: ellipsis;
                    "
                  >
                    {{ month }}
                  </span>
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-form-item>
          <el-form-item label="账单周期:" v-else>
            <el-radio-group v-model="billSyncForm.bucketSyncCycle" size="large">
              <el-radio-button label="current">当月账单</el-radio-button>
              <el-radio-button label="all">全部账单</el-radio-button>
            </el-radio-group></el-form-item
          >
        </template></layout-container
      >
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="billSyncView = false">取消</el-button>
        <el-button type="primary" @click="syncBill(ruleFormSyncBill)">
          同步
        </el-button>
      </span>
    </template>
  </el-dialog>
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
