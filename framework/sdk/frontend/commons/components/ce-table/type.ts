import type { Ref } from "vue";

export class PaginationConfig {
  constructor(
    currentPage?: number,
    pageSize?: number,
    pageSizes?: Array<number>
  ) {
    this.currentPage = currentPage ? currentPage : 1;
    this.pageSize = pageSize ? pageSize : 10;
    this.pageSizes = pageSizes ? pageSizes : [5, 10, 20, 50, 100];
  }
  /**
   *当前页数
   */
  currentPage = 0;
  /**
   *每页多少
   */
  pageSize = 10;
  /**
   *总数据
   */
  total = 0;
  /**
   *每页显示条数 下拉
   */
  pageSizes: Array<number> = [5, 10, 20, 50, 100];
  /**
   * 修改页数
   */
  setPageSize: (pageSize: number, proxy: any) => void = (
    pageSize,
    proxy: any
  ) => {
    if (proxy) {
      proxy.pageSize = pageSize;
    }
  };
  /**
   * 修改当前页数
   */
  setCurrentPage: (currentPage: number, proxy: any) => void = (
    currentPage,
    proxy
  ) => {
    if (proxy) {
      proxy.currentPage = currentPage;
    }
  };

  setTotal: (total: number, proxy?: any) => void = (total, proxy) => {
    if (proxy) {
      proxy.total = total;
    }
  };
}

/**
 *过滤器
 */
class ComponentParent {
  constructor(field: string, label: string, component: string) {
    this.field = field;
    this.label = label;
    this.component = component;
  }
  /**
   * 字段名称
   */
  field: string;
  /**
   * 字段提示
   */
  label: string;
  /**
   * 组建名称
   */
  component: string;
}

/**
 * 查询
 */
export class SearchOption {
  /**
   *选项 提示
   */
  label = "";
  /**
   *选项 值
   */
  value = "";
  constructor(label: string, value: string) {
    this.label = label;
    this.value = value;
  }
}

/**
 * 选项过滤器配置
 */
class SelectComponent extends ComponentParent {
  /**
   * 选项
   */
  options: Array<{
    /**
     *选项 提示
     */
    label: string;
    /**
     *选项 值
     */
    value: string | number;
  }>;
  showLimit: number;
  /**
   *是否多选
   */
  multiple: boolean;
  /**
   *清除
   */
  clearable: boolean;
  /**
   * 是否可以过滤
   */
  filterable: boolean;

  constructor(
    field: string,
    label: string,
    component = "FuFilterSelect",
    options: Array<{
      /**
       *选项 提示
       */
      label: string;
      /**
       *选项 值
       */
      value: string | number;
    }>,
    showLimit = 3,
    multiple = true,
    clearable = true,
    filterable = true
  ) {
    super(field, label, component);
    this.options = options;
    this.showLimit = showLimit;
    this.multiple = multiple;
    this.clearable = clearable;
    this.filterable = filterable;
  }
  static newInstance(
    field: string,
    label: string,
    component = "FuFilterSelect",
    options: Array<{
      /**
       *选项 提示
       */
      label: string;
      /**
       *选项 值
       */
      value: string | number;
    }>,
    showLimit = 3,
    multiple = true,
    clearable = true,
    filterable = true
  ) {
    return new SelectComponent(
      field,
      label,
      component,
      options,
      showLimit,
      multiple,
      clearable,
      filterable
    );
  }
}

/**
 *日期过滤器
 */
class DateComponent extends ComponentParent {
  constructor(field: string, label: string, component = "FuFilterDate") {
    super(field, label, component);
  }
  static newInstance(
    field: string,
    label: string,
    component = "FuFilterDateTime"
  ) {
    return new DateComponent(field, label, component);
  }
}

/**
 *时间过滤器 秒
 */
class DateTimeComponent extends ComponentParent {
  constructor(field: string, label: string, component = "FuFilterDateTime") {
    super(field, label, component);
  }
  static newInstance(
    field: string,
    label: string,
    component = "FuFilterDateTime"
  ) {
    return new DateTimeComponent(field, label, component);
  }
}

export class Condition {
  /**
   *字段
   */
  field: string;
  /**
   *label
   */
  label: string;
  /**
   *值
   */
  value: Ref<any>;
  /**
   *值label
   */
  valueLabel: string;

  constructor(
    field: string,
    label: string,
    value: Ref<any>,
    valueLabel: string
  ) {
    this.field = field;
    this.label = label;
    this.value = value;
    this.valueLabel = valueLabel;
  }

  static toSearchObj(condition?: Condition) {
    if (!condition || !condition.value) {
      return {};
    }
    const field = condition.field;
    const value = condition.value;
    return { [field]: JSON.parse(JSON.stringify(value)) };
  }
}

/**
 * 搜索配置
 */
export class SearchConfig {
  showEmpty = false;
  /**
   *查询回掉函数
   */
  search: (condition: TableSearch) => void;
  /**
   * 搜索框提示
   */
  quickPlaceholder = "";

