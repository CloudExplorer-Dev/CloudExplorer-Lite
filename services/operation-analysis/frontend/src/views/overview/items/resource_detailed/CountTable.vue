<script lang="ts" setup>
import CommonApi from "@/api/common/index";
import type { CloudAccountDTO } from "@/api/common/type";
import { ref, watch } from "vue";
import PlatformIcon from "@commons/components/platform-icon/index.vue";
import {
  PaginationConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useUserStore } from "@commons/stores/modules/user";
const userStore = useUserStore();
const props = defineProps<{
  cloudAccountId?: string | undefined;
}>();
const tableLoading = ref<boolean>(false);
/**
 * 表格数据
 */
const dataList = ref<Array<CloudAccountDTO>>([]);
// 列表字段数据
const columns = ref([]);
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  CommonApi.cloudAccountResourceCountPage(
    tableConfig.value.paginationConfig.currentPage,
    tableConfig.value.paginationConfig.pageSize,
    {
      ...params,
      cloudAccountId:
        props.cloudAccountId === "all" ? "" : props.cloudAccountId,
    },
    tableLoading
  ).then((ok) => {
    dataList.value = ok.data.records;
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

/**
 * 表单配置
 */
const tableConfig = ref<any>({
  paginationConfig: new PaginationConfig(1, 5),
  tableOperations: new TableOperations([]),
});

watch(
  props,
  () => {
    search(new TableSearch());
  },
  { immediate: true }
);
watch(
  tableConfig.value.paginationConfig,
  (n, o) => {
    if (o) {
      search(new TableSearch());
    }
  },
  { immediate: true }
);
</script>
<template>
  <div
    style="
      font-weight: bold;
      font-size: 16px;
      padding-top: 24px;
      padding-bottom: 16px;
    "
  >
    资源明细
  </div>
  <div class="log-table">
    <ce-table
      v-loading="tableLoading"
      :columns="columns"
      :data="dataList"
      :tableConfig="tableConfig"
      row-key="id"
      ref="table"
      height="100%"
    >
      <el-table-column
        min-width="150"
        prop="accountName"
        column-key="accountIds"
        :label="$t('commons.cloud_account.native')"
      >
        <template #default="scope">
          <div style="display: flex">
            <PlatformIcon
              style="margin-top: 3px; width: 16px; height: 16px"
              :platform="scope.row.platform"
            ></PlatformIcon>
            <span style="margin-left: 10px">{{ scope.row.name }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        min-width="150"
        prop="vmCount"
        label="云主机"
        align="right"
      ></el-table-column>
      <el-table-column
        min-width="150"
        prop="diskCount"
        label="磁盘"
        align="right"
      ></el-table-column>
      <el-table-column
        min-width="150"
        prop="hostCount"
        label="宿主机"
        :show="userStore.currentRole === 'ADMIN'"
        align="right"
      ></el-table-column>
      <el-table-column
        min-width="150"
        prop="datastoreCount"
        label="存储器"
        :show="userStore.currentRole === 'ADMIN'"
        align="right"
      ></el-table-column>
    </ce-table>
  </div>
</template>
<style scoped lang="scss">
.log-table {
  //height: calc(100vh - 440px);
}
</style>
