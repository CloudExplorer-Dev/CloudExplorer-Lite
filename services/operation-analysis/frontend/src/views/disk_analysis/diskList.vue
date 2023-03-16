<script setup lang="ts">
import { ref, onMounted } from "vue";
import ResourceSpreadViewApi from "@/api/disk_analysis";
import type { VmCloudDiskVO } from "@/api/disk_analysis/type";
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
import _ from "lodash";

const { t } = useI18n();
const columns = ref([]);
const tableData = ref<Array<VmCloudDiskVO>>([]);
const tableLoading = ref<boolean>(false);
const table = ref();
const cloudAccount = ref<Array<SimpleMap<string>>>([]);

//硬盘状态
const diskStatus = ref<Array<SimpleMap<string>>>([
  { text: "已删除", value: "deleted" },
  { text: "已挂载", value: "in_use" },
  { text: "可用", value: "available" },
  { text: "挂载中", value: "attaching" },
  { text: "卸载中", value: "detaching" },
  { text: "创建中", value: "creating" },
  { text: "初始化中", value: "reiniting" },
  { text: "未知", value: "unknown" },
  { text: "错误", value: "error" },
  { text: "删除中", value: "deleting" },
  { text: "扩容中", value: "enlarging" },
]);

const filterChargeType = (instanceChargeType: string) => {
  let text = instanceChargeType;
  switch (instanceChargeType) {
    case "PostPaid":
      text = "按需计费";
      break;
    case "PrePaid":
      text = "包年/包月";
      break;
    case "SpotPaid":
      text = "竞价计费";
      break;
    default:
  }
  return text;
};

const filterStatus = (value: string) => {
  let status = "";
  let v = _.find(diskStatus.value, function(o) { return o.value == value; });
  if(v){
    status = v["text"];
  }
  return status;
};

/**
 * 查询
 * @param condition
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  ResourceSpreadViewApi.listDisk({
    currentPage: tableConfig.value.paginationConfig.currentPage,
    pageSize: tableConfig.value.paginationConfig.pageSize,
    ...params,
  }).then((res) => {
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
    quickPlaceholder: t("commons.btn.search"),
    components: [],
    searchOptions: [{ label: t("commons.name", "名称"), value: "name" }],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([]),
});
</script>
<template>
  <div class="log-table">
    <ce-table
      localKey="diskAnaltsisTable"
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
        prop="diskName"
        :label="$t('commons.name')"
        :show-overflow-tooltip="true"
      >
        <template #default="scope">
          {{ scope.row.diskName }}
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
        prop="region"
        label="区域/数据中心"
      ></el-table-column>
      <el-table-column
        min-width="150"
        prop="zone"
        label="集群/可用区"
      ></el-table-column>
      <el-table-column min-width="150" prop="vmInstanceName" label="所属云主机">
        <template #default="scope">
          <el-tooltip
            class="item"
            effect="dark"
            :content="scope.row.vmInstanceName"
            placement="top"
          >
            <p class="text-overflow">
              {{ scope.row.vmInstanceName }}
            </p>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column
        min-width="150"
        prop="status"
        column-key="status"
        :label="$t('commons.status')"
        :show="true"
      >
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span
              :style="{
                color: scope.row.status === 'deleted' ? 'red' : '',
              }"
              >{{ filterStatus(scope.row.status) }}</span
            >
            <ce-icon
              v-if="scope.row.status.toUpperCase().includes('ING')"
              code="Loading"
              class="is-loading"
              style="margin-left: 5px; color: var(--el-color-primary)"
            ></ce-icon>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        min-width="150"
        prop="size"
        label="大小(G)"
      ></el-table-column>
      <el-table-column min-width="150" prop="diskChargeType" label="付费方式">
        <template #default="scope">
          {{ filterChargeType(scope.row.diskChargeType) }}
        </template>
      </el-table-column>
      <template #buttons>
        <CeTableColumnSelect :columns="columns"/>
      </template>
    </ce-table>
  </div>
</template>
<style lang="scss" scoped>
.log-table {
  width: 100%;
  height: calc(100vh - 270px);
}
.text-overflow {
  max-width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
