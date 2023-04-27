<template>
  <div class="content">
    <div style="width: 100%; height: 100%" ref="billRuleChart"></div>
    <div class="breadcrumb">
      <span
        @click="fallback()"
        :class="groups.length > 0 ? 'has_child_title' : 'title'"
      >
        费用分布
      </span>
      <span
        class="breadcrumb_item"
        v-for="(group, index) in groups"
        @click="go(group, index)"
        :key="index"
        ><ce-icon class="icon" code="icon_right_outlined" size="12px"></ce-icon>
        <span class="group">{{ group }}</span></span
      >
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, inject, nextTick } from "vue";
import type { BillSummary } from "@/echarts/bill_view/type";
import type { ECharts } from "echarts";
import type { SimpleMap } from "@commons/api/base/type";
import type { BillView } from "@/api/bill_view/type";
import { initBillView, getBillViewOptions } from "@/echarts/bill_view/index";
// 图表对象
const chart = ref<ECharts>();
// echarts根据
const echarts: any = inject("echarts");
// 图表绘制容器
const billRuleChart = ref<InstanceType<typeof HTMLElement>>();
const emit = defineEmits(["update:viewData", "update:groups"]);
const props = withDefaults(
  defineProps<{
    /**
     * 月份
     */
    month: string;
    /**
     *获取账单
     * @param loading
     */
    getBillViewData: () => Promise<SimpleMap<Array<BillView>>>;
  }>(),
  {
    month:
      new Date().getFullYear().toString() +
      "-" +
      ((new Date().getMonth() + 1).toString().length === 1
        ? "0" + (new Date().getMonth() + 1).toString()
        : (new Date().getMonth() + 1).toString()),
  }
);
// 下钻面包屑
const groups = ref<Array<string>>([]);
// 列表数据
const viewData = ref<Array<BillSummary>>([]);
// 账单聚合数据
const billViewData = ref<SimpleMap<Array<BillView>>>();

/**
 * 获取表单数据 根据账单聚合数据获取账单table数据
 * @param billViewData 账单聚合数据
 */
const getViewtableData = (billViewData: SimpleMap<Array<BillView>>) => {
  const treedMap: any = Object.keys(billViewData)
    .flatMap((key) => {
      return billViewData[key].map((item) => {
        return {
          [key + item.billGroupDetails.map((i) => i.value).join(",")]:
            item.value,
        };
      });
    })
    .reduce((pre, next) => {
      return { ...pre, ...next };
    }, {});
  console.log(
    "xx",
    Object.keys(billViewData).includes(props.month),
    props.month
  );
  if (Object.keys(billViewData).includes(props.month)) {
    // 树形数据
    const data = billViewData[props.month].map((view) => {
      return view.billGroupDetails
        .map((b) => {
          return { [b.key]: b.value };
        })
        .reduce(
          (pre, next) => {
            return { ...pre, ...next };
          },
          {
            treed: Object.keys(billViewData).map((month) => {
              const v =
                treedMap[
                  month + view.billGroupDetails.map((i) => i.value).join(",")
                ];
              return { label: month, value: v ? v : 0 };
            }),
            value: view.value,
          }
        );
    });
    const viewTableData = data as unknown as Array<BillSummary>;
    viewTableData.sort((pre, next) => next.value - pre.value);
    return viewTableData;
  }
  return [];
};

/**
 * 绘制图表
 * @param viewData 表格数据
 */
const drawCharts = (viewData: Array<BillSummary>) => {
  groups.value = [];
  if (chart.value) {
    chart.value?.setOption(
      getBillViewOptions(viewData, groups, undefined, undefined)
    );
    chart.value.resize();
  } else {
    init();
  }
};

/**
 * 初始化饼图
 */
const init = () => {
  chart.value = initBillView(
    echarts,
    billRuleChart.value as HTMLElement,
    viewData.value,
    groups
  );
  // 监听图标点击事件
  chart.value?.on("click", "series", (param: { name: string }) => {
    if (
      viewData.value.every((i) =>
        Object.keys(i).includes("group" + (groups.value.length + 2))
      )
    ) {
      groups.value.push(param.name);
      console.log("groups.value)", groups.value);
      emit("update:groups", groups.value);
      nextTick(() => {
        chart.value?.setOption(
          getBillViewOptions(viewData.value, groups, undefined)
        );
      });
    }
  });
  // 监听legend选中事件
  chart.value?.on("legendselectchanged", (param: any) => {
    chart.value?.setOption(
      getBillViewOptions(viewData.value, groups, param.selected)
    );
  });
};

// 刷新数据
const refresh = () => {
  props.getBillViewData().then((billView) => {
    billViewData.value = billView;
    viewData.value = getViewtableData(billView);
    groups.value = [];
    emit("update:groups", groups.value);
    emit("update:viewData", viewData.value);
    drawCharts(viewData.value);
  });
};

/**
 *
 */
const reSize = () => {
  chart.value?.resize();
};
/**
 * 返回根
 */
const fallback = () => {
  groups.value = [];
  emit("update:groups", groups.value);
  chart.value?.setOption(getBillViewOptions(viewData.value, groups, undefined));
};
/**
 *返回指定位置
 * @param group 组
 * @param index 指定index
 */
const go = (group: string, index: number) => {
  if (index + 1 === groups.value.length) {
    return;
  }
  groups.value.splice(index + 1, groups.value.length);
  emit("update:groups", groups.value);
  chart.value?.setOption(getBillViewOptions(viewData.value, groups, undefined));
};

// 暴露刷新函数
defineExpose({ refresh, reSize });
</script>
<style lang="scss" scoped>
.content {
  width: 100%;
  height: 100%;
  position: relative;
  .breadcrumb {
    display: flex;
    position: absolute;
    top: 24px;
    left: 28px;
    height: 22px;
    font-weight: 500;
    font-size: 14px;
    line-height: 22px;

    .title {
      color: rgba(31, 35, 41, 1);
      cursor: pointer;
    }
    .has_child_title {
      color: rgba(100, 106, 115, 1);
      cursor: pointer;
    }
    .breadcrumb_item {
      display: flex;
      .icon {
        color: rgba(143, 149, 158, 1);
        line-height: 22px;
        display: inline-block;
      }
      .group {
        cursor: pointer;
        &:hover {
          color: rgba(51, 112, 255, 1);
        }
      }
    }
  }
}
</style>
