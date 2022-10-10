import { get,post } from "@commons/request";
import type Result from "@commons/request/Result";
import type { Page } from "@commons/request/Result";
import type { VmCloudServerVO, ListVmCloudServerRequest } from "./type";
import type { Ref } from "vue";


/**
 * 虚拟机列表
 * @param req
 * @param loading
 */
export function listVmCloudServer(
    req:ListVmCloudServerRequest,
    loading?:Ref<boolean>
):Promise<Result<Page<VmCloudServerVO>>>{
  return get("api/server/page", req,loading);
}

/**
 * 关机
 * @param instanceId
 * @param loading
 */
export function shutdownInstance(instanceId:string,loading?:Ref<boolean>):Promise<Result<boolean>>{
  return post("api/server/shutdown/"+instanceId,null,null,loading);
}

/**
 * 开机
 * @param instanceId
 * @param loading
 */
export function powerOn(instanceId:string,loading?:Ref<boolean>):Promise<Result<boolean>>{
  return post("api/server/powerOn/"+instanceId,null,null,loading);
}

/**
 * 重启
 * @param instanceId
 * @param loading
 */
export function reboot(instanceId:string,loading?:Ref<boolean>):Promise<Result<boolean>>{
  return post("api/server/reboot/"+instanceId,null,null,loading);
}

/**
 * 关闭电源
 * @param instanceId
 * @param loading
 */
export function powerOff(instanceId:string,loading?:Ref<boolean>):Promise<Result<boolean>>{
  return post("api/server/powerOff/"+instanceId,null,null,loading);
}

/**
 * 删除
 * @param instanceId
 * @param loading
 */
export function deleteInstance(instanceId:string,loading?:Ref<boolean>):Promise<Result<boolean>>{
  return post("api/server/delete/"+instanceId,null,null,loading);
}

/**
 * 批量操作
 * @param instanceIds
 * @param operate
 * @param loading
 */
export function batchOperate(instanceIds:any,operate:string,loading?:Ref<boolean>):Promise<Result<boolean>>{
  return post("api/server/batchOperate",null,{instanceIds:instanceIds,operate:operate},loading);
}


const VmCloudServerApi = {
  listVmCloudServer,
  shutdownInstance,
  powerOn,
  reboot,
  powerOff,
  deleteInstance,
  batchOperate
};

export default VmCloudServerApi;
