<template>
  <!--云主机列表，前端分页-->
  <div class="log-table">
    <ce-table
      v-loading="tableLoading"
      :columns="columns"
      :data="filterTableData"
      :tableConfig="tableConfig"
      @selection-change="handleSelectionChange"
      row-key="id"
      height="100%"
      ref="table"
    >
      <template #toolbar>
        <el-row style="width: 100%">
          <el-col :span="12">
            <el-button
              :disabled="batchBtnDisable"
              v-if="ignoreResourceTag === true"
              @click="batchCancelIgnore"
            >
              取消忽略
            </el-button>
            <el-button
              :disabled="batchBtnDisable"
              v-if="ignoreResourceTag === false"
              @click="batchAddIgnore"
            >
              忽略
            </el-button>
          </el-col>
          <el-col :span="11" style="text-align: right">
            <el-radio-group
              class="custom-radio-group"
              v-model="ignoreResourceTag"
              @change="changeIgnoreResourceTag"
            >
              <el-radio-button :label="false">优化资源</el-radio-button>
              <el-radio-button :label="true">已忽略资源</el-radio-button>
            </el-radio-group>
          </el-col>
        </el-row>
      </template>
      <el-table-column type="selection" />
      <el-table-column
        min-width="150"
        :show-overflow-tooltip="true"
        prop="instanceName"
        :label="$t('commons.name')"
      >
        <template #default="scope">
          {{ scope.row.instanceName }}
        </template>
      </el-table-column>
      <el-table-column
        min-width="150"
        prop="accountName"
        column-key="accountIds"
        :label="$t('commons.cloud_account.native')"
        :filters="cloudAccount"
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
            />
            <span style="margin-left: 10px">{{ scope.row.accountName }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        min-width="150"
        prop="instanceTypeDescription"
        :label="$t('commons.cloud_server.instance_type')"
      ></el-table-column>
      <el-table-column min-width="150" prop="ipArray" label="IP地址">
        <template #default="scope">
          <div :data-var="(_array = JSON.parse(scope.row.ipArray))">
            <span v-show="scope.row.ipArray?.length > 2">{{ _array[0] }}</span>
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
                    v-for="(item, index) in _array"
                    :key="index"
                  >
                    {{ item }}
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        :show="false"
        prop="instanceStatus"
        column-key="instanceStatus"
        :label="$t('commons.status')"
      >
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <span
              >{{ InstanceStatusUtils.getStatusName(scope.row.instanceStatus) }}
            </span>
          </div>
        </template>
      </el-table-column>

      <fu-table-operations
        :ellipsis="1"
        :columns="columns"
        :buttons="buttons"
        :label="$t('commons.operation')"
        fixed="right"
      />
    </ce-table>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, computed, watch } from "vue";
import OptimizationStrategyViewApi from "@commons/api/optimize/index";
import {
  PaginationConfig,
  TableConfig,
  TableOperations,
  TableSearch,
} from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
import type { SimpleMap } from "@commons/api/base/type";
import { platformIcon } from "@commons/utils/platform";
import BaseCloudAccountApi from "@commons/api/cloud_account";
import type { VmCloudServerVO } from "@commons/api/optimize/type";
import InstanceStatusUtils from "@commons/utils/vm_cloud_server/InstanceStatusUtils";
import _ from "lodash";
import type { CreateRequest } from "@commons/api/optimize/type";
import { UpdateRequest } from "@commons/api/optimize/type";
const props = withDefaults(
  defineProps<{
    createOptimizationStrategyForm: CreateRequest | UpdateRequest;
  }>(),
  {}
);
const { t } = useI18n();
const table = ref<any>(null);
const columns = ref([]);
/**
 * 表格显示数据
 * 通过参数过滤原始数据（tableData）,并执行分页
 */
const filterTableData = ref<Array<VmCloudServerVO>>([]);
/**
 * 优化资源原始数据
 */
const optimizationTableData = ref<Array<VmCloudServerVO>>([]);
/**
 * 忽略资源原始数据
 */
