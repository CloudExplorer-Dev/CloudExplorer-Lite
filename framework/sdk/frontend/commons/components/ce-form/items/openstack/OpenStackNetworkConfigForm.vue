<template>
  <template v-if="!confirm">
    <div
      style="
        width: 100%;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
      "
    >
      选择网络
      <el-input
        style="width: 240px; margin-bottom: 16px--el-color-danger: #c0c4cc"
        v-model="searchValue"
        placeholder="请输入关键字搜索"
        prefix-icon="Search"
        :validate-event="false"
      />
    </div>
    <el-checkbox-group v-model="_data" style="width: 100%">
      <el-table
        ref="multipleTableRef"
        :data="filterList(formItem?.optionList)"
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

    <div class="msg" v-show="modelValue?.length > 0">
      已选择:
      <span class="active">
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
      </span>
    </div>
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

const searchValue = ref<string>("");

function filterList(list: Array<any>) {
  if (searchValue.value == undefined || searchValue.value === "") {
    return list;
  } else {
    return _.filter(list, (o) => {
      return (
        _.includes(_.lowerCase(o?.name), _.lowerCase(searchValue?.value)) ||
        _.some(o.neutronSubnets, (subnet) => {
          return (
            _.includes(
              _.lowerCase(subnet?.name),
              _.lowerCase(searchValue?.value)
            ) || _.includes(subnet?.cidr, searchValue.value)
          );
        })
      );
    });
  }
}

watch(
  () => _data.value,
  (value) => {
    emit("change");
  }
);
</script>
<style lang="scss" scoped>
:deep(.el-table th.el-table__cell) {
  background-color: #f5f6f7;
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
