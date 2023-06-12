import type { KeyValue } from "@commons/api/base/type";

/**
 * 优化规则字段对象
 */
interface OptimizationRuleField {
  /**
   * 字段
   */
  field: string;
  /**
   * 提示
   */
  label: string;
  /**
   * 字段类型
   */
  fieldType: string;
  /**
   * 比较器
   */
  compares: Array<KeyValue<string, string>>;
  /**
   * 选择
   */
  options?: Array<KeyValue<string, string>>;
  esField: boolean;
  unit: string;
}
/**
 * 优化策略规则条件对象
 */
interface OptimizationRuleCondition {
  /**
   * 字段
   */
  field: string;
  /**
   * 比较器 默认EQ
   */
  compare: string;
  /**
   * 字段值
   */
  value: string;

  /**
   * es字段
   */
  esField: boolean;
}

/**
 * 优化规则树
 */
interface OptimizationRuleTree {
  /**
   * 子组件
   */
  children: Array<OptimizationRuleTree>;

  /**
   * 条件与或
   */
  conditionType: "AND" | "OR" | string;
  /**
   *  条件
   */
  conditions: Array<OptimizationRuleCondition>;
}

/**
 * 优化策略资源类型
 */
interface ResourceTypeDTO {
  /**
   * 资源类型名称
   */
  name: string;
  /**
   * 资源类型标识
   */
  value: string;
  /**
   * 资源类型优化规则可查询字段
   */
  optimizationRuleFieldList: Array<OptimizationRuleField>;
}

/**
 * 优化策略对象
 */
interface OptimizationStrategy {
  /**
   *主键id
   */
  id: string;
  /**
   * 名称
   */
  name: string;
  /**
   * 优化内容，优化建议
   */
  optimizationContent: string;
  /**
   * 策略类型，监控or资源
   */
  strategyType: string;
  /**
   * 资源类型
   */
  resourceType: string;
  /**
   * 优化范围，针对所有资源
   */
  optimizationScope: boolean;
  /**
   * 分析数据范围,过去多少天
   */
  days: number;
  /**
   * 是否启用
   */
  enabled: boolean;
  /**
   * 优化规则
   */
  optimizationRules: Array<OptimizationRuleTree>;
  /**
   * 授权id
   */
  authorizeId: string;
  /**
   *创建时间
   */
  createTime: string;
  /**
   * 修改时间
   */
  updateTime: string;

  ignoreResourceIdList: Array<string>;

  /**
   * 忽略资源数量
   */
  ignoreNumber?: number;

  [propName: string]: any;
}
interface ListOptimizationStrategyRequest {
  pageSize: number;
  currentPage: number;
}

interface PageOptimizationStrategyResourceRequest {
  pageSize: number;
  currentPage: number;
  [propName: string]: any;
}

export interface CreateRequest {
  id?: string;
  name: string;
  strategyType?: string;
  resourceType: string;
  days?: number;
  optimizationRules: Array<OptimizationRuleTree>;
  optimizationScope: boolean;
  ignoreResourceIdList?: Array<string> | undefined;
}

export class UpdateRequest {
  id: string;
  name: string;
  strategyType?: string;
  resourceType: string;
  days?: number;
  optimizationRules: Array<OptimizationRuleTree>;
  optimizationScope: boolean;
  ignoreResourceIdList?: Array<string> | undefined;

  constructor(optimizationStrategy: OptimizationStrategy) {
    this.id = optimizationStrategy.id;
    this.name = optimizationStrategy.name;
    this.strategyType = optimizationStrategy.strategyType;
    this.resourceType = optimizationStrategy.resourceType;
    this.optimizationRules = optimizationStrategy.optimizationRules;
    this.days = optimizationStrategy.days;
    this.optimizationScope = optimizationStrategy.optimizationScope;
    this.ignoreResourceIdList = optimizationStrategy.ignoreResourceIdList;
  }
}

/**
 * 更新策略状态
 */
interface UpdateStatusRequest {
  id: string;
  enabled: boolean;
}

/**
 * 监控数据对象
 */
interface MonitoringDataValue {
  maxValue: number;
  minValue: number;
  avgValue: number;
}

/**
 * 云主机
 */
interface VmCloudServerVO {
  id: string;
  instanceUuid?: string;
  accountId?: string;
  instanceId?: string;
  instanceName?: string;
  instanceType?: string;
  instanceTypeDescription?: string;
  remoteIp?: string;
  ipArray?: [];
  platform?: string;
  accountName?: string;
  content?: string;
  cpuMonitoringValue?: MonitoringDataValue;
  memoryMonitoringValue?: MonitoringDataValue;
}
interface OptimizationStrategyIgnoreResourceRequest {
  optimizationStrategyId: string;
  resourceIdList: Array<string>;
}

export type {
  ListOptimizationStrategyRequest,
  UpdateStatusRequest,
  OptimizationStrategy,
  OptimizationRuleTree,
  OptimizationRuleCondition,
  OptimizationRuleField,
  ResourceTypeDTO,
  VmCloudServerVO,
  PageOptimizationStrategyResourceRequest,
  OptimizationStrategyIgnoreResourceRequest,
};
