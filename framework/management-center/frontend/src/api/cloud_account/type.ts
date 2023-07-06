import type { SimpleMap } from "@commons/api/base/type";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import type {
  FormView as Form,
  FormViewObject,
} from "@commons/components/ce-form/type";

import type {
  Platform,
  CreateAccount,
  UpdateAccount,
} from "@commons/api/cloud_account/type";

interface ListOrganizationRequest {
  /**
   *每页显示多少
   */
  pageSize: number;
  /**
   *当前页
   */
  currentPage: number;
}

interface ListSyncRecordRequest {
  /**
   * 云账号id
   */
  cloudAccountId: string;
  /**
   *每页显示多少
   */
  pageSize: number;
  /**
   *当前页
   */
  currentPage: number;

  createTime?: string;

  status?: string;

  description?: string;
}

interface Region {
  /**
   * 区域名称
   */
  name: string;
  /**
   * 区域id
   */
  regionId: string;
}

interface CloudAccountJobDetailsResponse {
  /**
   * 模块任务
   */
  cloudAccountModuleJobs: Array<ModuleJob>;
}
/**
 * 模块定时任务信息
 */
interface ModuleJob {
  /**
   * 模块
   */
  module: any;
  /**
   * 模块名称
   */
  name: string;
  /**
   * 模块台湾
   */
  nameZhTw: string;
  /**
   * 中文
   */
  nameEn: string;
  /**
   * 基本path
   */
  basePath: string;
  /**
   * 图标
   */
  icon: string;
  /**
   * 排序
   */
  order: number;
  /**
   * 定时任务信息
   */
  jobDetailsList: Array<JobDetails>;
}
/**
 * 定时任务详情
 */
interface JobDetails {
  /**
   * 定时任务类型 CRON表达式 INTERVAL:间隔
   */
  jobType: string;
  /**
   * 定时任务名称
   */
  jobName: string;
  /**
   * 定时任务分组
   */
  jobGroup: string;
  /**
   * 描述
   */
  description: string;
  /**
   * 是否活跃
   */
  active: boolean;

  /**
   * 任务参数
   */
  params: SimpleMap<any>;

  /**
   * cron表达式
   */
  cronExpression?: string;
  /**
   * 间隔
   */
  interval?: number;
  /**
   * 间隔单位
   */
  unit?: "MINUTE" | "HOUR" | "DAY" | "WEEK" | "MONTH";
  /**
   * 定时任务状态是否只读
   */
  activeReadOnly: boolean;
  /**
   * 定时任务数据是否只读
   */
  cronReadOnly: boolean;

  paramsForm: FormViewObject;
}
interface UpdateJobsRequest extends CloudAccountJobDetailsResponse {
  cloudAccountId: string;
}

interface ResourceSync {
  /**
   * 模块
   */
  module: string;
  /**
   * 任务名称
   */
  jobName: string;
  /**
   * 资源描述
   */
  resourceDesc: string;
  /**
   * 任务组
   */
  jobGroup: string;
  /**
   * 是否选中
   */
  active?: boolean;
}

interface SyncRequest {
  /**
   * 云账号id
   */
  cloudAccountId: string;
  /**
   * 同步任务
   */
  syncJob: Array<{ module: string; jobName: string; jobGroup: string }>;
  /**
   *参数
   */
  params: SimpleMap<any>;
}
/**
 * 更新云账号名称所需要的参数
 */
interface UpdateAccountName {
  /**
   * 云账号id
   */
  id: string;
  /**
   * 云账号Name
   */
  name: string;
}

/**
 * 资源计数
 */
interface ResourceCount {
  /**
   * 资源图标
   */
  icon: string;
  /**
   * 资源名称
   */
  name: string;
  /**
   * 资源计数
   */
  count: number;

  unit?: string;
}
/**
 *云账户任务记录
 */
interface AccountJobRecord {
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

export type {
  ListOrganizationRequest,
  ListSyncRecordRequest,
  CloudAccount,
  Platform,
  Form,
  CreateAccount,
  Region,
  CloudAccountJobDetailsResponse,
  ModuleJob,
  UpdateJobsRequest,
  UpdateAccount,
  ResourceSync,
  SyncRequest,
  UpdateAccountName,
  ResourceCount,
  AccountJobRecord,
  JobDetails,
};
