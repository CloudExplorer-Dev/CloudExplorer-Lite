export const baseColors = [
  "rgba(78, 131, 253, 1)",
  "rgba(150, 189, 255, 1)",
  "rgba(250, 211, 85, 1)",
  "rgba(255, 230, 104, 1)",
  "rgba(20, 225, 198, 1)",
  "rgba(76, 253, 224, 1)",
  "rgba(80, 206, 251, 1)",
  "rgba(80, 206, 251, 1)",
  "rgba(80, 206, 251, 1)",
  "rgba(80, 206, 251, 1)",
  "rgba(255, 165, 61, 1)",
  "rgba(255, 199, 94, 1)",
  "rgba(241, 75, 169, 1)",
  "rgba(255, 137, 227, 1)",
  "rgba(247, 105, 100, 1)",
  "rgba(255, 158, 149, 1)",
  "rgba(219, 102, 219, 1)",
  "rgba(254, 157, 254, 1)",
  "rgba(254, 157, 254, 1)",
  "rgba(217, 244, 87, 1)",
  "rgba(217, 244, 87, 1)",
  "rgba(217, 244, 87, 1)",
  "rgba(98, 210, 86, 1)",
  "rgba(135, 245, 120, 1)",
];
/**
 * 竖向滚动条模板
 * @returns
 */
export const yDataZoomTemplate = () => {
  return [
    {
      type: "inside",
      startValue: 8,
      minValueSpan: 8,
      maxValueSpan: 8,
      yAxisIndex: [0],
      zoomOnMouseWheel: false, // 关闭滚轮缩放
      moveOnMouseWheel: true, // 开启滚轮平移
      moveOnMouseMove: true, // 鼠标移动能触发数据窗口平移
    },
    {
      type: "slider",
      realtime: true,
      startValue: 8,
      width: "3.5",
      height: "90%",
      yAxisIndex: [0], // 控制y轴滚动
      fillerColor: "rgba(255,255,255,0)", // 滚动条颜色
      borderColor: "rgba(255,255,255,0)",
      backgroundColor: "rgba(255,255,255,0)", //两边未选中的滑动条区域的颜色
      handleSize: 0, // 两边手柄尺寸
      showDataShadow: false, //是否显示数据阴影 默认auto
      showDetail: false, // 拖拽时是否展示滚动条两侧的文字
      top: "1%",
      right: "5",
    },
  ];
};
/**
 * 横向 滚动条
 */
export const xDataZoomTemplate = () => {
  return [
    {
      type: "inside",
      startValue: 0,
      endValue: 2,
      minValueSpan: 9,
      maxValueSpan: 9,
      yAxisIndex: [0],
      zoomOnMouseWheel: false, // 关闭滚轮缩放
      moveOnMouseWheel: true, // 开启滚轮平移
      moveOnMouseMove: true, // 鼠标移动能触发数据窗口平移
    },
    {
      type: "slider",
      realtime: true,
      startValue: 0,
      endValue: 2,
      width: "3.5",
      height: "80%",
      xAxisIndex: [0], // 控制y轴滚动
      fillerColor: "rgba(255,255,255,0)", // 滚动条颜色
      borderColor: "rgba(255,255,255,0)",
      backgroundColor: "rgba(255,255,255,0)", //两边未选中的滑动条区域的颜色
      handleSize: 0, // 两边手柄尺寸
      showDataShadow: false, //是否显示数据阴影 默认auto
      showDetail: false, // 拖拽时是否展示滚动条两侧的文字
      top: "1%",
      right: "5",
    },
  ];
};

export class OptionTemplate {
  /**
   * echarts 渲染对象
   */
  option: any;
  constructor(option: any) {
    this.option = option;
  }
  static of(option: any) {
    return new OptionTemplate(option);
  }
  /**
   *
   * @param field 需要修改的字段 比如 legend.type
   * @param value 需要修改的值
   */
  update(field: string, value: any) {
    const fieldList = field.split(".");
    fieldList.reduce((option: any, field: any, index) => {
      if (index === fieldList.length - 1) {
        option[field] = value;
      } else {
        if (option[field]) {
          return option[field];
        } else {
          option[field] = {};
          return option;
        }
      }
    }, this.option);
  }
  /**
   * 修改标题
   * @param title 标题名称
   */
  updateTitleText(title: string) {
    this.update("title.text", title);
  }
  /**
   * 获取数据
   * @param field 字段
   * @returns 数据
   */
  getData(field: string) {
    const fieldList = field.split(".");
    return fieldList.reduce((option: any, field: any) => {
      if (option) {
        if (option[field]) {
          return option[field];
        }
      }
    }, this.option);
  }

  /**
   * 交换俩个变量
   * @param field 字段1
   * @param field1 字段2
   */
  swap(field: string, field1: string) {
    const tmp = this.getData(field);
    const tmp1 = this.getData(field1);
    this.update(field, tmp1);
    this.update(field1, tmp);
  }
}
export interface DataItem {
  /**
   * 名称
   */
  name: string;
  /**
   * 值
   */
  value: number;
  /**
   * 其他数据
   */
  [propName: string]: any;
}
