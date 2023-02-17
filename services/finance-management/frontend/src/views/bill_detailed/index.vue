<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <ce-table
      v-loading="loading"
      height="100%"
      ref="table"
      :columns="columns"
      :data="dataList"
      :tableConfig="tableConfig"
      row-key="id"
    >
      <template #toolbar>
        <el-date-picker
          :editable="false"
          v-model="viewMonth"
          type="month"
          placeholder="请选择月份"
          format="YYYY-MM"
          :clearable="false"
          :disabled-date="disabledDate"
          value-format="YYYY-MM"
        >
          <template #default="cell">
            <div class="cell" :class="{ current: cell.isCurrent }">
              <span class="text">{{ cell.text }}</span>
            </div>
          </template>
        </el-date-picker>
      </template>
      <el-table-column type="selection" />
      <el-table-column prop="billingCycle" width="100px" label="账期">
        <template #default="scope">
          <span>{{ scope.row.billingCycle.substring(0, 7) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="resourceId" label="资源id">
        <template #default="scope">
          <el-tooltip
            :content="scope.row.resourceId ? scope.row.resourceId : 'N/A'"
            placement="top"
          >
            <div
              style="
                white-space: nowrap;
                text-overflow: ellipsis;
                overflow: hidden;
              "
            >
              {{ scope.row.resourceId ? scope.row.resourceId : "N/A" }}
            </div></el-tooltip
          >
        </template>
      </el-table-column>
      <el-table-column prop="resourceName" label="资源名称">
        <template #default="scope">
          <el-tooltip
            :content="scope.row.resourceName ? scope.row.resourceName : 'N/A'"
            placement="top"
          >
            <div
              style="
                white-space: nowrap;
                text-overflow: ellipsis;
                overflow: hidden;
              "
            >
              {{ scope.row.resourceName ? scope.row.resourceName : "N/A" }}
            </div></el-tooltip
          >
        </template>
      </el-table-column>
      <el-table-column
        prop="cloudAccountName"
        label="云账号"
        :filters="
          cloudAccountList.map((item) => ({
            text: item.name,
            value: item.id,
          }))
        "
        :filter-multiple="false"
        column-key="cloudAccountId"
        width="150px"
      >
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <platform_icon :platform="scope.row.provider"> </platform_icon>
            <div>{{ scope.row.cloudAccountName }}</div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="productName" label="产品名称" width="120px">
        <template #default="scope">
          <span>{{
            scope.row.productName ? scope.row.productName : "N/A"
          }}</span></template
        >
      </el-table-column>
      <el-table-column prop="productDetail" label="产品明细" width="200px">
        <template #default="scope">
          <span>{{
            scope.row.productDetail ? scope.row.productDetail : "N/A"
          }}</span></template
        >
      </el-table-column>
      <el-table-column prop="payAccountId" label="付款账号" width="200px" />
      <el-table-column prop="organizationName" label="组织">
        <template #default="scope">
          <span>{{
            scope.row.organizationName ? scope.row.organizationName : "N/A"
          }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="workspaceName" label="工作空间">
        <template #default="scope">
          <span>{{
            scope.row.workspaceName ? scope.row.workspaceName : "N/A"
          }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="billMode" label="计费模式">
        <template #default="scope">
          <span>{{
            scope.row.billMode === "MONTHLY"
              ? "包年包月"
              : scope.row.billMode === "ON_DEMAND"
              ? "按需"
              : "其他"
          }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="tags" label="标签">
        <template #default="scope">
          <el-tooltip
            raw-content
            :content="
              scope.row.tags
                ? Object.keys(scope.row.tags).length > 0
                  ? Object.keys(scope.row.tags)
                      .map(
                        (key) =>
                          '<div>' + key + '=' + scope.row.tags[key] + '</div>'
                      )
                      .join('')
                  : 'N/A'
                : 'N/A'
            "
            placement="top"
          >
            <div
              style="
                white-space: nowrap;
                text-overflow: ellipsis;
                overflow: hidden;
              "
            >
              {{
                scope.row.tags
                  ? Object.keys(scope.row.tags).length > 0
                    ? Object.keys(scope.row.tags)
                        .map((key) => key + "=" + scope.row.tags[key])
                        .join(",")
                    : "N/A"
                  : "N/A"
              }}
            </div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="projectName" label="企业项目" />
      <el-table-column prop="totalCost" label="总费用" sortable>
        <template #default="scope">
          <span :title="scope.row.totalCost + '元'">
            {{ _.round(scope.row.totalCost, 2).toFixed(2) }}元
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="realTotalCost" label="现金支付" sortable>
        <template #default="scope">
          <span :title="scope.row.realTotalCost + '元'">
            {{ _.round(scope.row.realTotalCost, 2).toFixed(2) }}元
          </span>
        </template>
      </el-table-column>
      <template #buttons>
        <fu-table-column-select type="icon" :columns="columns" size="small" />
      </template>
    </ce-table>
  </layout-content>
</template>
<script setup lang="ts">
import { ref, watch, onMounted } from "vue";
import {
  PaginationConfig,
  TableConfig,
  TableSearch,
} from "@commons/components/ce-table/type";
import billDetailApi from "@/api/bill_detailed";
import platform_icon from "@commons/components/platform-icon/index.vue";
import cloudAccountApi from "@commons/api/cloud_account/index";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import _ from "lodash";
/**
 *当前月份
 */
const currentMonth =
  new Date().getFullYear().toString() +
  "-" +
  ((new Date().getMonth() + 1).toString().length === 1
    ? "0" + (new Date().getMonth() + 1).toString()
    : (new Date().getMonth() + 1).toString());
const viewMonth = ref<string>(currentMonth);
/**
 * 日志选择框禁用大于当前月份的时间
 * @param time 时间
 */
const disabledDate = (time: Date) => {
  return time.getTime() > Date.now();
};

const cloudAccountList = ref<Array<CloudAccount>>([]);

/**
 * 表格对象
 */
const table = ref<any>();

onMounted(() => {
  /**
   * 组件挂载查询数据
   */
  search(table.value?.getTableSearch());
  cloudAccountApi.listAll().then((ok) => {
    cloudAccountList.value = ok.data;
  });
});

/**
 * 月份发生改变的时候查询数据
 */
watch(viewMonth, () => {
  const tabSearch: TableSearch = table.value?.getTableSearch({
    month: viewMonth.value,
  });
  search(tabSearch);
});
/**
 *加载器
 */
const loading = ref<boolean>(false);
/**
 * 数据列表
 */
const dataList = ref<Array<any>>([]);

// 列表字段数据
const columns = ref([]);
/**
 * 查询函数
 * @param condition 查询条件
 */
const search = (condition: TableSearch) => {
  if (viewMonth.value) {
    condition.conditions = {
      ...condition.conditions,
      ...{ month: viewMonth.value },
    };
  }
  const params = TableSearch.toSearchParams(condition);
  billDetailApi
    .pageBillDetailed(
      tableConfig.value.paginationConfig.currentPage,
      tableConfig.value.paginationConfig.pageSize,
      params,
      loading
    )
    .then((ok) => {
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
const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    // 查询函数
    search: search,
    quickPlaceholder: "搜索",
    components: [],
    searchOptions: [
      { label: "资源名称", value: "resourceName" },
      { label: "云账号", value: "cloudAccountName" },
      { label: "企业项目", value: "projectName" },
      { label: "产品名称", value: "productName" },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: undefined,
});
</script>
<style lang="scss" scoped></style>
