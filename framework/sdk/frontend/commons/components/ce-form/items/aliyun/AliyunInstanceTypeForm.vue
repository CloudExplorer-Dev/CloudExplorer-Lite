<template>
  <template v-if="!confirm">
    <el-radio-group v-model="_data" style="width: 100%">
      <el-table
        ref="singleTableRef"
        :data="filterTableData"
        highlight-current-row
        style="width: 100%"
        @current-change="handleCurrentChange"
        max-height="340px"
      >
        <el-table-column width="55">
          <template #default="scope">
            <el-radio :label="scope.row.instanceType">
              <template #default>{{}}</template>
            </el-radio>
          </template>
        </el-table-column>
        <el-table-column label="规格类型" property="instanceTypeFamily" />
        <el-table-column label="规格名称" property="instanceType" />
        <el-table-column label="实例规格" property="cpuMemory" />
        <el-table-column align="left">
          <template #header>
            <el-input v-model="search" size="small" placeholder="搜索" />
          </template>
        </el-table-column>
      </el-table>
    </el-radio-group>
  </template>
  <template v-else>
    {{
      _.get(
        _.find(formItem?.optionList, (o) => o.instanceType === modelValue),
        "instanceType",
        modelValue
      )
    }}
  </template>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import { computed, ref, watch } from "vue";
import _ from "lodash";
import type { ElTable } from "element-plus";

interface CloudInstanceType {
  instanceTypeFamily: string;
  instanceType: string;
  cpuMemory: string;
}

const props = defineProps<{
  modelValue: any;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  formItem?: FormView;
  otherParams: any;
  confirm?: boolean;
}>();

const emit = defineEmits(["update:modelValue", "change"]);
const singleTableRef = ref<InstanceType<typeof ElTable>>();

// 搜索
const search = ref();
const filterTableData = computed(() =>
  props.formItem?.optionList?.filter(
    (data: CloudInstanceType) =>
      !search.value ||
      data.instanceType.toLowerCase().includes(search.value.toLowerCase())
  )
);

const _data = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

/**
 * 列表选中
 */
const currentRow = computed<CloudInstanceType | undefined>({
  get() {
    return _.find(
      props.formItem?.optionList,
      (o: CloudInstanceType) => o.instanceType === _data.value
    );
  },
  set(value) {
    _data.value = value?.instanceType;
  },
});

function handleCurrentChange(val: CloudInstanceType | undefined) {
  currentRow.value = val;
}
</script>
<style lang="scss" scoped></style>
