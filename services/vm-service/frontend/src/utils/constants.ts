import { i18n } from "@commons/base-locales";
import { ref } from "vue";
import type { SimpleMap } from "@commons/api/base/type";

/**
 * 资源收费类型
 */
export const CHARGE_TYPE = {
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  PREPAID: i18n.global.t("commons.charge_type.prepaid"),
  POSTPAID: i18n.global.t("commons.charge_type.postpaid"),
};

/**
 * 磁盘状态
 */
export const DISK_STATUS = ref<Array<SimpleMap<string>>>([
  { text: "已删除", value: "deleted" },
  { text: "已挂载", value: "in_use" },
  { text: "可用", value: "available" },
  { text: "挂载中", value: "attaching" },
  { text: "卸载中", value: "detaching" },
  { text: "创建中", value: "creating" },
  { text: "初始化中", value: "reiniting" },
  { text: "未知", value: "unknown" },
  { text: "错误", value: "error" },
  { text: "删除中", value: "deleting" },
  { text: "扩容中", value: "enlarging" },
  { text: "待回收", value: "ToBeRecycled" },
]);

/**
 * 磁盘类型
 */
export const DISK_TYPE = ref<Array<SimpleMap<string>>>([
  { text: "高效云盘", value: "cloud_efficiency" },
  { text: "ESSD云盘", value: "cloud_essd" },
  { text: "SSD云盘", value: "cloud_ssd" },
  { text: "普通云盘", value: "cloud" },
  { text: "通用型SSD", value: "GPSSD" },
  { text: "极速型SSD", value: "ESSD" },
  { text: "超高IO", value: "SSD" },
  { text: "高IO", value: "SAS" },
  { text: "高性能云硬盘", value: "CLOUD_PREMIUM" },
  { text: "增强型SSD云硬盘", value: "CLOUD_HSSD" },
  { text: "通用型SSD云硬盘", value: "CLOUD_BSSD" },
  { text: "ESSD AutoPL云盘", value: "cloud_auto" },
  { text: "精简置备", value: "THIN" },
  { text: "厚置备置零", value: "THICK_EAGER_ZEROED" },
  { text: "厚置备延迟置零", value: "THICK_LAZY_ZEROED" },
  { text: "稀疏型", value: "SPARSE" },
  { text: "lvmdriver-1", value: "lvmdriver-1" },
  { text: "__DEFAULT__", value: "__DEFAULT__" },
  { text: "未知", value: "NA" },
]);

export const getTextByValue = (
  data: Array<SimpleMap<string>>,
  value: string | undefined,
  valueField?: string,
  textField?: string
) => {
  if (value == undefined) {
    return value;
  }
  let text = value;
  const vField = valueField == undefined ? "value" : valueField;
  const tField = textField == undefined ? "text" : textField;
  data.forEach((v) => {
    if (v[vField] == value) {
      text = v[tField];
      return;
    }
  });
  return text;
};
