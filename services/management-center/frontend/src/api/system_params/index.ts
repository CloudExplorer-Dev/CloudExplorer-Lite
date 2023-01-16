import { get, post } from "@commons/request";

/**
 * 变更回收站状态
 * @param req
 */
export const changeRecycleBinStatus = (req: any) => {
  return post(
    "/api/system-setting/recycle-setting/changeRecycleBinStatus",
    "",
    req
  );
};

/**
 * 获取回收站状态
 * @param req
 */
export const getRecycleBinStatus = () => {
  return get("api/system-setting/recycle-setting/getRecycleBinStatus", "");
};
