<script lang="ts" setup>
const props = withDefaults(
  defineProps<{
    needRoles?: Array<"ADMIN" | "ORGADMIN" | "USER">;
    permission?: any;
    module?: string;
  }>(),
  {
    needRoles: () => ["ADMIN", "ORGADMIN", "USER"],
    permission: "[vm-service]CLOUD_SERVER:READ",
    module: "vm-service",
  }
);

import { computed, onMounted, ref } from "vue";
import API from "@commons/api/vm_cloud_server/index";
import CurrencyFormat from "@commons/utils/currencyFormat";

import VChart from "vue-echarts";

import _ from "lodash";
import type { SimpleMap } from "@commons/api/base/type";
import { interpolationColor } from "@commons/utils/color";
import type { ECBasicOption } from "echarts/types/src/util/types";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useUserStore } from "@commons/stores/modules/user";

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

const loading = ref<boolean>(false);

const bills = ref<any>();

interface EchartValue {
  name: string;
  value: number;

  empty?: boolean;
}

const data = computed<Array<EchartValue>>(() => {
  const result: Array<EchartValue> = [];
  const list = _.head(_.values(bills.value));
  _.forEach(list, (o) => {
    result.push({
      name: _.head(o.billGroupDetails as Array<any>)?.value as string,
      value: o.value as number,
    });
  });

  if (result.length === 0) {
    result.push({
      name: "暂无数据",
      value: 0,
      empty: true,
    });
  }

  return result;
});

function getCurrentMonthBill() {
  if (!show.value) {
    return;
  }
  API.getCountsGroupByStatus(loading).then((ok) => {
    //bills.value = ok.data;
    console.log(ok.data);
  });
}

const option = computed<ECBasicOption>(() => {
  const selected = data.value
    .map((b) => {
      return { [b.name]: true };
    })
    .reduce((pre, next) => {
      return { ...pre, ...next };
    }, {});

  return {
    tooltip: {
      trigger: "item",
      formatter: (p: any) => {
        if (p.data?.empty) {
          return `${p.name}`;
        }
        return `${p.name}:${CurrencyFormat.format(p.value)}`;
      },
    },
    legend: {
      type: "scroll",
      itemGap: 4,
      orient: "vertical",

      right: 4,
      top: "center",
      height: 180,
      icon: "circle",
      itemHeight: 12,
      pageIcons: {
        vertical: [
          "M729.6 931.2l-416-425.6 416-416c9.6-9.6 9.6-25.6 0-35.2-9.6-9.6-25.6-9.6-35.2 0l-432 435.2c-9.6 9.6-9.6 25.6 0 35.2l432 441.6c9.6 9.6 25.6 9.6 35.2 0C739.2 956.8 739.2 940.8 729.6 931.2z",
          "M761.6 489.6l-432-435.2c-9.6-9.6-25.6-9.6-35.2 0-9.6 9.6-9.6 25.6 0 35.2l416 416-416 425.6c-9.6 9.6-9.6 25.6 0 35.2s25.6 9.6 35.2 0l432-441.6C771.2 515.2 771.2 499.2 761.6 489.6z",
        ],
      },
      pageButtonPosition: "end",
      textStyle: {
        fontSize: 12,
        color: "#828282",
        rich: {
          oneone: {
            width: 110,
            color: "rgba(100, 106, 115, 1)",
            fontSize: 12,
            fontWeight: "400",
          },
          twotwo: {
            width: 40,
            color: "rgba(100, 106, 115, 1)",
            fontSize: 12,
            fontWeight: "400",
          },
          threethree: {
            width: 40,
            color: "rgba(100, 106, 115, 1)",
            fontSize: 12,
            fontWeight: "400",
          },
        },
      },
      formatter: (name: string) => {
        const empty = _.head(data.value)?.empty;
        if (empty) {
          return `{oneone|${_.head(data.value)?.name}}`;
        }

        const dataItem = data.value.find((b) => b.name === name);

        return `{oneone|${
          (dataItem?.name as string).length < 8
            ? dataItem?.name
            : dataItem?.name.substring(0, 7) + "..."
        }}  {twotwo|${CurrencyFormat.format(dataItem?.value)}}`;
      },
    },
    series: [
      {
        itemStyle: {
          borderRadius: 0,
          borderColor: "#fff",
          borderWidth: 2,
        },
        type: "pie",
        center: [84, "50%"],
        radius: [60, 80],
        avoidLabelOverlap: false,
        showEmptyCircle: true,
        label: {
          show: true,
          position: "center",

          width: 120,
          fontWeight: 400,
          fontSize: "12px",
          color: "rgba(100, 106, 115, 1)",
          formatter: () => {
            const empty = _.head(data.value)?.empty;
            if (empty) {
              return `{title|总费用}\r\n{value|-}`;
            }
            const sum = data.value
              .filter((d) => (selected as SimpleMap<boolean>)[d.name])
              .map((a) => a.value)
              .reduce((p, n) => p + n, 0);
            return `{title|总费用}\r\n{value|${CurrencyFormat.format(sum)}}`;
          },
          rich: {
            title: {
              fontSize: 12,
              color: "#646A73",
            },
            value: {
              fontSize: 18,
              color: "#263238",
              lineHeight: 44,
            },
          },
        },
        emphasis: {
          disabled: !!_.head(data.value)?.empty,
          label: {
            show: true,
            width: 110,
            formatter: (a: any) => {
              return `{title|${a.name}}\r\n{value|${CurrencyFormat.format(
                a.value
              )}}`;
            },
            fontSize: "12px",
            fontWeight: "500",
            backgroundColor: "#fff",
            borderRadius: 12,
          },
        },
        labelLine: {
          show: false,
        },
        data: data.value,
        color: _.head(data.value)?.empty
          ? ["rgba(187, 191, 196, 1)"]
          : interpolationColor(
              [
                "rgba(148, 90, 246, 1)",
                "rgba(78, 131, 253, 1)",
                "rgba(250, 211, 85, 1)",
                "rgba(20, 225, 198, 1)",
                "rgba(80, 206, 251, 1)",
              ],
              data.value.length
            ),
      },
    ],
  };
});

onMounted(() => {
  getCurrentMonthBill();
});

defineExpose({ show });
</script>
<template>
  <div class="info-card" v-if="show">
    <div class="title">云主机状态</div>
    <v-chart class="chart" :option="option" autoresize v-loading="loading" />
  </div>
</template>

<style scoped lang="scss">
.info-card {
  background: #ffffff;
  border-radius: 4px;
  padding: 24px;
  overflow: hidden;

  .title {
    font-style: normal;
    font-weight: 500;
    font-size: 16px;
    line-height: 24px;
    margin-bottom: 10px;
  }

  .sub-main-title {
    line-height: 22px;
    font-size: 14px;
  }

  .chart {
    min-height: 200px;
    min-width: 370px;
    width: 100%;
  }
}
</style>
