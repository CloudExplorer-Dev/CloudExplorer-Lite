<template>
  <el-card shadow="never" class="info-card">
    <el-row :gutter="10">
      <el-col :span="10">
        <div class="title">基础资源分布</div>
      </el-col>
      <el-col :span="14" style="text-align: right">
        <el-radio-group class="custom-radio-group" v-model="spreadType">
          <el-radio-button label="host">宿主机</el-radio-button>
          <el-radio-button label="datastore">存储器</el-radio-button>
        </el-radio-group>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="24">
        <div class="echarts">
          <div class="echarts-content">
            <v-chart
              class="chart"
              :option="option"
              v-loading="loading"
              autoresize
            />
          </div>
        </div>
      </el-col>
    </el-row>
  </el-card>
</template>
<script setup lang="ts">
import VChart from "vue-echarts";
import { computed, ref, watch } from "vue";
import ResourceSpreadViewApi from "@/api/resource_spread_view/index";
import _ from "lodash";
import type { ResourceAnalysisRequest } from "@commons/api/resource_spread_view/type";
import type { ECBasicOption } from "echarts/types/src/util/types";
import type { SimpleMap } from "@commons/api/base/type";
import { interpolationColor } from "@commons/utils/color";
const props = defineProps<{
  cloudAccountId?: string | undefined;
  clusterId?: string | undefined;
  datastoreId?: string | undefined;
  hostId?: string | undefined;
}>();
const spreadType = ref<string>("host");
const params = ref<ResourceAnalysisRequest>();
const loading = ref<boolean>(false);
const apiData = ref<any>();
const setParams = () => {
  props.cloudAccountId
    ? _.set(
        params,
        "accountIds",
        props.cloudAccountId === "all" ? [] : [props.cloudAccountId]
      )
    : "";
  props.clusterId
    ? _.set(
        params,
        "clusterIds",
        props.clusterId === "all" ? [] : [props.clusterId]
      )
    : "";
  props.datastoreId
    ? _.set(
        params,
        "datastoreIds",
        props.datastoreId === "all" ? [] : [props.datastoreId]
      )
    : "";
  props.hostId
    ? _.set(params, "hostIds", props.hostId === "all" ? [] : [props.hostId])
    : "";
};
//获取数宿主机按云账号分布
const getSpreadInfo = () => {
  setParams();
  ResourceSpreadViewApi.getSpreadInfo(params, loading).then(
    (res) => (apiData.value = res.data)
  );
};
interface EchartsValue {
  name: string;
  value: number;
}

const data = computed<Array<EchartsValue>>(() => {
  const result: Array<EchartsValue> = [];
  _.forEach(apiData.value?.[spreadType.value], (v) => {
    result.push({ name: v.name, value: v.value });
  });
  return result;
});
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
        return `${p.name}:${p.value}`;
      },
    },
    legend: {
      type: "scroll",
      itemGap: 8,
      orient: "vertical",
      right: 20, //值位置
      top: "center",
      height: 150,
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
        color: "rgb(100,106,115,1)",
        rich: {
          oneone: {
            width: 120, //颜色标识位置
            color: "rgba(100, 106, 115, 1)",
            fontSize: 12,
            fontWeight: "400",
          },
          twotwo: {
            width: 40,
            color: "rgba(31, 35, 41, 1)",
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
        const dataItem = data.value.find((b) => b.name === name);

        return `{oneone|${
          (dataItem?.name as string).length < 8
            ? dataItem?.name
            : dataItem?.name.substring(0, 7) + "..."
        }}  {twotwo|${dataItem?.value}}`;
      },
    },
    series: [
      {
        itemStyle: {
          borderRadius: 0,
          borderColor: "#fff",
          borderWidth: 1,
        },
        type: "pie",
        center: [84, "50%"],
        radius: [65, 80],
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
            const sum = data.value
              .filter((d) => (selected as SimpleMap<boolean>)[d.name])
              .map((a) => a.value)
              .reduce((p, n) => p + n, 0);
            return `{title|总数（${
              spreadType.value === "host" ? "台" : "块"
            }）}\r\n{value|${sum}}`;
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
          disabled: apiData.value?.[spreadType.value].length === 0,
          label: {
            show: true,
            width: 110,
            formatter: (a: any) => {
              return `{title|${a.name}}\r\n{value|${a.value}}`;
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
        color:
          apiData.value?.[spreadType.value].length === 0
            ? ["rgba(187, 191, 196, 1)", "rgba(187, 191, 196, 1)"]
            : interpolationColor(
                [
                  "rgb(79, 131, 253, 1)",
                  "rgb(150, 189, 255, 1)",
                  "rgb(250, 211, 85, 1)",
                  "rgb(255, 230, 104, 1)",
                  "rgb(22, 225, 198, 1)",
                  "rgb(76, 253, 224, 1)",
                  "rgb(81, 206, 251, 1)",
                  "rgb(118, 240, 255, 1)",
                  "rgb(148, 90, 246, 1)",
                  "rgb(220, 155, 255, 1)",
                  "rgb(255, 165, 61, 1)",
                  "rgb(255, 199, 94, 1)",
                  "rgb(241, 75, 169, 1)",
                  "rgb(255, 137, 227, 1)",
                  "rgb(247, 105, 101, 1)",
                  "rgb(255, 158, 149, 1)",
                  "rgb(219, 102, 219, 1)",
                  "rgb(254, 157, 254, 1)",
                  "rgb(195, 221, 65, 1)",
                  "rgb(217, 244, 87, 1)",
                  "rgb(97, 106, 229, 1)",
                  "rgb(172, 173, 255, 1)",
                  "rgb(98, 210, 85, 1)",
                  "rgb(134, 245, 120, 1)",
                ],
                data.value.length
              ),
      },
    ],
  };
});
watch(
  props,
  () => {
    getSpreadInfo();
  },
  { immediate: true }
);
</script>
<style scoped lang="scss">
.info-card {
  height: 277px;
  background: #ffffff;
  border-radius: 4px;
  flex: none;
  order: 0;
  flex-grow: 0;
  min-width: 400px;
}
.chart {
  min-height: 189px;
  width: 100%;
}
.title {
  font-weight: bold;
  font-size: 16px;
  padding-top: 4px;
  padding-bottom: 16px;
}
.echarts-content {
  margin-top: 7px;
}
.echarts-footer {
  margin-top: 15px;
  margin-bottom: 10px;
  position: initial;

  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 12px;
  text-align: center;
  color: #1f2329;
}
</style>
