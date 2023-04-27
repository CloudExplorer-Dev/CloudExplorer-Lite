import { get } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { BillDetail } from "@/api/bill_detailed/type";

const pageBillDetailed: (
  currentPage: number,
  pageSize: number,
  query: unknown,
  loading?: Ref<boolean>
) => Promise<Result<Page<BillDetail>>> = (
  currentPage,
  pageSize,
  query,
  loading
) => {
  return get(`/api/bill_detailed/${currentPage}/${pageSize}`, query, loading);
};
export default { pageBillDetailed };
