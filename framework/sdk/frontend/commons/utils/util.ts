export enum IpType {
  IPv4,
  IPv6,
}

interface Ip {
  ip: string;
  type: IpType;
  isPublicIp: boolean;
}
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

/**
 * 判空
 * @param value
 */
export const isNotEmpty = (value: any) => {
  if (value !== null && value !== "null" && value !== undefined) {
    return true;
  }
  return false;
};

/**
 * 收费类型
 * @param instanceChargeType
 */
export const filterChargeType = (instanceChargeType?: string) => {
  let text = instanceChargeType;
  switch (instanceChargeType) {
    case "PostPaid":
      text = "按需计费";
      break;
    case "PrePaid":
      text = "包年/包月";
      break;
    case "SpotPaid":
      text = "竞价计费";
      break;
    default:
  }
  return text;
};
