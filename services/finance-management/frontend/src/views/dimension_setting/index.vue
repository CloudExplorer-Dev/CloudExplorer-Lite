<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <div class="contentWapper">
      <div class="tips">
        将各云账号的账单费用按照分账规则分摊到云管中的组织/工作空间上。
      </div>
      <div class="content">
        <div
          class="leftMenu"
          v-loading="orgLoading"
          style="--el-font-size-base: 14px"
        >
          <div
            style="
              width: 100%;
              height: 40px;
              display: flex;
              justify-content: center;
              align-items: flex-end;
              align-content: flex-end;
            "
          >
            <el-input
              v-model="filterText"
              placeholder="搜索"
              size="small"
              style="width: 90%; height: 30px"
            />
          </div>
          <div
            style="
              height: 50px;
              width: 100%;
              display: flex;
              padding-left: 30px;
              align-items: center;
              font-family: Helvetica, PingFang SC, Arial, sans-serif;
              font-size: 14px;
              cursor: pointer;
            "
            :class="activeUnassignedResource ? 'active' : ''"
            @click="handleUnassignedResource"
          >
            未分账资源
          </div>
          <el-tree
            ref="treeRef"
            :data="organizationWorkspaceTreeData"
            :props="defaultProps"
            node-key="id"
            :accordion="true"
            :expand-on-click-node="false"
            @node-click="handleNodeClick"
            :filter-node-method="filterNode"
            :highlight-current="true"
          >
            <template #default="{ node, data }">
              <span>
                <ce-icon
                  :code="`${
                    data.type === 'ORGANIZATION'
                      ? 'zuzhijiagou'
                      : 'project_space'
                  }`"
                  size="3px"
                ></ce-icon>
              </span>
              <span style="margin-left: 8px">{{ node.label }}</span>
            </template>
          </el-tree>
        </div>
        <div class="rightContent">
          <div class="title">
            <span>{{
              activeUnassignedResource
                ? "未分账资源"
                : activeWorkSpaceOrOrg?.name
            }}</span>
          </div>
          <div class="content">
            <el-tabs
              v-if="activeWorkSpaceOrOrg"
              v-model="activeTab"
              @tab-click="handleClick"
              style="width: 100%"
            >
              <el-tab-pane
                label="分账规则"
                name="dimension_rules"
                class="dimension_rules"
              >
                <BillRuleItemVue
                  v-if="activeWorkSpaceOrOrg"
                  :organization-workspace="activeWorkSpaceOrOrg"
                ></BillRuleItemVue>
              </el-tab-pane>
              <el-tab-pane label="已分账资源" name="allocated"
                ><ce-table
                  v-loading="resourceLoading"
                  height="100%"
                  ref="table"
                  :columns="columns"
                  :data="dataList"
                  :tableConfig="tableConfig"
                  row-key="id"
                >
                  <el-table-column type="selection" />
                  <el-table-column prop="resourceName" label="资源名称">
                    <template #default="scope">
                      <el-tooltip
                        :content="
                          scope.row.resourceId + '/' + scope.row.resourceName
                        "
                        placement="top"
                      >
                        <div
                          style="
                            white-space: nowrap;
                            text-overflow: ellipsis;
                            overflow: hidden;
                          "
                        >
                          {{ scope.row.resourceId }} /
                          {{ scope.row.resourceName }}
                        </div></el-tooltip
                      >
                    </template>
                  </el-table-column>
                  <el-table-column prop="cloudAccountName" label="云账号" />
                  <el-table-column prop="productName" label="产品名称" />
                  <el-table-column prop="tags" label="标签">
                    <template #default="scope">
                      <el-tooltip
                        raw-content
                        :content="
                          scope.row.tags
                            ? Object.keys(scope.row.tags).length > 0
                              ? Object.keys(scope.row.tags)
                                  .map(
                                    (key) =>
                                      '<div>' +
                                      key +
                                      '=' +
                                      scope.row.tags[key] +
                                      '</div>'
                                  )
                                  .join('')
                              : 'N/A'
                            : 'N/A'
                        "
                        placement="top"
                      >
                        <div
                          style="
                            white-space: nowrap;
                            text-overflow: ellipsis;
                            overflow: hidden;
                          "
                        >
                          {{
                            scope.row.tags
                              ? Object.keys(scope.row.tags).length > 0
                                ? Object.keys(scope.row.tags)
                                    .map(
                                      (key) => key + "=" + scope.row.tags[key]
                                    )
                                    .join(",")
                                : "N/A"
                              : "N/A"
                          }}
                        </div>
                      </el-tooltip>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="projectName"
                    label="企业项目"
                  /> </ce-table
              ></el-tab-pane>
            </el-tabs>
            <ce-table
              v-else
              v-loading="resourceLoading"
              height="100%"
              ref="table"
              :columns="columns"
              :data="dataList"
              :tableConfig="tableConfig"
              row-key="id"
            >
              <el-table-column type="selection" />
              <el-table-column prop="resourceName" label="资源名称">
                <template #default="scope">
                  <el-tooltip
                    :content="
                      scope.row.resourceId + '/' + scope.row.resourceName
                    "
                    placement="top"
                  >
                    <div
                      style="
                        white-space: nowrap;
                        text-overflow: ellipsis;
                        overflow: hidden;
                      "
                    >
                      {{ scope.row.resourceId }} /
                      {{ scope.row.resourceName }}
                    </div></el-tooltip
                  >
                </template>
              </el-table-column>
              <el-table-column prop="cloudAccountName" label="云账号" />
              <el-table-column prop="productName" label="产品名称" />
              <el-table-column prop="tags" label="标签">
                <template #default="scope">
                  <el-tooltip
                    raw-content
                    :content="
                      scope.row.tags
                        ? Object.keys(scope.row.tags).length > 0
                          ? Object.keys(scope.row.tags)
                              .map(
                                (key) =>
                                  '<div>' +
                                  key +
                                  '=' +
                                  scope.row.tags[key] +
                                  '</div>'
                              )
                              .join('')
                          : 'N/A'
                        : 'N/A'
                    "
                    placement="top"
                  >
                    <div
                      style="
                        white-space: nowrap;
                        text-overflow: ellipsis;
                        overflow: hidden;
                      "
                    >
                      {{
                        scope.row.tags
                          ? Object.keys(scope.row.tags).length > 0
                            ? Object.keys(scope.row.tags)
                                .map((key) => key + "=" + scope.row.tags[key])
                                .join(",")
                            : "N/A"
                          : "N/A"
                      }}
                    </div>
                  </el-tooltip>
                </template>
              </el-table-column>
              <el-table-column prop="projectName" label="企业项目" />
            </ce-table>
          </div>
        </div>
      </div>
    </div>
  </layout-content>
</template>
<script setup lang="ts">
import BaseOrganizationApi from "@commons//api/organization/index";
import type {
  OrganizationTree,
  OrganizationWorkspaceTree,
} from "@/api/organization/type";
import dimensionSettingApi from "@/api/dimension_setting";
import { onMounted, ref, watch } from "vue";
import { ElTree } from "element-plus";
import type { TabsPaneContext } from "element-plus";
import BillRuleItemVue from "@/components/split_bill_rule_group/index.vue";
import type { AuthorizeResourcesResponse } from "@/api/dimension_setting/type";
import {
  PaginationConfig,
  TableConfig,
  TableSearch,
} from "@commons/components/ce-table/type";
/**
 * 树对象
 */
const treeRef = ref<InstanceType<typeof ElTree>>();
/**
 * 当前选中的table
 */
const activeTab = ref<string>("dimension_rules");

/**
 * 切换tab触发函数
 * @param tab table对象
 * @param event
 */
const handleClick = (tab: TabsPaneContext) => {
  activeTab.value = tab.props.name as string;
};

/**
 *过滤文本
 */
const filterText = ref("");
/**
 *监听过滤文本变化
 */
watch(filterText, (val) => {
  treeRef.value?.filter(val);
});
/**
 * 过滤节点
 * @param value
 * @param data
 */
const filterNode = (value: string, data: OrganizationTree) => {
  if (!value) return true;
  return data.name.includes(value);
};

/**
 * 合并
 * @param orgData 组织数据
 * @param res    返回值
 */
const mergeTree = (
  orgData: Array<OrganizationTree>,
  res: Array<OrganizationWorkspaceTree>
) => {
  return orgData.map((item) => {
    const itemP: OrganizationWorkspaceTree = {
      id: item.id,
      name: item.name,
      type: "ORGANIZATION",
      children: [],
    };
    res.push(itemP);
    if (item.workspaces) {
      item.workspaces.forEach((w) =>
        itemP.children?.push({
          id: w.id,
          name: w.name,
          type: "WORKSPACE",
          children: [],
        })
      );
    }
    if (item.children) {
      mergeTree(
        item.children,
        itemP.children as Array<OrganizationWorkspaceTree>
      );
    }
    return itemP;
  });
};
/**
 * 组织加载器
 */
const orgLoading = ref<boolean>(false);

onMounted(() => {
  BaseOrganizationApi.tree("ORGANIZATION_AND_WORKSPACE", orgLoading).then(
    (ok) => {
      organizationWorkspaceTreeData.value = JSON.parse(
        JSON.stringify(mergeTree(ok.data, []))
      );
    }
  );
  search(new TableSearch());
});

/**
 * 选中的组织
 */
const activeWorkSpaceOrOrg = ref<OrganizationWorkspaceTree>();
/**
 * 选中未分账资源
 */
const activeUnassignedResource = ref<boolean>(true);
/**
 * 选中树节点触发函数
 * @param data 数节点
 */
const handleNodeClick = (data: OrganizationWorkspaceTree) => {
  activeWorkSpaceOrOrg.value = data;
  activeUnassignedResource.value = false;
};

/**
 * 选中未出账资源处理函数
 */
const handleUnassignedResource = () => {
  activeUnassignedResource.value = true;
  activeWorkSpaceOrOrg.value = undefined;
  treeRef.value?.setCurrentKey(undefined, false);
};

/**
 * 组织书对象
 */
const organizationWorkspaceTreeData = ref<Array<OrganizationWorkspaceTree>>([]);

/**
 * 组织树配置
 */
const defaultProps = {
  label: "name",
};
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
  (activeUnassignedResource.value
    ? dimensionSettingApi.pageNotAuthorizeResource
    : dimensionSettingApi.pageAuthorizeResources)(
    tableConfig.value.paginationConfig.currentPage,
    tableConfig.value.paginationConfig.pageSize,
    {
      ...params,
      authorizeId: activeWorkSpaceOrOrg.value?.id,
      type: activeWorkSpaceOrOrg.value?.type,
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

watch(activeWorkSpaceOrOrg, () => {
  if (activeTab.value === "allocated" && activeWorkSpaceOrOrg) {
    search(new TableSearch());
  }
  if (!activeWorkSpaceOrOrg.value) {
    search(new TableSearch());
  }
});

watch(activeTab, () => {
  if (activeTab.value === "allocated" && activeWorkSpaceOrOrg) {
    search(new TableSearch());
  }
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
.active {
  background-color: var(--el-color-primary-light-9);
}
:deep(.el-tab-pane) {
  height: 100%;
}
.contentWapper {
  height: 100%;
  width: 100%;
  display: flex;
  flex-wrap: wrap;

  .tips {
    height: 40px;
    width: 100%;
    line-height: 40px;
    border: 1px solid var(--el-border-color);
  }
  .content {
    display: flex;
    justify-content: space-between;
    height: calc(100% - 80px);
    width: 100%;
    .leftMenu {
      height: 100%;
      width: 200px;
      border: 1px solid var(--el-border-color);
      overflow: hidden;
    }
    .rightContent {
      width: calc(100% - 220px);
      height: 100%;
      border: 1px solid var(--el-border-color);
      box-sizing: border-box;
      padding: 20px;
      overflow: hidden;
      .title {
        height: 20px;
        span {
          text-rendering: optimizeLegibility;
          font-family: "PingFangSC-Regular", "PingFang SC", sans-serif;
          font-weight: 400;
          font-style: normal;
          font-size: 20px;
        }
      }
      .content {
        box-sizing: border-box;
        margin-top: 10px;
        height: calc(100% - 20px);
        .dimension_rules {
          width: 100%;
          height: 100%;
          overflow-y: auto;
          overflow-x: auto;
        }
      }
    }
  }
}
:deep(.el-tabs__content) {
  height: calc(100% - 40px);
}
:deep(.el-tree) {
  width: 100%;
  overflow: scroll;
  height: calc(100% - 40px);
  > .el-tree-node {
    display: inline-block;
    min-width: 100%;
  }
}

:deep(.el-tree-node__content) {
  height: 40px;
}
</style>
