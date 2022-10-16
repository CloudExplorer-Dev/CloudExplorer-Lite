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
}

interface CloudAccount {
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
interface Form {
  /**
   * 输入类型
   */
  inputType: string;
  /**
   * 字段名称
   */
  field: string;
  /**
   * 提示
   */
  label: string;
  /**
   * 是否必填
   */
  required: boolean;
  /**
   * 默认值
   */
  defaultValue: unknown;
  /**
   * 描述
   */
  description: string;
}
interface Platform {
  /**
   * 提示
   */
  label: string;
  /**
   * 字段
   */
  field: string;

  credentialFrom: Array<Form>;
}

interface CreateAccount {
  /**
   *名称
   */
  name: string;
  /**
   * 云平台
   */
  platform: string;
  /**
   * 认证属性
   */
  credential: { [propName: string]: string };
}

/**
 * 更新云账号所需要的参数
 */
interface UpdateAccount extends CreateAccount {
  /**
   * 云账号id
   */
  id: string;
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
  /**
   *选中的区域
   */
  selectRegion: Array<Region>;
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
   * 定时任务名称
   */
  jobName: string;
  /**
   * 定时任务分组
   */
  jobGroup: string;
  /**
   * 执行间隔时间
   */
  timeInterval: string;
  /**
   * 执行间隔单位
   */
  unit: string;
  /**
   * 描述
   */
  description: string;
  /**
   * 是否活跃
   */
  active: string;
  /**
   * 区域
   */
  regions: Array<Region>;
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
  syncJob: Array<{ module: string; jobName: string }>;
  /**
   * 区域
   */
  regions: Array<Region>;
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
  params: Array<{ size: number; region: string }>;
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
};
