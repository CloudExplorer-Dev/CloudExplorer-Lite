import { get } from "@commons/request";
import type Result from "@commons/request/Result";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import type { Ref } from "vue";
import type { CloudAccountDTO, CommonRequest } from "@/api/common/type";
import type { Page } from "@commons/request/Result";

/**
 * 云账号
 * @param req
 * @param loading
 */
function listAll(loading?: Ref<boolean>): Promise<Result<Array<CloudAccount>>> {
  return get("api/common/cloud_account/list", null, loading);
}

/**
 * 分页查询
 * @param currentPage 当前页
 * @param pageSize    每页显示多少条
 * @param query       查询参数对象
 * @param loading    加载器
 * @returns  云账号资源统计数据
 */
const cloudAccountResourceCountPage: (
  currentPage: number,
  pageSize: number,
  query: CommonRequest,
  loading?: Ref<boolean>
) => Promise<Result<Page<CloudAccountDTO>>> = (
  currentPage,
  pageSize,
  query,
  loading
) => {
  return get(
    `/api/common/cloud_account/resource/count/${currentPage}/${pageSize}`,
    query,
    loading
  );
};

const CommonApi = {
  listAll,
  cloudAccountResourceCountPage,
};

export default CommonApi;
