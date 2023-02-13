import { get } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  ComplianceInsuranceStatute,
  ComplianceInsuranceStatuteRequest,
} from "@/api/compliance_insurance_statute/type";

/**
 * 分页查询
 * @param currentPage 当前页面
 * @param pageSize    每页条数
 * @param query       请求过滤对象
 * @param loading     加载器
 * @returns 等保条例数据
 */
const page: (
  currentPage: number,
  pageSize: number,
  query: ComplianceInsuranceStatuteRequest,
  loading?: Ref<boolean>
) => Promise<Result<Page<ComplianceInsuranceStatute>>> = (
  currentPage,
  pageSize,
  query,
  loading
) => {
  return get(
    `/api/compliance_insurance_statute/${currentPage}/${pageSize}`,
    query,
    loading
  );
};

/**
 * 列举所有等保条例
 * @param query  清单过滤对象
 * @param loading 加载器
 * @returns 等保条例
 */
const list: (
  query?: ComplianceInsuranceStatuteRequest,
  loading?: Ref<boolean>
) => Promise<Result<Array<ComplianceInsuranceStatute>>> = (query, loading) => {
  return get("/api/compliance_insurance_statute", query, loading);
};

export default {
  page,
  list,
};
