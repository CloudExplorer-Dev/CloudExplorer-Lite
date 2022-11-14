<template>
  <template v-if="!confirm">
    <el-radio-group v-model="_data" style="width: 100%">
      <el-table
        ref="singleTableRef"
        :data="formItem?.optionList"
        highlight-current-row
        style="width: 100%"
        @current-change="handleCurrentChange"
      >
        <el-table-column width="55">
          <template #default="scope">
            <el-radio :label="scope.row.name">
              <template #default>{{}}</template>
            </el-radio>
          </template>
        </el-table-column>
        <el-table-column property="name" label="名称" />
        <el-table-column label="存储使用量">
          <template #default="scope">
            <div class="usage-bar-top-text">
              <span>可用:{{ scope.row.freeDisk }}GB</span>
            </div>
            <el-progress
              :color="customColors"
              :percentage="
                parseFloat(
                  (
                    ((scope.row.totalDisk - scope.row.freeDisk) /
                      scope.row.totalDisk) *
                    100
                  ).toFixed(2)
                )
              "
              :stroke-width="26"
              :text-inside="true"
            />
            <div class="usage-bar-bottom-text">
              <span>
                已用:
                {{ (scope.row.totalDisk - scope.row.freeDisk).toFixed(2) }}GB
              </span>
              <span>总量: {{ scope.row.totalDisk }}GB</span>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-radio-group>
  </template>
  <template v-else>
    {{
      _.get(
        _.find(formItem?.optionList, (o) => o.name === modelValue),
        "name",
        modelValue
      )
    }}
  </template>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import { computed, onMounted, ref } from "vue";
import _ from "lodash";
import type { ElTable } from "element-plus";

interface DataStore {
  mor: string;
  name: string;
  freeDisk: number;
  totalDisk: number;
  info: string;
  description?: string;
}

const props = defineProps<{
  modelValue?: string;
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
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

/**
 * 列表选中
 */
const currentRow = computed<DataStore | undefined>({
  get() {
    return _.find(
      props.formItem?.optionList,
      (o: DataStore) => o.name === _data.value
    );
  },
  set(value) {
    _data.value = value?.name;
  },
});

function handleCurrentChange(val: DataStore | undefined) {
  currentRow.value = val;
}

/**
 * 自定义进度条颜色
 */
const customColors = [
  { color: "#37ff00", percentage: 20 },
  { color: "#049638", percentage: 40 },
  { color: "#efa400", percentage: 60 },
  { color: "#ff4400", percentage: 80 },
  { color: "#ff0000", percentage: 100 },
];
</script>
<style lang="scss" scoped></style>
