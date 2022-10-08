<template>
  <ce-table
    :columns="columns"
    :data="tableData"
    :tableConfig="tableConfig"
    row-key="id"
    height="100%"
    ref="table"
  >
    <template #toolbar>
      <el-button type="primary">创建</el-button>
      <el-button>启动</el-button>
      <el-button>关机</el-button>
      <el-button>重启</el-button>
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
              <span class="text-overflow" >
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

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { ListVmCloudServer } from "@/api/vm_cloud_server";
import type { VmCloudServerVO } from "@/api/vm_cloud_server";
import { useRouter } from "vue-router";
import {
  PaginationConfig,
  TableConfig,
  SearchConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
import {ElMessage} from "element-plus";
import type { SimpleMap } from "@commons/api/base/type";
const { t } = useI18n();
const useRoute = useRouter();
const columns = ref([]);
const tableData = ref<Array<VmCloudServerVO>>();
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
  ListVmCloudServer({
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
// 用户详情
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
    label: "开机", icon: "", click: (row:VmCloudServerVO) => {
      startVm(row.instanceId);
    }, show: true,disabled: (row: { instanceStatus: string; }) => {
      return row.instanceStatus !== "Stopped"
    }
  }, {
    label: "关机", icon: "", click: (row:VmCloudServerVO) => {
      stopVm(row.instanceId);
    }, show: true,disabled: (row: { instanceStatus: string; }) => {
      return row.instanceStatus !== "Running"
    }
  },
  {
    label: "重启", icon: "", click: (row:VmCloudServerVO) => {
      restartVm(row.instanceId);
    }, show: true,disabled: (row: { instanceStatus: string; }) => {
  return row.instanceStatus === "Deleted"
    }
  }
]);
//关机
const stopVm = (instanceId:any) =>{
  //
  ElMessage.success("关机功能敬请期待！");
}
//开机
const startVm = (instanceId:any) =>{
  //
  ElMessage.success("开机功能敬请期待！");
}
//重启
const restartVm = (instanceId:any) =>{
  //
  ElMessage.success("重启功能敬请期待！");
}
/**
 * 更多操作
 */
//授权
const authorizeBatch = (arg:any) =>{
  debugger;
}
//删除
const deleteBatch = (arg:any) =>{
  debugger;
}
const moreActions = ref([
  { text:"授权",arg:"", fn: authorizeBatch},
  { text:"删除",arg:"", fn: deleteBatch}
])
//触发事件
const handleAction = (actionObj:any) =>{
  const { arg, fn} = actionObj
  debugger;
  if (fn) {
    fn(arg)
  }
}

</script>

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
