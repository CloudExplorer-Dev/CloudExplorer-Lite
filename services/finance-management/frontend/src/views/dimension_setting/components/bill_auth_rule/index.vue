<template>
  <el-drawer
    style="--el-drawer-padding-primary: 0px 24px 24px 24px"
    size="860px"
    v-model="drawer"
    direction="rtl"
    :modal="false"
    :before-close="close"
  >
    <template #header>
      <div class="title">分账规则设置 (实施)</div>
    </template>
    <el-alert
      style="--el-alert-bg-color: rgba(51, 112, 255, 0.15)"
      title="设置分账规则或修改分账规则,自变更时刻后延迟24小时更新已分账资源"
      type="info"
      :closable="false"
      show-icon
    />
    <div class="rule_tree_content" v-loading="loading">
      <ce-rule-tree
        :left-height="48"
        :component="AuthRuleItem"
        ref="ruleTreeRef"
      ></ce-rule-tree>
    </div>
    <template #footer>
      <el-button @click="close">取消</el-button
      ><el-button type="primary" @click="save">保存</el-button>
    </template>
  </el-drawer>
</template>
<script setup lang="ts">
import { ref } from "vue";
import AuthRuleItem from "@/views/dimension_setting/components/bill_auth_rule/AuthRuleItem.vue";
import type { Tree } from "@commons/components/ce-rule-tree/type";
import type { BillAuthorizeRuleTree } from "@/api/dimension_setting/type";
import dimensionSettingApi from "@/api/dimension_setting/index";
import CeRuleTree from "@commons/components/ce-rule-tree/index.vue";
import type { TreeNode } from "@commons/components/ce-tree/type";
import { nanoid } from "nanoid";
import { ElMessage } from "element-plus";
const ruleTreeRef = ref<InstanceType<typeof CeRuleTree>>();
const drawer = ref<boolean>(false);
const props = defineProps<{ organizationWorkspace: TreeNode }>();
const loading = ref<boolean>(false);
// 关闭
const close = () => {
  drawer.value = false;
};

/**
 *打开
 */
const open = () => {
  drawer.value = true;
  ruleTreeRef.value?.setTree([{ id: nanoid() }]);
  if (props.organizationWorkspace?.type !== "NOT_AUTH") {
    dimensionSettingApi
      .getBillDimensionSetting(
        props.organizationWorkspace.id,
        props.organizationWorkspace.type as "WORKSPACE" | "ORGANIZATION",
        loading
      )
      .then((ok) => {
        const tree = toTree([ok.data.authorizeRule]);
        console.log(tree);
        ruleTreeRef.value?.setTree(tree);
      });
  }
};

/**
 *
 * @param tree 树结构
 */
const mapTree = (tree?: Array<Tree>): Array<BillAuthorizeRuleTree> => {
  if (!tree) {
    return [];
  }
  return tree
    .map((item) => {
      let children: Array<BillAuthorizeRuleTree> = [];
      if (item.children && item.children.length > 0) {
        children = mapTree(item.children);
      }
      return {
        conditionType: item.value,
        conditions: item.items
          ? item.items
              .filter((i) => i.value)
              .map((i) => ({
                field: i.value.field,
                compare: i.value.compare,
                value: i.value.value,
              }))
          : [],
        children,
      };
    })
    .filter((item) => {
      return effective(item);
    });
};

const effective = (item: BillAuthorizeRuleTree) => {
  if (item.conditions && item.conditions.length > 0) {
    return true;
  }
  if (item.children && item.children.length > 0) {
    for (let index = 0; index < item.children.length; index++) {
      const element = item.children[index];
      if (effective(element)) {
        return true;
      }
    }
  }
  return false;
};

const toTree = (billAuthorize: Array<BillAuthorizeRuleTree>): Array<Tree> => {
  return billAuthorize.map((item) => {
    let children: Array<Tree> = [];
    if (item.children && item.children.length > 0) {
      children = toTree(item.children);
    }
    return {
      id: nanoid(),
      value: item.conditionType,
      items: item.conditions
        ? item.conditions
            .filter((i) => i.value)
            .map((i) => ({
              value: { field: i.field, compare: "EQ", value: i.value },
              id: nanoid(),
            }))
        : [],
      children,
    };
  });
};

/**
 * 插入
 */
const save = () => {
  ruleTreeRef.value?.validate().then(() => {
    const tree = ruleTreeRef.value?.getTree();
    if (props.organizationWorkspace?.type !== "NOT_AUTH") {
      const billRule = mapTree(tree);
      dimensionSettingApi
        .saveOrUpdate(
          props.organizationWorkspace.id,
          props.organizationWorkspace.type as "WORKSPACE" | "ORGANIZATION",
          billRule && billRule.length > 0
            ? billRule[0]
            : { children: [], conditions: [], conditionType: "AND" }
        )
        .then(() => {
          ElMessage.success("保存成功");
          close();
        });
    }
  });
};
defineExpose({ open, close });
</script>
<style lang="scss" scoped>
.title {
  font-size: 16px;
  line-height: 24px;
  height: 24px;
  margin-top: 24px;
  color: #1f2329;
}
.rule_tree_content {
  border: 1px solid #dee0e3;
  border-radius: 4px;
  box-sizing: border-box;
  padding: 8px 16px 8px 16px;
  margin-top: 24px;
}
:deep(.el-drawer) {
  .el-drawer__header {
    margin-bottom: 24px !important;
  }
}
</style>
