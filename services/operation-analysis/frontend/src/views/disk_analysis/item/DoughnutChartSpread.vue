<template>
  <el-card shadow="never" class="info-card">
    <el-row :gutter="10">
      <el-col :span="24">
        <div class="title">{{ props.typeText }}</div>
      </el-col>
    </el-row>
    <el-row :gutter="10">
      <el-col :span="24">
        <div class="echarts">
          <div class="echarts-content">
            <v-chart
              class="chart"
              :option="option"
              v-loading="props.loading"
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
import { computed } from "vue";
import _ from "lodash";
import type { ECBasicOption } from "echarts/types/src/util/types";
import type { SimpleMap } from "@commons/api/base/type";
import { interpolationColor } from "@commons/utils/color";
const props = defineProps<{
  apiData: any;
  type: string;
  typeText: string;
  loading: boolean;
  currentUnit: string;
}>();
interface EchartsValue {
  name: string;
  value: number;
}
const data = computed<Array<EchartsValue>>(() => {
  const result: Array<EchartsValue> = [];
  if (props.apiData && props.apiData?.[props.type]?.length > 0) {
    _.forEach(props.apiData?.[props.type], (v) => {
      result.push({ name: v.name, value: v.value });
    });
  } else {
    result.push({ name: "", value: 0 });
  }
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
      right: 0, //值位置
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
            width: 85, //颜色标识位置
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
              props.currentUnit === "block" ? "块" : "GB"
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
          disabled: props.apiData?.[props.type]?.length === 0,
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
          props.apiData?.[props.type]?.length === 0 ||
          !props.apiData?.[props.type]
            ? ["rgba(187, 191, 196, 1)", "rgba(187, 191, 196, 1)"]
            : interpolationColor(chartColor.value, data.value.length),
      },
    ],
  };
});
const chartColor = computed<Array<string>>(() => {
  if (props.type === "byStatus") {
    const result = [];
    if (_.find(data.value, ["name", "空闲"])) {
      result.push("rgb(98, 210, 85, 1)");
    }
    if (_.find(data.value, ["name", "已挂载"])) {
      result.push("rgb(187, 191, 196, 1)");
    }
    if (_.find(data.value, ["name", "其他"])) {
      result.push("rgb(255, 199, 94, 1)");
    }
    return result;
  } else {
    return allColor;
  }
});
const allColor = [
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
];
</script>
<style scoped lang="scss">
.info-card {
  height: 277px;
  background: #ffffff;
  border-radius: 4px;
  flex: none;
  order: 0;
  flex-grow: 0;
}
.chart {
  min-height: 180px;
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
