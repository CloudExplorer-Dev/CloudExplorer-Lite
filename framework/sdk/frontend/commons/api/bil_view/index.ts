import { get } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";

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
  loading?: Ref<boolean>
): Promise<Result<number>> {
  return get(
    (import.meta.env.VITE_APP_NAME === "finance-management"
      ? ""
      : "/finance-management") + `/api/bill_view/expenses/${type}/${value}`,
    {},
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

export default { getExpenses, getCurrentMonthBill };
