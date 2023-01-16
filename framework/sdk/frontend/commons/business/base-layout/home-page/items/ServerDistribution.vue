<script lang="ts" setup>
import { computed, onMounted, ref } from "vue";
import _ from "lodash";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useUserStore } from "@commons/stores/modules/user";
import {
  defaultPieDoughnutOptions,
  emptyOptions,
} from "@commons/components/echart";

import API from "./api";
import ChartsSpeed from "@commons/components/echart/ChartsSpeed.vue";

const props = withDefaults(
  defineProps<{
    needRoles: Array<"ADMIN" | "ORGADMIN" | "USER">;
    permission: any;
    module?: string;
    type: "byAccount" | "byStatus" | "byChargeType";
    title: string;
    cardShadow?: "always" | "hover" | "never";
    height?: number;
  }>(),
  {
    module: "operation-analytics",
    height: 300,
    cardShadow: "always",
  }
);

//参数
const params = {};

const childRefMap: Map<string, any> = new Map();
const childRef = (el: any, chartName: any) => {
  childRefMap.set(chartName, el);
};

const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();
const userStore = useUserStore();

const show = computed<boolean>(
  () =>
    _.some(
      moduleStore.runningModules,
      (module) => module.id === props.module
    ) &&
    permissionStore.hasPermission(props.permission) &&
    _.includes(props.needRoles, userStore.currentRole)
);

const spreadOption = ref<any>({});

const getSpreadData = (spreadType: string) => {
  if (!show.value) {
    return;
  }
  childRefMap.get(spreadType + "-chart").echartsClear();
  childRefMap.get(spreadType + "-chart").echartsLoading();
  _.set(params, "accountIds", []);
  _.set(params, "hostIds", []);
  API.getSpreadData(params).then((res) => {
    if (res.data) {
      const spreadData = res.data;
      const options = _.cloneDeep(defaultPieDoughnutOptions);
      _.set(options, "series[0].name", "云主机分布");
      _.set(options, "series[0].data", spreadData[spreadType]);
      _.set(
        options,
        "series[0].label.normal.formatter",
        `{title|总数}\r\n{value|${_.sumBy(spreadData[spreadType], "value")}}`
      );

      spreadOption.value = options;

      childRefMap.get(spreadType + "-chart").hideEchartsLoading();
    }
  });
};

onMounted(() => {
  _.set(params, "hostIds", []);
  _.set(params, "accountIds", []);
  _.set(params, "hostIds", []);

  spreadOption.value = emptyOptions;

  getSpreadData(props.type);
});
</script>
<template>
  <el-card class="server-distribution" v-if="show" :shadow="cardShadow">
    <div class="echart-title">
      <div class="echart-title-left">{{ title }}</div>
    </div>
    <div style="position: relative">
      <ChartsSpeed
        :height="height"
        :options="spreadOption"
        :ref="(el) => childRef(el, type + '-chart')"
      />
    </div>
  </el-card>
</template>

<style scoped lang="scss">
.server-distribution {
  height: 100%;

  .echart-title {
    height: 20px;
    font-weight: bold;
    font-size: 16px;
    padding-bottom: 26px;
  }
  .echart-title-left {
    float: left;
  }
  .echart-title-right {
    float: right;
    margin-top: -5px;
  }
}
</style>
