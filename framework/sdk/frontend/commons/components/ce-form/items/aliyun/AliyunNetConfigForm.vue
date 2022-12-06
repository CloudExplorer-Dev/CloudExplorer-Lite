<template>
  <template v-if="!confirm">
    <div style="margin-bottom: 10px">
      <el-input v-model="search" placeholder="输入关键字搜索">
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
            <el-radio :label="scope.row.networkId">
              <template #default>{{}}</template>
            </el-radio>
          </template>
        </el-table-column>
        <el-table-column label="虚拟交换机" property="networkName" />
        <el-table-column label="所属VPC" property="vpcName" />
        <el-table-column label="IPV4网段" property="ipSegment" />
      </el-table>
    </el-radio-group>
  </template>
  <template v-else>
    {{ modelValue?.networkName }}
  </template>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import { computed, ref, watch } from "vue";
import _ from "lodash";
import type { ElTable } from "element-plus";

interface NetworkConfig {
  vpcId: string;
  networkId: string;
  vpcName: string;
  networkName: string;
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
const selectRowId = ref();

// 搜索
const search = ref();
const filterTableData = computed(() =>
    props.formItem?.optionList?.filter(
        (data: NetworkConfig) =>
            !search.value ||
            data.networkName
                .toLowerCase()
                .includes(search.value.toLowerCase().replace(/\s/g, "")) ||
            data.vpcName
                .toLowerCase()
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
 * 触发 change 事件
 */
watch(
  () => _data.value,
  (value) => {
    if (value != null) {
      emit("change");
    }
  },
  { deep: true }
);

/**
 * 列表选中
 */
const currentRow = computed<NetworkConfig | undefined>({
  get() {
    return _.find(
      props.formItem?.optionList,
      (o: NetworkConfig) => o.networkId === _data.value?.networkId
    );
  },
  set(value) {
    _data.value = value;
  },
});

function handleCurrentChange(val: NetworkConfig | undefined) {
  currentRow.value = val;
  selectRowId.value = val?.networkId;
  emit("change");
}
</script>
<style lang="scss" scoped></style>
