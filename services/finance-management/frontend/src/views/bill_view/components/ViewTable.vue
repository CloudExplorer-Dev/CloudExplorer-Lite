<template>
  <el-table
    @sort-change="sortChange"
    :data="tableData"
    class="table"
    header-cell-class-name="table-handler"
  >
    <el-table-column
      v-for="(g, index) in billRule ? billRule.groups : []"
      min-width="180px"
      show-overflow-tooltip
      :key="index"
      :label="g.name"
    >
      <template #default="scope">{{
        scope.row["group" + (index + 1)]
      }}</template>
    </el-table-column>
    <el-table-column
      fixed="right"
      label="总费用(元)"
      min-width="120"
      prop="value"
      sortable
    >
      <template #default="scope">
        {{ DecimalFormat.format(scope.row.value, 2) }}
      </template>
    </el-table-column>
    <el-table-column fixed="right" min-width="80" label="占比">
      <template #default="scope">{{
        PercentFormat.format(
          scope.row.value / viewData.map((b) => b.value).reduce((p, n) => p + n)
        )
      }}</template></el-table-column
    >
    <el-table-column width="200" fixed="right" label="趋势">
      <template #default="scope"
        ><ViewTrend :billSummary="scope.row"></ViewTrend></template
    ></el-table-column>
  </el-table>
  <div class="pagination">
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[5, 10, 20]"
      :small="false"
      layout="total, sizes, prev, pager, next, jumper"
      :total="filterTableData.length"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>
<script setup lang="ts">
import { ref, computed } from "vue";
import type { BillSummary } from "@/echarts/bill_view/type";
import type { BillRule } from "@/api/bill_rule/type";
import ViewTrend from "@/views/bill_view/components/ViewTrendChart.vue";
import PercentFormat from "@commons/utils/percentFormat";
import DecimalFormat from "@commons/utils/decimalFormat";

const props = withDefaults(
  defineProps<{
    // 分组
    groups: Array<string>;
    // 表格全量数据
    viewData: Array<BillSummary>;
    // 当前规则组
    billRule?: BillRule;
  }>(),
  {
    viewData: () => [],

    groups: () => [],
  }
);
/**
 * 获取table的分页数据
 * @param currentPage 当前页
 * @param pageSize    每页大小
 * @param datas       数据
 */
const getPageData = (
  currentPage: number,
  pageSize: number,
  datas: Array<BillSummary>
) => {
  const newArray = [];
  for (let i = (currentPage - 1) * pageSize; i < currentPage * pageSize; i++) {
    if (datas && datas.length > i) {
      newArray.push(datas[i]);
    }
  }
  return newArray;
};

// 过滤后的table数据
const filterTableData = computed(() => {
  // 处理分页和筛选
  if (props.groups.length > 0) {
    return props.viewData.filter((item) => {
      return props.groups.every((groupValue, index) => {
        return item["group" + (index + 1)] === groupValue;
      });
    });
  }
  return props.viewData;
});
/**
 * 排序
 * @param sortObj 排序对象
 */
const sortChange = (sortObj: any) => {
  sort(props.viewData, sortObj.prop, sortObj.order);
};
/**
 *
 * @param viewData 需要排序的数据
 * @param field    排序的字段
 * @param order    排序方式 ascending｜descending
 */
const sort = (
  viewData: Array<BillSummary>,
  field: string,
  order: "ascending" | "descending"
) => {
  if (order === "ascending") {
    viewData.sort((pre, next) => next[field].value - pre[field].value);
  } else {
    viewData.sort((pre, next) => pre[field].value - next[field].value);
  }
};
/**
 * 筛选后账单表格数据
 */
const tableData = computed(() => {
  return getPageData(currentPage.value, pageSize.value, filterTableData.value);
});
// 当前页
const currentPage = ref<number>(1);
// 每页大小
const pageSize = ref<number>(10);
/**
 * 每页大小发生变化
 * @param size 变化后的每页大小
 */
const handleSizeChange = (size: number) => {
  pageSize.value = size;
};
/**
 *当前页发生变化
 * @param current 变化后的当前页
 */
const handleCurrentChange = (current: number) => {
  currentPage.value = current;
};
</script>
<style scoped lang="scss">
@use "@commons/styles/mixins.scss" as *;
:deep(th) {
  background-color: var(--el-color-primary-light-9) !important;
}
:deep(.complex-table__header) {
  @include flex-row(flex-start, center);
  line-height: 60px;
  font-size: 18px;
}
.table-handler {
  height: var(--ce-table-header-height, 56px);
}
.table {
  width: 100%;
  height: 100%;
}
.tree {
  height: 600px;
  width: 300px;
}
.pagination {
  padding: 19px 24px 24px 0;
  display: flex;
  justify-content: flex-end;
}
:deep(.el-table) {
  .cell {
    box-sizing: border-box;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: normal;
    word-break: break-all;
    line-height: 23px;
    padding: 0 12px;
  }
}
</style>
