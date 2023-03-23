import type { DataItem } from "@commons/utils/echarts/template/index";
import {
  baseColors,
  OptionTemplate,
} from "@commons/utils/echarts/template/index";
import _ from "lodash";
import { interpolationColor } from "@commons/utils/color";
/**
 * 饼图模板配置
 * @returns 饼图option模板
 */
const pieChartOptionTemplate = (dataList: Array<DataItem>) => {
  return {
    title: {
      text: "",
      top: "24",
      left: "24",
      textStyle: {
        color: "#1F2329",
        fontSize: 16,
        fontWeight: 500,
      },
    },
    legend: {
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
          one: {
            width: 160,
            color: "rgba(100, 106, 115, 1)",
            fontSize: 12,
            fontWeight: "400",
          },
          two: {
            width: 130,
            color: "rgba(100, 106, 115, 1)",
            fontSize: 12,
            fontWeight: "400",
          },
          three: {
            width: 160,
            color: "rgba(100, 106, 115, 1)",
            fontSize: 12,
            fontWeight: "400",
          },
        },
      },
      formatter: (name: string) => {
        const data = dataList.find((data) => data.name === name);
        // 获取总数
        const sum = dataList
          .map((i) => i.value)
          .reduce((p: number, n: number) => p + n, 0);
        // 获取百分比
        const percentage =
          Math.floor(((data ? data.value : (0 as number)) / sum) * 10000) / 100;
        const two =
          _.round(data ? data.value : 0, 2).toFixed(2) +
          "    (" +
          percentage +
          "%)";
        return `{one|${
          (name as string).length < 20 ? name : name.substring(0, 15) + ".."
        }} {two|${two}}`;
      },
    },
    tooltip: {
      trigger: "item",
      formatter: (p: any) => {
        return `${p.name}:${p.value}`;
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
        center: ["120px", "50%"],
        radius: ["80px", "100px"],
        zlevel: 100,
        avoidLabelOverlap: false,
        label: {
          show: true,
          position: "center",
          backgroundColor: "#fff",
          width: 120,
          fontWeight: 400,
          fontSize: "12px",
          color: "rgba(100, 106, 115, 1)",
          formatter: (dataItem: any) => {
            // 获取总数
            const sum = dataList
              .map((i) => i.value)
              .reduce((p: number, n: number) => p + n, 0);
            return `{title|资源总数}\r\n{value|${_.round(sum, 2).toFixed(2)}}`;
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
        emphasis: {
          label: {
            show: true,
            width: 120,
            formatter: (a: any) => {
              console.log("a", a);
              return `{title|${a.name}}\r\n{value|${_.round(a.value, 2).toFixed(
                2
              )}}`;
            },
            fontSize: "18px",
            fontWeight: "500",
          },
        },
        labelLine: {
          show: false,
        },
        data: dataList,
        color: interpolationColor(baseColors, dataList.length),
      },
    ],
  };
};

export class PieTemplate extends OptionTemplate {
  /**
   *
   * @param dataList 根据数据渲染options
   * @returns        饼图模板对象
   */
  static of(dataList: Array<DataItem>) {
    return new PieTemplate(pieChartOptionTemplate(dataList));
  }
  /**
   * 修改数据
   * @param dataList 修改数据
   */
  updateDataList(dataList: Array<DataItem>) {
    this.update("series.0.data", dataList);
    this.update("series.0.color", dataList);
  }
  updateTextRichWidth(
    oneWidth?: number,
    twoWidth?: number,
    threeWidth?: number
  ) {
    if (oneWidth) {
      this.update("textStyle.rich.one.width", oneWidth);
    }
    if (twoWidth) {
      this.update("textStyle.rich.two.width", twoWidth);
    }
    if (threeWidth) {
      this.update("textStyle.rich.three.width", threeWidth);
    }
  }
  /**
   * 修改图标位置
   */
  updateSeriesCenter(center: Array<string | number>) {
    this.update("series.0.center", center);
  }
  /**
   * 修改图表 圆角 内圈和外圈直径大小
   * @param radius
   */
  updateSeriesRadius(radius: Array<string | number>) {
    this.update("series.0.radius", radius);
  }
}
