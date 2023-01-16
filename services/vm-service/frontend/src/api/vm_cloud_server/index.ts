import { get, post, put } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type {
  VmCloudServerVO,
  ListVmCloudServerRequest,
  CreateServerRequest,
  CloudServerJobRecord,
  PerfMonitorData,
  GetPerfMonitorRequest,
  ChangeServerConfigRequest,
  GrantRequest,
} from "./type";
import type { Ref } from "vue";
import type { SimpleMap } from "@commons/api/base/type";

/**
 * 虚拟机列表
 * @param req
 * @param loading
 */
export function listVmCloudServer(
  req: ListVmCloudServerRequest,
  loading?: Ref<boolean>
): Promise<Result<Page<VmCloudServerVO>>> {
  return get("api/server/page", req, loading);
}

/**
 * 关机
 * @param instanceId
 * @param loading
 */
export function shutdownInstance(
  instanceId: string,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return post("api/server/shutdown/" + instanceId, null, null, loading);
}

/**
 * 开机
 * @param instanceId
 * @param loading
 */
export function powerOn(
  instanceId: string,
  loading?: Ref<boolean>
): Promise<Result<null>> {
  return post("api/server/powerOn/" + instanceId, null, null, loading);
}

/**
 * 重启
 * @param instanceId
 * @param loading
 */
export function reboot(
  instanceId: string,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return post("api/server/reboot/" + instanceId, null, null, loading);
}

/**
 * 关闭电源
 * @param instanceId
 * @param loading
 */
export function powerOff(
  instanceId: string,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return post("api/server/powerOff/" + instanceId, null, null, loading);
}

/**
 * 放入回收站
 * @param instanceId
 * @param loading
 */
export function recycleInstance(
  instanceId: string,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return post("api/server/recycle/" + instanceId, null, null, loading);
}

/**
 * 删除
 * @param instanceId
 * @param loading
 */
export function deleteInstance(
  instanceId: string,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return post("api/server/delete/" + instanceId, null, null, loading);
}

/**
 * 批量操作
 * @param instanceIds
 * @param operate
 * @param loading
 */
export function batchOperate(
  instanceIds: Array<string>,
  operate: string,
  loading?: Ref<boolean>
): Promise<Result<null>> {
  return post(
    "api/server/batchOperate",
    null,
    { instanceIds: instanceIds, operate: operate },
    loading
  );
}

/**
 * 根据ID查询
 * @param id
 * @param loading
 */
export function getVmCloudServerById(
  id: string,
  loading?: Ref<boolean>
): Promise<Result<VmCloudServerVO>> {
  return get("api/server/" + id, null, loading);
}

/**
 * 根据IDS查询
 * @param id
 * @param loading
 */
export function getVmCloudServerByIds(
  cloudServerIds: Array<string>,
  loading?: Ref<boolean>
): Promise<Result<Array<VmCloudServerVO>>> {
  return get("api/server/ids", { cloudServerIds: cloudServerIds }, loading);
}

/**
 * 获取云主机最新任务状态
 * @param cloudServerIds id
 * @param loading         加载器
 * @returns               云主机任务记录
 */
export function getServerJobRecord(
  cloudServerIds: Array<string>,
  loading?: Ref<boolean>
): Promise<Result<SimpleMap<Array<CloudServerJobRecord>>>> {
  return get(
    "/api/server/operate/job_record",
    { cloudServerIds: cloudServerIds },
    loading
  );
}

/**
 * 查询监控数据
 * @param req
 * @param loading
 */
export function listPerfMetricMonitor(
  req: Ref<GetPerfMonitorRequest | null>,
  loading?: Ref<boolean>
): Promise<Result<Array<PerfMonitorData>>> {
  return get("/api/base/monitor/list", req, loading);
}

export function createServer(
  req: CreateServerRequest,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return post("api/server/create", null, req, loading);
}

/**
 *配置变更
 * @param req
 * @param loading
 */
export function changeServerConfig(
  req: ChangeServerConfigRequest,
  loading?: Ref<boolean>
): Promise<Result<boolean>> {
  return put("api/server/changeConfig", null, req, loading);
}

/**
 * 根据云账号查询配置变更表单数据
 * @param req
 * @param loading
 */
export function getConfigUpdateForm(platform: string, loading?: Ref<boolean>) {
  return get("/api/server/configUpdateForm/" + platform, null, loading);
}

/**
 * 查询配置变更价格
 * @param req
 * @param loading
 */
export function getConfigUpdatePrice(
  platform: string,
  req: unknown,
  loading?: Ref<boolean>
) {
  return post("/api/server/configUpdatePrice/" + platform, null, req, loading);
}

/**
 * 云主机授权
 * @param req
 * @param loading
 */
export function grantVmCloudServer(req: GrantRequest, loading?: Ref<boolean>) {
  return post("/api/server/grant", null, req, loading);
}

const VmCloudServerApi = {
  listVmCloudServer,
  shutdownInstance,
  powerOn,
  reboot,
  powerOff,
  recycleInstance,
  deleteInstance,
  batchOperate,
  getVmCloudServerById,
  getServerJobRecord,
  listPerfMetricMonitor,
  getVmCloudServerByIds,
  createServer,
  changeServerConfig,
  getConfigUpdateForm,
  getConfigUpdatePrice,
  grantVmCloudServer,
};

export default VmCloudServerApi;
