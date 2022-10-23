const message = {
  vm_cloud_server: {
    label: {
      ip_address: "IP地址",
    },
    btn: {
      power_on: "启动",
      shutdown: "关机",
      power_off: "关闭电源",
      reboot: "重启",
    },
    message_box: {
      confirm_power_on: "确认启动",
      confirm_shutdown: "确认关机",
      confirm_power_off: "确认关闭电源",
      confirm_reboot: "确认重启",
      confirm_delete: "确认删除",
      confirm_batch_operate: "确认执行批量{0}操作",
      check_vm_tools_status_confirm_shutdown: "当前虚拟机未安装VmTools或VmTools未运行，无法软关机，若继续操作则关闭电源，是否继续？",
    },
  },
  vm_cloud_image: {
    label: {
      management_info: "管理信息",
      image_name: "镜像名称",
      image_id: "镜像ID",
    },
    btn: {
      set_management_info: "设置管理信息",
    },
  },
  vm_cloud_disk: {
    label: {
      vm: "所属虚拟机",
      size: "大小",
      disk_category: "磁盘种类",
      disk_type: "磁盘类型",
      mount_info: "挂载信息",
      delete_with_instance: "随实例删除",
    },
    btn: {
      uninstall: "卸载",
      mount: "挂载",
    },
  },
};
export default {
  ...message,
};
