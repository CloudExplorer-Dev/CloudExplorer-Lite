import { get } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import BASE_API from "@commons/api/compliance-view/index";
import type {
  ComplianceCountRequest,
  ComplianceGroupRequest,
  ComplianceViewCountResponse,
  ComplianceViewGroupResponse,
} from "@/api/view/type";

/**
 * 获取首页求和数据
 */
function count(
  complianceCountRequest: ComplianceCountRequest,
  loading?: Ref<boolean>
): Promise<Result<ComplianceViewCountResponse>> {
  return BASE_API.getComplianceViewResourceCount(
    complianceCountRequest,
    loading
  );
}

/**
 * 获取首页 饼图分组数据
 * @param complianceGroupRequest 请求对象
 * @param loading  加载器
 * @returns  首页分组数据
 */
function group(
  complianceGroupRequest: ComplianceGroupRequest,
  loading?: Ref<boolean>
): Promise<Result<Array<ComplianceViewGroupResponse>>> {
  return get(
    "/api/compliance_view/resource/group",
    complianceGroupRequest,
    loading
  );
}

const API = {
  ...BASE_API,
  count,
  group,
};

export default API;
