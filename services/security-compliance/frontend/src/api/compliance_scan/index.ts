import { get, post, del, put } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  ScanComplianceRuleGroupResponse,
  ComplianceScanRequest,
  ComplianceScanResponse,
  ComplianceResourceResponse,
} from "@/api/compliance_scan/type";
import type { SingleOrRange } from "element-plus";

/**
 * 获取扫描合规规则数据
 * @param compliance_rule_group_id 规则组id
 * @param loading  加载器
 * @returns 合规规则组数据
 */
const getScanComplianceRuleGroup: (
  compliance_rule_group_id: string,
  loading: Ref<boolean>
) => Promise<Result<ScanComplianceRuleGroupResponse>> = (
  compliance_rule_group_id,
  loading
) => {
  return get(
    `/api/compliance_scan/rule_group/${compliance_rule_group_id}`,
    {},
    loading
  );
};

/**
 * 获取合规扫描结果
 * @param compliance_scan_request 查询请求参数
 * @param loading     加载器
 * @returns 合规扫描结果
 */
const listScanComplianceRule: (
  compliance_scan_request: ComplianceScanRequest,
  loading?: Ref<boolean>
) => Promise<Result<Array<ComplianceScanResponse>>> = (
  compliance_scan_request,
  loading
) => {
  return get("/api/compliance_scan", compliance_scan_request, loading);
};

/**
 * 分页查询合规扫描结果
 * @param currentPage 当前页
 * @param pageSize     每页大小
 * @param complianceScanRequest 请求参数
 * @param loading  加载器
 * @returns 规则合规结果
 */
const pageScanComplianceRule: (
  currentPage: number,
  pageSize: number,
  complianceScanRequest: ComplianceScanRequest,
  loading?: Ref<boolean>
) => Promise<Result<Page<ComplianceScanResponse>>> = (
  currentPage,
  pageSize,
  complianceScanRequest,
  loading
) => {
  return get(
    `/api/compliance_scan/${currentPage}/${pageSize}`,
    complianceScanRequest,
    loading
  );
};
/**
 *分页查询资源
 * @param currentPage 当前页
 * @param pageSize    每页显示多少条
 * @param compliance_rule_group_id 合规规则id
 * @param complianceScanRequest    过滤参数
 * @param loading 加载器
 * @returns 分页资源数据
 */
const pageResource: (
  currentPage: number,
  pageSize: number,
  complianceRuleId: string,
  complianceScanRequest: ComplianceScanRequest,
  loading?: Ref<boolean>
) => Promise<Result<Page<ComplianceResourceResponse>>> = (
  currentPage,
  pageSize,
  compliance_rule_group_id,
  complianceScanRequest,
  loading
) => {
  return get(
    `/api/compliance_scan/resource/${compliance_rule_group_id}/${currentPage}/${pageSize}`,
    complianceScanRequest,
    loading
  );
};
export default {
  getScanComplianceRuleGroup,
  listScanComplianceRule,
  pageScanComplianceRule,
  pageResource,
};
