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
          style="--el-font-size-base: 18px"
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
          <el-tree
            ref="treeRef"
            :data="data"
            :props="defaultProps"
            node-key="id"
            :accordion="true"
            :expand-on-click-node="false"
            :default-checked-keys="currentNodeKey"
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
            <span>{{ activeWorkSpaceOrOrg?.name }}</span>
          </div>
          <div class="content">
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
                >已分账资源</el-tab-pane
              >
            </el-tabs>
          </div>
        </div>
      </div>
    </div>
  </layout-content>
</template>
<script setup lang="ts">
import OrganizationApi from "@/api/organization/index";
import type {
  OrganizationTree,
  OrganizationWorkspaceTree,
} from "@/api/organization/type";
import dimensionSettingApi from "@/api/dimension_setting";
import { onMounted, ref, watch, nextTick } from "vue";
import { ElTree } from "element-plus";
import type { TabsPaneContext } from "element-plus";
import type { SimpleMap } from "@commons/api/base/type";
import BillRuleItemVue from "@/components/split_bill_rule_group/index.vue";

const rukeKey = ref<string>();

const treeRef = ref<InstanceType<typeof ElTree>>();

const ruleValues = ref<Array<SimpleMap<string>>>();

watch(rukeKey, () => {
  if (rukeKey.value) {
    dimensionSettingApi.listAuthorizeValues(rukeKey.value).then((ok) => {
      ruleValues.value = ok.data;
    });
  }
});
const activeTab = ref<string>("dimension_rules");

const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event);
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
const orgLoading = ref<boolean>(false);
const currentNodeKey = ref<Array<string>>([]);

onMounted(() => {
  OrganizationApi.tree("ORGANIZATION_AND_WORKSPACE", orgLoading).then((ok) => {
    data.value = JSON.parse(JSON.stringify(mergeTree(ok.data, [])));
    if (data.value) {
      activeWorkSpaceOrOrg.value = data.value[0];
      nextTick(() => {
        treeRef.value?.setCurrentKey(data.value[0].id, false);
      });
    }
  });
  dimensionSettingApi.listAuthorizeKeys().then((ok) => {
    dimensionSettingKeys.value = ok.data;
  });
});
const dimensionSettingKeys = ref<Array<SimpleMap<string>>>([]);

const activeWorkSpaceOrOrg = ref<OrganizationWorkspaceTree>();

const handleNodeClick = (data: OrganizationWorkspaceTree) => {
  activeWorkSpaceOrOrg.value = data;
};

const data = ref<Array<OrganizationWorkspaceTree>>([]);

const defaultProps = {
  label: "name",
};
</script>
<style lang="scss" scoped>
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
