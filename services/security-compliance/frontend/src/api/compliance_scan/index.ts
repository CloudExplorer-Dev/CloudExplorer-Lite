import { get, post } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  ComplianceResourceResponse,
  SyncScanResourceRequest,
  SupportCloudAccountResourceResponse,
  SupportPlatformResourceResponse,
  ComplianceResourceRequest,
} from "@/api/compliance_scan/type";
import type { AccountJobRecord } from "@commons/api/cloud_account/type";

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
  complianceScanRequest: ComplianceResourceRequest,
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
 *
 * @returns 获取支持的云平台以及对应的资源
 */
const listSupportPlatformResource: (
  loading?: Ref<boolean>
) => Promise<Result<Array<SupportPlatformResourceResponse>>> = (loading) => {
  return get("/api/compliance_scan/support_platform", undefined, loading);
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
  pageResource,
  syncScan,
  listSupportCloudAccountResource,
  listJobRecord,
  listSupportPlatformResource,
};
