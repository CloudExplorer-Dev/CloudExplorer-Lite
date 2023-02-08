const message = {
  vm_cloud_server: {
    label: {
      ip_address: "IP Address",
      info: "Host information",
      vm: "Cloud server",
      current_config: "Current config",
      instance_type: "Instance type",
      new_config: "New config",
      region: "Region/Data Center",
      zone: "Zone/Cluster",
      vm_tools_status: "VMTools Status",
      vpc: "VPC",
      subnet: "Subnet",
      securityGroup: "Security Group",
      expired_time: "Expired Time",
      charge_type: "Charge Type",
      project: "Project",
    },
    vm_tools_status: {
      running: "Running",
      not_running: "Not Running",
    },
    status: {
      creating: "Creating",
      running: "Running",
      stopped: "Stopped",
      rebooting: "Rebooting",
      wait_recycle: "Wait to Recycle",
      deleted: "Deleted",
    },
    btn: {
      power_on: "Power on",
      shutdown: "Shutdown",
      power_off: "Power off",
      reboot: "Reboot",
      change_config: "Change config ",
    },
    message_box: {
      confirm_power_on: "Confirm Start",
      confirm_shutdown: "Confirm Shutdown",
      confirm_power_off: "Confirm power off",
      confirm_reboot: "Confirm restart",
      confirm_delete: "confirm deletion",
      confirm_batch_operate: "Confirm to execute batch {0} operation",
      check_vm_tools_status_confirm_shutdown:
        "The current virtual machine is not installed with VmTools or VmTools is not running and cannot be soft shut down. If you continue, turn off the power. Do you want to continue?",
      confirm_config_update:
        "The configuration change will shut down the instance.Confirm to continue?",
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
      size: "Disk Size",
      disk_category: "Disk category",
      disk_attribute: "Disk attribute",
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
      system_disk: "System Disk",
      data_disk: "Data Disk",
      disk_id: "Disk ID",
    },
    status: {
      creating: "Creating",
      in_use: "In Use",
      available: "Available",
      enlarging: "Enlarging",
      wait_recycle: "Wait to Recycle",
      deleted: "Deleted",
    },
    btn: {
      create: "Add Disk",
      enlarge: "Enlarge",
      uninstall: "Uninstall",
      mount: "Mount",
      delete: "Delete",
    },
    confirm: {
      detach: "Confirm to uninstall cloud disk {0} from virtual machine {1}",
      delete:
        "The  recycle bin is not opened. Confirm to delete cloud disk {0} directly",
      recycle:
        "The current recycle bin is open. Confirm to put cloud disk {0} into the recycle bin",
      batch_detach: "Confirm to uninstall cloud disk in batch",
      batch_delete:
        "The recycle bin is not opened. Confirm to delete cloud disks in batches",
      batch_recycle:
        "The recycle bin is open. Confirm to put cloud disks into the recycle bin in batches",
    },
    msg: {
      canceled: "{0}Cancelled",
      select_one: "At least to select one record",
      datastore_null: "Datastore can not be null",
    },
  },
  recycle_bin: {
    batch_recover_tips: "Do you want to restore the selected resources?",
    batch_delete_tips:
      "Do you want to delete the selected resource completely? After deletion, it cannot be recovered!",
    recover_tips: "Do you want to recover the resource [{0}]?",
    delete_tips:
      "Do you want to delete [{0}] completely? After deletion, it cannot be recovered!",
    recover: "recover",
    delete: "Delete completely",
    resource_name: "Resource Name",
    resource_type: "resource type",
    workspace_name: "Workspace",
    resource_status: "Status",
    relate_resource: "Associated resource",
    resource_config: "Resource configuration",
    user_name: "Operator",
    put_into_recycle_bin_time: "Put in the recycle bin time",
    create_time: "Creation time",
    organization_name: "Organization",
    account_name: "Cloud Account",
    ip_array: "IP address",
  },
};
export default {
  ...message,
};
