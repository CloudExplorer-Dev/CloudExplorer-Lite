import type { SimpleMap } from "@commons/api/base/type";

interface BillDetail {
  /**
   * 主键id
   */
  id: string;
  /**
   * 组织级别
   */
  orgTree: SimpleMap<string>;
  /**
   * 工作空间id
   */
  workspaceId: string;
  /**
   * 工作空间名称
   */
  workspaceName: string;
  /**
   * 组织id
   */
  organizationId: string;
  /**
   * 组织名称
   */
  organizationName: string;
  /**
   * 资源id
   */
  resourceId: string;
  /**
   * 资源名称
   */
  resourceName: string;
  /**
   * 企业项目id
   */
  projectId: string;
  /**
   *
   * 企业项目名称
   */
  projectName: string;
  /**
   * 付款账号id
   */
  payAccountId: string;
  /**
   * 账期
   */
  billingCycle: string;
  /**
   * 账单开始时间
   */
  usageStartDate: string;
  /**
   * 账单结束时间
   */
  usageEndDate: string;
  /**
   * 云平台
   */
  provider: string;
  /**
   * 产品id
   */
  productId: string;
  /**
   * 产品名称
   */
  productName: string;
  /**
   * 计费模式
   */
  billMode: string;
  /**
   * 区域id
   */
  regionId: string;
  /**
   * 区域名称
   */
  regionName: string;
  /**
   * 可用区
   */
  zone: string;
  /**
   * 云账户id
   */
  cloudAccountId: string;
  /**
   * 云账号名称
   */
  cloudAccountName: string;
  /**
   * 标签
   */
  tags: SimpleMap<string>;
  /**
   * 原价
   */
  totalCost: number;
  /**
   * 优惠后总价
   */
  realTotalCost: string;
}

export type { BillDetail };
