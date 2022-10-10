<script setup lang="ts">
import { ref, onMounted } from "vue";
import VmCloudImageApi from "@/api/vm_cloud_image";
import type { VmCloudImageVO } from "@/api/vm_cloud_image/type";
import { useRouter } from "vue-router";
import {
  PaginationConfig,
  TableConfig,
  SearchConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
import {ElMessage} from "element-plus";
import type { SimpleMap } from "@commons/api/base/type";
import ManageInfo from "@/views/VmCloudImage/ManageInfo.vue";
const { t } = useI18n();
const useRoute = useRouter();
const columns = ref([]);
const tableData = ref<Array<VmCloudImageVO>>();

/**
 * 打开管理信息
 */
const manageInfoRef = ref();
const showManageInfoDialog = (v: VmCloudImageVO) => {
  manageInfoRef.value.dialogVisible = true;
  manageInfoRef.value.logInfo = v;
};
/**
 * 查询
 * @param condition
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  VmCloudImageApi.listVmCloudImage({
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
    searchOptions: [
      { label: "名称", value: "imageName" },
      { label: "云账号", value: "accountName" },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([TableOperations.buildButtons().newInstance(
      "设置管理信息",
      "primary",
      showManageInfoDialog,
      "InfoFilled"
  ),]),
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
    <el-table-column prop="imageName" label="镜像名称">
      <template #default="scope">
        <el-tooltip
            class="item"
            effect="dark"
            :content="scope.row.imageName"
            placement="top"
        >
          <p class="text-overflow">
            {{ scope.row.imageName }}
          </p>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column prop="imageId" label="镜像ID"></el-table-column>
    <el-table-column prop="accountName" label="云账号"></el-table-column>
    <el-table-column prop="region" label="数据中心"></el-table-column>
    <el-table-column prop="os" label="操作系统"></el-table-column>
    <el-table-column prop="osVersion" label="操作系统版本"></el-table-column>
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