const ignoreTableData = ref<Array<VmCloudServerVO>>([]);
/**
 * 存储原始数据
 * 根据忽略资源标签，获取不同的原始数据
 */
const tableData = ref<Array<VmCloudServerVO>>([]);

/**
 * 表格选择数据
 */
const selectedRowData = ref<Array<VmCloudServerVO>>([]);
const tableLoading = ref<boolean>(false);
const cloudAccount = ref<Array<SimpleMap<string>>>([]);
/**
 * 修改父组件form数据
 */
const emit = defineEmits(["ignoreServer"]);
/**
 * 忽略资源标签
 */
const ignoreResourceTag = ref<boolean>(false);
/**
 * 回显忽略资源
 */
const echoIgnoreTableData = () => {
  const ignoreResourceIdNumber =
    props.createOptimizationStrategyForm.ignoreResourceIdList?.length;
  if (ignoreResourceIdNumber && ignoreResourceIdNumber > 0) {
    // 如果在优化资源里面存在那么回显，否则保持原样
    const optimizationTableDataHasIgnoreList = _.filter(
      optimizationTableData.value,
      (item) =>
        _.includes(
          props.createOptimizationStrategyForm.ignoreResourceIdList,
          item.id
        )
    );
    if (optimizationTableDataHasIgnoreList?.length > 0) {
      ignoreTableData.value = _.filter(optimizationTableData.value, (item) =>
        _.includes(
          props.createOptimizationStrategyForm.ignoreResourceIdList,
          item.id
        )
      );
      optimizationTableData.value = _.filter(
        optimizationTableData.value,
        (item) =>
          !_.includes(
            props.createOptimizationStrategyForm.ignoreResourceIdList,
            item.id
          )
      );
    }
  }
  tableData.value = optimizationTableData.value;
  tableLoading.value = false;
};
/**
 * 获取优化资源原始数据
 */
const getOptimizationTableData = () => {
  tableLoading.value = true;
  const params = TableSearch.toSearchParams(new TableSearch());
  params.optimizationStrategyId = props.createOptimizationStrategyForm.id;
  params.ignore = false;
  OptimizationStrategyViewApi.getServerResourceList({
    ...params,
    tableLoading,
  }).then((res) => {
    optimizationTableData.value = res.data;
    echoIgnoreTableData();
  });
};

/**
 * 获取忽略资源原始数据
 */
const getIgnoreTableData = () => {
  const params = TableSearch.toSearchParams(new TableSearch());
  params.optimizationStrategyId = props.createOptimizationStrategyForm.id;
  params.ignore = true;
  OptimizationStrategyViewApi.getServerResourceList({
    ...params,
  }).then((res) => {
    ignoreTableData.value = res.data;
  });
};

/**
 * 显示表格数据
 * 表格查询过滤没做
 * @param condition
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  params.optimizationStrategyId = props.createOptimizationStrategyForm.id;
  params.ignore = ignoreResourceTag.value;
  if (tableData.value) {
    filterTableData.value = tableData.value.filter((item) => {
      if (params.instanceName && params.ipArray) {
        return (
          item.instanceName?.includes(params.instanceName) &&
          item.ipArray?.toString().includes(params.ipArray)
        );
      }
      if (params.instanceName || params.ipArray) {
        return (
          item.instanceName?.includes(params.instanceName) ||
          item.ipArray?.toString().includes(params.ipArray)
        );
      }
      return true;
    });
    const startIndex =
      (tableConfig.value.paginationConfig.currentPage - 1) *
      tableConfig.value.paginationConfig.pageSize;
    const endIndex =
      tableConfig.value.paginationConfig.currentPage *
      tableConfig.value.paginationConfig.pageSize;
    tableConfig.value.paginationConfig?.setTotal(
      filterTableData.value.length,
      tableConfig.value.paginationConfig
    );
    tableConfig.value.paginationConfig?.setCurrentPage(
      tableConfig.value.paginationConfig.currentPage,
      tableConfig.value.paginationConfig
    );
    filterTableData.value = filterTableData.value.slice(startIndex, endIndex);
  }
};
/**
 * 监听表格原始数据的变化
 */
