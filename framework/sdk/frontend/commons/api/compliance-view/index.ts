import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import { get } from "@commons/request";
import type {
  ComplianceCountRequest,
  ComplianceViewCountResponse,
} from "@commons/api/compliance-view/type";

export function getComplianceViewResourceCount(
  req: ComplianceCountRequest,
  loading?: Ref<boolean>
): Promise<Result<ComplianceViewCountResponse>> {
  return get(
    (import.meta.env.VITE_APP_NAME === "security-compliance"
      ? ""
      : "/security-compliance/") + "api/compliance_view/resource/count",
    req,
    loading
  );
}
export function getComplianceViewRuleCount(
  req: ComplianceCountRequest,
  loading?: Ref<boolean>
): Promise<Result<ComplianceViewCountResponse>> {
  return get(
    (import.meta.env.VITE_APP_NAME === "security-compliance"
      ? ""
      : "/security-compliance/") + "api/compliance_view/rule/count",
    req,
    loading
  );
}

const api = {
  getComplianceViewResourceCount,
  getComplianceViewRuleCount,
};

export default api;
