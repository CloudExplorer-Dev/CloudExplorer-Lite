import type { KeyValue } from "./../api/base/type";

/**
 * 定时任务对象 any
 */
export interface CronObj {
  // label
  key: string;
  // 值
  value: number;
  // 下拉选
  selects: () => Array<KeyValue<string, number>>;
  children: Array<number>;
}

/**
 * 定时任务类型对象
 */
export interface CronType {
  [propName: number]: {
    key: string;
    value: number;
    selects:
      | Array<KeyValue<string, number>>
      | (() => Array<KeyValue<string, number>>);
    children: Array<number>;
  };
}
/**
 * 获取指定区间 数组
 * @param start 开始
 * @param end   结束
 * @returns 指定区间数组
 */
export const region = (start: number, end: number) => {
  const arr: Array<number> = [];
  for (let index = start; index <= end; index++) {
    arr.push(index);
  }

  return arr;
};
/**
 * 表示匹配该域的任意值。假如在Minutes域使用*, 即表示每分钟都会触发事件。
 */
const all = "*";
/**
 *  不指定,周每日,和月每日上,不能都用通配符不然表达式不正确
 */
const not = "?";

/**
 * 表示列出枚举值。例如：在Minutes域使用5,20，则意味着在5和20分每分钟触发一次。
 */
const inS = ",";

export class SimpleCron {
  /**
   * 秒
   */
  second: Array<number> = [];
  /**
   * 分钟
   */
  minute: Array<number> = [];
  /**
   * 小时
   */
  hours: Array<number> = [];
  /**
   * 天 基于月
   */
  dayOfMonth: Array<number> = [];
  /**
   * 月
   */
  month: Array<number> = [];
  /**
   * 天基于周
   */
  dayOfWeek: Array<number> = [];
  /**
   *年
   */
  year: Array<number> = [];

  /**
   *所有的cron
   */
  crons: Array<Array<number>> = [];
  /**
   *
   */
  cronType = 0;

  constructor(cronExpression: string) {
    const split = cronExpression.split(" ");
    const crons = region(0, split.length - 1).map((index) => {
      return split[index]
        .split(",")
        .filter((s) => s !== all && s !== not)
        .map((s) => parseInt(s));
    });
    this.second = crons[0];
    this.minute = crons[1];
    this.hours = crons[2];
    this.dayOfMonth = crons[3];
    this.month = crons[4];
    this.dayOfWeek = crons[5];
    if (crons.length == 7) {
      this.year = crons[6];
    } else {
      this.year = [];
    }
    crons.forEach((item, index) => {
      if (item.length > 0) {
        this.cronType = index + 1;
        if (index === 5) {
          this.cronType = 5;
        }
      }
    });
    this.crons = crons;
  }
  /**
   * 获取 in的
   * @param second 秒
   * @param minute 分钟
   * @param hours 小时
   * @param dayOfMonth 每月日
   * @param month     月
   * @param dayOfWeek  每周 日
   * @param year  年
   * @returns cron字符串
   */
  static parseInCron(
    second: Array<number>,
    minute: Array<number>,
    hours: Array<number>,
    dayOfMonth: Array<number>,
    month: Array<number>,
    dayOfWeek: Array<number>,
    year: Array<number>
  ) {
    if (dayOfMonth.length > 0 && dayOfWeek.length > 0) {
      throw new Error("定时任务每日只能基于周或者基于月,不能俩个同时满足");
    }

    const seconds: Array<Array<number>> = [
      second,
      minute,
      hours,
      dayOfMonth,
      month,
      dayOfWeek,
      year,
    ];

    let cronExpression = "";
    if (dayOfWeek.length > 0) {
      if (year.length > 0) {
        cronExpression = [
          SimpleCron.getInCron(
            second,
            SimpleCron.getDefaultValue(0, seconds, true)
          ),
          SimpleCron.getInCron(
            minute,
            SimpleCron.getDefaultValue(1, seconds, true)
          ),
          SimpleCron.getInCron(
            hours,
            SimpleCron.getDefaultValue(2, seconds, true)
          ),
          SimpleCron.getInCron(
            dayOfMonth,
            SimpleCron.getDefaultValue(3, seconds, true)
          ),
          SimpleCron.getInCron(
            month,
            SimpleCron.getDefaultValue(4, seconds, true)
          ),
          SimpleCron.getInCron(
            dayOfWeek,
            SimpleCron.getDefaultValue(5, seconds, true)
          ),
          SimpleCron.getInCron(year, all),
        ].join(" ");
      } else {
        cronExpression = [
          SimpleCron.getInCron(
            second,
            SimpleCron.getDefaultValue(0, seconds, true)
          ),
          SimpleCron.getInCron(
            minute,
            SimpleCron.getDefaultValue(1, seconds, true)
          ),
          SimpleCron.getInCron(
            hours,
            SimpleCron.getDefaultValue(2, seconds, true)
          ),
          SimpleCron.getInCron(
            dayOfMonth,
            SimpleCron.getDefaultValue(3, seconds, true)
          ),
          SimpleCron.getInCron(
            month,
            SimpleCron.getDefaultValue(4, seconds, true)
          ),
          SimpleCron.getInCron(
            dayOfWeek,
            SimpleCron.getDefaultValue(5, seconds, true)
          ),
        ].join(" ");
      }
    } else {
      cronExpression = [
        SimpleCron.getInCron(
          second,
          SimpleCron.getDefaultValue(0, seconds, false)
        ),
        SimpleCron.getInCron(
          minute,
          SimpleCron.getDefaultValue(1, seconds, false)
        ),
        SimpleCron.getInCron(
          hours,
          SimpleCron.getDefaultValue(2, seconds, false)
        ),
        SimpleCron.getInCron(
          dayOfMonth,
          SimpleCron.getDefaultValue(3, seconds, false)
        ),
        SimpleCron.getInCron(
          month,
          SimpleCron.getDefaultValue(4, seconds, false)
        ),
        SimpleCron.getInCron(
          dayOfWeek,
          SimpleCron.getDefaultValue(5, seconds, false)
        ),
        SimpleCron.getInCron(year, SimpleCron.getInCron(year, all)),
      ].join(" ");
    }
    return cronExpression;
  }

