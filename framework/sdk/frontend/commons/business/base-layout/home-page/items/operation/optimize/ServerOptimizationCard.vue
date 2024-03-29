<template>
  <div class="div-card" :class="{ checked: checked }" v-loading="loading">
    <div class="text">
      <div class="card-header">
        <span class="left">
          {{ props.optimizationStrategy.name }}
        </span>
        <el-tooltip class="box-item" effect="dark" content="查询策略">
          <span class="right" v-show="props.showSettingIcon">
            <CeIcon
              size="14px"
              color="#646A73"
              code="icon-setting"
              @click.stop="
                showOptimizeStrategyDialog(props.optimizationStrategy)
              "
            />
          </span>
        </el-tooltip>
      </div>
    </div>
    <div class="text">
      <span class="main">
        {{ value }}
      </span>
      台
    </div>
  </div>
</template>
<script lang="ts" setup>
import { ref, watch } from "vue";
import OptimizationStrategyViewApi from "@commons/api/optimize";
import type {
  OptimizationStrategy,
  PageOptimizationStrategyResourceRequest,
} from "@commons/api/optimize/type";
import CeIcon from "@commons/components/ce-icon/index.vue";
import _ from "lodash";

const props = withDefaults(
  defineProps<{
    optimizationStrategy: OptimizationStrategy;
    tableSearchParams?: any;
    show?: boolean;
    checked?: boolean;
    showSettingIcon?: boolean;
  }>(),
  {
    show: true,
    checked: false,
    showSettingIcon: false,
  }
);

const loading = ref(false);

const value = ref(0);
function getCurrentOptimizationStrategyId() {
  return props.optimizationStrategy.id;
}
function getOptimizeServerList() {
  if (!props.show) {
    return;
  }
  const params: PageOptimizationStrategyResourceRequest = {
    pageSize: 10,
    currentPage: 1,
  };
  _.assign(params, props.tableSearchParams);
  _.assign(params, props.optimizationStrategy);
  _.assign(params, {
    optimizationStrategyId: props.optimizationStrategy.id,
    ignore: false,
  });
  OptimizationStrategyViewApi.pageOptimizationStrategyServerResourceList(
    params,
    loading
  ).then((res) => {
    value.value = res.data.total;
  });
}

watch(
  () => {
    return {
      optimizeSuggest: props.optimizationStrategy,
      show: props.show,
      tableSearchParams: props.tableSearchParams,
    };
  },
  (data, old) => {
    //防止重复查询
    if (
      data.show === old?.show &&
      _.isEqual(
        JSON.parse(JSON.stringify(data.optimizeSuggest)),
        JSON.parse(JSON.stringify(_.defaultTo(old?.optimizeSuggest, {})))
      ) &&
      _.isEqual(
        JSON.parse(JSON.stringify(data.tableSearchParams)),
        JSON.parse(JSON.stringify(_.defaultTo(old?.tableSearchParams, {})))
      )
    ) {
      return;
    }
    getOptimizeServerList();
  },
  { immediate: true, deep: true }
);
const emit = defineEmits(["showStrategyDialog"]);
const showOptimizeStrategyDialog = (o: OptimizationStrategy) => {
  emit("showStrategyDialog", o);
};
defineExpose({ getOptimizeServerList, getCurrentOptimizationStrategyId });
</script>

<style scoped lang="scss">
.div-card {
  cursor: pointer;
  background: #ffffff;
  border: 1px solid #dee0e3;
  border-radius: 4px;
  padding: 16px;

  .text {
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    line-height: 22px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  .main {
    font-style: normal;
    font-weight: 500;
    font-size: 28px;
    line-height: 36px;
  }
  .card-header {
    display: inline;
    .left {
      float: left;
    }
    .right {
      float: right;
    }
  }
}

.div-card:hover {
  box-shadow: 0 6px 24px rgba(31, 35, 41, 0.08);
}

.div-card.checked {
  background-color: rgba(51, 112, 255, 0.1);
  border-color: #3370ff;
  border-left-width: 4px;
  padding-left: 13px;
}
</style>
