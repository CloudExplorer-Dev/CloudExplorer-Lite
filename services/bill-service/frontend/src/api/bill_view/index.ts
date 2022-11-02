import { get, post, del, put } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { SimpleMap } from "@commons/api/base/type";
import type { BillView } from "@/api/bill_view/type";
/**
 * 指定月份花费或者是年花费
 * @param type  MONTH月份 YEAR年
 * @param value  yyyy-MM 月 yyyy年
 * @param loading 加载器
 * @returns 花销
 */
const getExpenses: (
  type: "MONTH" | "YEAR",
  value: string,
  loading?: Ref<boolean>
) => Promise<Result<number>> = (type, value, loading) =>
  get(`/api/bill_view/expenses/${type}/${value}`, {}, loading);

/**
 * 获取历史趋势
 * @param type
 * @param historyNum
 * @param loading
 * @returns
 */
const getHistoryTrend: (
  type: "MONTH" | "YEAR",
  historyNum: number,
  loading?: Ref<boolean>
) => Promise<Result<Array<any>>> = (type, historyNum, loading) => {
  return get(`/api/bill_view/history_trend/${type}/${historyNum}`, {}, loading);
};

/**
 * 获取账单详情
 * @param ruleId 规则id
 * @param month  月份
 * @param loading 加载器
 * @returns 账单显示
 */
const getBillView: (
  ruleId: string,
  month: string,
  loading?: Ref<boolean>
) => Promise<Result<SimpleMap<Array<BillView>>>> = (ruleId, month, loading) => {
  return get(`/api/bill_view/${ruleId}/${month}`, {}, loading);
};
export default { getExpenses, getHistoryTrend, getBillView };
