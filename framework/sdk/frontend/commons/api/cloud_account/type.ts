import type { SimpleMap } from "@commons/api/base/type";
export interface CloudAccount {
  /**
   * 主键id
   */
  id: string;
  /**
   * 云账号名称
   */
  name: string;
  /**
   * 云平台
   */
  platform: string;
  /**
   * 云账号状态 true有效 false无效
   */
  state: boolean;
  /**
   * 凭证信息
   */
  credential: string;
  /**
   * 状态(0:同步成功,1:同步失败,2:同步中)
   */
  status: number;
  /**
   * 账单设置
   */
  billSetting?: any;
  /**
   *创建时间
   */
  createTime: string;
  /**
   *修改时间
   */
  updateTime: string;
}

/**
 *云账户任务记录
 */
export interface AccountJobRecord {
  /**
   *云账户id
   */
  accountId: string;
  /**
   *任务记录id
   */
  jobRecordId: string;
  /**
   *任务记录类型
   */
  type: string;
  /**
   * 资源类型
   */
  resourceType: string;
  /**
   * 资源id
   */
  resourceId: string;
  /**
   * 任务状态
   */
  status: string;
  /**
   * 任务描述
   */
  description: string;
  /**
   * 创建时间
   */
  createTime: string;
  /**
   * 更新时间
   */
  updateTime: string;
  /**
   *任务参数
   */
  params: SimpleMap<Array<{ size: number; region: string } | any>>;
}
