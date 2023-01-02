import { get, post, del, put } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  ComplianceRuleGroup,
  ComplianceRuleGroupRequest,
  UpdateComplianeRuleGroupRequest,
} from "@/api/rule_group/type";
/**
 * 获取所有规则组
 * @returns 规则组
 */
const list: (
  loading?: Ref<boolean>
) => Promise<Result<Array<ComplianceRuleGroup>>> = () => {
  return get("/api/compliance_rule_group");
};

/**
 * 分页查询规则组
 * @param currentPage 当前页
 * @param pageSize    每页大小
 * @param query       请求过滤参数
 * @param loading     加载器
 * @returns           规则组分页数据
 */
const page: (
  currentPage: number,
  pageSize: number,
  query: unknown,
  loading?: Ref<boolean>
) => Promise<Result<Page<ComplianceRuleGroup>>> = (
  currentPage,
  pageSize,
  query,
  loading
) => {
  return get(
    `/api/compliance_rule_group/${currentPage}/${pageSize}`,
    query,
    loading
  );
};

/**
 * 插入规则组
 * @param complianceGroupRequest 规则组对象
 * @param loading 加载器
 * @returns 插入成功后的数据
 */
const save: (
  complianceGroupRequest: ComplianceRuleGroupRequest,
  loading?: Ref<boolean>
) => Promise<Result<ComplianceRuleGroup>> = (
  complianceGroupRequest,
  loading
) => {
  return post(
    "/api/compliance_rule_group/",
    {},
    complianceGroupRequest,
    loading
  );
};

/**
 * 修改规则组
 * @param complianceRuleGroup 规则组对象
 * @param loading             加载器
 * @returns
 */
const update: (
  complianceRuleGroup: UpdateComplianeRuleGroupRequest,
  loading?: Ref<boolean>
) => Promise<Result<ComplianceRuleGroup>> = (complianceRuleGroup, loading) => {
  return put("/api/compliance_rule_group/", {}, complianceRuleGroup, loading);
};
/**
 * 根据id删除 安规规则组
 * @param complianceRuleGroupId 规则组id
 * @param loading  加载器
 * @returns 是否删除成功
 */
const deleteById: (
  complianceRuleGroupId: string,
  loading?: Ref<boolean>
) => Promise<Result<boolean>> = (complianceRuleGroupId, loading) => {
  return del(
    `/api/compliance_rule_group/${complianceRuleGroupId}`,
    undefined,
    undefined,
    loading
  );
};
/**
 * 根据规则组id获取规则组信息
 * @param complianceRuleGroupId 规则组id
 * @param loading               加载器
 * @returns 规则组信息
 */
const getComplianceRuleGroupById: (
  complianceRuleGroupId: string,
  loading?: Ref<boolean>
) => Promise<Result<ComplianceRuleGroup>> = (
  complianceRuleGroupId,
  loading
) => {
  return get(
    `/api/compliance_rule_group/${complianceRuleGroupId}`,
    {},
    loading
  );
};
export default {
  list,
  page,
  save,
  deleteById,
  update,
  getComplianceRuleGroupById,
};
