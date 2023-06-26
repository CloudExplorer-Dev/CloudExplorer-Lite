<template>
  <!-- 创建 -->
  <el-drawer
    :close-on-press-escape="false"
    :close-on-click-modal="false"
    v-model="createOptimizationStrategyVisible"
    size="840px"
    destroy-on-close
    :before-close="close"
  >
    <template #header>
      <span class="title">
        {{ operationType === "edit" ? "编辑" : "创建" }}
        <span v-if="operationType === 'edit'" style="color: #8f959e">
          ({{ createOptimizationStrategyForm.name }})
        </span>
      </span>
    </template>
    <div>
      <!-- 步骤 -->
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="优化规则配置"></el-step>
        <el-step title="优化范围配置"></el-step>
      </el-steps>
      <el-form
        label-position="top"
        :inline="true"
        require-asterisk-position="right"
        :model="createOptimizationStrategyForm"
        :rules="createOptimizationStrategyFormRules"
        ref="optimizationStrategyForm"
        label-width="120px"
        v-loading="loading"
      >
        <!-- 优化规则配置 -->
        <base-container
          v-if="active === 0"
          class="base_container"
          :contentBorder="false"
        >
          <template #content>
            <div class="base_info">
              <el-form-item
                prop="name"
                style="width: 100%"
                label="策略名称"
                v-if="operationType === 'create'"
              >
                <el-input
                  style="width: 100%"
                  v-model="createOptimizationStrategyForm.name"
                  maxlength="128"
                  show-word-limit
                />
              </el-form-item>
              <el-form-item
                prop="resourceType"
                style="width: 100%"
                label="资源类型"
              >
                <el-radio-group
                  v-model="createOptimizationStrategyForm.resourceType"
                >
                  <el-radio :key="'1'" :label="'VIRTUAL_MACHINE'">
                    云主机
                  </el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item
                prop="days"
                style="width: 100%"
                label="分析数据范围"
                v-if="monitoringField"
              >
                <div class="space-content">
                  <el-space>
                    <div style="width: 36px">过去</div>
                    <LineNumber
                      v-number="{
                        min: 1,
                        max: 36500,
                        isNull: false,
                        type: 'int',
                      }"
                      special-step="10"
                      v-model.number="createOptimizationStrategyForm.days"
                      required
                      @change="
                        formatNumber(createOptimizationStrategyForm.days)
                      "
                    >
                      <template #unit> 天 </template>
                    </LineNumber>
                    <div style="width: 180px">监控数据分析</div>
                  </el-space>
                </div>
              </el-form-item>

              <el-form-item
                prop="optimizationRules"
                style="width: 100%"
                label="优化规则"
              >
                <div class="rule_tree_content">
                  <ce-rule-tree
                    :left-height="48"
                    :component="rule_item"
                    ref="ruleTreeRef"
                    :store="fields"
                  />
                </div>
              </el-form-item>
            </div>
          </template>
        </base-container>
        <!-- 优化范围配置 -->
        <base-container
          v-if="active === 1"
          class="base_container"
          :contentBorder="false"
        >
          <template #content>
            <div class="base_info">
              <el-form-item
                prop="optimizationScope"
                style="width: 100%"
                label="优化范围"
              >
                <el-radio-group
                  v-model="createOptimizationStrategyForm.optimizationScope"
                >
                  <el-radio :label="true">所有资源</el-radio>
                  <el-radio :label="false">设置忽略资源</el-radio>
                </el-radio-group>
              </el-form-item>
              <server-list
                @ignoreServer="ignoreServer"
                v-if="
                  !createOptimizationStrategyForm.optimizationScope &&
                  createOptimizationStrategyForm.resourceType ===
                    'VIRTUAL_MACHINE'
                "
                :optimization-scope="
                  createOptimizationStrategyForm.optimizationScope
                "
                :optimization-strategy-id="optimizationStrategyId"
                :create-optimization-strategy-form="
                  _createOptimizationStrategyForm
                "
              />
            </div>
          </template>
        </base-container>
      </el-form>
    </div>

    <template #footer>
      <span>
        <el-button @click="close" :disabled="loading">取消</el-button>
        <el-button @click="prev" v-if="active === 1" :disabled="loading">
          上一步
        </el-button>
        <el-button
          @click="next"
          type="primary"
          v-if="active === 0"
          :disabled="loading"
          >下一步</el-button
        >
        <el-button
          type="primary"
          v-if="active === 1"
          @click="submit"
          :disabled="loading"
        >
          保存策略
        </el-button>
      </span>
    </template>
  </el-drawer>
