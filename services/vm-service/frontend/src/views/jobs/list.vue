<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
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
import { ResourceTypeConst } from "@commons/utils/constants";
import OrgTreeFilter from "@commons/components/table-filter/OrgTreeFilter.vue";

const { t } = useI18n();
const useRoute = useRouter();
const table = ref();
const columns = ref([]);
const tableData = ref<Array<JobInfo>>();
const tableLoading = ref<boolean>(false);
const ResourceType = {
  VM: "VM",
  DISK: "DISK",
};

/**
 * 表头：组织树筛选
 */
const orgTreeRef = ref();
const orgPopRef = ref();
const selectedOrganizationIds = computed(() =>
  orgTreeRef.value?.getSelectedIds(false)
);

/**
 * 表头：工作空间树筛选
 */
const workspaceTreeRef = ref();
const workspacePopRef = ref();
const selectedWorkspaceIds = computed(() =>
  workspaceTreeRef.value?.getSelectedIds(true)
);

/**
 * 表头：清空组织和工作空间树的选中项
 * @param field
 */
const clearCondition = (field: string) => {
  if (field === "organizationIds") {
    orgTreeRef.value.cancelChecked();
  }
  if (field === "workspaceIds") {
    workspaceTreeRef.value.cancelChecked();
  }
};

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

const jumpToResource = (resourceType: string, resourceId: string) => {
  if (ResourceType.VM === resourceType) {
    jumpToServer(resourceId);
  }
  if (ResourceType.DISK === resourceType) {
    jumpToDisk(resourceId);
  }
};

const jumpToServer = (serverId: string) => {
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace(
      "jobs/list",
      `vm_cloud_server/detail/${serverId}`
    ),
  });
};

const jumpToDisk = (diskId: string) => {
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace(
      "jobs/list",
      `vm_cloud_disk/detail/${diskId}`
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
  JobsApi.listJobs(
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

const getColor = (status: string) => {
  let color = null;
  switch (status) {
    case "SUCCESS":
      color = "green";
      break;
    case "FAILED":
      color = "red";
      break;
    case "EXECUTION_ING":
      color = "#80d363";
      break;
    default:
  }
  return color;
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
    searchOptions: [
      { label: t("job.detail.id", "ID"), value: "id" },
      { label: "关联资源", value: "resourceName" },
      { label: "操作人", value: "operateUserName" },
    ],
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
    localKey="jobsTable"
    v-loading="tableLoading"
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    @clearCondition="clearCondition"
    row-key="id"
    height="100%"
    ref="table"
  >
    <el-table-column
      prop="id"
      label="ID"
      fixed
      min-width="180px"
    ></el-table-column>
    <el-table-column
      prop="type"
      column-key="type"
      label="任务类型"
      sortable
      :filters="types"
      width="160px"
    >
      <template #default="scope">
        {{ _.get(JobTypeConst, scope.row.type, scope.row.type) }}
      </template>
    </el-table-column>
    <el-table-column label="关联资源" width="160px">
      <template #default="scope">
        <a
          @click="jumpToResource(scope.row.resourceType, scope.row.resourceId)"
          style="color: var(--el-color-primary)"
          v-if="scope.row.resourceType !== null"
        >
          {{
            _.get(
              ResourceTypeConst,
              scope.row.resourceType,
              scope.row.resourceType
            )
          }}: {{ scope.row.resourceName }}
        </a>
      </template>
    </el-table-column>
    <el-table-column
      prop="organizationName"
      column-key="organizationIds"
      :label="$t('commons.org', '组织')"
      :show="false"
      min-width="180px"
      ><template #header>
        <span :class="{ highlight: selectedOrganizationIds?.length > 0 }">{{
          $t("commons.org", "组织")
        }}</span>
        <el-popover
          ref="orgPopRef"
          placement="bottom"
          :width="200"
          trigger="click"
          :show-arrow="false"
        >
          <template #reference>
            <span class="el-table__column-filter-trigger"
              ><el-icon><ArrowDown /></el-icon
            ></span>
          </template>
          <OrgTreeFilter
            tree-type="org"
            ref="orgTreeRef"
            field="organizationIds"
            label="组织"
            :popover-ref="orgPopRef"
            :table-ref="table"
          />
        </el-popover>
      </template>
      <template #default="scope">
        <span v-html="scope.row.organizationName"></span>
      </template>
    </el-table-column>
    <el-table-column
      prop="workspaceName"
      column-key="workspaceIds"
      :label="$t('commons.workspace')"
      :show="false"
      min-width="180px"
    >
      <template #header>
        <span :class="{ highlight: selectedWorkspaceIds?.length > 0 }">{{
          $t("commons.workspace", "工作空间")
        }}</span>
        <el-popover
          ref="workspacePopRef"
          placement="bottom"
          :width="200"
          trigger="click"
          :show-arrow="false"
        >
          <template #reference>
            <span class="el-table__column-filter-trigger"
              ><el-icon><ArrowDown /></el-icon
            ></span>
          </template>
          <OrgTreeFilter
            tree-type="workspace"
            ref="workspaceTreeRef"
            field="workspaceIds"
            label="工作空间"
            :popover-ref="workspacePopRef"
            :table-ref="table"
          />
        </el-popover>
      </template>
      <template #default="scope">
        <span v-html="scope.row.workspaceName"></span>
      </template>
    </el-table-column>
    <el-table-column
      prop="status"
      column-key="status"
      label="状态"
      sortable
      :filters="status"
      min-width="100px"
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <a
            @click="showDetail(scope.row)"
            :style="{ color: getColor(scope.row.status) }"
          >
            {{ _.get(JobStatusConst, scope.row.status, scope.row.status) }}
          </a>
          <el-icon
            v-show="scope.row.status === 'EXECUTION_ING'"
            class="is-loading"
            :style="{
              color: getColor(scope.row.status),
            }"
            ><Loading
          /></el-icon>
        </div>
      </template>
    </el-table-column>
    <el-table-column prop="operateUserName" label="操作人" min-width="100px" />
    <el-table-column
      prop="createTime"
      label="开始时间"
      sortable
      min-width="160px"
    />
    <el-table-column
      prop="finishTime"
      label="结束时间"
      sortable
      min-width="160px"
    />
    <fu-table-operations v-bind="tableConfig.tableOperations" fixed="right" />
    <template #buttons>
      <fu-table-column-select type="icon" :columns="columns" size="small" />
    </template>
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
