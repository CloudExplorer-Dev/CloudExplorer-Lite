export interface SimpleMap<V> {
  [propName: string]: V;
}

export interface PageRequest {
  /**
   *每页显示多少
   */
  pageSize?: number;
  /**
   *当前页
   */
  currentPage?: number;

  setPage(pageSize: number, currentPage: number): PageRequest;
}

export class OrderObject {
  column?: string;
  asc?: boolean = true;

  constructor(column?: string, asc?: boolean) {
    this.column = column;
    this.asc = asc;
  }
}
