import { get } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { SimpleMap } from "@commons/api/base/type";
import type { BillView } from "@/api/bill_view/type";
import BaseBillViewApi from "@commons/api/bil_view";

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
export default { ...BaseBillViewApi, getBillView };
