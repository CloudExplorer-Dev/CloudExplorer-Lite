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
      <fu-filter-bar v-bind="tableConfig.searchConfig" @exec="search">
        <template #tl>
          <slot name="toolbar"></slot>
        </template>
        <template #default>
          <slot name="complex"></slot>
        </template>
        <template #buttons>
          <slot name="buttons"></slot>
        </template>
      </fu-filter-bar>
    </template>

    <div class="complex-table__body">
      <fu-table
        v-bind="$attrs"
        header-cell-class-name="table-handler"
        @sort-change="sortChange"
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
import { ref, defineProps } from "vue";
import {
  TableConfig,
  Conditions,
  Order,
  TableSearch,
  OrderDesc,
  OrderAsc,
} from "./index";
const props = defineProps<{
  header?: string;
  tableConfig: TableConfig;
}>();
const condition = ref<Conditions>({});

const order = ref<Order | undefined>();

const sortChange = (sortObj: any) => {
  if (sortObj.order) {
    order.value = {
      field: sortObj.prop,
      order: sortObj.order === "ascending" ? OrderAsc : OrderDesc,
    };
  } else {
    order.value = undefined;
  }
  props.tableConfig.searchConfig?.search(
    new TableSearch(condition.value, order.value)
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
    new TableSearch(condition.value, order.value)
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
    new TableSearch(condition.value, order.value)
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
    new TableSearch(condition.value, order.value)
  );
};
</script>

<style lang="scss">
@use "../../styles/mixins.scss" as *;
.table-handler {
  height: var(--ce-table-header-height, 56px);
}
.complex-table {
  height: 100%;
  width: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  .complex-table__header {
    @include flex-row(flex-start, center);
    line-height: 60px;
    font-size: 18px;
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
</style>
