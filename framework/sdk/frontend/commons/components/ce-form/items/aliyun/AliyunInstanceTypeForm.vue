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

    <el-form ref="ruleFormRef" :model="form">
      <el-form-item
        :rules="{
          message: '实例类型不能为空',
          trigger: 'blur',
          required: true,
        }"
        prop="selectRowId"
        size="small"
      >
        <el-radio-group
          v-model="form.selectRowId"
          style="width: 100%"
          @change="validate"
        >
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
            <el-table-column
              label="规格类型"
              property="instanceTypeFamilyName"
            />
            <el-table-column label="规格名称" property="instanceType" />
            <el-table-column label="实例规格" property="cpuMemory" />
          </el-table>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <span style="font-size: 12px; color: #606266"
      >当前选择实例 {{ form.selectRowId }}</span
    >
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
import { computed, onMounted, reactive, ref, watch } from "vue";
import _ from "lodash";
import type { ElTable, FormInstance } from "element-plus";

interface InstanceTypeConfig {
  instanceTypeFamilyName: string;
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
    (data: InstanceTypeConfig) =>
      !search.value ||
      formatValue(data.instanceType).includes(formatValue(search.value)) ||
      formatValue(data.instanceTypeFamilyName).includes(
        formatValue(search.value)
      ) ||
      formatValue(data.cpuMemory).includes(formatValue(search.value))
  )
);

/**
 * 格式处理
 * @param value
 */
const formatValue = (value: string) => {
  return value.toLowerCase().replace(/\s/g, "");
};

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
  form.selectRowId = val?.instanceType;
  emit("change");
}

/**
 * 校验方法
 */
const ruleFormRef = ref<FormInstance>();
const form = reactive({
  selectRowId: currentRow.value?.instanceType,
});
function validate(): Promise<boolean> {
  if (ruleFormRef.value) {
    return ruleFormRef.value.validate();
  } else {
    return new Promise((resolve, reject) => {
      return reject(true);
    });
  }
}

defineExpose({
  validate,
  field: props.field,
});

onMounted(() => {
  form.selectRowId = currentRow.value?.instanceType;
});

watch(
  () => props.formItem?.optionList,
  (value) => {
    if (
      value != null &&
      value.length > 0 &&
      (form.selectRowId == null ||
        value.every(
          (instanceTypeConfig: InstanceTypeConfig) =>
            instanceTypeConfig.instanceType !== form.selectRowId
        ))
    ) {
      handleCurrentChange(value[0]);
    }
  },
  { deep: true }
);
</script>
<style lang="scss" scoped></style>
