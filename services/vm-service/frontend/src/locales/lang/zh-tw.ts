const message = {
  vm_cloud_server: {
    label: {
      ip_address: "IP地址",
    },
    btn: {
      power_on: "啟動",
      shutdown: "關機",
      power_off: "關閉電源",
      reboot: "重啓",
    },
    message_box: {
      confirm_power_on: "確認啟動",
      confirm_shutdown: "確認關機",
      confirm_power_off: "確認關閉電源",
      confirm_reboot: "確認重啓",
      confirm_delete: "確認删除",
      confirm_batch_operate: "確認執行批量{0}操作",
    },
  },
  vm_cloud_image: {
    label: {
      management_info: "管理資訊",
      image_name: "鏡像名稱",
      image_id: "鏡像ID",
    },
    btn: {
      set_management_info: "設定管理資訊"
    }
  },
  vm_cloud_disk: {
    label: {
      "vm": "所屬虛擬機器",
      "size": "大小",
      "disk_category": "磁片種類",
      "disk_type": "磁片類型",
      "mount_info": "掛載資訊",
      "delete_with_instance": "隨實例删除"
    },
    btn: {
      uninstall: "卸載",
      mount: "掛載"
    }
  }
};
export default {
  ...message,
};
