<template>
  <ServerOptimization
    no-title
    no-padding
    v-model:check-id="checkedId"
    checkable
    :show-setting-icon="showSettingIcon"
    @change="selectChange"
    :req="searchParams"
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
      <el-table-column
        :show-overflow-tooltip="true"
        prop="instanceName"
        column-key="instanceName"
        :label="$t('commons.name')"
        fixed
        min-width="120px"
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
        min-width="100"
        prop="optimizeSuggest"
        label="优化建议"
      ></el-table-column>
      <el-table-column
        min-width="150"
        prop="content"
        label="建议原因"
      ></el-table-column>
      <el-table-column
        min-width="150"
        prop="instanceTypeDescription"
        :label="$t('commons.cloud_server.instance_type')"
      ></el-table-column>
      <el-table-column
        align="right"
        min-width="150"
        prop="cpuAverage"
        label="CPU平均使用率"
      >
        <template #default="scope">
          {{
            scope.row.cpuAverage
              ? PercentFormat.format(scope.row.cpuAverage / 100)
              : "-"
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="right"
        min-width="150"
        prop="cpuMaximum"
        label="CPU最大使用率"
        :show="false"
      >
        <template #default="scope">
          {{
            scope.row.cpuMaximum
              ? PercentFormat.format(scope.row.cpuMaximum / 100)
              : "-"
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="right"
        min-width="150"
        prop="memoryAverage"
        label="内存平均使用率"
      >
        <template #default="scope">
          {{
            scope.row.memoryAverage
              ? PercentFormat.format(scope.row.memoryAverage / 100)
              : "-"
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="right"
        min-width="150"
        prop="memoryMaximum"
        label="内存最大使用率"
        :show="false"
      >
        <template #default="scope">
          {{
            scope.row.memoryMaximum
              ? PercentFormat.format(scope.row.memoryMaximum / 100)
              : "-"
          }}
        </template>
      </el-table-column>
      <template #buttons>
        <CeTableColumnSelect :columns="columns" />
      </template>
    </ce-table>
  </div>
</template>

<script setup lang="ts">
import { computed, onBeforeMount, onMounted, ref } from "vue";
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
import type { VmCloudServerVO } from "@/api/server_analysis/type";
import ResourceOptimizationViewApi from "@/api/resource_optimization";
import type { ListOptimizationRequest } from "@commons/api/resource_optimization/type";
import { useRouter } from "vue-router";
import ServerOptimization from "@commons/business/base-layout/home-page/items/operation/ServerOptimization.vue";
import _ from "lodash";
import MicroAppRouterUtil from "@commons/router/MicroAppRouterUtil";
import { useUserStore } from "@commons/stores/modules/user";
import PercentFormat from "@commons/utils/percentFormat";
const userStore = useUserStore();

const optimizeDivRef = ref<InstanceType<typeof ServerOptimization> | null>();

const router = useRouter();

const checkedId = ref(
  _.defaultTo(_.parseInt(router.currentRoute.value.query?.checked as string), 1)
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

function selectChange(id: number) {
  search(table?.value.getTableSearch());
}

const searchParams = computed(() => {
  return table?.value
    ? TableSearch.toSearchParams(table?.value?.getTableSearch())
    : {};
});

/**
 * 查询
 * @param condition
 */
const search = (condition: TableSearch) => {
  const params: ListOptimizationRequest | undefined =
    optimizeDivRef.value?.getCheckedSearchParams(
      checkedId.value,
      TableSearch.toSearchParams(condition)
    );
  //讲云账号查询条件下传到卡片
  if (_.has(TableSearch.toSearchParams(condition), "accountIds")) {
    checkedAccountIds.value = TableSearch.toSearchParams(condition)?.accountIds;
  } else {
    checkedAccountIds.value = [];
  }
  ResourceOptimizationViewApi.listServer(
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
};

/**
 * 页面挂载
 */
onMounted(() => {
  if (!checkedAccountIds.value) {
    search(table?.value.getTableSearch());
  }
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
    bottom: 0px;
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
  max-width: 120px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
//更多IP
.dropdown_box {
  margin-left: 10px;
  margin-top: 2px;
}
</style>
