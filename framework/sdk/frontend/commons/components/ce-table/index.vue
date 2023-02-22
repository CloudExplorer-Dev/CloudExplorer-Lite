<template>
  <div class="complex-table">
    <div class="complex-table__header" v-if="$slots.header || header">
      <slot name="header">{{ header }}</slot>
    </div>
    <div
      v-if="$slots.toolbar && !tableConfig.searchConfig"
      style="margin-bottom: 10px"
    >
      <slot name="toolbar"></slot>
    </div>
    <template v-if="tableConfig.searchConfig">
      <ce-filter-bar>
        <template #tr>
          <ce-filter-input
            @change="inputSearch"
            :searchOptions="tableConfig.searchConfig.searchOptions"
            :quick-placeholder="tableConfig.searchConfig.quickPlaceholder"
          ></ce-filter-input
        ></template>
        <template #tl>
          <slot name="toolbar"></slot>
        </template>
        <template #default>
          <slot name="complex"></slot>
        </template>
        <template #buttons>
          <slot name="buttons"></slot>
        </template>
        <template #filter>
          <ce-filter
            :all-conditions="allConditions"
            @clearOne="handleClear"
            @clearAll="clearAll"
          />
        </template>
      </ce-filter-bar>
    </template>

    <div class="complex-table__body">
      <fu-table
        ref="table"
        v-bind="$attrs"
        header-cell-class-name="table-handler"
        @sort-change="sortChange"
        @filter-change="filterChange"
      >
        <slot></slot>
      </fu-table>
    </div>

    <div
      class="complex-table__pagination"
      v-if="$slots.pagination || tableConfig.paginationConfig"
    >
      <!-- 外部可以指定分页组建 -->
      <slot name="pagination">
        <fu-table-pagination
          v-bind="tableConfig.paginationConfig"
          @update:pageSize="updatePageSize"
          @update:currentPage="updateCurrentPage"
        />
      </slot>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import CeFilterInput from "./CeFilterInput.vue";
import {
  TableConfig,
  Conditions,
  Condition,
  Order,
  TableSearch,
} from "@commons/components/ce-table/type";
import CeFilterBar from "@commons/components/ce-table/CeFilterBar.vue";
import CeFilter from "@commons/components/ce-table/CeFilter.vue";

const props = defineProps<{
  header?: string;
  tableConfig: TableConfig;
}>();

const emit = defineEmits(["clearCondition"]);

const table = ref<any>();
const condition = ref<Conditions>({}); // 自定义的筛选条件
const searchCondition = ref<Conditions>({}); // 搜索框筛选条件
const tableHeaderFilter = ref<Conditions>({}); // 表头筛选条件
const order = ref<Order | undefined>(undefined); // 排序

const sortChange = (sortObj: any) => {
  if (sortObj.order) {
    order.value = {
      column: sortObj.prop,
      asc: sortObj.order === "ascending",
    };
  } else {
    order.value = undefined;
  }
  props.tableConfig.searchConfig?.search(
    new TableSearch(
      condition.value,
      order.value,
      searchCondition.value,
      tableHeaderFilter.value
    )
  );
};

const filterChange = (filterObj: any) => {
  const column = table.value.$slots
    .default()
    .flatMap((item: any) => {
      return item.children;
    })
    .find((item: any) => {
      return (
        item.props["column-key"] === Object.getOwnPropertyNames(filterObj)[0]
      );
    });
  const filters = column.props.filters;
  const label = column.props.label;

  Object.keys(filterObj).forEach((key: string) => {
    const value = filterObj[key];
    if (value.length > 0) {
      const filter: Condition = {
        field: key,
        label: label,
        value: value,
        valueLabel: filters
          .filter((item: any) => value.includes(item.value))
          .map((item: any) => item.text)
          .join(" | "),
      };
      tableHeaderFilter.value[key] = filter;
    } else {
      delete tableHeaderFilter.value[key];
      removeConditions(key);
    }
  });

  collectConditions(tableHeaderFilter.value);

  props.tableConfig.searchConfig?.search(
    new TableSearch(
      condition.value,
      order.value,
      searchCondition.value,
      tableHeaderFilter.value
    )
  );
};

/**
 * 更新PageSize
 */
const updatePageSize = (pageSize: number) => {
  props.tableConfig.paginationConfig?.setPageSize(
    pageSize,
    props.tableConfig.paginationConfig
  );
  props.tableConfig.searchConfig?.search(
    new TableSearch(
      condition.value,
      order.value,
      searchCondition.value,
      tableHeaderFilter.value
    )
  );
};

/**
 * 更新当前页面
 */
