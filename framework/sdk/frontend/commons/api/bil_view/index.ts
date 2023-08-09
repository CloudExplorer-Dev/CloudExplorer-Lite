import { request } from "./../../request/index";
import { get } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type {
  BillExpensesRequest,
  HistoryTrendRequest,
} from "@commons/api/bil_view/type";
/**
 * 指定月份花费或者是年花费
 * @param type  MONTH月份 YEAR年
 * @param value  yyyy-MM 月 yyyy年
 * @param loading 加载器
 * @returns 花销
 */
function getExpenses(
  type: "MONTH" | "YEAR",
  value: string,
  request: BillExpensesRequest,
  loading?: Ref<boolean>
): Promise<Result<{ current: number; up: number }>> {
  return get(
    (import.meta.env.VITE_APP_NAME === "finance-management"
      ? ""
      : "/finance-management") + `/api/bill_view/expenses/${type}/${value}`,
    request,
    loading
  );
}

function getCurrentMonthBill(loading?: Ref<boolean>) {
  return get(
    (import.meta.env.VITE_APP_NAME === "finance-management"
      ? ""
      : "/finance-management") + "/api/bill_view/cloud_account/current_month",
    {},
    loading
  );
}

/**
 * 获取历史趋势
 * @param type
 * @param historyNum
 * @param loading
 * @returns
 */
function getHistoryTrend(
  type: "MONTH" | "YEAR",
  historyNum: number,
  request: HistoryTrendRequest,
  loading?: Ref<boolean>
): Promise<Result<Array<any>>> {
  return get(
    (import.meta.env.VITE_APP_NAME === "finance-management"
      ? ""
      : "/finance-management/") +
      `api/bill_view/history_trend/${type}/${historyNum}`,
    request,
    loading
  );
}

export default { getExpenses, getCurrentMonthBill, getHistoryTrend };
