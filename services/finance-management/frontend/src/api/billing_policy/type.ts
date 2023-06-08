import type { SimpleMap } from "@commons/api/base/type";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import type { FormView } from "@commons/components/ce-form/type";

interface BillingPolicy {
  id: string;
  /**
   * 策略名称
   */
  name: string;
  /**
   * 创建时间
   */
  createTime: string;
  /**
   * 修改时间
   */
  updateTime: string;
}
interface BillingField {
  /**
   * 字段
   */
  field: string;
  /**
   * 单价
   */
  price: number;
  /**
   * 单位
   */
  unit: string;
}
interface PolicyField {
  /**
   * 匹配字段
   */
  field: string;
  /**
   * 值
   */
  number: number;
}
interface PackagePriceBillingPolicy {
  /**
   * 主键id
   */
  id: string;
  /**
   *套餐名称
   */
  name: string;
  /**
   * 套餐匹配字段
   */
  billPolicyFields: Array<PolicyField>;
  /**
   * 按需
   */
  onDemand: number;
  /**
   * 包年包月
   */
  monthly: number;
}
interface BillingFieldMeta {
  /**
   *默认按量付费价钱
   */
  defaultOnDemandMPrice: number;
  /**
   * 默认按量付费价钱
   */
  defaultMonthlyMPrice: number;
  /**
   * 字段Label
   */
  fieldLabel: string;

  /**
   * 单位Label
   */
  unitLabel: string;
  /**
   *  价钱单位 元
   */
  priceLabel: string;
  /**
   * 其他元数据
   */
  meta: SimpleMap<any>;
}
interface BillingPolicyDetails {
  /**
   * 资源类型
   */
  resourceType: string;
  /**
   * 资源名称
   */
  resourceName: string;
  /**
   * 按量付费策略
   */
  unitPriceOnDemandBillingPolicy: Array<BillingField>;
  /**
   * 包年包月策略
   */
  unitPriceMonthlyBillingPolicy: Array<BillingField>;
  /**
   * 套餐策略
   */
  packagePriceBillingPolicy: Array<PackagePriceBillingPolicy>;

  billingFieldMeta: SimpleMap<BillingFieldMeta>;
  /**
   * 全局设置表单对象
   */
  globalConfigMetaForms: Array<FormView>;
  /**
   * 全局设置
   */
  globalConfigMeta: SimpleMap<any>;
}
interface BillingPolicyDetailsResponse {
  name: string;
  billingPolicyDetailsList: Array<BillingPolicyDetails>;
}

interface CreateBillingPolicyRequest {
  name: string;
  linkCloudAccountIds: Array<string>;
}
interface LinkCloudAccountRequest {
  /**
   * 策略id
   */
  billingPolicyId: string;
  /**
   * 云账号id
   */
  cloudAccountIdList: Array<string>;
}
interface CloudAccountResponse extends CloudAccount {
  /**
   * 是否是公有云
   */
  publicCloud: boolean;
  /**
   * 是否选中
   */
  selected: boolean;
}

export type {
  BillingPolicy,
  BillingPolicyDetailsResponse,
  BillingPolicyDetails,
  BillingField,
  BillingFieldMeta,
  PackagePriceBillingPolicy,
  CreateBillingPolicyRequest,
  LinkCloudAccountRequest,
  CloudAccountResponse,
};
