<script setup lang="ts">
import { ref, onMounted } from "vue";
import VmCloudServerApi from "@/api/vm_cloud_server";
import type { VmCloudServerVO } from "@/api/vm_cloud_server/type";
import { useRouter } from "vue-router";
import {
  PaginationConfig,
  TableConfig,
  SearchConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
import {ElMessage, ElMessageBox, type MessageBoxData} from "element-plus";
import type { SimpleMap } from "@commons/api/base/type";
import _ from "lodash";
import {InstanceOperateEnum} from "@/api/vm_cloud_server/type";
const { t } = useI18n();
const useRoute = useRouter();
const table = ref<any>(null);
const columns = ref([]);
const tableData = ref<Array<VmCloudServerVO>>();
const selectedRowData = ref<Array<VmCloudServerVO>>();
const tableLoading = ref<boolean>(false);
//状态
const instanceStatus = ref<Array<SimpleMap<string>>>([
  { text: 'Running', value: 'Running' },
  { text: 'Deleted', value: 'Deleted' },
  { text: 'Stopped', value: 'Stopped' },
]);

/**
 * 查询
 * @param condition
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  VmCloudServerApi.listVmCloudServer({
    currentPage: tableConfig.value.paginationConfig.currentPage,
    pageSize: tableConfig.value.paginationConfig.pageSize,
    ...params,
  },tableLoading).then((res) => {
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
 * 选中的数据
 */
const handleSelectionChange = (list: Array<VmCloudServerVO>) => {
  selectedRowData.value = list;
};
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
      { label: "名称", value: "instanceName" },
      { label: "云账号", value: "accountName" },
      { label: "IP地址", value: "ipArray" },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([]),
});
/**
 * 详情
 */
const showDetail = (row: VmCloudServerVO) => {
  useRoute.push({
    path: useRoute.currentRoute.value.path.replace("/list", "/detail"),
    query: { id: row.id },
  });
};
/**
 * 操作按钮
 */
const buttons = ref([
  {
    label: "启动", icon: "", click: (row:VmCloudServerVO) => {
      powerOn(row);
    }, show: true,disabled: (row: { instanceStatus: string; }) => {
      return row.instanceStatus !== "Stopped"
    }
  }, {
    label: "关机", icon: "", click: (row:VmCloudServerVO) => {
      shutdown(row);
    }, show: true,disabled: (row: { instanceStatus: string; }) => {
      return row.instanceStatus !== "Running"
    }
  },
  {
    label: "关闭电源", icon: "", click: (row:VmCloudServerVO) => {
      powerOff(row);
    }, show: true,disabled: (row: { instanceStatus: string; }) => {
      return row.instanceStatus !== "Running"
    }
  },
  {
    label: "重启", icon: "", click: (row:VmCloudServerVO) => {
      reboot(row);
    }, show: true,disabled: (row: { instanceStatus: string; }) => {
      return row.instanceStatus !== "Running"
    }
  },
  {
    label: "删除", icon: "", click: (row:VmCloudServerVO) => {
      deleteInstance(row);
    }, show: true,disabled: (row: { instanceStatus: string; }) => {
      return row.instanceStatus === "Deleted"
    }
  }
]);
//开机
const powerOn = (row:VmCloudServerVO) =>{
  ElMessageBox.confirm(
      "确认启动",
      "提示",
      {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
      }
  ).then(() => {
    VmCloudServerApi.powerOn(row.id as string,tableLoading).then(() => {
    }).catch((err) => {
      console.log(err);
    }).finally(() => {
      table.value?.search();
    });
  });
}
//关机
const shutdown = (row:VmCloudServerVO) =>{
  ElMessageBox.confirm(
      "确认关机",
      "提示",
      {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
      }
  ).then(() => {
    VmCloudServerApi.shutdownInstance(row.id as string,tableLoading).then(() => {
    }).catch((err) => {
      console.log(err);
    }).finally(() => {
      table.value?.search();
    });
  });

}
//关闭电源
const powerOff = (row:VmCloudServerVO) =>{
  ElMessageBox.confirm(
      "确认关闭电源",
      "提示",
      {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
      }
  ).then(() => {
    VmCloudServerApi.powerOff(row.id as string,tableLoading).then(() => {
    }).catch((err) => {
      console.log(err);
    }).finally(() => {
      table.value?.search();
    });
  });

}
//重启
const reboot = (row:VmCloudServerVO) =>{
  ElMessageBox.confirm(
      "确认重启",
      "提示",
      {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
      }
  ).then(() => {
    VmCloudServerApi.reboot(row.id as string,tableLoading).then(() => {
    }).catch((err) => {
      console.log(err);
    }).finally(() => {
      table.value?.search();
    });
  });
}

