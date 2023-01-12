import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import { get } from "@commons/request";

export const getHistoryTrend: (
  type: "MONTH" | "YEAR",
  historyNum: number,
  loading?: Ref<boolean>
) => Promise<Result<Array<any>>> = (type, historyNum, loading) => {
  return get(
    `/finance-management/api/bill_view/history_trend/${type}/${historyNum}`,
    {},
    loading
  );
};
