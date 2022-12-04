import type { Ref } from "vue";
import type { BillSummary, TrendData } from "@/echarts/bill_view/type";
import type { SimpleMap } from "@commons/api/base/type";
import _ from "lodash";
/**
 * 重置数据
 * @param billData 数据
 * @param groupNum 级别
 * @returns 重置后的数据
 */
const resetBillData = (billData: Array<BillSummary>, groupNum: number) => {
  return [...new Set(billData.map((b) => b["group" + groupNum]))].map(
    (group: string) => {
      const value = billData
        .filter((b) => b["group" + groupNum] === group)
        .map((b) => b.value)
        .reduce((p, n) => p + n, 0);
      return { name: group, value };
    }
  );
};

/**
 * 获取账单总览Options
 * @param billData 账单数据
 * @param groups   所在组级
 * @param selected 选中状态
 * @returns 账单总览Options
 */
const getBillViewOptions = (
  billData: Array<BillSummary>,
  groups: Ref<Array<string>>,
  selected?: SimpleMap<boolean>,
  legendRichWidth?: number
) => {
  if (groups.value && groups.value.length > 1) {
    billData = billData.filter((item) => {
      return Array.from({ length: groups.value.length })
        .map((i, index) => index)
        .filter((i) => i > 0)
        .every((index) => {
          return item["group" + index] === groups.value[index];
        });
    });
  } else {
    groups.value = ["root"];
  }
  const filterData = resetBillData(billData, groups.value.length);
  if (!selected) {
    // 设置选中状态
    selected = filterData
      .map((b) => {
        return { [b.name]: true };
      })
      .reduce((pre, next) => {
        return { ...pre, ...next };
      }, {});
  }
  /**
   *设置echarts 数据
   */
  const options = {
    legend: {
      selected: selected,
      type: "scroll",
      itemGap: 4,
      orient: "vertical",
      top: "center",
      left: "50%",
      icon: "circle",
      itemHeight: 12,
      textStyle: {
        fontSize: 24,
        color: "#828282",
        rich: {
          oneone: {
            width: legendRichWidth ? legendRichWidth : 150,
            color: "#333333",
            fontSize: 12,
            fontWeight: "bolder",
          },
          twotwo: {
            width: legendRichWidth ? legendRichWidth : 150,
            color: "#333333",
            fontSize: 12,
          },
          threethree: {
            width: legendRichWidth ? legendRichWidth : 150,
            color: "#333333",
            fontSize: 12,
          },
        },
      },
      formatter: (name: string) => {
        const dataItem = filterData.find((b) => b.name === name);
        const sum = filterData
          .map((i) => i.value)
          .reduce((p: number, n: number) => p + n, 0);
        const a = Math.floor(((dataItem?.value as number) / sum) * 10000) / 100;
        return `{oneone|${
          (dataItem?.name as string).length < 15
            ? dataItem?.name
            : dataItem?.name.substring(0, 15) + ".."
        }}  {twotwo|${_.round(dataItem ? dataItem.value : 0, 2).toFixed(
          2
        )}}   {threethree|${a}%}`;
      },
    },
    tooltip: {
      trigger: "item",

      formatter: (p: any) => {
        return `${p.name}:${_.round(p.value, 2).toFixed(2)}`;
      },
    },
    series: [
      {
        itemStyle: {
          borderRadius: 10,
          borderColor: "#fff",
          borderWidth: 2,
        },
        type: "pie",
        center: ["20%", "50%"],
        radius: ["50%", "90%"],
        zlevel: 1,
        avoidLabelOverlap: false,
        label: {
          normal: {
            show: true,
            position: "center",
            backgroundColor: "#fff",
            width: 140,
            color: "#4c4a4a",
            formatter: () => {
              const sum = filterData
                .filter((d) => (selected as SimpleMap<boolean>)[d.name])
                .map((a) => a.value)
                .reduce((p, n) => p + n, 0);
              return `{title|总费用}\r\n{value|${_.round(sum, 2).toFixed(2)}}`;
            },
            rich: {
              title: {
                fontSize: 14,
                color: "#333",
              },
              value: {
                fontSize: 30,
                color: "#000",
                lineHeight: 44,
              },
            },
          },
        },
        emphasis: {
          label: {
            show: true,
            width: 140,
            formatter: (a: any) => {
              return `{title|${a.name}}\r\n{value|${_.round(a.value, 2).toFixed(
                2
              )}}`;
            },
            fontWeight: "bold",
          },
        },
        labelLine: {
          show: false,
        },
        data: filterData,
      },
    ],
  };
  return options;
};

const getTrendViewOption = (
  data: Array<TrendData>,
  type: "line" | "bar",
  showx: boolean,
  showy: boolean,
  showSymbol: boolean
) => {
  const option = {
    xAxis: {
      type: "category",
      data: data.map((d) => d.label),
      show: showx,
    },
    yAxis: {
      type: "value",
      show: showy,
    },
    series: [
      {
        data: data.map((d) => d.value),
        type: type,
        smooth: true,
        showSymbol: showSymbol,
      },
    ],
  };
  return option;
};

/**
 * 初始化账单总览
 * @param echarts  echars对象
 * @param el       图标所在标签
 * @param billData 账单数据
 * @param groups   分组对象
 * @returns        echarts实例对象
 */
const initBillView = (
  echarts: any,
  el: HTMLElement,
  billData: Array<BillSummary>,
  groups: Ref<Array<string>>,
  width?: number
) => {
  // 初始化echarts图表
  const myChart = echarts.init(el);

  // 获取数据
  const options = getBillViewOptions(billData, groups, undefined, width);
  myChart.setOption(options);
  return myChart;
};

export { initBillView, getBillViewOptions, getTrendViewOption };
