import aliyun from "@commons/assets/img/cloud-vendor-icon/aliyun.ico";
import huawei from "@commons/assets/img/cloud-vendor-icon/fusion.ico";
import aws from "@commons/assets/img/cloud-vendor-icon/aws.ico";
import vmware from "@commons/assets/img/cloud-vendor-icon/vmware.ico";
import qcloud from "@commons/assets/img/cloud-vendor-icon/qcloud.ico";
import openstack from "@commons/assets/img/cloud-vendor-icon/openstack.ico";

import LogoAliyun from "@commons/assets/img/cloud-vendor-icon/logo-aliyun.png";
import LogoHuawei from "@commons/assets/img/cloud-vendor-icon/logo-huawei.svg";
import LogoAws from "@commons/assets/img/cloud-vendor-icon/logo-aws.svg";
import LogoVmware from "@commons/assets/img/cloud-vendor-icon/logo-vsphere.png";
import LogoOpenStack from "@commons/assets/img/cloud-vendor-icon/logo-openstack.svg";
import LogoTencent from "@commons/assets/img/cloud-vendor-icon/logo-tencent.svg";

import IconVmware from "@commons/assets/img/cloud-vendor-icon/icon-vsphere.svg";

import type { SimpleMap } from "@commons/api/base/type";
import CeIcon from "@commons/components/ce-icon/index.vue";

interface PlatFormIcon {
  component?: any;
  color?: string;
  icon?: any;
  oldIcon?: any;
  name: string;
  logo?: any;
}

export const platformIcon: SimpleMap<PlatFormIcon> = {
  fit2cloud_ali_platform: {
    component: CeIcon,
    icon: { code: "aliyun" },
    oldIcon: aliyun,
    logo: LogoAliyun, //就你不是svg
    color: "#ff6c06",
    name: "阿里云",
  },
  fit2cloud_huawei_platform: {
    component: CeIcon,
    icon: { code: "huawei" },
    oldIcon: huawei,
    logo: LogoHuawei,
    color: "#f40f0a",
    name: "华为云",
  },
  // fit2cloud_aws_platform: {
  //   component: CeIcon,
  //   icon: { code: "aws" },
  //   oldIcon: aws,
  //   logo: LogoAws,
  //   color: "#e38d2e",
  //   name: "AWS",
  // },
  fit2cloud_vsphere_platform: {
    /*component: ElImage,
    icon: { src: IconVmware, style: { height: "1em", width: "1em" } },*/
    component: CeIcon,
    icon: { src: IconVmware, type: "img" },
    oldIcon: vmware,
    logo: LogoVmware,
    // color: "#6e6e71",
    name: "vmware",
  },
  fit2cloud_tencent_platform: {
    component: CeIcon,
    icon: { code: "tengxunyun" },
    oldIcon: qcloud,
    logo: LogoTencent,
    color: "#139df6",
    name: "腾讯云",
  },
  fit2cloud_openstack_platform: {
    component: CeIcon,
    icon: { code: "icon_openstack" },
    oldIcon: openstack,
    logo: LogoOpenStack,
    color: "#d21f33",
    name: "OpenStack",
  },
};

export enum Platform {
  ALI = "fit2cloud_ali_platform",
  HUAWEI = "fit2cloud_huawei_platform",
  TENCENT = "fit2cloud_tencent_platform",
  VSPHERE = "fit2cloud_vsphere_platform",
  OPENSTACK = "fit2cloud_openstack_platform",
}
