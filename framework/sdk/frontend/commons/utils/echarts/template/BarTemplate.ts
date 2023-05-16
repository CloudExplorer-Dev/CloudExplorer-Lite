import type { DataItem } from "@commons/utils/echarts/template/index";
import {
  xDataZoomTemplate,
  yDataZoomTemplate,
  OptionTemplate,
} from "@commons/utils/echarts/template/index";
const barCahrtEmptyOptionTemplate = (title: string) => {
  const option = {
    title: [
      {
        text: title,
        top: "24",
        left: "24",
        textStyle: {
          color: "#1F2329",
          fontSize: 16,
          fontWeight: 500,
        },
      },
      {
        text: "暂无数据",
        top: "50%",
        left: "45%",
        backgroundColor: "#F5F6F7",
        borderColor: "#ccc",
        borderRadius: "4px",
        textStyle: {
          color: "#646A73",
          fontSize: 12,
          fontWeight: 400,
          width: "74px",
          height: "32px",
        },
      },
    ],
    grid: {
      x: 50,
      y: 79,
      x2: 24,
      y2: 48,
    },
    xAxis: {
      type: "category",
      data: ["-", "-", "-", "-", "-", "-"],
      axisLabel: {
        show: true,
        color: "rgba(143, 149, 158, 1)",
        fontSize: 12,
      },
    },
    yAxis: {
      triggerEvent: true,
      type: "value",
      min: 1,
      max: 15,
      axisLabel: {
        color: "rgba(143, 149, 158, 1)",
        fontSize: 12,
        formatter: () => "-",
      },
    },
    series: [
      {
        barWidth: 10,
        type: "bar",
        smooth: true,
      },
    ],
  };
  return option;
};
const barCahrtOptionTemplate = (dataList: Array<DataItem>) => {
  const option = {
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
    grid: {
      x: 50,
      y: 79,
      x2: 24,
      y2: 48,
    },
    xAxis: {
      type: "category",
      data: dataList.map((d) =>
        d.name.length > 6 ? d.name.substring(0, 6) + "..." : d.name
      ),
      axisLabel: {
        show: true,
        color: "rgba(143, 149, 158, 1)",
        fontSize: 12,
      },
    },
    yAxis: {
      triggerEvent: true,
      type: "value",
      axisLabel: {
        color: "rgba(143, 149, 158, 1)",
        fontSize: 12,
      },
    },
    series: [
      {
        barWidth: 16,

        data: dataList.map((d) => ({
          ...d,
          value: d.value,
          name: d.name,
          itemStyle: {
            borderRadius: [2, 2, 0, 0],
            color: "#4E83FD",
          },
        })),

        type: "bar",
        smooth: true,
      },
    ],
  };
  return option;
};

export class BarTemplate extends OptionTemplate {
  /**
   * 获取空数据骨架
   * @param title 标题
   * @returns 柱状图模板
   */
  static ofEmpty(title: string) {
    return new BarTemplate(barCahrtEmptyOptionTemplate(title));
  }
  /**
   *
   * @param dataList 根据数据渲染options
   * @returns        柱状图模板
   */
  static of(dataList: Array<DataItem>) {
    return new BarTemplate(barCahrtOptionTemplate(dataList));
  }
  /**
   *添加横向滚动条
   */
  appendXDataZoom() {
    this.update("dataZoom", xDataZoomTemplate());
  }
  /**
   * 添加竖向滚动条
   */
  appendYDataZoom() {
    this.update("dataZoom", yDataZoomTemplate());
  }
  /**
   *转换为横向
   */
  toHorizontal() {
    this.swap("xAxis", "yAxis");
  }
}
