<template>
  <div class="bill_rule_group_wapper" v-loading="loading">
    <div class="content">
      <div class="condition" v-if="billGroups.length > 0">
        <el-select
          v-model="condition"
          class="m-2"
          placeholder="Select"
          style="width: 100px; margin-left: 10px"
        >
          <el-option
            v-for="item in [
              { label: '并且', value: 'AND' },
              { label: '或者', value: 'OR' },
            ]"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
      <div class="bill_rule_group_list">
        <div
          style="margin-top: 20px"
          v-for="group in billGroups"
          :key="group.id"
        >
          <BillRuleGroup
            ref="billRuleGroup"
            :group="group"
            :add-rule-item="addRuleItem"
            :delete-rule-item="deleteRuleItem"
            :delete-group="deleteGroup"
          ></BillRuleGroup>
        </div>
      </div>
    </div>
    <div class="add_rule_group">
      <div @click="addRuleGroup">添加规则组</div>
    </div>
  </div>
  <layout-container :border="false" style="margin-top: 20px"
    ><el-button @click="submit" type="primary">保存</el-button>
  </layout-container>
</template>
<script setup lang="ts">
import BillRuleGroup from "@/components/split_bill_rule_group/BillRuleGroup.vue";
import type { OrganizationWorkspace } from "@/api/organization/type";
import { nanoid } from "nanoid";
import { ElMessage } from "element-plus";
import type {
  BillAuthorizeRuleSettingGroup,
  BillAuthorizeRuleCondition,
} from "@/api/dimension_setting/type";
import dimensionSettingApi from "@/api/dimension_setting/index";
import { ref, watch } from "vue";
const props = defineProps<{ organizationWorkspace: OrganizationWorkspace }>();
/**
 * 授权规则组
 */
const billGroups = ref<Array<BillAuthorizeRuleSettingGroup>>([
  {
    id: nanoid(),
    billAuthorizeRules: [],
    conditionType: "AND",
  },
]);
/**
 * 获取授权规则组加载器
 */
const loading = ref<false>(false);
/**
 *账单规则组实例对象
 */
const billRuleGroup = ref<Array<InstanceType<typeof BillRuleGroup>>>([]);
/**
 * 规则组条件
 */
const condition = ref<string>("AND");
/**
 * 给某个组添加条件
 * @param groupId 规则组id
 * @param billAuthorizeRule 条件id
 */
const addRuleItem = (
  groupId: string,
  billAuthorizeRule: BillAuthorizeRuleCondition
) => {
  billGroups.value
    .find((b) => b.id === groupId)
    ?.billAuthorizeRules.push(billAuthorizeRule);
};

/**
 * 删除规则组条件
 * @param groupId 规则组id
 * @param billAuthorizeRuleId 条件id
 */
const deleteRuleItem = (groupId: string, billAuthorizeRuleId: string) => {
  const group = billGroups.value.find((b) => b.id === groupId);
  if (group) {
    group.billAuthorizeRules = group.billAuthorizeRules
      ? group.billAuthorizeRules.filter((i) => i.id !== billAuthorizeRuleId)
      : [];
  }
};

/**
 * 删除授权规则组
 * @param groupId 规则组id
 */
const deleteGroup = (groupId: string) => {
  billGroups.value = billGroups.value.filter((i) => i.id !== groupId);
};

watch(
  () => props.organizationWorkspace,
  () => {
    if (props.organizationWorkspace) {
      dimensionSettingApi
        .getBillDimensionSetting(
          props.organizationWorkspace.id,
          props.organizationWorkspace.type,
          loading
        )
        .then((ok) => {
          if (ok.data) {
            billGroups.value =
              ok.data.authorizeRule.billAuthorizeRuleSettingGroups;
            condition.value = ok.data.authorizeRule.conditionType;
          } else {
            billGroups.value = [
              {
                id: nanoid(),
                billAuthorizeRules: [
                  {
                    id: nanoid(),
                    field: "",
                    value: [],
                  },
                ],
                conditionType: "AND",
              },
            ];
          }
        });
    }
  },
  { immediate: true }
);
/**
 *添加规则组
 */
const addRuleGroup = () => {
  Promise.all(billRuleGroup.value.map((i) => i.validate())).then((ok) => {
    billGroups.value.push({
      id: nanoid(),
      billAuthorizeRules: [
        {
          id: nanoid(),
          field: "",
          value: [],
        },
      ],
      conditionType: "AND",
    });
  });
};
/**
 * 设置规则组数据
 */
const setData = () => {
  billRuleGroup.value.forEach((g) => g.setData(g.$props.group));
};
/**
 * 保存
 */
const submit = () => {
  Promise.all(billRuleGroup.value.map((i) => i.validate())).then((ok) => {
    setData();
    dimensionSettingApi
      .saveOrUpdate(
        props.organizationWorkspace.id,
        props.organizationWorkspace.type,
        {
          conditionType: condition.value,
          billAuthorizeRuleSettingGroups: billGroups.value,
        },
        loading
      )
      .then((ok) => {
        ElMessage.success("保存成功");
      });
  });
};
</script>
<style lang="scss" scoped>
.bill_rule_group_wapper {
  width: calc(100% - 2px);
  min-width: 1000px;
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  border: 1px solid var(--el-border-color);
  .content {
    width: 100%;
    display: flex;
    .condition {
      height: 100%;
      width: 120px;
      display: flex;
      align-items: center;
    }
    .bill_rule_group_list {
      width: calc(85% - 20px);
      flex-wrap: wrap;
    }
  }

  .add_rule_group {
    display: flex;
    width: 100%;
    justify-content: center;
    height: 40px;
    align-items: center;
    div {
      cursor: pointer;
    }
  }
}
</style>
