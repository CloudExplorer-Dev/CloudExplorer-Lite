import { get, post, del, put } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";

import type { CloudAccount, CreateAccount, Platform } from "./type";

function listAll(loading?: Ref<boolean>): Promise<Result<Array<CloudAccount>>> {
  return get("api/base/cloud_account/list", null, loading);
}

/**
 * 根据云账号id查询到云账号对象
 * @param cloudAccountId 云账号id
 * @param loading        加载器
 * @returns 云账号对象
 */
function getCloudAccount(
  cloudAccountId: string,
  loading?: Ref<boolean>
): Promise<Result<CloudAccount>> {
  return get(`/api/base/cloud_account/${cloudAccountId}`, null, loading);
}

/**
 * 查询云账户余额
 * @param cloudAccountId
 * @param loading
 */
function getAccountBalance(
  cloudAccountId: string,
  loading?: Ref<boolean>
): Promise<Result<number | string>> {
  return get(
    `/api/base/cloud_account/balance/${cloudAccountId}`,
    null,
    loading
  );
}

/**
 * 获取所有的云账号供应商
 * @param loading   加载器
 * @returns         云账号供应商
 */
const getPlatformAll: (
  loading?: Ref<boolean>
) => Promise<Result<Array<Platform>>> = (loading) => {
  return get("/api/base/cloud_account/platform", null, loading);
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
  return post(
    (import.meta.env.VITE_APP_NAME === "management-center"
      ? ""
      : "/management-center/") + "api/cloud_account",
    null,
    createAccount,
    loading
  );
};

const BaseCloudAccountApi = {
  getCloudAccount,
  listAll,
  getAccountBalance,
  getPlatformAll,
  save,
};

export default BaseCloudAccountApi;
