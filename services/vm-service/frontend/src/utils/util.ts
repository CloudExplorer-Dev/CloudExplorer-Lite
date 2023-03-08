import { IpType } from "@/api/vm_cloud_server/type";
import type { Ip } from "@/api/vm_cloud_server/type";

/**
 * IP 分类
 * @param ipArray
 * @param remoteIp
 */
export const classifyIP = (ipArray: string, remoteIp: string) => {
  return JSON.parse(ipArray)
    .map((item: string) => {
      return {
        ip: item,
        type: item.indexOf(".") > -1 ? IpType.IPv4 : IpType.IPv6,
        isPublicIp: item === remoteIp ? true : false,
      };
    })
    .sort((a: Ip, b: Ip) => {
      return a.type - b.type;
    });
};
