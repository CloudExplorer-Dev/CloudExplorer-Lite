<template>
  <div class="bill_rule_wapper">
    <div class="content">
      <div class="condition" v-if="group.billAuthorizeRules.length > 0">
        <el-select
          v-model="condition"
          class="m-2"
          placeholder="Select"
          size="small"
          style="width: 60px"
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
      <el-form
        ref="ruleFormRef"
        :model="props.group"
        class="list"
        style="flex: auto"
      >
        <el-form-item
          style="width: 100%; margin-bottom: 0"
          prop="billAuthorizeRules"
          :rules="{
            message: '规则组不能为空',
            trigger: 'change',
            required: true,
            type: 'array',
            min: 1,
          }"
        >
          <div
            class="item"
            v-for="item in group.billAuthorizeRules"
            :key="item.id"
          >
            <BillRuleItem
              :key="item.id"
              ref="billItem"
              :item="item"
              :delete-rule="deleteRule"
            ></BillRuleItem>
          </div>
        </el-form-item>
        <el-form-item>
          <div class="operate">
            <div class="add">
              <div @click="addRule">
                <ce-icon code="icon_add_outlined" size="12px"></ce-icon>添加条件
              </div>
            </div>
            <div class="delete" @click="deleteGroup(props.group.id)">
              <ce-icon code="icon_delete-trash_outlined" size="12px"></ce-icon>
              删除组
            </div>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script setup lang="ts">
import type {
  BillAuthorizeRuleSettingGroup,
  BillAuthorizeRuleCondition,
} from "@/api/dimension_setting/type";
import BillRuleItem from "@/components/split_bill_rule_group/BillRuleItem.vue";
import { ref } from "vue";
import { nanoid } from "nanoid";
import type { FormInstance } from "element-plus";

/**
 * 校验实例对象
 */
const ruleFormRef = ref<FormInstance>();

/**
 *数据item
 */
const billItem = ref<Array<InstanceType<typeof BillRuleItem>>>([]);

const props = defineProps<{
  group: BillAuthorizeRuleSettingGroup;
  addRuleItem: (groupId: string, item: BillAuthorizeRuleCondition) => void;
  deleteRuleItem: (groupId: string, billAuthorizeRuleId: string) => void;
  deleteGroup: (groupId: string) => void;
}>();

/**
 * 删除规则条件
 * @param item
 */
const deleteRule = (item: BillAuthorizeRuleCondition) => {
  props.deleteRuleItem(props.group.id, item.id);
};
/**
 * 条件判断 and 或者 or
 */
const condition = ref<string>(props.group.conditionType);

/**
 *添加规则
 */
const addRule = () => {
  Promise.all(billItem.value.map((b) => b.validate()))
    .then(() => {
      billItem.value.forEach((item) => item.setData(item.$props.item));
      props.addRuleItem(props.group.id, {
        id: nanoid(),
        field: "",
        value: [],
      });
    })
    // eslint-disable-next-line @typescript-eslint/no-empty-function
    .catch(() => {});
};

/**
 * 校验参数
 */
const validate = () => {
  return Promise.all([
    ...billItem.value.map((b) => b.validate()),
    ruleFormRef.value?.validate(),
  ]);
};

/**
 * 设置数据
 * @param group 分组规则
 */
const setData = (group: BillAuthorizeRuleSettingGroup) => {
  group.conditionType = condition.value;
  billItem.value.forEach((b) => {
    b.setData(b.$props.item);
  });
};

defineExpose({
  validate,
  setData,
});
</script>
<style lang="scss" scoped>
.bill_rule_wapper {
  display: flex;
  flex-wrap: wrap;
  border: 1px solid var(--el-border-color);
  border-radius: 5px;
  padding: 10px;

  .content {
    align-items: center;
    display: flex;
    width: 100%;
    .condition {
      height: 100%;
      width: 70px;
      display: flex;
      align-items: center;
    }
    .list {
      width: 75%;
      .item {
        display: flex;
        align-items: center;
        width: 100%;
      }
    }
    .operate {
      margin-top: 10px;
      background: #edf2f5;
      border-radius: 4px;
      width: 100%;
      display: flex;

      .delete {
        color: #f54a45;
        margin-left: 10px;
      }
      .add {
        color: #3370ff;
        margin-left: 20px;
      }
      div {
        height: 32px;
        width: 100px;
        line-height: 32px;
        cursor: pointer;
      }
    }
  }
}
</style>
