import aliyun from "@commons/assets/img/cloud-vendor-icon/aliyun.ico";
import huawei from "@commons/assets/img/cloud-vendor-icon/fusion.ico";
import aws from "@commons/assets/img/cloud-vendor-icon/aws.ico";
import vmware from "@commons/assets/img/cloud-vendor-icon/vmware.ico";
import qcloud from "@commons/assets/img/cloud-vendor-icon/qcloud.ico";
import openstack from "@commons/assets/img/cloud-vendor-icon/openstack.ico";
import type { SimpleMap } from "@commons/api/base/type";
import CeIcon from "@commons/components/ce-icon/index.vue";

interface PlatFormIcon {
  component?: any;
  color?: string;
  icon?: any;
  oldIcon?: any;
  name: string;
}

export const platformIcon: SimpleMap<PlatFormIcon> = {
  fit2cloud_ali_platform: {
    component: CeIcon,
    icon: { code: "aliyun" },
    oldIcon: aliyun,
    color: "#ff6c06",
    name: "阿里云",
  },
  fit2cloud_huawei_platform: {
    component: CeIcon,
    icon: { code: "huawei" },
    oldIcon: huawei,
    color: "#f40f0a",
    name: "华为云",
  },
  fit2cloud_aws_platform: {
    component: CeIcon,
    icon: { code: "aws" },
    oldIcon: aws,
    color: "#e38d2e",
    name: "AWS",
  },
  fit2cloud_vsphere_platform: {
    component: CeIcon,
    icon: { code: "a-ziyuan38" },
    oldIcon: vmware,
    color: "#6e6e71",
    name: "vmware",
  },
  fit2cloud_tencent_platform: {
    component: CeIcon,
    icon: { code: "tengxunyun" },
    oldIcon: qcloud,
    color: "#139df6",
    name: "腾讯云",
  },
  fit2cloud_openstack_platform: {
    component: CeIcon,
    icon: { code: "icon_openstack" },
    oldIcon: openstack,
    color: "#d21f33",
    name: "OpenStack",
  },
};
