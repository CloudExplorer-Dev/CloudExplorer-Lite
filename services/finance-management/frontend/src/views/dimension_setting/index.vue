<template>
  <layout-content :style="{ '--ce-main-top-height': topHeight + 'px' }">
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <template #top>
      <el-alert
        style="--el-alert-bg-color: rgba(51, 112, 255, 0.15)"
        title="各云账号的账单费用按照分账规则分摊到云管中的组织/工作空间上。"
        type="info"
        show-icon
        @close="topHeight = 0"
      />
    </template>
    <div
      class="contentWrapper"
      style="--el-tree-node-hover-bg-color: rgba(51, 112, 255, 0.1)"
    >
      <div class="content">
        <div class="left_content">
          <ce-tree
            v-model="activeTreeNode"
            :reset-data="
              (tree) => [
                { id: 'NOT_AUTH', name: '未分账资源', type: 'NOT_AUTH' },
                ...tree,
              ]
            "
          >
            <template #default="treeNode">
              <div>
                <ce-icon
                  type="code"
                  :code="`${
                    treeNode.data.type === 'ORGANIZATION'
                      ? 'zuzhijiagou1'
                      : treeNode.data.type === 'WORKSPACE'
                      ? 'project_space'
                      : 'icon_bill_allocation'
                  }`"
                  size="10px"
                />
                <span style="margin-left: 8px">{{ treeNode.node.label }}</span>
              </div>
            </template>
          </ce-tree>
        </div>
        <div class="table_content">
          <div class="content">
            <div class="share_resource">
              <ce-table
                localKey="shareResourceTable"
                v-loading="resourceLoading"
                cell-class-name="table_cell"
                height="100%"
                ref="table"
                :columns="columns"
                :data="dataList"
                :tableConfig="tableConfig"
                row-key="id"
                :empty-text="
                  activeTreeNode.type === 'NOT_AUTH'
                    ? '暂无数据'
                    : '暂无数据，请先去设置 分账规则'
                "
              >
                <template #toolbar>
                  <div class="title">
                    <span v-if="activeTreeNode.type !== 'NOT_AUTH'"
                      >已分配资源 &nbsp;&nbsp;<span
                        >({{ activeTreeNode.name }})</span
                      ></span
                    >
                    <span v-else>{{ activeTreeNode.name }}</span>
                  </div>
                </template>
                <template #buttons v-if="activeTreeNode.type !== 'NOT_AUTH'">
                  <el-button @click="openSplitBillSetting" type="primary"
                    >分账规则</el-button
                  >
                </template>
                <el-table-column
                  prop="resourceName"
                  label="资源ID/名称"
                  show-overflow-tooltip
                  min-width="100"
                >
                  <template #default="scope">
                    {{ scope.row.resourceId }} /{{ scope.row.resourceName }}
                  </template>
                </el-table-column>
                <el-table-column
                  :filters="
                    cloudAccountList.map((item) => ({
                      text: item.name,
                      value: item.id,
                    }))
                  "
                  :filter-multiple="false"
                  column-key="cloudAccountId"
                  prop="cloudAccountName"
                  label="云账号"
                  label-width="150px"
                >
                  <template #default="scope">
                    <div style="display: flex; align-items: center">
                      <PlatformIcon :platform="scope.row.provider" />
                      <div style="margin-left: 8px">
                        {{ scope.row.cloudAccountName }}
                      </div>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column prop="productName" label="产品名称" />
                <el-table-column prop="tags" label="标签" show-overflow-tooltip>
                  <template #default="scope">
                    {{
                      scope.row.tags
                        ? Object.keys(scope.row.tags).length > 0
                          ? Object.keys(scope.row.tags)
                              .map((key) => key + "=" + scope.row.tags[key])
                              .join(",")
                          : "N/A"
                        : "N/A"
                    }}
                  </template>
                </el-table-column>
                <el-table-column prop="projectName" label="企业项目" />
              </ce-table>
            </div>
          </div>
        </div>
      </div>
    </div>
    <BillAuthRule
      :organization-workspace="activeTreeNode"
      ref="billAuthRuleRef"
    ></BillAuthRule>
  </layout-content>
</template>
<script setup lang="ts">
import dimensionSettingApi from "@/api/dimension_setting";
import { onMounted, ref, watch } from "vue";
import type { AuthorizeResourcesResponse } from "@/api/dimension_setting/type";
import {
  PaginationConfig,
  TableConfig,
  TableSearch,
} from "@commons/components/ce-table/type";
import cloudAccountApi from "@commons/api/cloud_account/index";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import PlatformIcon from "@commons/components/platform-icon/index.vue";
import BillAuthRule from "@/views/dimension_setting/components/bill_auth_rule/index.vue";
import CeTree from "@commons/components/ce-tree/index.vue";
import type { TreeNode } from "@commons/components/ce-tree/type";

const topHeight = ref<number>(40);
// 选中的树节点
const activeTreeNode = ref<TreeNode>({
  id: "NOT_AUTH",
  name: "未分账资源",
  type: "NOT_AUTH",
});
const cloudAccountList = ref<Array<CloudAccount>>([]);

const billAuthRuleRef = ref<InstanceType<typeof BillAuthRule>>();

// 打开授权规则设置
const openSplitBillSetting = () => {
  billAuthRuleRef.value?.open();
};

const table = ref();

onMounted(() => {
  search(new TableSearch());
  cloudAccountApi.listAll().then((ok) => {
    cloudAccountList.value = ok.data;
  });
});

// table ---------- start ------
// 列表字段数据
const columns = ref([]);
// 资源列表
const resourceLoading = ref<boolean>(false);
// 资源列表数据
const dataList = ref<Array<AuthorizeResourcesResponse>>([]);

/**
 * 搜索
 * @param condition 搜索对象
 */
const search = (condition: TableSearch) => {
  const params = TableSearch.toSearchParams(condition);
  (activeTreeNode.value.type === "NOT_AUTH"
    ? dimensionSettingApi.pageNotAuthorizeResource
    : dimensionSettingApi.pageAuthorizeResources)(
    tableConfig.value.paginationConfig.currentPage,
    tableConfig.value.paginationConfig.pageSize,
    {
      ...params,
      authorizeId: activeTreeNode.value?.id,
      type: activeTreeNode.value?.type,
    },
    resourceLoading
  ).then((ok) => {
    dataList.value = ok.data.records;
    tableConfig.value.paginationConfig?.setTotal(
      ok.data.total,
      tableConfig.value.paginationConfig
    );
    tableConfig.value.paginationConfig?.setCurrentPage(
      ok.data.current,
      tableConfig.value.paginationConfig
    );
  });
};

watch(activeTreeNode, () => {
  table.value?.clearAll();
});

/**
 * 表单配置
 */
const tableConfig = ref<TableConfig>({
  searchConfig: {
    showEmpty: false,
    // 查询函数
    search: search,
    quickPlaceholder: "搜索",
    components: [],
    searchOptions: [
      { label: "资源名称", value: "resourceName" },
      { label: "云账号", value: "cloudAccountName" },
      { label: "企业项目", value: "projectName" },
      { label: "产品名称", value: "productName" },
    ],
  },
  paginationConfig: new PaginationConfig(),
  tableOperations: undefined,
});
</script>
<style lang="scss" scoped>
.contentWrapper {
  font-weight: 400;
  font-size: 14px;
  line-height: 22px;
  color: #1f2329;
  height: 100%;
  width: 100%;
  display: flex;
  flex-wrap: wrap;

  .content {
    display: flex;
    justify-content: space-between;
    height: 100%;
    width: 100%;
    .left_content {
      width: 240px;
      height: calc(100% - 24px);
      padding: 0 24px 24px 0;
      border-right: 1px solid rgba(31, 35, 41, 0.15);
      margin-right: 24px;
    }
    .table_content {
      width: calc(100% - 270px);
      height: 100%;
      box-sizing: border-box;
      overflow: hidden;
      font-weight: 500;
      font-size: 14px;
      line-height: 22px;
      .title {
        height: 32px;
        display: flex;
        align-items: center;
        width: 100%;

        span {
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
          margin-right: 10px;
          color: #1f2329;
          > span {
            color: rgba(100, 106, 115, 1);
          }
        }
      }
      .content {
        box-sizing: border-box;
        height: calc(100% - 20px);
        width: 100%;
        .share_resource {
          margin-top: 10px;
          width: 100%;
          height: 100%;
          padding-right: 20px;
        }
      }
    }
  }
}
:deep(.table_cell) {
  .cell {
    white-space: nowrap;
  }
}
</style>
