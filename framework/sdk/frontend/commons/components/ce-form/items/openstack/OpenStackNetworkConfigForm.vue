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
        <el-table-column label="已连接的子网">
          <template #default="scope">
            <div
              v-for="(subnet, index) in scope.row.neutronSubnets"
              :key="index"
            >
              <div style="font-weight: bold">{{ subnet?.name }}</div>
              <div style="color: var(--el-text-color-secondary)">
                {{ subnet?.cidr }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="共享">
          <template #default="scope">
            {{ scope.row.shared ? "是" : "否" }}
          </template>
        </el-table-column>
      </el-table>
    </el-checkbox-group>
  </template>
  <template v-else>
    {{
      _.get(
        _.find(formItem?.optionList, (o) => o.id === modelValue),
        "name",
        modelValue
      )
    }}
  </template>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import { computed, onMounted, ref, watch } from "vue";
import _ from "lodash";
import type { ElTable } from "element-plus";

interface Network {
  id: string;
  name: string;
  neutronSubnets: any;
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

function handleRowClick(row: Network, column: any) {
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
