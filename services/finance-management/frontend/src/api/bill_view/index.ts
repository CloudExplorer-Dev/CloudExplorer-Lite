import { get, post, del, put } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { SimpleMap } from "@commons/api/base/type";
import type { BillView } from "@/api/bill_view/type";
import BaseBillViewApi from "@commons/api/bil_view";

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
export default { ...BaseBillViewApi, getHistoryTrend, getBillView };
