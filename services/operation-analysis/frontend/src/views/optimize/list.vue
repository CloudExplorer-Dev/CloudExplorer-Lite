<template>
  <ServerOptimization
    no-title
    :no-padding="true"
    v-model:check-id="checkedId"
    checkable
    :show-setting-icon="showSettingIcon"
    @change="selectChange"
    :table-search-params="tableSearchParams"
    ref="optimizeDivRef"
    :table-loading="tableLoading"
    :cloud-account-ids="checkedAccountIds"
  />

  <div class="log-table">
    <ce-table
      localKey="resourceOptimizationTable"
      v-loading="tableLoading"
      :columns="columns"
      :data="tableData"
      :tableConfig="tableConfig"
      row-key="id"
      height="100%"
      ref="table"
    >
      <template #toolbar>
        <el-row justify="space-around" style="width: 100%">
          <el-col :span="12">
            <span>资源明细</span>
          </el-col>
          <el-col :span="10" style="text-align: right">
            <el-radio-group
              class="custom-radio-group"
              v-model="ignoreResourceTag"
              @change="changeIgnoreResourceTag"
            >
              <el-radio-button :label="false">优化资源</el-radio-button>
              <el-radio-button :label="true" v-if="showIgnoreResourceTag">已忽略资源</el-radio-button>
            </el-radio-group>
          </el-col>
        </el-row>
      </template>
      <el-table-column
        :show-overflow-tooltip="true"
        prop="instanceName"
        column-key="instanceName"
        :label="$t('commons.name')"
        fixed
        min-width="200px"
      >
        <template #default="scope">
          <span @click="showDetail(scope.row)" class="name-span-class">
            {{ scope.row.instanceName }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        min-width="150px"
        prop="accountName"
        column-key="accountIds"
        :label="$t('commons.cloud_account.native')"
        :filters="cloudAccount"
        :filtered-value="checkedAccountIds"
      >
        <template #default="scope">
          <div style="display: flex">
            <component
              style="margin-top: 3px; width: 16px; height: 16px"
              :is="platformIcon[scope.row.platform]?.component"
              v-bind="platformIcon[scope.row.platform]?.icon"
              :color="platformIcon[scope.row.platform]?.color"
              size="16px"
              v-if="scope.row.platform"
            ></component>
            <span style="margin-left: 10px">{{ scope.row.accountName }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="ipArray"
        column-key="ipArray"
        label="IP地址"
        min-width="180px"
      >
        <template #default="scope">
          <span v-show="scope.row.ipArray?.length > 2">{{
            JSON.parse(scope.row.ipArray)[0]
          }}</span>
          <el-dropdown
            class="dropdown_box"
            :hide-on-click="false"
            v-if="scope.row.ipArray.length > 2"
            max-height="100px"
          >
            <span>
              {{ t("commons.cloud_server.more", "更多")
              }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item
                  v-for="(item, index) in JSON.parse(scope.row.ipArray)"
                  :key="index"
                  >{{ item }}</el-dropdown-item
                >
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </el-table-column>
      <el-table-column
        min-width="150"
        prop="instanceTypeDescription"
        :label="$t('commons.cloud_server.instance_type')"
      ></el-table-column>
      <el-table-column
        min-width="150"
        prop="content"
        column-key="content"
        label="建议原因"
        show-overflow-tooltip
      >
        <template #default="scope">
          <span class="table_overflow">
            {{ scope.row.content ? scope.row.content : "N/A" }}</span
          >
        </template>
      </el-table-column>
      <el-table-column
        min-width="150"
        prop="cpuMonitoringValue.avgValue"
        label="CPU平均使用率"
      >
        <template #default="scope">
          {{
            scope.row.cpuMonitoringValue?.avgValue
              ? PercentFormat.format(
                  scope.row.cpuMonitoringValue.avgValue / 100
                )
              : "-"
          }}
        </template>
      </el-table-column>
      <el-table-column
        min-width="150"
        prop="cpuMonitoringValue.maxValue"
        label="CPU最大使用率"
        :show="monitoringStrategy"
      >
        <template #default="scope">
          {{
            scope.row.cpuMonitoringValue?.maxValue
              ? PercentFormat.format(
                  scope.row.cpuMonitoringValue.maxValue / 100
                )
              : "-"
          }}
        </template>
      </el-table-column>
      <el-table-column
        min-width="150"
        prop="memoryMonitoringValue.avgValue"
        label="内存平均使用率"
      >
        <template #default="scope">
          {{
            scope.row.memoryMonitoringValue?.avgValue
              ? PercentFormat.format(
                  scope.row.memoryMonitoringValue.avgValue / 100
                )
              : "-"
          }}
        </template>
      </el-table-column>
      <el-table-column
        min-width="150"
        prop="memoryMonitoringValue.maxValue"
        label="内存最大使用率"
        :show="false"
      >
        <template #default="scope">
          {{
            scope.row.memoryMonitoringValue?.maxValue
              ? PercentFormat.format(
                  scope.row.memoryMonitoringValue.maxValue / 100
                )
              : "-"
          }}
        </template>
      </el-table-column>
      <fu-table-operations
        :ellipsis="1"
        :columns="columns"
        :buttons="buttons"
        :label="$t('commons.operation')"
        fixed="right"
      />
      <template #buttons>
        <!-- 导出 -->
        <el-button @click="exportData('xlsx')" style="width: 32px;">
          <ce-icon size="16" code="icon_bottom-align_outlined"></ce-icon>
        </el-button>

        <CeTableColumnSelect :columns="columns" />
      </template>
    </ce-table>
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onMounted, ref, watch } from "vue";
import {
  PaginationConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
import type { SimpleMap } from "@commons/api/base/type";
import { platformIcon } from "@commons/utils/platform";
import CommonApi from "@/api/common/index";
import OptimizationStrategyViewApi from "@commons/api/optimize";
import type {
  PageOptimizationStrategyResourceRequest,
  VmCloudServerVO,
} from "@commons/api/optimize/type";
import { useRouter } from "vue-router";
import ServerOptimization from "@commons/business/base-layout/home-page/items/operation/optimize/ServerOptimization.vue";
import _ from "lodash";
import MicroAppRouterUtil from "@commons/router/MicroAppRouterUtil";
import { useUserStore } from "@commons/stores/modules/user";
import PercentFormat from "@commons/utils/percentFormat";
import { ElMessage, ElMessageBox } from "element-plus";
import { usePermissionStore } from "@commons/stores/modules/permission";
const userStore = useUserStore();
const permissionStore = usePermissionStore();
const optimizeDivRef = ref<InstanceType<typeof ServerOptimization> | null>();
/**
 * 忽略资源标签
 */
const ignoreResourceTag = ref<boolean>(false);

const router = useRouter();

const checkedId = ref(
  _.defaultTo(router.currentRoute.value.query?.checked as string, null)
);

const checkedAccountIds = ref(
  router.currentRoute.value.query?.accountIds
    ? JSON.parse(
        decodeURI(router.currentRoute.value.query?.accountIds as string)
      )
    : undefined
);

const { t } = useI18n();
const table = ref<any>(null);
const columns = ref([]);
const tableData = ref<Array<VmCloudServerVO>>([]);
const tableLoading = ref<boolean>(false);
const cloudAccount = ref<Array<SimpleMap<string>>>([]);

function selectChange() {
  search(table?.value.getTableSearch());
}

const tableSearchParams = computed(() => {
  return table?.value
    ? TableSearch.toSearchParams(table?.value?.getTableSearch())
    : {};
});

/**
 * 查询
 * @param condition
 */
const search = (condition: TableSearch) => {
  const tableParams = TableSearch.toSearchParams(condition);
  tableParams.ignore = ignoreResourceTag.value;
  const params: PageOptimizationStrategyResourceRequest =
    optimizeDivRef.value?.getCheckedSearchParams(checkedId.value, tableParams);
  //云账号查询条件下传到卡片
  if (_.has(TableSearch.toSearchParams(condition), "accountIds")) {
    checkedAccountIds.value = TableSearch.toSearchParams(condition)?.accountIds;
  } else {
    _.set(params, "accountIds", []);
    checkedAccountIds.value = [];
  }
  if (checkedId.value) {
    OptimizationStrategyViewApi.pageOptimizationStrategyServerResourceList(
      {
        ...params,
        currentPage: tableConfig.value.paginationConfig.currentPage,
        pageSize: tableConfig.value.paginationConfig.pageSize,
      },
      tableLoading
    ).then((res) => {
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
  }
};

/**
 * 页面挂载
 */
onMounted(() => {
  search(new TableSearch());
  searchCloudAccount();
});

const searchCloudAccount = () => {
  CommonApi.listAll().then((result) => {
    if (result.data.length > 0) {
      result.data.forEach(function (v) {
        const ca = { text: v.name, value: v.id };
        cloudAccount.value.push(ca);
      });
      if (checkedAccountIds.value) {
        table?.value?.filterChange({
          accountIds: checkedAccountIds.value,
        });
      }
    }
  });
};

/**
 * 表单配置
 */
const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    // 查询函数
    search: search,
    quickPlaceholder: t("commons.btn.search", "查找"),
    components: [],
    searchOptions: [
      {
        label: t("commons.name", "名称"),
        value: "instanceName",
      },
      {
        label: t("vm_cloud_server.label.ip_address", "IP地址"),
        value: "ipArray",
      },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: new TableOperations([]),
});
/**
 * 详情
 */
const showDetail = (row: VmCloudServerVO) => {
  MicroAppRouterUtil.jumpToChildrenPath(
    "vm-service",
    "/vm-service/vm_cloud_server/detail/" + row.id,
    router
  );
};
const needRoles = ref<Array<string>>(["ADMIN"]);
const showSettingIcon = computed<boolean>(() =>
  _.includes(needRoles.value, userStore.currentRole)
);

/**
 */
const changeIgnoreResourceTag = () => {
  search(table?.value.getTableSearch());
};
const showIgnoreResourceTag = computed(()=>{
  return permissionStore.hasPermission(
      "[operation-analysis]OPTIMIZATION_STRATEGY_IGNORE_RESOURCE:READ"
  )
})
/**
 * 导出
 */
const exportData = (version: string) => {
  const condition = table?.value.getTableSearch();
  const tableParams = TableSearch.toSearchParams(condition);
  tableParams.ignore = ignoreResourceTag.value;
  const params: PageOptimizationStrategyResourceRequest =
    optimizeDivRef.value?.getCheckedSearchParams(checkedId.value, tableParams);
  _.set(params, "version", version);
  //云账号查询条件下传到卡片
  if (_.has(TableSearch.toSearchParams(condition), "accountIds")) {
    checkedAccountIds.value = TableSearch.toSearchParams(condition)?.accountIds;
  } else {
    _.set(params, "accountIds", []);
    checkedAccountIds.value = [];
  }
  if (checkedId.value) {
    OptimizationStrategyViewApi.exportServerData(
      "云主机优化.xlsx",
      {
        ...params,
        currentPage: tableConfig.value.paginationConfig.currentPage,
        pageSize: tableConfig.value.paginationConfig.pageSize,
      },
      tableLoading
    );
  }
};

const monitoringStrategy = computed<boolean>(() => {
  return false;
});

const addIgnore = (row: VmCloudServerVO) => {
  ElMessageBox.confirm(
    "忽略后，该资源将不再参与当前优化策略，是否继续忽略？",
    t("commons.message_box.prompt", "提示"),
    {
      confirmButtonText: t("commons.message_box.confirm", "确认"),
      cancelButtonText: t("commons.btn.cancel", "取消"),
      type: "warning",
    }
  ).then(() => {
    if (checkedId.value) {
      OptimizationStrategyViewApi.addIgnore(
        checkedId.value,
        { optimizationStrategyId: checkedId.value, resourceIdList: [row.id] },
        tableLoading
      )
        .then(() => {
          search(table?.value.getTableSearch());
          optimizeDivRef.value?.changeCard();
          ElMessage.success(t("commons.msg.op_success"));
        })
        .catch((err) => {
          ElMessage.error(err.response.data.message);
        });
    }
  });
};
const cancelIgnore = (row: VmCloudServerVO) => {
  ElMessageBox.confirm(
    "该资源将继续参与当前的优化策略，是否继续？",
    t("commons.message_box.prompt", "提示"),
    {
      confirmButtonText: t("commons.message_box.confirm", "确认"),
      cancelButtonText: t("commons.btn.cancel", "取消"),
      type: "warning",
    }
  ).then(() => {
    if (checkedId.value) {
      OptimizationStrategyViewApi.cancelIgnore(
        checkedId.value,
        { optimizationStrategyId: checkedId.value, resourceIdList: [row.id] },
        tableLoading
      )
        .then(() => {
          search(table?.value.getTableSearch());
          optimizeDivRef.value?.changeCard();
          ElMessage.success(t("commons.msg.op_success"));
        })
        .catch((err) => {
          ElMessage.error(err.response.data.message);
        });
    }
  });
};
const buttons = ref([
  {
    label: "忽略",
    icon: "",
    click: (row: VmCloudServerVO) => {
      addIgnore(row);
    },
    show: (row: { instanceStatus: string }) => {
      return (
        !ignoreResourceTag.value &&
        permissionStore.hasPermission(
          "[operation-analysis]OPTIMIZATION_STRATEGY_IGNORE_RESOURCE:ADD"
        )
      );
    },
  },
  {
    label: "取消忽略",
    icon: "",
    click: (row: VmCloudServerVO) => {
      cancelIgnore(row);
    },
    show: (row: { instanceStatus: string }) => {
      return (
        ignoreResourceTag.value &&
        permissionStore.hasPermission(
          "[operation-analysis]OPTIMIZATION_STRATEGY_IGNORE_RESOURCE:CANCEL"
        )
      );
    },
  },
]);
</script>

<style scoped lang="scss">
.header {
  min-width: 900px;
}
.log-table {
  min-width: 900px;
  padding-top: 26px;
  width: 100%;
  height: calc(100vh - 100px);
}
.boxCenter {
  height: 150px;
  overflow: hidden;
  position: relative;
  //按钮
  button {
    position: absolute;
    display: inline-block;
    color: #f7ba2a;
    width: 100%;
    bottom: 5px;
    right: 0;
    text-align: center;
  }
  //角标主要样式
  .AngleOfTheBox {
    position: absolute;
    padding: 0 5px;
    display: flex;
    align-items: center;
    width: 15%;
    height: 26px;
    color: #fff;
    background-color: #ff9899;
    //文字
    span {
      position: absolute;
      display: inline-block;
      color: #fff;
      width: 100%;
      bottom: 5px;
      left: 0;
      text-align: center;
    }
  }
  .AngleOfTheBox::after {
    content: "";
    position: absolute;
    left: 100%;
    top: 0;
    border-color: transparent transparent transparent #ff9899;
    border-width: 13px 0 13px 13px;
    border-style: solid;
  }
  .BtnOfTheBox {
    cursor: pointer;
    position: absolute;
    padding: 0 5px;
    display: flex;
    align-items: center;
    width: 20px;
    height: 26px;
    right: 0;
    z-index: 1000;
  }
  .CenterTheBox {
    border-radius: 1px;
    width: 100%;
    height: 80%;
    position: absolute;
    top: 30px;
    text-align: center;
    line-height: 65px;
  }
  .BottomTheBox {
    border-radius: 1px;
    width: 100%;
    height: 40px;
    position: absolute;
    bottom: 0;
    text-align: center;
    line-height: 40px;
  }
}

.el-dialog__body {
  display: flex;

  justify-content: center;

  align-content: center;
}
.name-span-class {
  color: var(--el-color-primary);
}
.name-span-class:hover {
  cursor: pointer;
}
.highlight {
  color: var(--el-color-primary);
}
//文字长度限制
.text_overflow {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
//更多IP
.dropdown_box {
  margin-left: 10px;
  margin-top: 2px;
}
.table_overflow {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
