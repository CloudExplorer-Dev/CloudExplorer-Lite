import { get, post, del, put } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { BillRule } from "@/api/bill_rule/type";
/**
 *获取所有规则
 * @param loading
 * @returns
 */
const listBillRules: (
  loading?: Ref<boolean>
) => Promise<Result<Array<BillRule>>> = (loading) => {
  return get("/api/bill_rule/list", {}, loading);
};
export default { listBillRules };
