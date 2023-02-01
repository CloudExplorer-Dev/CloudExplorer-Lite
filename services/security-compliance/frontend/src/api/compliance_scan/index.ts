import { get, post, del, put } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  ScanComplianceRuleGroupResponse,
  ComplianceScanRequest,
  ComplianceScanResponse,
  ComplianceResourceResponse,
  SyncScanResourceRequest,
  SupportCloudAccountResourceResponse,
} from "@/api/compliance_scan/type";
import type { AccountJobRecord } from "@commons/api/cloud_account/type";

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

/**
 * 同步扫描资源
 * @param syncScanResource 同步扫描请求对象
 * @param loading         加载器
 * @returns 是否发送同步请求
 */
const syncScan: (
  syncScanResource: SyncScanResourceRequest,
  loading?: Ref<boolean>
) => Promise<Result<boolean>> = (syncScanResource, loading) => {
  return post(
    "/api/compliance_scan/sync_scan",
    undefined,
    syncScanResource,
    loading
  );
};
/**
 *
 * @returns 获取支持的云账号以及云账号支持的资源
 */
const listSupportCloudAccountResource: (
  loading?: Ref<boolean>
) => Promise<Result<Array<SupportCloudAccountResourceResponse>>> = (
  loading
) => {
  return get("/api/compliance_scan/support_cloud_account", undefined, loading);
};

/**
 * 获取任务记录
 * @param loading 加载器
 * @returns 任务记录
 */
const listJobRecord: (
  loading?: Ref<boolean>
) => Promise<Result<Array<AccountJobRecord>>> = (loading) => {
  return get("/api/compliance_scan/job_record", undefined, loading);
};
export default {
  getScanComplianceRuleGroup,
  listScanComplianceRule,
  pageScanComplianceRule,
  pageResource,
  syncScan,
  listSupportCloudAccountResource,
  listJobRecord,
};
