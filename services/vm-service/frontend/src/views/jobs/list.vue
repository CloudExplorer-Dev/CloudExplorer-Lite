<script setup lang="ts">
import { ref, onMounted } from "vue";
import JobsApi from "@/api/jobs";
import _ from "lodash";
import type { JobInfo } from "@/api/jobs/type";
import { JobTypeConst, JobStatusConst } from "@commons/utils/constants";
import { useRouter } from "vue-router";
import {
  PaginationConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
import ManageInfo from "@/views/vm_cloud_image/ManageInfo.vue";
import type { VmCloudDiskVO } from "@/api/vm_cloud_disk/type";
import type { VmCloudServerVO } from "@/api/vm_cloud_server/type";

const { t } = useI18n();
const useRoute = useRouter();
const columns = ref([]);
const tableData = ref<Array<JobInfo>>();

/**
 * 打开管理信息
 */
const manageInfoRef = ref();
const showDetail = (row: JobInfo) => {
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace(
      "/list",
      `/detail/${row.id}`
    ),
  });
};
const jumpToServer = (server: VmCloudServerVO) => {
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace(
      "jobs/list",
      `vm_cloud_server/detail/${server.id}`
    ),
  });
};
const jumpToDisk = (disk: VmCloudDiskVO) => {
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace(
      "jobs/list",
      `vm_cloud_disk/detail/${disk.id}`
    ),
  });
};

const types = [];
_.forIn(JobTypeConst, (value, key) => {
  types.push({ text: value, value: key });
});

const status = [];
_.forIn(JobStatusConst, (value, key) => {
  status.push({ text: value, value: key });
});

/**
 * 查询
 * @param condition
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  JobsApi.listJobs({
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
/**
 * 页面挂载
 */
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
    quickPlaceholder: t("commons.btn.search"),
    components: [],
    searchOptions: [{ label: t("job.detail.id", "ID"), value: "id" }],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations(
    [
      TableOperations.buildButtons().newInstance(
        t("job.detail.btn.detail", "查看详情"),
        "primary",
        showDetail
      ),
    ],
    "label"
  ),
});
</script>
<template>
  <ce-table
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    row-key="id"
    height="100%"
    ref="table"
  >
    <el-table-column prop="id" label="ID"></el-table-column>
    <el-table-column
      prop="type"
      column-key="type"
      label="任务类型"
      sortable
      :filters="types"
    >
      <template #default="scope">
        {{ _.get(JobTypeConst, scope.row.type, scope.row.type) }}
      </template>
    </el-table-column>
    <el-table-column label="关联资源">
      <template #default="scope">
        <div v-for="server in scope.row.servers" :key="server.id">
          <a
            @click="jumpToServer(server)"
            style="color: var(--el-color-primary)"
          >
            云主机: {{ server.instanceName }}
          </a>
        </div>
        <div v-for="disk in scope.row.disks" :key="disk.id">
          <a @click="jumpToDisk(disk)" style="color: var(--el-color-primary)">
            磁盘: {{ disk.diskName }}
          </a>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="status"
      column-key="status"
      label="状态"
      sortable
      :filters="status"
    >
      <template #default="scope">
        <a
          @click="showDetail(scope.row)"
          :style="{ color: scope.row.status === 'FAILED' ? 'red' : null }"
        >
          {{ _.get(JobStatusConst, scope.row.status, scope.row.status) }}
        </a>
      </template>
    </el-table-column>
    <el-table-column
      prop="createTime"
      label="开始时间"
      sortable
    ></el-table-column>
    <el-table-column
      prop="finishTime"
      label="结束时间"
      sortable
    ></el-table-column>

    <fu-table-operations v-bind="tableConfig.tableOperations" fix />
  </ce-table>
  <ManageInfo ref="manageInfoRef"></ManageInfo>
</template>
<style lang="scss" scoped>
.text-overflow {
  max-width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
