<template>
  <div>
    <template v-if="!confirm">
      <el-radio-group v-model="_data" style="width: 100%">
        <el-table
          ref="singleTableRef"
          :data="formItem?.optionList"
          highlight-current-row
          style="width: 100%"
          @current-change="handleCurrentChange"
        >
          <el-table-column width="40px">
            <template #default="scope">
              <el-radio :label="scope.row.name">
                <template #default>{{}}</template>
              </el-radio>
            </template>
          </el-table-column>
          <el-table-column property="name" label="名称" min-width="200px" />
          <el-table-column label="存储使用量" min-width="300px">
            <template #default="scope">
              <div style="height: 12px">
                <el-popover
                  placement="top-start"
                  :title="scope.row.name"
                  :width="200"
                  trigger="hover"
                >
                  <template #reference>
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
                      :stroke-width="7"
                      width="120px"
                      striped
                    />
                  </template>
                  <div>
                    <el-row
                      ><el-col :span="6">总量</el-col
                      ><el-col :span="18">
                        <span class="value"
                          >{{
                            DecimalFormat.format(scope.row.totalDisk, 2)
                          }}&nbsp;&nbsp;GB</span
                        ></el-col
                      ></el-row
                    >
                    <el-divider border-style="dashed" />
                    <el-row
                      ><el-col :span="6">可用</el-col
                      ><el-col :span="18">
                        <span class="value"
                          >{{
                            DecimalFormat.format(scope.row.freeDisk, 2)
                          }}&nbsp;&nbsp;GB</span
                        ></el-col
                      ></el-row
                    >
                    <el-row style="margin-top: 8px"
                      ><el-col :span="6">已用</el-col
                      ><el-col :span="18">
                        <span class="value">
                          {{
                            DecimalFormat.format(
                              scope.row.totalDisk - scope.row.freeDisk,
                              2
                            )
                          }}&nbsp;&nbsp;GB</span
                        >
                      </el-col></el-row
                    >
                  </div>
                </el-popover>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-radio-group>
      <span class="msg" v-show="props.modelValue">
        当前选择存储器 <span class="active"> {{ props.modelValue }}</span>
      </span>
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
  </div>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import { computed, ref } from "vue";
import _ from "lodash";
import type { ElTable } from "element-plus";
import DecimalFormat from "@commons/utils/decimalFormat";

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
  { color: "rgba(52, 199, 36, 1)", percentage: 60 },
  { color: "rgba(255, 136, 0, 1)", percentage: 80 },
  { color: "rgba(245, 74, 69, 1)", percentage: 100 },
];
</script>
<style lang="scss" scoped>
@mixin valueScss() {
  color: rgba(31, 35, 41, 1);
  font-weight: 500;
  font-size: 12px;
  line-height: 22px;
  height: 22px;
}
:deep(.el-divider--horizontal) {
  margin: 8px 0;
}

.value {
  float: right;
  @include valueScss;
}

.msg {
  margin-top: 12px;
  color: #646a73;
  height: 22px;
  font-weight: 400;
  font-size: 14px;
  line-height: 22px;
  .active {
    color: rgba(51, 112, 255, 1);
  }
}
</style>
