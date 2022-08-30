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
        <template #tr>
          <ce-filter-input
            @change="inputSearch"
            :searchOptions="tableConfig.searchConfig.searchOptions"
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
import { ref, defineProps, defineExpose } from "vue";
import CeFilterInput from "./CeFilterInput.vue";
import {
  TableConfig,
  Conditions,
  Condition,
  Order,
  TableSearch,
} from "@commons/components/ce-table/index";
const props = defineProps<{
  header?: string;
  tableConfig: TableConfig;
}>();

const condition = ref<Conditions>({});

const searchCondition = ref<Condition>({});

const order = ref<Order | undefined>(undefined);

const sortChange = (sortObj: any) => {
  if (sortObj.order) {
    order.value = {
      column: sortObj.prop,
      asc: sortObj.order === "ascending" ? true : false,
    };
  } else {
    order.value = undefined;
  }
  props.tableConfig.searchConfig?.search(
    new TableSearch(condition.value, order.value, searchCondition.value)
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
    new TableSearch(condition.value, order.value, searchCondition.value)
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
    new TableSearch(condition.value, order.value, searchCondition.value)
  );
};

const inputSearch = (search: Condition) => {
  searchCondition.value = search;
  props.tableConfig.searchConfig?.search(
    new TableSearch(condition.value, order.value, search)
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
    new TableSearch(condition.value, order.value, searchCondition.value)
  );
};

defineExpose({
  search,
});
</script>

<style lang="scss">
@use "../../styles/mixins.scss" as *;
.table-handler {
  height: var(--ce-table-header-height, 56px);
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
    height: 80px;
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
</style>