</template>
<script setup lang="ts">
import { ref, watch, nextTick, computed } from "vue";
import type { FormRules, FormInstance } from "element-plus";
import type { Tree } from "@commons/components/ce-rule-tree/type";
import type {
  CreateRequest,
  OptimizationRuleTree,
  OptimizationRuleField,
} from "@commons/api/optimize/type";
import OptimizationStrategyViewApi from "@commons/api/optimize/index";
import rule_item from "./RuleItem.vue";
import CeRuleTree from "@commons/components/ce-rule-tree/index.vue";
import { nanoid } from "nanoid";
import _ from "lodash";
import ServerList from "./ServerList.vue";
import { ElMessage } from "element-plus";
import LineNumber from "@commons/components/ce-form/items/LineNumber.vue";
import { UpdateRequest } from "@commons/api/optimize/type";

const loading = ref<boolean>(false);
/**
 * 活跃步骤
 */
const active = ref<number>(0);
/**
 * 优化条件字段
 */
const fields = ref<Array<OptimizationRuleField>>([]);
/**
 * 表单对象
 */
const optimizationStrategyForm = ref<FormInstance>();
/**
 * 规则组件
 */
const ruleTreeRef = ref<InstanceType<typeof CeRuleTree>>();

/**
 * 创建弹出框
 */
const createOptimizationStrategyVisible = ref<boolean>(false);

const optimizationStrategyId = ref<string | undefined>();

/**
 * 修改父组件form数据
 */
const emit = defineEmits(["confirm"]);

/**
 * 创建策略表单
 */
const createOptimizationStrategyForm = ref<CreateRequest | UpdateRequest>({
  id: optimizationStrategyId.value,
  name: "",
  resourceType: "VIRTUAL_MACHINE",
  days: 10,
  optimizationRules: [
    {
      conditionType: "AND",
      children: [],
      conditions: [],
    },
  ],
  optimizationScope: true,
  ignoreResourceIdList: [],
});

const operationType = computed<"edit" | "create">(() => {
  if (optimizationStrategyId.value) {
    return "edit";
  } else {
    return "create";
  }
});
const monitoringField = computed<boolean>(() => {
  return createOptimizationStrategyForm.value.strategyType === "MONITORING";
});

/**
 * 表单校验对象
 */
const createOptimizationStrategyFormRules = ref<FormRules>({
  name: [
    {
      required: true,
      message: "名称不能为空",
      trigger: "blur",
      type: "string",
    },
  ],
});

/**
 * 数字框格式化值
 */
function formatNumber(v?: number) {
  createOptimizationStrategyForm.value.days = parseInt(
    `${v}`.toString().split(".")[0]
  );
}

/**
 * 监听资源类型变化
 * 目前只有一个云主机类型
 */
watch(
  createOptimizationStrategyForm,
  () => {
    if (createOptimizationStrategyForm.value.resourceType) {
      OptimizationStrategyViewApi.getResourceTypeList(loading).then((ok) => {
        fields.value = ok.data[0]?.optimizationRuleFieldList;
        if (monitoringField.value) {
          fields.value = _.filter(fields.value, { esField: true });
        } else {
          fields.value = _.filter(fields.value, { esField: false });
        }
      });
    }
  },
  {
    immediate: true,
  }
);

/**
 * 上一步
 * 回显上一步规则条件
 */
const prev = () => {
  if (active.value-- < 0) {
    active.value = 0;
  }
  nextTick(() => {
    const tree = toTree(createOptimizationStrategyForm.value.optimizationRules);
    ruleTreeRef.value?.setTree(tree);
  });
};

/**
 * 下一步
 * 验证规则条件，将规则条件存储到表单中
 */