watch(tableData, () => {
  search(table?.value.getTableSearch());
});
/**
 * 忽略数据
 */
watch(ignoreTableData, () => {
  emit(
    "ignoreServer",
    _.map(ignoreTableData.value, (r) => r.id)
  );
});

/**
 * 忽略与取消忽略操作
 * 表格原始数据互换
 * 忽略操作时，将优化资源资源加入到忽略资源数据中
 * 取消忽略操作时，将已忽略的资源加入到优化资源数据中
 * 最后触发改变优化资源标签方法
 */
const handelIgnore = (rowIds: Array<string>) => {
  if (ignoreResourceTag.value) {
    optimizationTableData.value = _.unionWith(
      optimizationTableData.value,
      ignoreTableData.value.filter((item) => _.includes(rowIds, item.id))
    );
    ignoreTableData.value = _.filter(
      ignoreTableData.value,
      (item) => !_.includes(rowIds, item.id)
    );
  } else {
    ignoreTableData.value = _.unionWith(
      ignoreTableData.value,
      _.filter(optimizationTableData.value, (item) =>
        _.includes(rowIds, item.id)
      )
    );
    optimizationTableData.value = _.filter(
      optimizationTableData.value,
      (item) => !_.includes(rowIds, item.id)
    );
  }
  changeIgnoreResourceTag();
};
/**
 * 设置显示表格原始数据
 */
const changeIgnoreResourceTag = () => {
  if (ignoreResourceTag.value) {
    tableData.value = ignoreTableData.value;
  } else {
    tableData.value = optimizationTableData.value;
  }
};

/**
 * 页面挂载
 */
onMounted(() => {
  getOptimizationTableData();
  getIgnoreTableData();
  searchCloudAccount();
});

const searchCloudAccount = () => {
  BaseCloudAccountApi.listAll().then((result) => {
    if (result.data.length > 0) {
      result.data.forEach(function (v) {
        const ca = { text: v.name, value: v.id };
        cloudAccount.value.push(ca);
      });
    }
  });
};
/**
 * 选中的数据
 */
const handleSelectionChange = (list: Array<VmCloudServerVO>) => {
  selectedRowData.value = list;
};

/**
 * 启用禁用批量操作按钮
 */
const batchBtnDisable = computed(() => {
  return !(selectedRowData.value && selectedRowData.value.length > 0);
});
/**
 * 批量取消忽略资源
 */
const batchCancelIgnore = () => {
  if (!(selectedRowData.value && selectedRowData.value.length > 0)) {
    return;
  }
  handelIgnore(_.map(selectedRowData.value, "id"));
};
/**
 * 批量忽略资源
 */
const batchAddIgnore = () => {
  if (!(selectedRowData.value && selectedRowData.value.length > 0)) {
    return;
  }
  handelIgnore(_.map(selectedRowData.value, "id"));
};
/**
 * 忽略资源
 * @param row
 */
const addIgnore = (row: VmCloudServerVO) => {
  handelIgnore([row.id]);
};
/**
 * 取消忽略
 * @param row
 */
const cancelIgnore = (row: VmCloudServerVO) => {
  handelIgnore([row.id]);
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
const buttons = ref([
  {
    label: "忽略",
    icon: "",
    click: (row: VmCloudServerVO) => {
      addIgnore(row);
    },
    show: (row: { instanceStatus: string }) => {
      return !ignoreResourceTag.value;
    },
  },
  {
    label: "取消忽略",
    icon: "",
    click: (row: VmCloudServerVO) => {
      cancelIgnore(row);
    },
    show: (row: { instanceStatus: string }) => {
      return ignoreResourceTag.value;
    },
  },
]);
</script>

<style lang="scss" scoped>
.log-table {
  width: 100%;
  height: calc(100vh - 270px);
}
.dropdown_box {
  margin-left: 10px;
  margin-top: 2px;
}
</style>
