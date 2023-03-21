<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <div
      class="contentWapper"
      style="--el-tree-node-hover-bg-color: rgba(51, 112, 255, 0.1)"
    >
      <div class="content">
        <div
          class="leftMenu"
          v-loading="orgLoading"
          style="--el-font-size-base: 14px"
        >
          <div class="top">
            <div class="search">
              <el-input
                v-model="filterText"
                placeholder="搜索关键词"
                size="small"
                :prefix-icon="Search"
                class="input"
              />
            </div>
            <div
              class="tree-item-content"
              :class="activeUnassignedResource ? 'active' : ''"
              @click="handleUnassignedResource"
            >
              未分账资源
            </div>
          </div>
          <div class="tree">
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
                <div>
                  <ce-icon
                    style="color: black"
                    :code="`${
                      data.type === 'ORGANIZATION'
                        ? 'zuzhijiagou1'
                        : 'project_space'
                    }`"
                    size="3px"
                  ></ce-icon>
                  <span style="margin-left: 8px">{{ node.label }}</span>
                </div>
              </template>
            </el-tree>
          </div>
        </div>
        <div class="rightContent">
          <div class="title" v-if="!activeUnassignedResource">
            <span>{{
              activeUnassignedResource
                ? "未分账资源"
                : activeWorkSpaceOrOrg?.name
            }}</span>
            <el-popover
              placement="top-start"
              :width="500"
              trigger="hover"
              content="各云账号的账单费用按照分账规则分摊到云管中的组织/工作空间上。"
            >
              <template #reference>
                <ce-icon
                  code="icon-maybe_outlined"
                  size="16px"
                  color="#646A73"
                ></ce-icon>
              </template>
            </el-popover>
          </div>

          <div class="content">
            <div v-if="activeWorkSpaceOrOrg" class="share-resource">
              <el-tabs
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
                    localKey="allocatedResourceTable"
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
                          <platform_icon :platform="scope.row.provider">
                          </platform_icon>
                          <div>{{ scope.row.cloudAccountName }}</div>
                        </div>
                      </template>
                    </el-table-column>
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
            </div>
            <div v-else class="not-share-resource">
              <ce-table
                localKey="notShareResourceTable"
                v-loading="resourceLoading"
                height="100%"
                ref="table"
                :columns="columns"
                :data="dataList"
                :tableConfig="tableConfig"
                row-key="id"
              >
                <template #toolbar>
                  <div class="title" v-if="activeUnassignedResource">
                    <span>未分账资源</span>
                    <el-popover
                      placement="top-start"
                      :width="500"
                      trigger="hover"
                      content="各云账号的账单费用按照分账规则分摊到云管中的组织/工作空间上。"
                    >
                      <template #reference>
                        <ce-icon
                          code="icon-maybe_outlined"
                          size="16px"
                          color="#646A73"
                        ></ce-icon>
                      </template>
                    </el-popover></div
                ></template>
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
                      <platform_icon :platform="scope.row.provider">
                      </platform_icon>
                      <div>{{ scope.row.cloudAccountName }}</div>
                    </div>
                  </template>
                </el-table-column>
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
import cloudAccountApi from "@commons/api/cloud_account/index";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import platform_icon from "@commons/components/platform-icon/index.vue";
import { Search } from "@element-plus/icons-vue";
const cloudAccountList = ref<Array<CloudAccount>>([]);
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
const filterNode = (value: string, data: OrganizationTree | any) => {
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
  cloudAccountApi.listAll().then((ok) => {
    cloudAccountList.value = ok.data;
  });
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
// 选中样式
@mixin active() {
  background-color: var(--el-tree-node-hover-bg-color);
  color: rgba(51, 112, 255, 1); //节点的字体颜色
  font-weight: 500;
}
.active {
  @include active;
}
:deep(.el-tab-pane) {
  height: 100%;
}
.contentWapper {
  font-weight: 400;
  font-size: 14px;
  line-height: 22px;
  color: #1f2329;
  height: calc(100% - 2px);
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  border: 1px solid var(--el-border-color);
  border-radius: 2px;
  .tips {
    height: 40px;
    width: 100%;
    line-height: 40px;
    border: 1px solid var(--el-border-color);
  }
  .content {
    display: flex;
    justify-content: space-between;
    height: 100%;
    width: 100%;

    .leftMenu {
      height: 100%;
      width: 250px;
      border-right: 1px solid var(--el-border-color);

      .top {
        height: 100px;
        .search {
          width: 100%;
          height: 44px;
          margin-bottom: 13px;
          display: flex;
          justify-content: center;
          align-items: flex-end;
          align-content: flex-end;
          .input {
            margin: 0 13px;
            height: 28px;
          }
        }
      }
      .tree {
        height: calc(100% - 100px);
        width: 250px;
        overflow: hidden;
        &:hover {
          overflow: auto;
        }
      }
    }
    .rightContent {
      width: calc(100% - 270px);
      height: 100%;
      box-sizing: border-box;
      overflow: hidden;
      .title {
        height: 32px;
        display: flex;
        align-items: center;

        span {
          margin-right: 10px;
        }
      }
      .content {
        box-sizing: border-box;
        height: calc(100% - 20px);
        width: 100%;
        .not-share-resource {
          margin-top: 10px;
          width: 100%;
          height: 100%;
          padding-right: 20px;
        }
        .share-resource {
          width: 100%;
          height: calc(100% - 30px);
          padding-right: 20px;
          .el-tabs {
            height: 100%;
          }
          .dimension_rules {
            width: 100%;
            height: 100%;
            &:hover {
              overflow-y: auto;
              overflow-x: auto;
            }
          }
        }
      }
    }
  }
}
:deep(.el-tabs__content) {
  height: calc(100% - 40px);
}
:deep(.el-tree) {
  --el-tree-node-hover-bg-color: rgba(51, 112, 255, 0.1);
  --el-tree-text-color: rgba(31, 35, 41, 1);
  --el-tree-expand-icon-color: rgba(100, 106, 115, 1);
  color: #1f2329;
  width: 100%;
  height: calc(100% - 90px);

  > .el-tree-node {
    display: inline-block;
    min-width: 100%;
  }
}

@mixin tree-item-content() {
  margin: 0 13px;
  box-sizing: border-box;
  height: 40px;
  border-radius: 4px;
  cursor: pointer;
  line-height: 40px;
  padding-left: 10px;
}
.tree-item-content {
  @include tree-item-content;
  &:hover {
    background-color: var(--el-tree-node-hover-bg-color);
  }
}
:deep(.el-tree-node__content) {
  @include tree-item-content;
}

:deep(.el-tree--highlight-current) {
  .el-tree-node.is-current {
    > .el-tree-node__content {
      @include active;
    }
  }
}
</style>
