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
      check_vm_tools_status_confirm_shutdown: "當前虛擬機器未安裝VmTools或VmTools未運行，無法軟關機，若繼續操作則關閉電源，是否繼續？",
    },
  },
  vm_cloud_image: {
    label: {
      management_info: "管理資訊",
      image_name: "鏡像名稱",
      image_id: "鏡像ID",
    },
    btn: {
      set_management_info: "設定管理資訊",
    },
  },
  vm_cloud_disk: {
    label: {
      vm: "所屬虛擬機器",
      size: "大小",
      disk_category: "磁片種類",
      disk_type: "磁片類型",
      mount_info: "掛載資訊",
      delete_with_instance: "隨實例删除",
      select_vm: "請選擇要掛載的雲主機",
      cloudVm: "雲主機",
      deleteWithVm: "隨雲主機刪除",
      disk_info: "磁盤信息",
      disk_name: "磁盤名稱",
      change_config: "配置變更",
      current_config: "當前配置",
      after_config: "變更後配置",
      disk_size: "磁盤大小",
    },
    btn: {
      create:"添加磁盤",
      enlarge: "擴容",
      uninstall: "卸載",
      mount: "掛載",
      delete: "刪除",
    },
    confirm: {
      detach: "確認將雲磁盤{0}從雲主機{1}上卸載",
      delete: "確認將雲磁盤{0}刪除",
    },
    msg: {
      canceled: "已取消{0}",
    },
  },
};
export default {
  ...message,
};
