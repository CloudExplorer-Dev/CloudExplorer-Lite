<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import RecycleBinsApi from "@/api/recycle_bin";
import _ from "lodash";
import type { RecycleBinInfo } from "@/api/recycle_bin/type";
import { ResourceTypeConst } from "@commons/utils/constants";
import {
  PaginationConfig,
  TableConfig,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
import ButtonToolBar from "@commons/components/button-tool-bar/ButtonToolBar.vue";
import { ButtonAction } from "@commons/components/button-tool-bar/type";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { ElMessage, ElMessageBox } from "element-plus";
const permissionStore = usePermissionStore();

const { t } = useI18n();
const columns = ref([]);
const tableData = ref<Array<RecycleBinInfo>>();
const table = ref();
const selectedRowData = ref<Array<RecycleBinInfo>>([]);

/**
 * 查询
 * @param condition
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  RecycleBinsApi.listRecycleBins({
    currentPage: tableConfig.value.paginationConfig.currentPage,
    pageSize: tableConfig.value.paginationConfig.pageSize,
    ...params,
  }).then((res) => {
    tableData.value = res.data.records;
    tableConfig.value.paginationConfig?.setTotal(
      res.data.total,
      tableConfig.value.paginationConfig
    );
    tableConfig.value.paginationConfig?.setCurrentPage(
      res.data.current,
      tableConfig.value.paginationConfig
    );
  });
};

/**
 * 页面挂载
 */
onMounted(() => {
  search(new TableSearch());
});
/**
 * 表单配置
 */
const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    // 查询函数
    search: search,
    quickPlaceholder: t("commons.btn.search"),
    components: [],
    searchOptions: [
      { label: t("recycle_bin.resource_name"), value: "resourceName" },
    ],
  },
  paginationConfig: new PaginationConfig(),
});
/**
 * 批量恢复操作
 */
const batchRecoverOperate = (operate: string) => {
  if (!(selectedRowData.value && selectedRowData.value.length > 0)) {
    return;
  }
  const message = t("recycle_bin.batch_recover_tips");
  ElMessageBox.confirm(message, t("commons.message_box.prompt", "提示"), {
    confirmButtonText: t("commons.message_box.confirm", "确认"),
    cancelButtonText: t("commons.btn.cancel", "取消"),
    type: "warning",
  }).then(() => {
    RecycleBinsApi.batchRecoverResource(_.map(selectedRowData.value, "id"))
      .then(() => {
        ElMessage.success(t("commons.msg.op_success"));
        refresh();
      })
      .catch((err) => {
        console.log(err);
      });
  });
};

/**
 * 批量彻底删除操作
 */
const batchDeleteOperate = () => {
  if (!(selectedRowData.value && selectedRowData.value.length > 0)) {
    return;
  }
  const message = t("recycle_bin.batch_delete_tips");
  ElMessageBox.confirm(message, t("commons.message_box.prompt", "提示"), {
    confirmButtonText: t("commons.message_box.confirm", "确认"),
    cancelButtonText: t("commons.btn.cancel", "取消"),
    type: "warning",
  }).then(() => {
    RecycleBinsApi.batchDeleteResource(_.map(selectedRowData.value, "id"))
      .then(() => {
        ElMessage.success(t("commons.msg.op_success"));
        refresh();
      })
      .catch((err) => {
        console.log(err);
      });
  });
};

/**
 * 恢复操作
 */
const recoverOperate = (row: RecycleBinInfo) => {
  const message = t("recycle_bin.recover_tips", [row.resourceName]);
  ElMessageBox.confirm(message, t("commons.message_box.prompt", "提示"), {
    confirmButtonText: t("commons.message_box.confirm", "确认"),
    cancelButtonText: t("commons.btn.cancel", "取消"),
    type: "warning",
  }).then(() => {
    RecycleBinsApi.recoverResource(row.id)
      .then(() => {
        ElMessage.success(t("commons.msg.op_success"));
        refresh();
      })
      .catch((err) => {
        console.log(err);
      });
  });
};

/**
 * 彻底删除操作
 */
const deleteOperate = (row: RecycleBinInfo) => {
  const message = t("recycle_bin.delete_tips", [row.resourceName]);
  ElMessageBox.confirm(message, t("commons.message_box.prompt", "提示"), {
    confirmButtonText: t("commons.message_box.confirm", "确认"),
    cancelButtonText: t("commons.btn.cancel", "取消"),
    type: "warning",
  }).then(() => {
    RecycleBinsApi.deleteResource(row.id)
      .then(() => {
        ElMessage.success(t("commons.msg.op_success"));
        refresh();
      })
      .catch((err) => {
        console.log(err);
      });
  });
};

const disableBatch = computed<boolean>(() => {
  return (
    selectedRowData.value.length > 0 &&
    selectedRowData.value.some(
      (row) => row.relateResource !== "-" && row.resourceType == "DISK"
    )
  );
});

