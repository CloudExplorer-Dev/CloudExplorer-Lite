<script setup lang="ts">
import { platformIcon } from "@/utils/platform";
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
import type { FormView } from "@/components/form/type";
const { t } = useI18n();
// 路由实例对象
const router = useRouter();
// 表格数据
const clouAccountList = ref<Array<CloudAccount>>([]);
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
const showAccountDetail = (row: CloudAccount) => {
  router.push({ name: "cloud_account_detail", params: { id: row.id } });
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
  cloudAccountApi.verificationCloudAccount(row.id).then(() => {
    ElMessage.success(t("native_state_valid_message", "云账号有效"));
    table.value?.search();
  });
};
/**
 * 去编辑定时任务页面
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
    .page({
      currentPage: tableConfig.value.paginationConfig.currentPage,
      pageSize: tableConfig.value.paginationConfig.pageSize,
      ...params,
    })
    .then((ok) => {
      cloudAccountApi
        .getAccountJobRecord(ok.data.records.map((r) => r.id))
        .then((data) => {
          cloudAccountJobRecord.value = data.data;
        });
      clouAccountList.value = ok.data.records;
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
    cloudAccountApi
      .getAccountJobRecord(clouAccountList.value.map((r) => r.id))
      .then((data) => {
        cloudAccountJobRecord.value = data.data;
      });
  }, 6000);
});

onBeforeUnmount(() => {
  if (cloudAccountInterval) {
    clearInterval(cloudAccountInterval);
  }
});

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
  cloudAccountApi.getResourceSync(resourceLoading).then((ok) => {
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
 * 去创建云账号页面
 */
const create = () => {
  router.push({ name: "cloud_account_create" });
};

/**
 * 资源是否全选
 */
const resourcescheckedAll = computed(() => {
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
        regions: regions.value.filter((r) =>
          syncForm.value.checkedRegions.includes(r.regionId)
        ),
        syncJob: resources.value
          .filter((r) => syncForm.value.checkedResources.includes(r.jobName))
          .map((source) => {
            return { module: source.module, jobName: source.jobName };
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

const getStatusIcone = (status: string) => {
  return status === "FAILED"
    ? "Warning"
    : status === "INIT"
    ? "Sunrise"
    : status === "SUCCESS"
    ? "CircleCheck"
    : status === "SYNCING"
    ? "Loading"
    : "InfoFilled";
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
    : t("cloud_account.native_sync.unknown", "未知");
};

const getColorByAccountStatus = (status: string) => {
  return status === "FAILED"
    ? "var(--el-color-error)"
    : status === "INIT"
    ? "var(--el-color-warning)"
    : status === "SUCCESS"
    ? "var(--el-color-success)"
    : status === "SYNCING"
    ? "var(--el-color-primary)"
    : "var(--el-color-info)";
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
const BillFrom = ref<Array<FormView>>([]);
//-------------------------------点击按钮同步 END----------------
const currentAccount = ref<CloudAccount>();
const showBillSetting = (row: CloudAccount) => {
  BillFrom.value = [];
  currentAccount.value = row;
  ceform.value?.clearData();
  cloudAccountApi.getBillFormByPlatform(row.platform).then((ok) => {
    BillFrom.value = ok.data;
    if (row.billSetting) {
      ceform.value.setData(row.billSetting, ok.data);
    }
  });
  billSettingVisible.value = true;
};

// 是否展示账单设置
const billSettingVisible = ref<boolean>(false);

/**
 * 需要展示账单设置的row
 * @param row 当前row
 */
const showBillBtn = (row: CloudAccount) => {
  const showsPlatform = [
    "fit2cloud_huawei_platform",
    "fit2cloud_tencent_platform",
    "fit2cloud_ali_platform",
  ];
  return showsPlatform.includes(row.platform);
};

// 清除表单数据,关闭表单
const clearBillForm = () => {
  ceform.value.clearData();
  BillFrom.value = [];
  billSettingVisible.value = false;
};
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
        t("commons.update_time", "修改时间")
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
      "EditPen"
    ),
    TableOperations.buildButtons().newInstance(
      t("cloud_account.verification", "校验"),
      "primary",
      check,
      "Search"
    ),
    TableOperations.buildButtons().newInstance(
      t("cloud_account.sync", "同步"),
      "primary",
      openSync,
      "Refresh"
    ),
    TableOperations.buildButtons().newInstance(
      t("cloud_account.edit_job_message", "编辑定时任务"),
      "primary",
      updateJob,
      "Document"
    ),
    TableOperations.buildButtons().newInstance(
      "编辑账单设置",
      "primary",
      showBillSetting,
      "Document",
      undefined,
      showBillBtn
    ),
    TableOperations.buildButtons().newInstance(
      t("commons.btn.delete", "删除"),
      "primary",
      deleteItem,
      "Delete"
    ),
  ]),
});

