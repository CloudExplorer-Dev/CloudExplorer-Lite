import { get, post, put, del } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";

import type {
  BillingPolicy,
  BillingPolicyDetailsResponse,
  CreateBillingPolicyRequest,
  LinkCloudAccountRequest,
  CloudAccountResponse,
  BillingPolicyDetails,
} from "@/api/billing_policy/type";

/**
 * 获取计费策略
 * @returns 计费策略
 */
const list: (
  loading?: Ref<boolean>
) => Promise<Result<Array<BillingPolicy>>> = (loading) => {
  return get("/api/billing_policy", {}, loading);
};

/**
 * 查看策略详情
 * @param billingPolicyId
 * @param loading
 * @returns
 */
const details: (
  billingPolicyId: string,
  loading?: Ref<boolean>
) => Promise<Result<BillingPolicyDetailsResponse>> = (
  billingPolicyId,
  loading
) => {
  return get(`/api/billing_policy/details/${billingPolicyId}`, {}, loading);
};
const create: (
  request: CreateBillingPolicyRequest,
  loading?: Ref<boolean>
) => Promise<Result<BillingPolicy>> = (request, loading) => {
  return post("/api/billing_policy", {}, request, loading);
};

/**
 * 查询到可关联的云账号
 * @param billingPolicyId 策略id
 * @param loading         加载器
 * @returns 云账号
 */
const listCloudAccount: (
  billingPolicyId?: string,
  loading?: Ref<boolean>
) => Promise<Result<Array<CloudAccountResponse>>> = (
  billingPolicyId,
  loading
) => {
  return get(
    "/api/billing_policy/list_cloud_account",
    {
      billingPolicyId: billingPolicyId,
    },
    loading
  );
};

/**
 * 云账号关联
 * @param reuqest 请求对象
 * @param loading 加载器
 * @returns
 */
const link: (
  reuqest: LinkCloudAccountRequest,
  loading?: Ref<boolean>
) => Promise<Result<any>> = (reuqest, loading) => {
  return post("/api/billing_policy/link", {}, reuqest, loading);
};

/**
 * 重命名
 * @param billing_policy_id 策略id
 * @param name              策略名称
 * @param loading 加载器
 * @returns
 */
const reName: (
  billing_policy_id: string,
  name: string,
  loading?: Ref<boolean>
) => Promise<Result<any>> = (billing_policy_id, name, loading) => {
  return put(
    `/api/billing_policy/${billing_policy_id}`,
    {},
    { name: name },
    loading
  );
};

const deleteById: (
  billing_policy_id: string,
  loading?: Ref<boolean>
) => Promise<Result<any>> = (billing_policy_id, loading) => {
  return del(`/api/billing_policy/${billing_policy_id}`, {}, loading);
};

const updatePolicy: (
  billing_policy_id: string,
  request: Array<BillingPolicyDetails>,
  loading?: Ref<boolean>
) => Promise<Result<any>> = (billing_policy_id, request, loading) => {
  return put(
    `/api/billing_policy/${billing_policy_id}`,
    {},
    { billingPolicyDetailsList: request },
    loading
  );
};
export default {
  list,
  details,
  create,
  listCloudAccount,
  link,
  reName,
  deleteById,
  updatePolicy,
};
