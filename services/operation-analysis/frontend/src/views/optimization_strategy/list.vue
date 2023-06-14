<template>
  <ce-table
    ref="table"
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    row-key="id"
    height="100%"
    table-layout="auto"
    v-loading="loading"
  >
    <template #toolbar>
      <!--      <el-button-->
      <!--          @click="create"-->
      <!--          type="primary"-->
      <!--          v-hasPermission="'[operation-analysis]OPTIMIZATION_STRATEGY:CREATE'"-->
      <!--      >-->
      <!--        {{ $t("commons.btn.create") }}-->
      <!--      </el-button>-->
    </template>
    <el-table-column
      min-width="80px"
      max-width="200px"
      prop="name"
      label="策略名称"
      show-overflow-tooltip
      fixed
    >
      <template #default="scope">
        <span
          class="table_overflow"
          style="cursor: pointer; color: var(--el-color-primary)"
        >
          {{ scope.row.name }}
        </span>
      </template>
    </el-table-column>
    <el-table-column
      prop="optimizationContent"
      min-width="120px"
      label="建议原因"
      show-overflow-tooltip
    >
      <template #default="scope">
        <span class="table_overflow">
          {{
            scope.row.optimizationContent
              ? scope.row.optimizationContent
              : "N/A"
          }}</span
        >
      </template>
    </el-table-column>
    <el-table-column prop="optimizationScope" label="优化范围">
      <template #default="scope">
        <span
          v-if="!scope.row.optimizationScope"
          style="cursor: pointer; color: var(--el-color-primary)"
        >
          已忽略资源({{ scope.row.ignoreNumber }})
        </span>
        <span v-if="scope.row.optimizationScope"> 所有资源 </span>
      </template>
    </el-table-column>
    <fu-table-operations
      v-bind="tableConfig.tableOperations"
      fixed="right"
      :ellipsis="1"
      :columns="columns"
    />
  </ce-table>

  <CreateOrEdit
    ref="createEditFormRef"
    @confirm="editConfirmed"
    :id="optimizationStrategyId"
  />
</template>
<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import type { OptimizationStrategy } from "@commons/api/optimize/type";
import { useRouter } from "vue-router";
import {
  PaginationConfig,
  SearchConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
import { usePermissionStore } from "@commons/stores/modules/permission";
import CreateOrEdit from "@commons/business/base-layout/home-page/items/operation/optimize/CreateOrEdit.vue";
import OptimizationStrategyViewApi from "@commons/api/optimize/index";

const { t } = useI18n();
const router = useRouter();
const permissionStore = usePermissionStore();
const columns = ref([]);
const tableData = ref<Array<OptimizationStrategy>>();
const table = ref();
const createEditFormRef = ref<InstanceType<typeof CreateOrEdit>>();
const selectedId = ref<string | undefined>();

const loading = ref<boolean>(false);

onMounted(() => {
  search(new TableSearch());
});

// 刷新列表
const refresh = () => {
  table.value.search();
};

const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  OptimizationStrategyViewApi.pageOptimizationStrategy(
    {
      currentPage: tableConfig.value.paginationConfig.currentPage,
      pageSize: tableConfig.value.paginationConfig.pageSize,
      ...params,
    },
    loading
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

const optimizationStrategyId = computed(() => {
  return selectedId.value;
});
const create = () => {
  selectedId.value = undefined;
  createEditFormRef.value?.open();
};

const edit = (row: OptimizationStrategy) => {
  selectedId.value = row.id;
  createEditFormRef.value?.open(row.id);
};
const showDetail = (row: OptimizationStrategy) => {
  //
};

function editConfirmed() {
  refresh();
}

const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    search: search,
    quickPlaceholder: "搜索",
    components: [
      SearchConfig.buildComponent().DateComponent.newInstance(
        "createTime",
        t("commons.create_time")
      ),
    ],
    searchOptions: [{ label: "策略名称", value: "name" }],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([
    TableOperations.buildButtons().newInstance(
      t("commons.btn.edit"),
      "primary",
      edit,
      "EditPen",
      undefined,
      permissionStore.hasPermission(
        "[operation-analysis]OPTIMIZATION_STRATEGY:EDIT"
      )
    ),
  ]),
});
</script>

<style lang="scss" scoped>
:deep(.el-table__body) {
  .el-table__placeholder {
    display: none;
  }
}

.role_display {
  height: 24px;
  line-height: 24px;
  display: flex;
  .role_numbers {
    cursor: pointer;
    margin-left: 8px;
    border-radius: 2px;
    padding: 0 6px;
    height: 24px;
    font-size: 14px;
    background-color: rgba(31, 35, 41, 0.1);
  }
  .role_numbers:hover {
    background-color: #ebf1ff;
    color: #3370ff;
  }
}

.inner-table-th {
  border-bottom: 1px dashed #eff0f1;
  font-style: normal;
  font-weight: 400;
  font-size: 12px;
  color: #646a73;
}

.inner-table-padding {
  padding: 8px;
}

.inner-table-role {
  font-style: normal;
  font-weight: 400;
  font-size: 12px;
}

.inner-table-source {
  font-style: normal;
  font-weight: 500;
  font-size: 12px;
}
.table_overflow {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