const moreActions = ref<Array<ButtonAction>>([
  new ButtonAction(
    t("recycle_bin.recover"),
    "primary",
    undefined,
    batchRecoverOperate,
    permissionStore.hasPermission("[vm-service]RECYCLE_BIN:RECOVER"),
    disableBatch
  ),
  new ButtonAction(
    t("recycle_bin.delete"),
    undefined,
    "POWER_ON",
    batchDeleteOperate,
    permissionStore.hasPermission("[vm-service]RECYCLE_BIN:DELETE"),
    disableBatch
  ),
]);

/**
 * 操作按钮
 */
const buttons = ref([
  {
    label: t("recycle_bin.recover"),
    icon: "",
    click: (row: RecycleBinInfo) => {
      recoverOperate(row);
    },
    show: permissionStore.hasPermission("[vm-service]RECYCLE_BIN:RECOVER"),
    disabled: (row: { relateResource: string; resourceType: string }) => {
      return row.relateResource !== "-" && row.resourceType == "DISK";
    },
  },
  {
    label: t("recycle_bin.delete"),
    icon: "",
    click: (row: RecycleBinInfo) => {
      deleteOperate(row);
    },
    show: permissionStore.hasPermission("[vm-service]RECYCLE_BIN:DELETE"),
    disabled: (row: { relateResource: string; resourceType: string }) => {
      return row.relateResource !== "-" && row.resourceType == "DISK";
    },
  },
]);

/**
 * 选中的数据
 */
const handleSelectionChange = (list: Array<RecycleBinInfo>) => {
  selectedRowData.value = list;
};

// 刷新列表
const refresh = () => {
  table.value.search();
};
</script>
<template>
  <ce-table
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    @selection-change="handleSelectionChange"
    row-key="id"
    height="100%"
    ref="table"
  >
    <template #toolbar>
      <ButtonToolBar :actions="moreActions" :ellipsis="4" />
    </template>
    <el-table-column type="selection" />
    <el-table-column
      prop="resourceName"
      column-key="resourceName"
      :label="t('recycle_bin.resource_name')"
    >
    </el-table-column>
    <el-table-column
      prop="resourceType"
      column-key="resourceType"
      :label="t('recycle_bin.resource_type')"
    >
      <template #default="scope">
        {{
          _.get(
            ResourceTypeConst,
            scope.row.resourceType,
            scope.row.resourceType
          )
        }}
      </template>
    </el-table-column>
    <el-table-column
      :show="true"
      prop="deleteWithInstance"
      column-key="deleteWithInstance"
      :label="$t('vm_cloud_disk.label.delete_with_instance')"
    >
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span v-if="scope.row.deleteWithInstance === 'YES'">{{
            t("commons.btn.yes")
          }}</span>
          <span v-else-if="scope.row.deleteWithInstance === 'NO'">{{
            t("commons.btn.no")
          }}</span>
          <span v-else>{{ scope.row.deleteWithInstance }}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="relateResource"
      column-key="relateResource"
      :label="t('recycle_bin.relate_resource')"
    >
      <template #default="scope">
        <el-tooltip
          class="item"
          effect="dark"
          :content="scope.row.relateResource"
          placement="top"
        >
          <p class="text-overflow">
            {{ scope.row.relateResource }}
          </p>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column
      prop="organizationName"
      column-key="organizationName"
      :label="t('recycle_bin.organization_name')"
    >
    </el-table-column>
    <el-table-column
      prop="workspaceName"
      column-key="workspaceName"
      :label="t('recycle_bin.workspace_name')"
    >
    </el-table-column>
    <el-table-column
      prop="accountName"
      column-key="accountName"
      :label="t('recycle_bin.account_name')"
    >
    </el-table-column>
    <el-table-column
      prop="resourceStatus"
      column-key="resourceStatus"
      label="状态"
    >
    </el-table-column>

    <el-table-column
      prop="resourceConfig"
      column-key="resourceConfig"
      :label="t('recycle_bin.resource_config')"
    >
    </el-table-column>
    <el-table-column
      prop="ipArray"
      column-key="ipArray"
      :label="t('recycle_bin.ip_array')"
    >
    </el-table-column>
    <el-table-column
      prop="userName"
      column-key="userName"
      :label="t('recycle_bin.user_name')"
    ></el-table-column>
    <el-table-column
      prop="createTime"
      :label="t('recycle_bin.put_into_recycle_bin_time')"
    ></el-table-column>
    <el-table-column
      prop="resourceCreateTime"
      :label="t('recycle_bin.create_time')"
    ></el-table-column>
    <fu-table-operations
      :buttons="buttons"
      :label="$t('commons.operation')"
      fix
    />
  </ce-table>
</template>
<style lang="scss" scoped>
.text-overflow {
  max-width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.name-span-class {
  color: var(--el-color-primary);
}
.name-span-class:hover {
  cursor: pointer;
}
.text-overflow {
  max-width: 100px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
