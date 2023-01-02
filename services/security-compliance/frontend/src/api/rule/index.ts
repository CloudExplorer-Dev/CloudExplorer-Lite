import { get, post, del, put } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  ComplianceRule,
  InstanceSearchField,
  SaveComplianceRuleRequest,
  UpdateComplianceRuleRequest,
} from "@/api/rule/type";
import type { KeyValue } from "@commons/api/base/type";

/**
 * 分页查询
 * @param currentPage 当前页
 * @param pageSize    每页显示多少条
 * @param query       查询参数对象
 * @param loading    加载器
 * @returns  合规规则数据
 */
const page: (
  currentPage: number,
  pageSize: number,
  query: unknown,
  loading?: Ref<boolean>
) => Promise<Result<Page<ComplianceRule>>> = (
  currentPage,
  pageSize,
  query,
  loading
) => {
  return get(`/api/compliance_rule/${currentPage}/${pageSize}`, query, loading);
};

/**
 * 获取可查询字段
 * @param platform       供应商
 * @param resourceType   资源类型
 * @param loading        加载器
 * @returns 当前供应商和资源 可过滤的字段
 */
const listInstanceSearchField: (
  platform: string,
  resourceType: string,
  loading?: Ref<boolean>
) => Promise<Result<Array<InstanceSearchField>>> = (
  platform,
  resourceType,
  loading
) => {
  return get(
    "/api/compliance_rule/instance_search_field",
    {
      platform: platform,
      resourceType: resourceType,
    },
    loading
  );
};

/**
 * 获取支持的实例类型
 * @returns 实例类型数组
 */
const listResourceType: (
  loading?: Ref<boolean>
) => Promise<Result<Array<KeyValue<string, string>>>> = (loading) => {
  return get("/api/compliance_rule/resource_type", undefined, loading);
};

/**
 * 插入规则
 * @param complianceRule 合规规则
 * @param loading  记载器
 * @returns 插入成功后的对象
 */
const saveComplianceRule: (
  complianceRule: SaveComplianceRuleRequest,
  loading?: Ref<boolean>
) => Promise<Result<ComplianceRule>> = (complianceRule, loading) => {
  return post("/api/compliance_rule/", undefined, complianceRule, loading);
};

/**
 * 修改规则
 * @param complianceRule 需要修改的规则
 * @param loading
 * @returns
 */
const updateComplianceRule: (
  complianceRule: UpdateComplianceRuleRequest,
  loading?: Ref<boolean>
) => Promise<Result<ComplianceRule>> = (complianceRule, loading) => {
  return put("/api/compliance_rule/", undefined, complianceRule, loading);
};

/**
 * 删除合规规则
 * @param complianceRuleId 合规规则id
 * @param loading          加载器
 * @returns 是否删除
 */
const deleteComplianceRule: (
  complianceRuleId: string,
  loading?: Ref<boolean>
) => Promise<Result<boolean>> = (complianceRuleId, loading) => {
  return del(`/api/compliance_rule/${complianceRuleId}`, undefined, loading);
};

/**
 * 是否开启
 * @param complianceRuleId 合规规则id
 * @param enable           启用状态
 * @param loading          加载器
 * @returns
 */
const switchEnable: (
  complianceRuleId: string,
  enable: boolean,
  loading?: Ref<boolean>
) => Promise<Result<ComplianceRule>> = (complianceRuleId, enable, loading) => {
  return put(
    "/api/compliance_rule/",
    undefined,
    { id: complianceRuleId, enable },
    loading
  );
};
/**
 *  获取合规规则根据合规id
 * @param complianceRuleId 合规规则id
 * @param loading   加载器
 * @returns  合规规则
 */
const getComplianceRuleById: (
  complianceRuleId: string,
  loading?: Ref<boolean>
) => Promise<Result<ComplianceRule>> = (complianceRuleId, loading) => {
  return get(`/api/compliance_rule/${complianceRuleId}`, {}, loading);
};
export default {
  page,
  listInstanceSearchField,
  listResourceType,
  saveComplianceRule,
  updateComplianceRule,
  deleteComplianceRule,
  switchEnable,
  getComplianceRuleById,
};
