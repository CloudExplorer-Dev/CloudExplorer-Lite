import { get } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type {
  ComplianceCountRequest,
  ComplianceGroupRequest,
  ComplianceViewCountResponse,
  ComplianceViewGroupResponse,
} from "@/api/view/type";
/**
 * 获取首页求和数据
 */
const count: (
  complianceCountRequest: ComplianceCountRequest,
  loading?: Ref<boolean>
) => Promise<Result<ComplianceViewCountResponse>> = (
  complianceCountRequest,
  loading
) => {
  return get(
    "/api/compliance_view/resource/count",
    complianceCountRequest,
    loading
  );
};

/**
 * 获取首页 饼图分组数据
 * @param complianceGroupRequest 请求对象
 * @param loading  加载器
 * @returns  首页分组数据
 */
const group: (
  complianceGroupRequest: ComplianceGroupRequest,
  loading?: Ref<boolean>
) => Promise<Result<Array<ComplianceViewGroupResponse>>> = (
  complianceGroupRequest,
  loading
) => {
  return get(
    "/api/compliance_view/resource/group",
    complianceGroupRequest,
    loading
  );
};

export default {
  count,
  group,
};
