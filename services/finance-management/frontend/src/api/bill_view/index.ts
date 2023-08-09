import { get, put } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { KeyValue, SimpleMap } from "@commons/api/base/type";
import type { BillView, CurrencyRequest } from "@/api/bill_view/type";
import type { BillViewRequest, Currency } from "@commons/api/bil_view/type";
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
  request: BillViewRequest,
  loading?: Ref<boolean>
) => Promise<Result<SimpleMap<Array<BillView>>>> = (
  ruleId,
  month,
  request,
  loading
) => {
  return get(`/api/bill_view/${ruleId}/${month}`, request, loading);
};

/**
 * 获取成本类型列表
 * @param loading 加载器
 * @returns 成本类型列表
 */
const listCost: (
  loading?: Ref<boolean>
) => Promise<Result<Array<KeyValue<string, string>>>> = (loading) => {
  return get(`/api/bill_view/list_cost`, {}, loading);
};

/**
 * 获取币种汇率列表
 * @param loading 加载器
 * @returns 币种汇率列表
 */
const listCurrency: (
  loading?: Ref<boolean>
) => Promise<Result<Array<Currency>>> = (loading) => {
  return get(`/api/bill_view/currency/list`, {}, loading);
};

/**
 * 批量修改币种汇率
 * @param request 请求对象
 * @param loading 加载器
 * @returns 是否修改成功
 */
const batchUpdateCurrency: (
  request: Array<CurrencyRequest>,
  loading?: Ref<boolean>
) => Promise<Result<boolean>> = (request, loading) => {
  return put(`/api/bill_view/currency`, {}, request, loading);
};

export default {
  ...BaseBillViewApi,
  getBillView,
  listCost,
  listCurrency,
  batchUpdateCurrency,
};
