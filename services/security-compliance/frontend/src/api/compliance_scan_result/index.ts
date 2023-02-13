import { get } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  ComplianceScanResultResponse,
  ComplianceScanResultRequest,
  ComplianceScanResultRuleGroupResponse,
} from "@/api/compliance_scan_result/type";
/**
 * 获取合规扫描结果列表
 * @param compliance_scan_request 请求对象
 * @param loading 加载器
 * @returns 合规扫描结果列表
 */
const list: (
  compliance_scan_request: ComplianceScanResultRequest,
  loading?: Ref<boolean>
) => Promise<Result<Array<ComplianceScanResultResponse>>> = (
  compliance_scan_request,
  loading
) => {
  return get("/api/compliance_scan_result", compliance_scan_request, loading);
};

/**
 * 分页查询合规扫描结果
 * @param currentPage 当前页
 * @param pageSize     每页大小
 * @param complianceScanRequest 请求参数
 * @param loading  加载器
 * @returns 规则合规结果
 */
const page: (
  currentPage: number,
  pageSize: number,
  complianceScanRequest: ComplianceScanResultRequest,
  loading?: Ref<boolean>
) => Promise<Result<Page<ComplianceScanResultResponse>>> = (
  currentPage,
  pageSize,
  complianceScanRequest,
  loading
) => {
  return get(
    `/api/compliance_scan_result/${currentPage}/${pageSize}`,
    complianceScanRequest,
    loading
  );
};

/**
 * 获取扫描合规规则数据
 * @param compliance_rule_group_id 规则组id
 * @param loading  加载器
 * @returns 合规规则组数据
 */
const getComplianceScanResultRuleGroupResponse: (
  compliance_rule_group_id: string,
  loading: Ref<boolean>
) => Promise<Result<ComplianceScanResultRuleGroupResponse>> = (
  compliance_rule_group_id,
  loading
) => {
  return get(
    `/api/compliance_scan_result/rule_group/${compliance_rule_group_id}`,
    {},
    loading
  );
};
export default { list, page, getComplianceScanResultRuleGroupResponse };