const updateCurrentPage = (currentPage: number) => {
  props.tableConfig.paginationConfig?.setCurrentPage(
    currentPage,
    props.tableConfig.paginationConfig
  );
  props.tableConfig.searchConfig?.search(
    new TableSearch(
      condition.value,
      order.value,
      searchCondition.value,
      tableHeaderFilter.value
    )
  );
};

const inputSearch = (search: Condition) => {
  searchCondition.value[search.field] = search;

  collectConditions(searchCondition.value);

  props.tableConfig.searchConfig?.search(
    new TableSearch(
      condition.value,
      order.value,
      searchCondition.value,
      tableHeaderFilter.value
    )
  );
};

/**
 * 查询函数
 */
const search = (conditions: Conditions) => {
  if (conditions) {
    condition.value = conditions;
  }
  props.tableConfig.searchConfig?.search(
    new TableSearch(
      condition.value,
      order.value,
      searchCondition.value,
      tableHeaderFilter.value
    )
  );
};

/**
 * 刷新表格
 */
const refresh = () => {
  props.tableConfig.searchConfig?.search(
    new TableSearch(
      condition.value,
      order.value,
      searchCondition.value,
      tableHeaderFilter.value
    )
  );
};

/**
 * 获取查询对象
 */
const getTableSearch: (otherConditions: Conditions) => TableSearch = (
  otherConditions
) => {
  condition.value = otherConditions
    ? { ...condition.value, ...otherConditions }
    : condition.value;

  collectConditions(condition.value);

  return new TableSearch(
    condition.value,
    order.value,
    searchCondition.value,
    tableHeaderFilter.value
  );
};

/**
 * 清除表格上方回显的某个过滤项，从所有种类的查询条件中清除
 */
const handleClear = (field: string) => {
  clearConditions("0", condition, field);
  clearConditions("1", searchCondition, field);
  clearConditions("2", tableHeaderFilter, field);

  removeConditions(field);
  refresh();
};

/**
 * 根据字段值清除过滤项
 */
const clearConditions = (
  conditionType: string,
  condition: any,
  field: string
) => {
  Object.keys(condition.value).forEach((key: string) => {
    if (key === field) {
      delete condition.value[key];

      // 清除自定义的搜索项
      if (conditionType === "0") {
        emit("clearCondition", field);
      }

      // 清除表格搜索项
      if (conditionType === "2") {
        table.value.refElTable.clearFilter(new Array(field));
      }
    }
  });
};

/**
 *  清空所有筛选条件
 */
const clearAll = () => {
  // 清空自定义的筛选：如组织和工作空间筛选
  Object.keys(condition.value).forEach((key) => emit("clearCondition", key));
  table.value.refElTable.clearFilter();

  condition.value = {};
  searchCondition.value = {};
  tableHeaderFilter.value = {};
  allConditions.value = {};

  refresh();
};

/**
 * 所有查询条件：包含自定义的筛选条件、搜索框筛选条件、表头筛选条件
 */
const allConditions = ref<Conditions>({});

/**
 * 按照点击的顺序收集筛选条件
 */
const collectConditions = (condition: Conditions) => {
  allConditions.value = { ...allConditions.value, ...condition };
};

/**
 * 按照点击的顺序移除筛选条件
 * @param condition
 */
const removeConditions = (field: string) => {
  Object.keys(allConditions.value).forEach((key: string) => {
    if (key === field) {
      delete allConditions.value[key];
    }
  });
};

defineExpose({
  search,
  getTableSearch,
  handleClear,
});
</script>

<style lang="scss">
@use "../../styles/mixins.scss" as *;
.table-handler {
  height: var(--ce-table-header-height, 56px);
}
.fu-table-header {
  th {
    background-color: var(--el-color-primary-light-9) !important;
  }
}
.complex-table {
  height: 100%;
  width: 100%;
  overflow-y: hidden;
  overflow-x: hidden;
  .complex-table__header {
    @include flex-row(flex-start, center);
    line-height: 60px;
    font-size: 18px;
  }
  .complex-table__body {
    height: calc(100% - 140px);
    width: 100%;
  }
  .fu-filter-bar {
    height: auto;
    margin-bottom: 16px;
  }

  .fu-filter-bar__bottom {
    margin-top: 16px;
  }

  .complex-table__toolbar {
    @include flex-row(space-between, center);

    .fu-search-bar {
      width: auto;
    }
  }

  .complex-table__pagination {
    margin-top: 20px;
    @include flex-row(flex-end);
  }
}

:deep(.el-table__body-wrapper) {
  &::-webkit-scrollbar {
    /* 滚动条整体样式 */
    width: 6px !important; /* 高宽分别对应横竖滚动条的尺寸 */
  }
}
.table_content_ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
<style scoped lang="scss">
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
