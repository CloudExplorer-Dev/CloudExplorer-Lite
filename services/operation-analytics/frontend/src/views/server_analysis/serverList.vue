<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from "vue";
import ResourceSpreadViewApi from "@/api/server_analysis/index";
import {
  PaginationConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
import type { SimpleMap } from "@commons/api/base/type";
import { platformIcon } from "@commons/utils/platform";
import BaseCloudAccountApi from "@commons/api/cloud_account";
import type { VmCloudServerVO } from "@/api/server_analysis/type";

const { t } = useI18n();
const table = ref<any>(null);
const columns = ref([]);
const tableData = ref<Array<VmCloudServerVO>>([]);
const tableLoading = ref<boolean>(false);
const cloudAccount = ref<Array<SimpleMap<string>>>([]);
const filterInstanceStatus = (value: string) => {
  let status = "";
  InstanceStatus.value.forEach((v) => {
    if (v.value == value) {
      status = v.text;
      return;
    }
  });
  return status;
};
//状态
const InstanceStatus = ref<Array<SimpleMap<string>>>([
  { text: t("", "运行中"), value: "Running" },
  { text: "已删除", value: "Deleted" },
  { text: "已关机", value: "Stopped" },
  { text: "启动中", value: "Starting" },
  { text: "关机中", value: "Stopping" },
  { text: "重启中", value: "Rebooting" },
  { text: "删除中", value: "Deleting" },
  { text: "创建中", value: "Createding" },
  { text: "排队中", value: "WaitCreating" },
  { text: "创建中", value: "Creating" },
  { text: "配置变更中", value: "ConfigChanging" },
  { text: "失败", value: "Failed" },
  { text: "未知", value: "Unknown" },
]);

/**
 * 查询
 * @param condition
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  ResourceSpreadViewApi.listServer(
    {
      currentPage: tableConfig.value.paginationConfig.currentPage,
      pageSize: tableConfig.value.paginationConfig.pageSize,
      ...params,
    },
    tableLoading
  ).then((res) => {
    tableData.value = res.data.records;
    tableConfig.value.paginationConfig?.setTotal(
      res.data.total,
      tableConfig.value.paginationConfig
    );
    tableConfig.value.paginationConfig?.setCurrentPage(
      res.data.current,
      tableConfig.value.paginationConfig
    );
  });
};

/**
 * 页面挂载
 */
onMounted(() => {
  search(new TableSearch());
  searchCloudAccount();
});
onBeforeUnmount(() => {});

const searchCloudAccount = () => {
  BaseCloudAccountApi.listAll().then((result) => {
    if (result.data.length > 0) {
      result.data.forEach(function (v) {
        const ca = { text: v.name, value: v.id };
        cloudAccount.value.push(ca);
      });
    }
  });
};

/**
 * 表单配置
 */
const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    // 查询函数
    search: search,
    quickPlaceholder: t("commons.btn.search", "查找"),
    components: [],
    searchOptions: [
      {
        label: t("commons.name", "名称"),
        value: "name",
      },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([]),
});
</script>
<template>
  <ce-table
    v-loading="tableLoading"
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    row-key="id"
    height="100%"
    ref="table"
  >
    <template #toolbar> </template>
    <el-table-column type="selection" />
    <el-table-column
      min-width="150"
      :show-overflow-tooltip="true"
      prop="instanceName"
      :label="$t('commons.name')"
    >
      <template #default="scope">
        <!--        <span @click="" class="name-span-class">-->
        {{ scope.row.instanceName }}
        <!--        </span>-->
      </template>
    </el-table-column>
    <el-table-column
      min-width="150"
      prop="accountName"
      column-key="accountIds"
      :label="$t('commons.cloud_account.native')"
      :filters="cloudAccount"
    >
      <template #default="scope">
        <div style="display: flex">
          <component
            style="margin-top: 3px; width: 16px; height: 16px"
            :is="platformIcon[scope.row.platform]?.component"
            v-bind="platformIcon[scope.row.platform]?.icon"
            :color="platformIcon[scope.row.platform]?.color"
            size="16px"
            v-if="scope.row.platform"
          ></component>
          <span style="margin-left: 10px">{{ scope.row.accountName }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      min-width="150"
      prop="instanceTypeDescription"
      :label="$t('commons.cloud_server.instance_type')"
    ></el-table-column>
    <el-table-column min-width="150" prop="ipArray" label="IP地址">
      <template #default="scope">
        <span v-show="scope.row.ipArray?.length > 2">{{
          JSON.parse(scope.row.ipArray)[0]
        }}</span>
        <el-dropdown
          class="dropdown_box"
          :hide-on-click="false"
          v-if="scope.row.ipArray.length > 2"
          max-height="100px"
        >
          <span>
            {{ t("commons.cloud_server.more", "更多")
            }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item
                v-for="(item, index) in JSON.parse(scope.row.ipArray)"
                :key="index"
                >{{ item }}</el-dropdown-item
              >
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </template>
    </el-table-column>
    <el-table-column
      :show="false"
      prop="instanceStatus"
      column-key="instanceStatus"
      :label="$t('commons.status')"
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span>{{ filterInstanceStatus(scope.row.instanceStatus) }} </span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      min-width="150"
      prop="cpuAverage"
      label="CPU平均使用率(%)"
    ></el-table-column>
    <el-table-column
      min-width="150"
      prop="cpuMaximum"
      label="CPU最大使用率(%)"
    ></el-table-column>
    <el-table-column
      min-width="150"
      prop="memoryAverage"
      label="内存平均使用率(%)"
    ></el-table-column>
    <el-table-column
      min-width="150"
      prop="memoryMaximum"
      label="内存最大使用率(%)"
    ></el-table-column>
    <el-table-column
      min-width="150"
      prop="diskAverage"
      label="磁盘使用率(%)"
    ></el-table-column>
    <template #buttons>
      <fu-table-column-select type="icon" :columns="columns" size="small" />
    </template>
  </ce-table>
</template>
<style lang="scss" scoped>
.name-span-class {
  // color: var(--el-color-primary);
}
.name-span-class:hover {
  //cursor: pointer;
}
.dropdown_box {
  margin-left: 10px;
  margin-top: 2px;
}
</style>
