import type { SimpleMap } from "@commons/api/base/type";
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
export type { BillView };
