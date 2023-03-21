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
const getExpenses: (
  type: "MONTH" | "YEAR",
  value: string,
  loading?: Ref<boolean>
) => Promise<Result<number>> = (type, value, loading) =>
  get(
    (import.meta.env.VITE_APP_NAME === "finance-management"
      ? ""
      : "/finance-management") + `/api/bill_view/expenses/${type}/${value}`,
    {},
    loading
  );

export default { getExpenses };