//删除
const deleteInstance = (row:VmCloudServerVO) =>{
  ElMessageBox.confirm(
      "确认删除",
      "提示",
      {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
      }
  ).then(() => {
    VmCloudServerApi.deleteInstance(row.id as string,tableLoading).then(() => {
    }).catch((err) => {
      console.log(err);
    }).finally(() => {
      table.value?.search();
    });
  });
}

/**
 * 批量操作
 */
const batchOperate = (operate:string) =>{
  if (!(selectedRowData.value && selectedRowData.value.length > 0)) {
    return;
  }
  ElMessageBox.confirm(
      "确认执行批量"+InstanceOperateEnum[operate as any]+"操作",
      "提示",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }
  ).then(() => {
    VmCloudServerApi.batchOperate(_.map(selectedRowData.value, "id"), operate, tableLoading)
        .then(() => {})
        .catch((err) => {
          console.log(err);
        })
        .finally(() => {
          table.value?.search();
        });
  });
}
/**
 * 更多操作
 */
//授权
const authorizeBatch = (arg:any) =>{
  ElMessage.info("等等");
}
//删除
const deleteBatch = (arg:any) =>{
  batchOperate("DELETE");
}
const moreActions = ref([
  { text:"授权",arg:"", fn: authorizeBatch},
  { text:"删除",arg:"", fn: deleteBatch}
])
//触发事件
const handleAction = (actionObj:any) =>{
  const { arg, fn} = actionObj
  if (fn) {
    fn(arg)
  }
}

</script>
<template>
  <ce-table v-loading="tableLoading"
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    @selection-change="handleSelectionChange"
    row-key="id"
    height="100%"
    ref="table"
  >
    <template #toolbar>
      <el-button type="primary">创建</el-button>
      <el-button @click="batchOperate('POWER_ON')">启动</el-button>
      <el-button @click="batchOperate('SHUTDOWN')">关机</el-button>
      <el-button @click="batchOperate('REBOOT')">重启</el-button>
      <el-dropdown @command="handleAction" trigger="click" style="margin-left: 12px;">
        <el-button>
          更多操作<el-icon class="el-icon--right"><arrow-down /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item v-for="(item, index) in moreActions"
                              :key="index"
                              :command="{ arg:item.arg ,fn: item.fn }">{{ item.text }}</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </template>
    <el-table-column type="selection" />
    <el-table-column prop="instanceName" label="名称">
      <template #default="scope">
        <el-tooltip
            class="item"
            effect="dark"
            :content="scope.row.instanceName"
            placement="top"
        >
            <el-link type="primary" @click="showDetail(scope.row)">
              <span class="text-overflow">
              {{ scope.row.instanceName }}
             </span>
            </el-link>
        </el-tooltip>
      </template>
    </el-table-column>
    <el-table-column prop="organizationName" label="组织"></el-table-column>
    <el-table-column prop="workspaceName" label="工作空间"></el-table-column>
    <el-table-column prop="accountName" label="云账号"></el-table-column>
    <el-table-column prop="instanceStatus" column-key="instanceStatus" label="状态" :filters="instanceStatus">
      <template #default="scope">
        <div style="display: flex; align-items: center">
          <span
              :style="{ color: scope.row.instanceStatus === 'Delete' ? 'red' : '' }"
          >{{scope.row.instanceStatus}}</span>
        </div>
      </template>
    </el-table-column>
    <el-table-column prop="instanceTypeDescription" label="实例规格"></el-table-column>
    <el-table-column prop="ipArray" label="IP地址">
      <template #default="scope">
          <span v-show="scope.row.ipArray.length>2">{{ JSON.parse(scope.row.ipArray)[0]}}</span>
          <el-dropdown class="dropdown-box" :hide-on-click="false" v-if="scope.row.ipArray.length>2" max-height="100px">
            <span>
              更多<el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-for="(item,index) in JSON.parse(scope.row.ipArray)" :key="index">{{item}}</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
      </template>
    </el-table-column>
    <el-table-column prop="createUser" label="申请人" :show="false"></el-table-column>
    <el-table-column prop="createTime" label="创建时间"></el-table-column>
    <fu-table-operations :ellipsis="2" :columns="columns" :buttons="buttons" label="操作" fix/>
    <template #buttons>
      <fu-table-column-select type="icon" :columns="columns" size="small" />
    </template>
  </ce-table>
</template>
<style lang="scss" scoped>
.text-overflow {
  max-width: 120px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.dropdown-box{
  margin-left: 10px;
  margin-top: 2px;
}

</style>
