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
    },
    btn: {
      uninstall: "Uninstall",
      mount: "Mount",
    },
  },
};
export default {
  ...message,
};
