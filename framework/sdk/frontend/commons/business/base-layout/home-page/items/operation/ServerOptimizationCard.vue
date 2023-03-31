<script lang="ts" setup>
import { ref, watch } from "vue";
import type { ListOptimizationRequest } from "@commons/api/resource_optimization/type";
import ResourceOptimizationViewApi from "@commons/api/resource_optimization";
import CeIcon from "@commons/components/ce-icon/index.vue";
import _ from "lodash";

const props = withDefaults(
  defineProps<{
    req: ListOptimizationRequest;
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

function getOptimizeSuggests() {
  if (!props.show) {
    return;
  }
  ResourceOptimizationViewApi.listServer(props.req, loading).then((res) => {
    value.value = res.data.total;
  });
}

watch(
  () => {
    return { req: props.req, show: props.show };
  },
  (data, old) => {
    //防止重复查询
    if (
      data.show === old?.show &&
      _.isEqual(
        JSON.parse(JSON.stringify(data.req)),
        JSON.parse(JSON.stringify(_.defaultTo(old?.req, {})))
      )
    ) {
      return;
    }
    getOptimizeSuggests();
  },
  { immediate: true, deep: true }
);
const emit = defineEmits(["showConditionDialog"]);
const showConditionDialog = (req: ListOptimizationRequest) => {
  emit("showConditionDialog", req);
};
</script>
<template>
  <div class="div-card" :class="{ checked: checked }" v-loading="loading">
    <div class="text">
      <div class="card-header">
        <span class="left">
          {{ req.name }}
        </span>
        <el-tooltip class="box-item" effect="dark" content="查询策略">
          <span class="right" v-show="props.showSettingIcon">
            <CeIcon
              size="14px"
              color="#646A73"
              code="icon-setting"
              @click.stop="showConditionDialog(req)"
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
