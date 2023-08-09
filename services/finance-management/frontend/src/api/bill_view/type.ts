interface BillView {
  /**
   * 账单分组详情
   */
  billGroupDetails: Array<KeyValue>;
  /**
   * 账单值
   */
  value: number;
}
interface KeyValue {
  key: string;
  value: string;
}

interface CurrencyRequest {
  /**
   * 币种 code
   */
  code: string;
  /**
   * 汇率
   */
  exchangeRate: number;
}

export type { BillView, CurrencyRequest };
