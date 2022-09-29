<script setup lang="ts">
import { platformIcon } from "@/utils/platform";
import { ref, onMounted } from "vue";
import {
  PaginationConfig,
  SearchConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import cloudAccountApi from "@/api/cloud_account/index";
import type { CloudAccount } from "@/api/cloud_account/type";
import { ElMessage, ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
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
const multipleSelectionIds = ref<Array<string>>();
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

const sync = () => {
  console.log("同步");
};
/**
 * 去创建云账号页面
 */
const create = () => {
  router.push({ name: "cloud_account_create" });
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

onMounted(() => {
  search(new TableSearch());
});

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
      sync,
      "Refresh"
    ),
    TableOperations.buildButtons().newInstance(
      t("cloud_account.edit_job_message", "编辑定时任务"),
      "primary",
      updateJob,
      "Document"
    ),
    TableOperations.buildButtons().newInstance(
      t("commons.btn.delete", "删除"),
      "primary",
      deleteItem,
      "Delete"
    ),
  ]),
});
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
    </template>
    <el-table-column type="selection" />
    <el-table-column prop="name" :label="t('commons.name', '名称')" sortable />
    <el-table-column
      column-key="platform"
      :filters="platformFilters"
      prop="platform"
      :label="t('cloud_account.platform', '云平台')"
      sortable
    >
      <template #default="scope">
        <div
          style="
            display: flex;
            align-items: center;
            justify-content: space-between;
          "
        >
          <span>{{ platformIcon[scope.row.platform].name }}</span>
          <el-image
            style="margin-right: 20%; display: flex"
            :src="platformIcon[scope.row.platform].icon"
          ></el-image>
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
          <span
            :style="{ color: scope.row.status === 'FAILED' ? 'red' : '' }"
            >{{
              scope.row.status === "FAILED"
                ? t("cloud_account.native_sync.failed", "同步失败")
                : scope.row.status === "INIT"
                ? t("cloud_account.native_sync.init", "初始化")
                : scope.row.status === "SUCCESS"
                ? t("cloud_account.native_sync.success", "同步成功")
                : scope.row.status === "SYNCING"
                ? t("cloud_account.native_sync.syncing", "同步中")
                : t("cloud_account.native_sync.unknown", "未知")
            }}</span
          >
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
</template>

<style lang="scss"></style>