const syncAll = () => {
  if (
    clouAccountList.value
      .filter((a) => multipleSelectionIds.value.includes(a.id))
      .every((a) => a.state)
  ) {
    cloudAccountApi.syncAll(multipleSelectionIds.value).then((ok) => {
      ElMessage.success("发送同步任务成功");
    });
  } else {
    ElMessage.success("请选择有效的定时任务进行同步");
  }
};
const ceform = ref<any>(null);
const saveBillSetting = () => {
  ceform.value.submit((formData: any) => {
    cloudAccountApi
      .saveOrUpdateBillSetting(
        currentAccount.value ? currentAccount.value.id : "",
        formData
      )
      .then((ok) => {
        table.value?.search();
        clearBillForm();
        ElMessage.success("修改账单设置成功");
      });
  });
};
</script>
<template>
  <ce-table
    height="100%"
    ref="table"
    :columns="columns"
    :data="clouAccountList"
    :tableConfig="tableConfig"
    @selection-change="handleSelectionChange"
    row-key="id"
  >
    <template #toolbar>
      <el-button type="primary" @click="create">{{
        t("commons.btn.create", "创建")
      }}</el-button>
      <el-button @click="batchDelete">{{
        t("commons.btn.delete", "删除")
      }}</el-button>
      <el-button @click="syncAll">{{
        t("commons.btn.sync", "同步")
      }}</el-button>
    </template>
    <el-table-column type="selection" />
    <el-table-column prop="name" :label="t('commons.name', '名称')" sortable>
      <template #default="scope">
        <span
          style="cursor: pointer; color: var(--el-color-primary)"
          @click="showAccountDetail(scope.row)"
        >
          {{ scope.row.name }}</span
        >
      </template>
    </el-table-column>
    <el-table-column
      column-key="platform"
      :filters="platformFilters"
      prop="platform"
      :label="t('cloud_account.platform', '云平台')"
      sortable
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <el-image
            style="margin-right: 20%; display: flex"
            :src="platformIcon[scope.row.platform].icon"
          ></el-image>
          <span>{{ platformIcon[scope.row.platform].name }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="state"
      :label="t('cloud_account.native_state', '云账号状态')"
      column-key="state"
      sortable
      :filters="[
        { text: t('cloud_account.native_state_valid', '有效'), value: true },
        { text: t('cloud_account.native_state_invalid', '无效'), value: false },
      ]"
    >
      <template #default="scope">
        <div
          style="display: flex; align-items: center"
          :style="{ color: scope.row.state ? '' : 'red' }"
        >
          <span>{{
            scope.row.state
              ? t("cloud_account.native_state_valid", "有效")
              : t("cloud_account.native_state_invalid", "无效")
          }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
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
                <div style="width: 80px">{{ j.description }}</div>
                <div>{{ mapStatus(j.status) }}</div>
                <div>{{ j.createTime }}</div>
              </div>
            </template>
            <div
              style="display: flex; width: 60%; justify-content: space-between"
              :style="{
                color: getColorByAccountStatus(
                  getStatusByAccountId(scope.row.id)
                ),
              }"
            >
              <span>{{ mapStatus(getStatusByAccountId(scope.row.id)) }}</span>
              <ce-icon
                style="cursor: pointer; font-size: 20px"
                :code="getStatusIcone(getStatusByAccountId(scope.row.id))"
                :class="
                  getStatusByAccountId(scope.row.id) === 'SYNCING'
                    ? 'is-loading'
                    : ''
                "
              ></ce-icon>
            </div>
          </el-tooltip>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="updateTime"
      :label="t('cloud_account.last_sync_time', '最近同步时间')"
      sortable
    />
    <el-table-column
      prop="createTime"
      :label="t('commons.create_time', '创建时间')"
      sortable
    />
    <fu-table-operations v-bind="tableConfig.tableOperations" fix />
    <template #buttons>
      <fu-table-column-select type="icon" :columns="columns" size="small" />
    </template>
  </ce-table>
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
                ><span
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
            v-model="resourcescheckedAll"
            @change="handleCheckAllResource"
            >全选</el-checkbox
          >
          <el-form-item prop="checkedResources">
            <el-checkbox-group
              v-loading="resourceLoading"
              @change="changeResource"
              v-model="syncForm.checkedResources"
            >
              <el-checkbox
                :title="resource.resourceDesc"
                v-for="resource in resources"
                :key="resource.jobName"
                :label="resource.jobName"
                size="large"
                ><span
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
  <el-dialog v-model="billSettingVisible" title="账单设置" width="50%">
    <layout-container :border="false">
      <template #content>
        <CeForm
          :formViewData="BillFrom"
          ref="ceform"
          :otherParams="currentAccount"
        ></CeForm>
      </template>
    </layout-container>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="clearBillForm()">取消</el-button>
        <el-button type="primary" @click="saveBillSetting()">保存</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style lang="scss"></style>
