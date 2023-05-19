<template>
  <div>
    <div class="header">
      <div class="title">{{ formItem.propsInfo.title }}</div>
      <el-input
          v-model="filterText"
          :validate-event="false"
          placeholder="请输入关键字搜索"
          class="input-with-select"
          style="--el-color-danger: #c0c4cc"
      >
        <template #prepend>
          <el-button :icon="Search" />
        </template>
      </el-input>
    </div>
    <el-checkbox-group
      v-model="_data"
      style="width: 100%; height: calc(100% - 48px - 22px)"
    >
      <el-table
        ref="singleTableRef"
        :data="tableData"
        highlight-current-row
        style="width: 100%; height: 100%; --el-bg-color: #f5f6f7"
      >
        <el-table-column width="50px">
          <template #default="scope">
            <el-checkbox
              :label="
                scope.row[formItem.valueField ? formItem.valueField : 'name']
              "
            >
              <span v-show="false">{{
                scope.row[formItem.valueField ? formItem.valueField : "name"]
              }}</span>
            </el-checkbox>
          </template>
        </el-table-column>
        <el-table-column
          v-for="(column, index) in tableColumns"
          :key="index"
          v-bind="column"
        >
          <template #default="scope">
            <template v-if="column.innerHtml">
              <span v-html="evalF(column.innerHtml, scope.row)"></span
            ></template>
            <template v-else>
              <span>{{ scope.row[column.property] }}</span></template
            >
          </template>
        </el-table-column>
      </el-table>
    </el-checkbox-group>
    <div class="msg" v-show="props.modelValue?.length > 0">
      {{ formItem.propsInfo.activeMsg }}
      <span class="active">
        {{ _.join(activeText, ",") }}
      </span>
    </div>
  </div>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import { computed, ref } from "vue";
import { Search } from "@element-plus/icons-vue";
import type { ElTable } from "element-plus";
import _ from "lodash";
const filterText = ref<string>("");
const props = defineProps<{
  modelValue?: Array<string>;
  formItem: FormView;
  setDefaultValue?: boolean;
}>();
const evalF = (text: string, row: any) => {
  return eval(text);
};
const emit = defineEmits(["update:modelValue", "change"]);

const singleTableRef = ref<InstanceType<typeof ElTable>>();

const _data = computed({
  get() {
    if (!props.modelValue) {
      return [];
    }
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

const tableColumns = computed(() => {
  if (props.formItem.propsInfo.tableColumns) {
    return props.formItem.propsInfo.tableColumns;
  }
  return [];
});

const tableData = computed(() => {
  if (props.formItem.optionList) {
    if (filterText.value) {
      return props.formItem.optionList.filter((item: any) =>
        tableColumns.value.some(
          (c: any) => item[c.property]?.indexOf(filterText.value) >= 0
        )
      );
    } else {
      return props.formItem.optionList.filter(
        (item: any) => item[props.formItem.valueField || "name"]
      );
    }
  }
  return [];
});

const activeText = computed(() => {
  if (props.modelValue) {
    const rows: Array<any> = props.formItem.optionList.filter((f: any) =>
      props.modelValue?.includes(
        f[props.formItem.valueField ? props.formItem.valueField : "value"]
      )
    );
    if (rows.length === 0) {
      emit("update:modelValue", undefined);
      emit("change", props.formItem);
      return undefined;
    }
    if (props.formItem.propsInfo.activeTextEval) {
      return rows.map((row) =>
        evalF(props.formItem.propsInfo.activeTextEval, row)
      );
    } else if (props.formItem.textField) {
      return rows.map((row) => row[props.formItem.textField || "value"]);
    }
  }
  return props.modelValue;
});
</script>
<style lang="scss" scoped>
.header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
  .title {
    color: #1f2329;
    font-weight: 400;
    font-size: 14px;
    line-height: 22px;
  }
  .input-with-select {
    width: 45%;
  }
}
.msg {
  margin-top: 5px;
  color: rgba(100, 106, 115, 1);
  .active {
    margin-left: 3px;
    color: rgba(51, 112, 255, 1);
  }
}
</style>
