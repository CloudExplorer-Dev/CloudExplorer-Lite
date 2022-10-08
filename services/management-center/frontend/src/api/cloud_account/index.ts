import { get, post, del, put } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  ListOrganizationRequest,
  CloudAccount,
  Platform,
  CreateAccount,
  Region,
  CloudAccountJobDetailsResponse,
  UpdateJobsRequest,
  UpdateAccount,
  SyncRequest,
  UpdateAccountName,
  ResourceCount,
} from "./type";
/**
 * 分页查询云账号
 * @param request 分页查询所需参数
 * @returns       分页查询云账号列表
 */
const page: (
  request: ListOrganizationRequest
) => Promise<Result<Page<CloudAccount>>> = (request) => {
  return get("/api/cloud_account/page", request);
};
/**
 * 获取所有的云账号供应商
 * @param loading   加载器
 * @returns         云账号供应商
 */
const getPlatformAll: (
  loading?: Ref<boolean>
) => Promise<Result<Array<Platform>>> = (loading) => {
  return get("/api/cloud_account/platform", null, loading);
};

/**
 * 校验云账号
 * @param cloud_account_id 云账号id
 * @param loading          加载器
 * @returns                云账号对象
 */
const verificationCloudAccount: (
  cloud_account_id: string,
  loading?: Ref<boolean>
) => Promise<Result<Page<CloudAccount>>> = (cloud_account_id, loading) => {
  return get(
    "/api/cloud_account/verification/" + cloud_account_id,
    null,
    loading
  );
};
/**
 * 保存一个云账号
 * @param createAccount 云账号对象
 * @param loading       加载器
 * @returns             保存成功后的云账号对象
 */
const save: (
  createAccount: CreateAccount,
  loading?: Ref<boolean>
) => Promise<Result<CloudAccount>> = (createAccount, loading) => {
  return post("/api/cloud_account", null, createAccount, loading);
};
/**
 * 根据云账号id查询到云账号对象
 * @param cloudAccountId 云账号id
 * @param loading        加载器
 * @returns 云账号对象
 */
const getCloudAccount: (
  cloudAccountId: string,
  loading?: Ref<boolean>
) => Promise<Result<CloudAccount>> = (cloudAccountId, loading) => {
  return get("/api/cloud_account/" + cloudAccountId, null, loading);
};

/**
 * 更新云账号
 * @param data 云账号数据
 * @param loading 加载器
 * @returns  云账号对象
 */
const updateCloudAccount: (
  data: UpdateAccount,
  loading?: Ref<boolean>
) => Promise<Result<CloudAccount>> = (data, loading) => {
  return put("/api/cloud_account/", null, data, loading);
};
/**
 * 删除云账号 根据云账号id
 * @param cloud_account_id 云账号id
 * @param loading          加载器
 * @returns  是否删除成功
 */
const deleteCloudAccount: (
  cloud_account_id: string,
  loading?: Ref<boolean>
) => Promise<Result<boolean>> = (cloud_account_id, loading) => {
  return del("/api/cloud_account/" + cloud_account_id, null, null, loading);
};

/**
 * 批量删除云账号
 * @param cloud_account_ids 需要删除的云账号id
 * @param loading           加载器
 * @returns                 是否删除成功
 */
const batchDeleteCloudAccount: (
  cloud_account_ids: Array<string>,
  loading?: Ref<boolean>
) => Promise<Result<boolean>> = (cloud_account_ids, loading) => {
  return del("/api/cloud_account/", null, cloud_account_ids, loading);
};
/**
 * 获取区域
 * @param accountId 云账号id
 * @param loading   login
 * @returns
 */
const getRegions: (
  accountId: string,
  loading?: Ref<boolean>
) => Promise<Result<Array<Region>>> = (accountId, loading) => {
  return get("/api/cloud_account/region/" + accountId, null, loading);
};

/**
 * 获取当前于账号的定时任务根据云账号id
 * @param accountId    云账号id
 * @param loading      加载器
 * @returns            当前云账号的定时任务
 */
const getJobs: (
  accountId: string,
  loading?: Ref<boolean>
) => Promise<Result<CloudAccountJobDetailsResponse>> = (accountId, loading) => {
  return get("/api/cloud_account/jobs/" + accountId, null, loading);
};

/**
 * 更新云账号定时任务
 * @param data      更新定时任务所需要的数据
 * @param loading   加载器
 * @returns         更新后的定时任务
 */
const updateJobs: (
  data: UpdateJobsRequest,
  loading?: Ref<boolean>
) => Promise<Result<CloudAccountJobDetailsResponse>> = (data, loading) => {
  return put("/api/cloud_account/jobs", null, data, loading);
};

/**
 * 获取定时任务
 * @param loading 加载器
 * @returns 获取同步资源
 */
const getResourceSync = (loading?: Ref<boolean>) => {
  return get("api/cloud_account/jobs/resource");
};

const syncJob=(data:SyncRequest,loading?: Ref<boolean>)=>{
  return post("/api/cloud_account/sync",null,data,loading);
}

/*
 * 查询云账户余额
 * @param cloudAccountId
 * @param loading
 */
const getAccountBalance: (
  cloudAccountId: string,
  loading?: Ref<boolean>
) => Promise<Result<number | string>> = (cloudAccountId, loading) => {
  return get("/api/cloud_account/balance/" + cloudAccountId, null, loading);
};

/**
 * 更新云账号名称
 * @param data
 * @param loading
 */
const updateAccountName: (
  data: UpdateAccountName,
  loading?: Ref<boolean>
) => Promise<Result<number | string>> = (data, loading) => {
  return put("/api/cloud_account/updateName", null, data, loading);
};

/**
 * 查询云账户余额
 * @param cloudAccountId
 * @param loading
 */
const getResourceCount: (
  cloudAccountId: string,
  loading?: Ref<boolean>
) => Promise<Result<Array<ResourceCount>>> = (cloudAccountId, loading) => {
  return get(
    "/api/cloud_account/resourceCount/" + cloudAccountId,
    null,
    loading
  );
};

export default {
  page,
  getPlatformAll,
  save,
  getRegions,
  getJobs,
  updateJobs,
  getCloudAccount,
  updateCloudAccount,
  deleteCloudAccount,
  batchDeleteCloudAccount,
  verificationCloudAccount,
  getResourceSync,
  syncJob,
  getAccountBalance,
  updateAccountName,
  getResourceCount,
};
