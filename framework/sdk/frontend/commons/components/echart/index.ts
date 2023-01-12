import _ from "lodash";

const defaultSpeedOptions = {
  series: [
    {
      type: "gauge",
      progress: {
        show: false,
        width: 1,
      },
      axisLine: {
        lineStyle: {
          width: 10,
          color: [
            [0.6, "#99cc00"],
            [0.8, "#ffcc00"],
            [1, "#ff3300"],
          ],
        },
      },
      axisTick: {
        show: false,
      },
      splitLine: {
        distance: 1,
        length: 5,
        lineStyle: {
          width: 1,
          color: "#999",
        },
      },
      axisLabel: {
        distance: 15,
        color: "#999",
        fontSize: 8,
      },
      anchor: {
        show: true,
        showAbove: true,
        size: 8,
        itemStyle: {
          borderWidth: 3,
        },
      },
      pointer: {
        width: 2,
      },
      title: {
        offsetCenter: [0, "100%"],
        fontSize: 15,
        show: true,
      },
      radius: "80%",
      //center:['30%','35%'],
      detail: {
        formatter: function (value: string) {
          if (value) {
            return value + "%";
          } else {
            return "0" + "%";
          }
        },
        valueAnimation: true,
        fontSize: 20,
        offsetCenter: [0, "65%"],
      },
      data: [{ value: 0 }],
    },
  ],
};
const defaultTrendOptions = {
  color: ["#80FFA5", "#00DDFF", "#0080ff", "#FFBF00", "#FF0087", "#37A2FF"],
  title: {},
  tooltip: {
    trigger: "axis",
    axisPointer: {
      type: "cross",
      label: {
        backgroundColor: "#6a7985",
      },
    },
  },
  legend: {
    type: "scroll",
    data: ["Line 1", "Line 2", "Line 3", "Line 4", "Line 5"],
  },
  toolbox: {
    feature: {
      // saveAsImage: {}
    },
  },
  grid: {
    left: "3%",
    right: "4%",
    bottom: "3%",
    containLabel: true,
  },
  xAxis: [
    {
      type: "category",
      boundaryGap: false,
      data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
    },
  ],
  yAxis: [
    {
      type: "value",
    },
  ],
  series: [
    {
      name: "Line 1",
      type: "line",
      stack: "Total",
      smooth: true,
      lineStyle: {
        width: 0,
      },
      showSymbol: false,
      areaStyle: {
        opacity: 0.8,
      },
      emphasis: {
        focus: "series",
      },
      data: [140, 232, 101, 264, 90, 340, 250],
    },
  ],
};
const defaultLineOption = {
  tooltip: {
    trigger: "axis",
    axisPointer: {
      type: "cross",
      label: {
        backgroundColor: "#6a7985",
      },
    },
  },
  xAxis: {
    type: "category",
    boundaryGap: false,
    data: [],
  },
  yAxis: {
    type: "value",
  },
  series: [
    {
      data: [],
      type: "line",
      areaStyle: {},
    },
  ],
};
const emptyOptions = {
  title: {
    text: "暂无数据",
    x: "center",
    y: "center",
    textStyle: {
      fontSize: 12,
      fontWeight: "normal",
    },
  },
};
/**
 * 圆环饼图
 */
const defaultPieDoughnutOptions = {
  tooltip: {
    trigger: "item",
  },
  legend: {
    orient: "vertical",
    type: "scroll",
    right: "45",
    top: "10%",
    height: "180",
    align: "left",
    textStyle: {
      width: "10px",
      fontFamily:
        "'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif",
    },
    formatter: function (name: any) {
      const len = name.length;
      if (len > 5) {
        name = name.slice(0, 5) + "...";
      }
      return name;
    },
  },
  icon: "circle",
  title: {
    // 主标题样式
    textStyle: {
      color: "#666",
      fontSize: 18,
    },
    itemGap: 10,
    x: "center",
    y: "center",
    text: "",
    // 副标题样式
    subtextStyle: {
      color: "#666",
    },
    position: "center",
  },
  series: [
    {
      name: "",
      type: "pie",
      radius: ["40%", "65%"],
      center: ["31%", "45%"],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: "#fff",
        borderWidth: 2,
      },
      label: {
        show: true,
        position: "center",
        normal: {
          show: true,
          position: "center",
          fontSize: 14,
          backgroundColor: "#fff",
          formatter: "总数\n0",
          fontWeight: "bold",
          rich: {
            title: {
              fontSize: 14,
              color: "#333",
            },
            value: {
              fontSize: 30,
              color: "#000",
            },
          },
        },
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 14,
          width: 100,
          fontWeight: "bold",
          formatter: `{title|{b}}\r\n{value|{c}}`,
        },
      },
      labelLine: {
        show: false,
      },
      data: [{ value: 0 }],
    },
  ],
};

const defaultBarOptions = {
  color: ["#95ceff", "#434348"],
  tooltip: {
    show: true,
  },
  xAxis: {
    type: "category",
    data: [],
  },
  yAxis: {
    type: "value",
  },
  series: [
    {
      data: [],
      type: "bar",
    },
  ],
};

/**
 * 颜色
 */
const trendSeriesColor = [
  [
    {
      offset: 0,
      color: "rgb(128, 255, 165)",
    },
    {
      offset: 1,
      color: "rgb(1, 191, 236)",
    },
  ],
  [
    {
      offset: 0,
      color: "rgb(0, 221, 255)",
    },
    {
      offset: 1,
      color: "rgb(77, 119, 255)",
    },
  ],
  [
    {
      offset: 0,
      color: "rgb(55, 162, 255)",
    },
    {
      offset: 1,
      color: "rgb(116, 21, 219)",
    },
  ],
  [
    {
      offset: 0,
      color: "rgb(255, 191, 0)",
    },
    {
      offset: 1,
      color: "rgb(224, 62, 76)",
    },
  ],
  [
    {
      offset: 0,
      color: "rgb(255, 0, 135)",
    },
    {
      offset: 1,
      color: "rgb(135, 0, 157)",
    },
  ],
];
/**
 * 随机颜色
 */
const getRandomColor = () => {
  const randomColor = [
    {
      offset: 0,
      color:
        "rgb(" +
        _.random(0, 255) +
        ", " +
        _.random(0, 255) +
        ", " +
        _.random(0, 255) +
        ")",
    },
    {
      offset: 1,
      color:
        "rgb(" +
        _.random(0, 255) +
        ", " +
        _.random(0, 255) +
        ", " +
        _.random(0, 255) +
        ")",
    },
  ];
  return randomColor;
};

export {
  defaultSpeedOptions,
  defaultTrendOptions,
  defaultLineOption,
  emptyOptions,
  defaultPieDoughnutOptions,
  trendSeriesColor,
  getRandomColor,
  defaultBarOptions,
};
