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

/**
 * 数字千分位格式
 * @param num
 */
export const numberFormatter = (value: number) => {
  if (!value) return 0;
  // 获取整数部分
  const intPart = Math.trunc(value);
  // 整数部分处理，增加,
  const intPartFormat = intPart
    .toString()
    .replace(/(\d)(?=(?:\d{3})+$)/g, "$1,");
  // 预定义小数部分
  let floatPart = "";
  // 将数值截取为小数部分和整数部分
  const valueArray = value.toString().split(".");
  if (valueArray.length === 2) {
    // 有小数部分
    floatPart = valueArray[1].toString(); // 取得小数部分
    return intPartFormat + "." + floatPart;
  }
  return intPartFormat + floatPart;
};
