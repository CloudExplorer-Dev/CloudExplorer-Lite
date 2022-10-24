import { get, post, del, put } from "@commons/request";
import type { Ref } from "vue";
import type Result from "@commons/request/Result";

import type { CloudAccount } from "./type";

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
  return get("/api/base/cloud_account/" + cloudAccountId, null, loading);
}

const BaseCloudAccountApi = { getCloudAccount, listAll };

export default BaseCloudAccountApi;
