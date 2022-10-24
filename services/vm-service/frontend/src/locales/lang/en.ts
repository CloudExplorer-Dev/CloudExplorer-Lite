const message = {
  vm_cloud_server: {
    label: {
      ip_address: "IP Address",
    },
    btn: {
      power_on: "Power on",
      shutdown: "Shutdown",
      power_off: "Power off",
      reboot: "Reboot",
    },
    message_box: {
      confirm_power_on: "Confirm Start",
      confirm_shutdown: "Confirm Shutdown",
      confirm_power_off: "Confirm power off",
      confirm_reboot: "Confirm restart",
      confirm_delete: "confirm deletion",
      confirm_batch_operate: "Confirm to execute batch {0} operation",
      check_vm_tools_status_confirm_shutdown: "The current virtual machine is not installed with VmTools or VmTools is not running and cannot be soft shut down. If you continue, turn off the power. Do you want to continue?",
    },
  },
  vm_cloud_image: {
    label: {
      management_info: "Management information",
      image_name: "Image name",
      image_id: "Image ID",
    },
    btn: {
      set_management_info: "Set management information",
    },
  },
  vm_cloud_disk: {
    label: {
      vm: "Virtual machine",
      size: "Size",
      disk_category: "Disk category",
      disk_type: "Disk Type",
      mount_info: "Mounting information",
      delete_with_instance: "Delete with Instance",
      select_vm: "Please Select Instance",
      cloudVm: "Cloud Instance",
      deleteWithVm: "Delete With Instance",
      disk_info: "Disk Info",
      disk_name: "Disk Name",
      change_config: "Config Change",
      current_config: "Current Config",
      after_config: "Config after change",
      disk_size: "Disk Size",
    },
    btn: {
      create:"Add Disk",
      enlarge: "Enlarge",
      uninstall: "Uninstall",
      mount: "Mount",
      delete: "Delete",
    },
    confirm: {
      detach: "Confirm to uninstall cloud disk {0} from virtual machine {1}",
      delete: "Confirm to delete cloud disk {0}",
    },
    msg: {
      canceled: "{0}Cancelled",
    },
  },
};
export default {
  ...message,
};