  /**
   *查询options
   */
  searchOptions: Array<SearchOption> = [];
  /**
   *筛选组建
   */
  components: Array<
    DateTimeComponent | DateComponent | SelectComponent | ComponentParent
  > = [];

  constructor(
    search: (condition: TableSearch) => void,
    quickPlaceholder: string,
    components: Array<
      DateTimeComponent | DateComponent | SelectComponent | ComponentParent
    >,
    searchOptions: Array<SearchOption>
  ) {
    this.search = search;
    this.quickPlaceholder = quickPlaceholder;
    this.components = components;
    this.searchOptions = searchOptions;
  }
  /**
   * 获取筛选组建对象
   * @returns
   */
  static buildComponent() {
    return {
      DateTimeComponent,
      DateComponent,
      SelectComponent,
    };
  }
}

class Button {
  /**
   *按钮文字提示
   */
  label: string;
  /**
   *按钮类型,与element的类型一样
   */
  type: string;
  /**
   *点击事件
   */
  click: (row: any) => void;
  /**
   * 是否禁用
   */
  disabled?: ((row: any) => boolean) | boolean;
  /**
   *图标,只能使用element的按钮
   */
  icon?: string;
  /**
   *是否展示
   */
  show?: ((row: any) => boolean) | boolean;
  constructor(
    label: string,
    type: string,
    click: (row: any) => void,
    icon?: string,
    disabled?: ((row: any) => boolean) | boolean,
    show?: ((row: any) => boolean) | boolean
  ) {
    this.label = label;
    this.type = type;
    this.click = click;
    this.icon = icon;
    this.disabled = disabled;
    this.show = show;
  }

  static newInstance(
    label: string,
    type: string,
    click: (row: any) => void,
    icon?: string,
    disabled: ((row: any) => boolean) | boolean = false,
    show: ((row: any) => boolean) | boolean = true
  ) {
    return new Button(label, type, click, icon, disabled, show);
  }
}

export class TableOperations {
  /**
   * icon
   * label
   */
  type: string;
  /**
   * 按钮提示
   */
  label: string;
  /**
   *所有操作按钮
   */
  buttons: Array<Button>;
  /**
   *  超过几个按钮时显示省略号，如果只超过一个也不显示省略号
   */
  ellipsis: number;
  constructor(
    buttons: Array<Button>,
    type = "icon",
    label = "操作",
    ellipsis = 3
  ) {
    this.type = type;
    this.label = label;
    this.buttons = buttons;
    this.ellipsis = ellipsis;
  }
  static buildButtons() {
    return Button;
  }
}

/**
 *表格配置
 */
export class TableConfig {
  /**
   *操作按钮配置
   */
  tableOperations?: TableOperations;

  /**
   *搜索相关配置
   */
  searchConfig: SearchConfig;
  /**
   *分页相关配置
   */
  paginationConfig: PaginationConfig;

  constructor(
    tableOperations: TableOperations,
    searchConfig: SearchConfig,
    paginationConfig: PaginationConfig
  ) {
    this.searchConfig = searchConfig;
    this.tableOperations = tableOperations;
    this.paginationConfig = paginationConfig;
  }
}

export class Order {
  /**
   *需要进行排序的字段
   */
  column: string;
  /**
   *是否正序排列，默认 true
   */
  asc: boolean;
  constructor(column: string, asc: boolean) {
    this.column = column;
    this.asc = asc;
  }
}

export class Conditions {
  [propName: string]: Condition | string;
}

export class TableSearch {
  /**
   *查询条件
   */
  conditions: Conditions = {};
  /**
   *排序对象
   */
  order?: Order;

  /**
   *查询相关接口
   */
  search?: Condition;
  /**
   *表头筛选
   */
  tableFilter?: Conditions = {};

  constructor(
    conditions: Conditions = {},
    order?: Order,
    search?: Condition,
    tableFilter?: Conditions
  ) {
    this.conditions = conditions;
    this.order = order;
    this.search = search;
    this.tableFilter = tableFilter;
  }
  /**
   * 返回查询对象
   */
  static toSearchParams(tableSearch: TableSearch): any {
    // 表头筛选
    const tableHeaderFilter = (
      tableSearch.tableFilter ? Object.keys(tableSearch.tableFilter) : []
    )
      .map((key: string) => {
        return Condition.toSearchObj(
          tableSearch.tableFilter
            ? (tableSearch.tableFilter[key] as Condition)
            : undefined
        );
      })
      .reduce((pre: any, next: any) => {
        return { ...pre, ...next };
      }, {});
    // 排序
    const order = tableSearch.order ? tableSearch.order : undefined;
    // 搜索框
    const searchObj: any = Condition.toSearchObj(tableSearch.search);
    // 所有查询
    return Object.keys(tableSearch.conditions)
      .map((key: string) => {
        const value: Condition | string = tableSearch.conditions[key];
        if (typeof value === "string") {
          return { [key]: value };
        } else {
          return Condition.toSearchObj(value);
        }
      })
      .reduce(
        (pre: any, next: any) => {
          return { ...pre, ...next };
        },
        {
          order,
          ...searchObj,
          ...tableHeaderFilter,
        }
      );
  }
}
