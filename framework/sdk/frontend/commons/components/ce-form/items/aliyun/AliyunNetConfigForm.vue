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
            <el-radio :label="scope.row.networkId">
              <template #default>{{}}</template>
            </el-radio>
          </template>
        </el-table-column>
        <el-table-column label="虚拟交换机名称" property="networkId" />
        <el-table-column label="所属VPC" property="vpcName" />
        <el-table-column label="IPV4网段" property="ipSegment" />
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
          _.find(formItem?.optionList, (o) => o.networkId === modelValue),
          "networkId",
          modelValue
      )
    }}
  </template>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import {computed, ref, watch} from "vue";
import _ from "lodash";
import type { ElTable } from "element-plus";

interface Network {
  networkId: string;
  vpcName: string;
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
        (data: Network) =>
            !search.value ||
            data.networkId.toLowerCase().includes(search.value.toLowerCase())
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
 * 触发 change 事件
 */
watch(
    () => _data.value,
    (value) => {
      if(value != null){
        emit("change");
      }
    },
    { deep: true }
);

/**
 * 列表选中
 */
const currentRow = computed<Network | undefined>({
  get() {
    return _.find(
        props.formItem?.optionList,
        (o: Network) => o.networkId === _data.value
    );
  },
  set(value) {
    _data.value = value?.networkId;
  },
});

function handleCurrentChange(val: Network | undefined) {
  currentRow.value = val;
}
</script>
<style lang="scss" scoped></style>
