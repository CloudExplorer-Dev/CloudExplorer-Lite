<template>
  <template v-if="!confirm">
    <div style="margin-bottom: 10px">
      <el-input
        v-model="search"
        placeholder="输入关键字搜索"
        style="width: 20%"
      >
        <template #append>
          <el-button style="color: var(--el-color-primary)" icon="Search" />
        </template>
      </el-input>
    </div>
    <el-radio-group v-model="selectRowId" style="width: 100%">
      <el-table
        ref="singleTableRef"
        :data="filterTableData"
        highlight-current-row
        style="width: 100%"
        @current-change="handleCurrentChange"
        max-height="240px"
        border
      >
        <el-table-column width="55">
          <template #default="scope">
            <el-radio :label="scope.row.instanceType">
              <template #default>{{}}</template>
            </el-radio>
          </template>
        </el-table-column>
        <el-table-column label="规格类型" property="instanceTypeFamilyName" />
        <el-table-column label="规格名称" property="instanceType" />
        <el-table-column label="实例规格" property="cpuMemory" />
      </el-table>
    </el-radio-group>
  </template>
  <template v-else>
    <el-descriptions>
      <el-descriptions-item label="实例规格">
        {{ modelValue?.instanceType }}
      </el-descriptions-item>
    </el-descriptions>
  </template>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import { computed, onMounted, ref, watch } from "vue";
import _ from "lodash";
import type { ElTable, FormInstance } from "element-plus";

interface InstanceTypeConfig {
  instanceTypeFamilyName: string;
  instanceTypeFamily: string;
  instanceType: string;
  cpuMemory: string;
  cpu: number;
  memory: number;
}

const props = defineProps<{
  modelValue: InstanceTypeConfig | undefined;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  formItem?: FormView;
  otherParams: any;
  confirm?: boolean;
}>();

const emit = defineEmits(["update:modelValue", "change"]);
const singleTableRef = ref<InstanceType<typeof ElTable>>();
const selectRowId = ref();

// 搜索
const search = ref();
const filterTableData = computed(() =>
  props.formItem?.optionList?.filter(
    (data: InstanceTypeConfig) =>
      !search.value ||
      data.instanceType.toLowerCase().includes(search.value.toLowerCase()) ||
      data.cpuMemory
        .toLowerCase()
        .replace(/\s/g, "")
        .includes(search.value.toLowerCase().replace(/\s/g, ""))
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
const currentRow = computed<InstanceTypeConfig | undefined>({
  get() {
    return _.find(
      props.formItem?.optionList,
      (o: InstanceTypeConfig) => o.instanceType === _data.value?.instanceType
    );
  },
  set(value) {
    _data.value = value;
  },
});

function handleCurrentChange(val: InstanceTypeConfig | undefined) {
  currentRow.value = val;
  selectRowId.value = val?.instanceType;
  emit("change");
}

onMounted(() => {
  selectRowId.value = currentRow.value?.instanceType;
});

watch(
  () => props.formItem?.optionList,
  (value) => {
    if (value != null && value.length > 0 && selectRowId.value == null) {
      currentRow.value = value[0];
      selectRowId.value = value[0].instanceType;
      emit("change");
    }
  },
  { deep: true }
);
</script>
<style lang="scss" scoped></style>
