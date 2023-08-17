import type { Ref } from "vue";
import type { BillSummary, TrendData } from "@/echarts/bill_view/type";
import type { SimpleMap } from "@commons/api/base/type";
import { buildColors } from "@commons/utils/color";
import _ from "lodash";
import CurrencyFormat from "@commons/utils/currencyFormat";
import type { Currency } from "@commons/api/bil_view/type";
/**
 * 重置数据
 * @param billData 数据
 * @param groupNum 级别
 * @returns 重置后的数据
 */
const resetBillData = (billData: Array<BillSummary>, groupNum: number) => {
  return [...new Set(billData.map((b) => b["group" + (groupNum + 1)]))].map(
    (group: string) => {
      const value = billData
        .filter((b) => b["group" + (groupNum + 1)] === group)
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
  currency: Currency,
  selected?: SimpleMap<boolean>,
  legendRichWidth?: number
) => {
  if (groups.value && groups.value.length > 0) {
    billData = billData.filter((item) => {
      return Array.from({ length: groups.value.length }).every((i, index) => {
        return item["group" + (index + 1)] === groups.value[index];
      });
    });
  } else {
    groups.value = [];
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

      left: "264px",
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
        fontSize: 24,
        color: "#828282",
        rich: {
          oneone: {
            width: legendRichWidth ? legendRichWidth : 160,
            color: "rgba(100, 106, 115, 1)",
            fontSize: 12,
            fontWeight: "400",
          },
          twotwo: {
            width: legendRichWidth ? legendRichWidth : 130,
            color: "rgba(100, 106, 115, 1)",
            fontSize: 12,
            fontWeight: "400",
          },
          threethree: {
            width: legendRichWidth ? legendRichWidth : 160,
            color: "rgba(100, 106, 115, 1)",
            fontSize: 12,
            fontWeight: "400",
          },
        },
      },
      formatter: (name: string) => {
        const dataItem = filterData.find((b) => b.name === name);

        const sum = filterData
          .map((i) => i.value)
          .reduce((p: number, n: number) => p + n, 0);
        const a =
          sum == 0
            ? 0
            : Math.floor(((dataItem?.value as number) / sum) * 10000) / 100;
        const tow =
          CurrencyFormat.format(
            Number.parseFloat(
              _.round(dataItem ? dataItem.value : 0, 2).toFixed(2)
            ),
            currency.code
          ) +
          "    (" +
          a +
          "%)";
        return `{oneone|${
          (dataItem?.name as string).length < 20
            ? dataItem?.name
            : dataItem?.name.substring(0, 15) + ".."
        }}  {twotwo|${tow}}`;
      },
    },
    tooltip: {
      trigger: "item",
      formatter: (p: any) => {
        return `${p.name}:${CurrencyFormat.format(
          Number.parseFloat(_.round(p.value, 2).toFixed(2)),
          currency.code
        )}`;
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
        center: ["130px", "50%"],
        radius: ["80px", "100px"],
        zlevel: 1,
        avoidLabelOverlap: false,
        label: {
          show: true,
          position: "center",
          backgroundColor: "#fff",
          width: 120,
          fontWeight: 400,
          fontSize: "12px",
          color: "rgba(100, 106, 115, 1)",
          formatter: () => {
            const sum = filterData
              .filter((d) => (selected as SimpleMap<boolean>)[d.name])
              .map((a) => a.value)
              .reduce((p, n) => p + n, 0);
            return `{title|总费用}\r\n{value|${CurrencyFormat.format(
              Number.parseFloat(_.round(sum, 2).toFixed(2)),
              currency.code
            )}}`;
          },
          rich: {
            title: {
              fontSize: 12,
              color: "#333",
            },
            value: {
              fontSize: 12,
              color: "#000",
              lineHeight: 44,
            },
          },
        },
        emphasis: {
          label: {
            show: true,
            width: 120,
            formatter: (a: any) => {
              return `{title|${a.name}}\r\n{value|${CurrencyFormat.format(
                Number.parseFloat(_.round(a.value, 2).toFixed(2)),
                currency.code
              )}}`;
            },
            fontSize: "18px",
            fontWeight: "500",
          },
        },
        labelLine: {
          show: false,
        },
        data: filterData,
        color: buildColors(filterData.length),
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
        barWidth: 10,
        data: data.map((d) => ({
          value: d.value,
          itemStyle: { normal: { barBorderRadius: [2, 2, 0, 0] } },
        })),
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
  currency: Currency,
  width?: number
) => {
  // 初始化echarts图表
  const myChart = echarts.init(el);

  // 获取数据
  const options = getBillViewOptions(
    billData,
    groups,
    currency,
    undefined,
    width
  );
  myChart.setOption(options);
  return myChart;
};

export { initBillView, getBillViewOptions, getTrendViewOption };
