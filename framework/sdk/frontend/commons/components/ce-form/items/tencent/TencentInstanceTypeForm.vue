<template>
  <div style="width: 100%">
    <template v-if="!confirm">
      <div class="header">
        <div class="title">选择实例规格</div>
        <el-input
          placeholder="请输入关键字搜索"
          class="input-with-select"
          style="--el-color-danger: #c0c4cc"
          v-model="searchValue"
          prefix-icon="Search"
          :validate-event="false"
        />
      </div>
      <el-form
        ref="ruleFormRef"
        label-position="left"
        require-asterisk-position="right"
        :model="_data"
      >
        <div
          style="
            width: 100%;
            display: flex;
            flex-direction: row;
            justify-content: space-between;
          "
        ></div>
        <el-form-item prop="instanceType">
          <el-radio-group v-model="_data.instanceType" style="width: 100%">
            <el-table
              ref="singleTableRef"
              :data="tableData"
              highlight-current-row
              style="width: 100%; height: 330px"
              @current-change="handleCurrentChange"
            >
              <el-table-column width="50px">
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
      <div class="msg">
        <span v-show="props.modelValue">
          当前选实例规格
          <span class="active"> {{ props.modelValue?.instanceType }}</span>
        </span>
      </div>
    </template>
    <template v-else>
      <el-descriptions>
        <el-descriptions-item>
          {{ modelValue.instanceType }}
        </el-descriptions-item>
      </el-descriptions>
    </template>
  </div>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import { computed, ref, watch } from "vue";
import _ from "lodash";
import type { ElTable } from "element-plus";

interface InstanceTypeConfig {
  instanceTypeFamilyName: string;
  instanceTypeFamily: string;
  instanceType: string;
  cpuMemory: string;
  cpu: number;
  memory: number;
}

const props = defineProps<{
  modelValue?: InstanceTypeConfig;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  otherParams: any;
  formItem: FormView;
  confirm?: boolean;
}>();

const emit = defineEmits(["update:modelValue", "change"]);

const singleTableRef = ref<InstanceType<typeof ElTable>>();
const _data = computed({
  get() {
    return props.modelValue ? (props.modelValue as InstanceTypeConfig) : {};
  },
  set(value) {
    emit("update:modelValue", value);
    emit("change");
  },
});

const searchValue = ref<string>("");

const tableData = computed(() => {
  if (props.formItem.optionList) {
    if (searchValue.value) {
      return props.formItem.optionList.filter((item: any) =>
        Object.values(item).some((value) =>
          String(value).includes(searchValue.value)
        )
      );
    } else {
      return props.formItem.optionList;
    }
  }
  return [];
});

/**
 * 监听表格数据，设置默认值
 */
watch(
  () => tableData.value,
  () => {
    if (tableData.value && tableData.value.length > 0) {
      let defaultItem = _.head(tableData.value) as InstanceTypeConfig;
      if (props.modelValue) {
        const row = tableData.value.find(
          (f: InstanceTypeConfig) =>
            f.instanceType === props.modelValue?.instanceType
        );
        if (row) {
          defaultItem = row;
        }
      }
      emit("update:modelValue", defaultItem as InstanceTypeConfig);
    } else {
      emit("update:modelValue", undefined);
    }
    emit("change");
  }
);

/**
 * 列表选中
 */
const currentRow = computed<InstanceTypeConfig | undefined>({
  get() {
    return _.find(
      props.formItem?.optionList,
      (o: InstanceTypeConfig) => o.instanceType === _.get(_data.value,"instanceType")
    );
  },
  set(value) {
    _data.value = value as InstanceTypeConfig;
  },
});

function handleCurrentChange(val: InstanceTypeConfig | undefined) {
  currentRow.value = val;
}
</script>
<style lang="scss" scoped>
.header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
  .title {
    display: flex;
    justify-content: center;
    align-items: center;
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
  margin-top: 20px;
  color: #646a73;
  height: 22px;
  font-weight: 400;
  font-size: 14px;
  line-height: 22px;
  .active {
    color: rgba(51, 112, 255, 1);
  }
}

:deep(.el-table th.el-table__cell) {
  background-color: #f5f6f7;
}
</style>
