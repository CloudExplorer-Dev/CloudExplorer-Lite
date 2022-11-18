import { get, post, del, put } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { BillRule } from "@/api/bill_rule/type";
import type { Page } from "@commons/request/Result";
import type { BillRuleRequest } from "@/api/bill_rule/type";
import type { SimpleMap } from "@commons/api/base/type";
/**
 *获取所有规则
 * @param loading 加载器
 * @returns 账单规则数据
 */
const listBillRules: (
  loading?: Ref<boolean>
) => Promise<Result<Array<BillRule>>> = (loading) => {
  return get("/api/bill_rule/list", {}, loading);
};

/**
 * 分页查询规则
 * @param currentPage 当前页
 * @param pageSize   每页大小
 * @param query      查询参数
 * @param loading    加载器
 * @returns 账单规则分页数据
 */
const pageBillRules: (
  currentPage: number,
  pageSize: number,
  query: BillRuleRequest,
  loading?: Ref<boolean>
) => Promise<Result<Page<BillRule>>> = (
  currentPage,
  pageSize,
  query,
  loading
) => {
  return get(`/api/bill_rule/page/${currentPage}/${pageSize}`, query, loading);
};

/**
 * 获取可分组字段
 * @param loading 加载器
 * @returns 可分组字段
 */
const getGroupKeys: (
  loading?: Ref<boolean>
) => Promise<Result<Array<SimpleMap<string>>>> = (loading) => {
  return get("/api/bill_rule/group_keys", {}, loading);
};

/**
 * 获取可分组字段子字段
 * @param parentKey 父字段
 * @param loading  加载器
 * @returns 可分组子字段
 */
const getGroupChildKeys: (
  parentKey: string,
  loading?: Ref<boolean>
) => Promise<Result<Array<SimpleMap<string>>>> = (parentKey, loading) => {
  return get(
    "/api/bill_rule/group_child_keys",
    { parentKey: parentKey },
    loading
  );
};
export default {
  listBillRules,
  pageBillRules,
  getGroupKeys,
  getGroupChildKeys,
};
