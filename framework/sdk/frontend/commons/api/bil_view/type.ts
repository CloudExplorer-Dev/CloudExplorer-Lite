interface BaseViewRequest {
  /**
   * 云账号id
   */
  cloudAccountId?: string;
  /**
   * 成本字段
   */
  costField?:
    | "officialAmount"
    | "payableAmount"
    | "cashAmount"
    | "couponAmount";
  /**
   * 币种
   */
  currency?:
    | "USD"
    | "JPY"
    | "CNY"
    | "EUR"
    | "GBP"
    | "KRW"
    | "HKD"
    | "AUD"
    | "CAD"
    | "RUB";
}
/**
 *账单规则展示请求对象
 */
type BillViewRequest = BaseViewRequest;
/**
 * 账单趋势请求对象
 */
type HistoryTrendRequest = BaseViewRequest;
/**
 * 账单费用聚合请求对象
 */
type BillExpensesRequest = BaseViewRequest;

interface Currency {
  /**
   * 符号
   */
  symbol: string;
  /**
   * 单位
   */
  unit: string;
  /**
   * 币种标识
   */
  code:
    | "USD"
    | "JPY"
    | "CNY"
    | "EUR"
    | "GBP"
    | "KRW"
    | "HKD"
    | "AUD"
    | "CAD"
    | "RUB";
  /**
   * 币种描述
   */
  message: string;
  /**
   * 汇率
   */
  exchangeRate: number;
}

export type {
  BaseViewRequest,
  BillViewRequest,
  HistoryTrendRequest,
  BillExpensesRequest,
  Currency,
};