const next = () => {
  const promises = [];
  promises.push(optimizationStrategyForm.value?.validate());
  promises.push(ruleTreeRef.value.validate());
  Promise.all(_.flatten(promises)).then((ok) => {
    const tree = ruleTreeRef.value?.getTree();
    createOptimizationStrategyForm.value.optimizationRules = mapTree(tree);
    // 子集是否设置了规则
    let checkChildrenItems = false;
    const treeChildren = tree[0].children;
    if (
      treeChildren &&
      treeChildren?.[0]?.items &&
      treeChildren?.[0]?.items?.length > 0
    ) {
      checkChildrenItems = true;
    }
    if ((tree[0].items && tree[0].items?.length > 0) || checkChildrenItems) {
      if (active.value++ > 1) {
        active.value = 0;
      }
    } else {
      ElMessage.warning("至少设置一条优化规则");
    }
  });
};

/**
 * 忽略资源操作
 */
const ignoreServer = (ignoreServerIdList: Array<string>) => {
  if (!createOptimizationStrategyForm.value.optimizationScope) {
    createOptimizationStrategyForm.value.ignoreResourceIdList =
      ignoreServerIdList;
  }
};

/**
 * 表单提交
 */
const submit = () => {
  optimizationStrategyForm.value?.validate().then(() => {
    if (optimizationStrategyId.value) {
      OptimizationStrategyViewApi.update(
        createOptimizationStrategyForm.value,
        loading
      ).then((ok) => {
        ElMessage.success("保存成功！");
        close();
        emit("confirm");
      });
    } else {
      OptimizationStrategyViewApi.create(
        createOptimizationStrategyForm.value,
        loading
      ).then((ok) => {
        ElMessage.success("保存成功！");
        close();
        emit("confirm");
      });
    }
  });
};

const _createOptimizationStrategyForm = computed(() => {
  return createOptimizationStrategyForm.value;
});
/**
 * 打开弹出框
 */
const open = (id: string | undefined) => {
  createOptimizationStrategyVisible.value = true;
  optimizationStrategyId.value = id;
  ruleTreeRef.value?.setTree([{ id: nanoid() }]);
  if (optimizationStrategyId.value) {
    OptimizationStrategyViewApi.getOptimizationStrategy(
      optimizationStrategyId.value,
      loading
    ).then((ok) => {
      const tree = toTree([ok.data.optimizationRules]);
      console.log(tree);
      ruleTreeRef.value?.setTree(tree);
      createOptimizationStrategyForm.value = ok.data;
    });
  } else {
    ruleTreeRef.value?.setTree([]);
  }
};
/**
 * 将表单或后台数据转为前端树结构数据
 * @param optimizationRule
 */
const toTree = (optimizationRule: Array<OptimizationRuleTree>): Array<Tree> => {
  return optimizationRule.map((item) => {
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
              value: {
                field: i.field,
                compare: i.compare,
                value: i.value,
              },
              id: nanoid(),
            }))
        : [],
      children,
    };
  });
};

/**
 * 将树结构数据转为表单数据
 * @param tree 树结构
 */
const mapTree = (tree?: Array<Tree>): Array<OptimizationRuleTree> => {
  if (!tree) {
    return [];
  }
  return tree
    .map((item) => {
      let children: Array<OptimizationRuleTree> = [];
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
                esField: monitoringField.value,
              }))
          : [],
        children,
      };
    })
    .filter((item) => {
      return effective(item);
    });
};
const effective = (item: OptimizationRuleTree) => {
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
/**
 * 关闭弹出框 清空表单数据
 */
const close = () => {
  createOptimizationStrategyVisible.value = false;
  createOptimizationStrategyForm.value = {
    id: "",
    name: "",
    resourceType: "VIRTUAL_MACHINE",
    days: 10,
    optimizationRules: [
      {
        conditionType: "AND",
        conditions: [],
        children: [],
      },
    ],
    optimizationScope: true,
    ignoreResourceIdList: [],
  };
  active.value = 0;
};
defineExpose({ open, close });
</script>
<style lang="scss" scoped>
.title {
  color: rgba(31, 35, 41, 1);
  font-size: 16px;
  font-weight: 500;
}
.base_container {
  width: 100%;
  height: auto;
  .rule_details {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
  }
}
.space-content {
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  padding: 12px;
  background-color: #f7f9fc;
  border-radius: 4px;
  margin-bottom: 10px;
  width: 100%;
  .el-form-item {
    margin-bottom: 0;
  }
  .delete-icon {
    cursor: pointer;
    margin-left: 8px;
  }
}
.rule_tree_content {
  border: 1px solid #dee0e3;
  border-radius: 4px;
  box-sizing: border-box;
  padding: 8px 16px 8px 16px;
  margin-top: 24px;
}
</style>
