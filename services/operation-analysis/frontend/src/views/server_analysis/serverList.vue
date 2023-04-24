<script setup lang="ts">
import { ref, onMounted } from "vue";
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

import InstanceStatusUtils from "@commons/utils/vm_cloud_server/InstanceStatusUtils";

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
  <div class="log-table">
    <ce-table
      localKey="serverAnalysisTable"
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
          {{ scope.row.instanceName }}
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
            <span
              >{{ InstanceStatusUtils.getStatusName(scope.row.instanceStatus) }}
            </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        align="right"
        min-width="150"
        prop="cpuAverage"
        label="CPU平均使用率"
      >
        <template #default="scope">
          {{
            scope.row.cpuAverage ? scope.row.cpuAverage.toFixed(2) + "%" : "-"
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="right"
        min-width="150"
        prop="cpuMaximum"
        label="CPU最大使用率"
      >
        <template #default="scope">
          {{
            scope.row.cpuMaximum ? scope.row.cpuMaximum.toFixed(2) + "%" : "-"
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="right"
        min-width="150"
        prop="memoryAverage"
        label="内存平均使用率"
      >
        <template #default="scope">
          {{
            scope.row.memoryAverage
              ? scope.row.memoryAverage.toFixed(2) + "%"
              : "-"
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="right"
        min-width="150"
        prop="memoryMaximum"
        label="内存最大使用率"
      >
        <template #default="scope">
          {{
            scope.row.memoryMaximum
              ? scope.row.memoryMaximum.toFixed(2) + "%"
              : "-"
          }}
        </template>
      </el-table-column>
      <template #buttons>
        <CeTableColumnSelect :columns="columns" />
      </template>
    </ce-table>
  </div>
</template>
<style lang="scss" scoped>
.log-table {
  width: 100%;
  height: calc(100vh - 270px);
}
.dropdown_box {
  margin-left: 10px;
  margin-top: 2px;
}
</style>