  /**
   * 拼接字符串,如果值不存在则返回默认值
   * @param inParams     需要拼接的数组
   * @param defaultValue 默认值
   * @returns 拼接后字符串
   */
  static getInCron(inParams: Array<number>, defaultValue: string) {
    if (inParams == null || inParams == undefined || inParams.length == 0) {
      return defaultValue;
    }
    return inParams.join(inS);
  }

  /**
   * 获取默认值
   * @param index  数组所在下标
   * @param seconds 数组
   * @param ofWeek  是否是 day Of week 周
   * @returns 默认值
   */
  static getDefaultValue(
    index: number,
    seconds: Array<Array<number>>,
    ofWeek: boolean
  ) {
    if (ofWeek) {
      if (index === 3) {
        return not;
      }
      if (index === 4) {
        return all;
      }
    } else {
      if (index === 5) {
        return not;
      }
    }

    const anyMatch = region(index, seconds.length - 1).some(
      (i) => seconds[i].length > 0
    );
    if (anyMatch) {
      return "0";
    } else {
      return all;
    }
  }
}
// 分钟和秒钟组合
export const minuteAndSecond = 100;
// 分钟和秒钟组合
export const hoursAndMinuteAndSecond = 101;
// 小时和分钟组合
export const hoursAndMinute = 102;
export const cronTypes: CronType = {
  2: {
    key: "每小时",
    value: 2,
    selects: () => {
      return region(0, 23).map((v) => ({
        key: v + "点",
        value: v,
      }));
    },
    children: [minuteAndSecond],
  },
  3: {
    key: "每天",
    value: 3,
    selects: () => {
      return region(1, 31).map((v) => ({
        key: v + "日",
        value: v,
      }));
    },
    children: [hoursAndMinute],
  },
  5: {
    key: "每周",
    value: 5,
    selects: () => {
      return [
        { key: "周日", value: 1 },
        { key: "周一", value: 2 },
        { key: "周二", value: 3 },
        { key: "周三", value: 4 },
        { key: "周四", value: 5 },
        { key: "周五", value: 6 },
        { key: "周六", value: 7 },
      ];
    },
    children: [5, hoursAndMinuteAndSecond],
  },
  4: {
    key: "每月",
    value: 4,
    selects: () => {
      return region(1, 12).map((v) => ({
        key: v + "月",
        value: v,
      }));
    },
    children: [3, hoursAndMinuteAndSecond],
  },
};
