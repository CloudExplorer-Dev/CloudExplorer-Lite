<template>
  <template v-if="!confirm">
    <el-checkbox-group v-model="_data" style="width: 100%">
      <el-table
        ref="multipleTableRef"
        :data="formItem?.optionList"
        highlight-current-row
        style="width: 100%"
        @row-click="handleRowClick"
        max-height="280px"
      >
        <el-table-column width="55" :index="0">
          <template #default="scope">
            <el-checkbox :label="scope.row.id">
              <template #default>{{}}</template>
            </el-checkbox>
          </template>
        </el-table-column>
        <el-table-column property="name" label="名称" />
        <el-table-column property="description" label="描述" />
      </el-table>
    </el-checkbox-group>
  </template>
  <template v-else>
    {{
      _.join(
        _.map(modelValue, (v) =>
          _.get(
            _.find(formItem?.optionList, (o) => o.id === v),
            "name",
            modelValue
          )
        ),
        ","
      )
    }}
  </template>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import { computed, onMounted, ref, watch } from "vue";
import _ from "lodash";
import type { ElTable } from "element-plus";

interface SecurityGroup {
  id: string;
  name: string;
  description: string;
  shared: boolean;
}

const props = defineProps<{
  modelValue?: Array<string>;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  otherParams: any;
  formItem: FormView;
  confirm?: boolean;
}>();

const emit = defineEmits(["update:modelValue", "change"]);

const multipleTableRef = ref<InstanceType<typeof ElTable>>();

const _data = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

function handleRowClick(row: SecurityGroup, column: any) {
  if (column.index === 0 || _data.value === undefined) {
    //直接点checkbox的话不处理，使用checkbox默认
    return;
  }
  //console.log(_.includes(_data.value, row.id), column);
  const list = _data.value;
  if (_.includes(_data.value, row.id)) {
    _.remove(list, (n) => n === row.id);
    _data.value = list;
  } else {
    list.push(row.id);
    _data.value = list;
  }
  multipleTableRef.value?.toggleRowSelection(
    row,
    _.indexOf(_data.value, row.id) === -1
  );
}

watch(
  () => _data.value,
  (value) => {
    emit("change");
  }
);
</script>
<style lang="scss" scoped></style>
