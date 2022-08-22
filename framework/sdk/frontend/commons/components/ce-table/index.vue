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
      <fu-table v-bind="$attrs" header-cell-class-name="table-handler">
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
import { TableConfig, Condition } from "./index";
const props = defineProps<{
  header?: string;
  tableConfig: TableConfig;
}>();
const condition = ref<Array<Condition | string>>([]);
console.log(props.tableConfig.tableOperations);
/**
 * 更新PageSize
 */
const updatePageSize = (pageSize: number) => {
  console.log("pageSize", pageSize);
  props.tableConfig.paginationConfig?.setPageSize(
    pageSize,
    props.tableConfig.paginationConfig
  );
  props.tableConfig.searchConfig?.search(condition.value);
};

/**
 * 更新当前页面
 */
const updateCurrentPage = (currentPage: number) => {
  props.tableConfig.paginationConfig?.setCurrentPage(
    currentPage,
    props.tableConfig.paginationConfig
  );
  props.tableConfig.searchConfig?.search(condition.value);
};

/**
 * 查询函数
 */
const search = (conditions: Array<Condition | string>) => {
  if (conditions) {
    condition.value = conditions;
  }
  props.tableConfig.searchConfig?.search(conditions);
};
</script>

<style lang="scss">
@use "../../styles/mixins.scss" as *;
.table-handler {
  height: var(--ce-table-header-height, 56px);
}
.complex-table {
  background-color: var(--ce-table-bg-color, #fff);
  padding: var(--ce-table-padding, 30px);
  margin: var(--ce-table-margin, 30px);
  height: calc(
    100% - var(--ce-table-margin, 30px) * 2 - var(--ce-table-padding, 30px) * 2
  );
  width: calc(
    100% - var(--ce-table-margin, 30px) * 2 - var(--ce-table-padding, 30px) * 2
  );
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
