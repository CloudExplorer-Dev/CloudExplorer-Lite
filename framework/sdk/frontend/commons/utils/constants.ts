export const Config = {
  TITLE: "CloudExplorer 云服务平台",
  CE_TOKEN_KEY: "ce-token",
  CE_ROLE_KEY: "CE-ROLE",
  CE_SOURCE_KEY: "CE-SOURCE",
  CE_LANG_KEY: "LANG",
  /**
   * microAPP在window对象上的属性名称前缀
   */
  MICRO_APP_PREFIX: "micro-app-",
  /**
   * microAPP 事件在window对象上的属性名称前缀
   */
  MICRO_APP_EVENTCENTER_PREFIX: "eventCenterForApp-",
};

// 角色类型常量
export const roleConst = {
  admin: "ADMIN",
  orgAdmin: "ORGADMIN",
  user: "USER",
};

// 指标常量
// 这个要对应后端的枚举类，随便一个云的都可以，主要是名称、描述还有单位一致CloudServerPerfMetricEnum.java
export const PerfMetricConst = {
  CPU_USED_UTILIZATION: {
    metricName: "CPU_USED_UTILIZATION",
    name: "CPUUtilization",
    description: "CPU使用率",
    unit: "%",
  },
  MEMORY_USED_UTILIZATION: {
    metricName: "MEMORY_USED_UTILIZATION",
    name: "MemoryUsedUtilization",
    description: "内存使用率",
    unit: "%",
  },
  DISK_READ_BPS: {
    metricName: "DISK_READ_BPS",
    name: "DiskReadBPS",
    description: "所有磁盘读取BPS",
    unit: "Byte/s",
  },
  DISK_WRITE_BPS: {
    metricName: "DISK_WRITE_BPS",
    name: "DiskWriteBPS",
    description: "所有磁盘写入BPS",
    unit: "Byte/s",
  },
  DISK_READ_IOPS: {
    metricName: "DISK_READ_IOPS",
    name: "DiskReadIOPS",
    description: "所有磁盘每秒读取次数",
    unit: "次/秒",
  },
  DISK_WRITE_IOPS: {
    metricName: "DISK_WRITE_IOPS",
    name: "DiskWriteIOPS",
    description: "所有磁盘每秒写入次数",
    unit: "次/秒",
  },
  INTERNET_IN_RATE: {
    metricName: "INTERNET_IN_RATE",
    name: "InternetInRate",
    description: "公网流入带宽",
    unit: "bit/s",
  },
  INTERNET_OUT_RATE: {
    metricName: "INTERNET_OUT_RATE",
    name: "InternetOutRate",
    description: "公网流出带宽",
    unit: "bit/s",
  },
  INTRANET_IN_RATE: {
    metricName: "INTRANET_IN_RATE",
    name: "IntranetInRate",
    description: "内网流入带宽",
    unit: "bit/s",
  },
  INTRANET_OUT_RATE: {
    metricName: "INTRANET_OUT_RATE",
    name: "IntranetOutRate",
    description: "内网流出带宽",
    unit: "bit/s",
  },
  DISK_USED_UTILIZATION: {
    metricName: "DISK_USED_UTILIZATION",
    name: "DiskUsedUtilization",
    description: "磁盘使用率",
    unit: "%",
  },
  //VMWARE
  VIRTUAL_DISK_READ_BPS: {
    metricName: "VIRTUAL_DISK_READ_BPS",
    name: "VirtualDiskReadBPS",
    description: "所有虚拟磁盘读取BPS",
    unit: "KBps",
  },
  VIRTUAL_DISK_WRITE_BPS: {
    metricName: "VIRTUAL_DISK_WRITE_BPS",
    name: "VirtualDiskWriteBPS",
    description: "所有虚拟磁盘写入BPS",
    unit: "KBps",
  },
};
// 监控资源类型常量
export const PerfMetricEntityTypeConst = {
  HOST: "HOST",
  VIRTUAL_MACHINE: "VIRTUAL_MACHINE",
  DISK: "DISK",
} as const;
export default Config;
