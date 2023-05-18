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
      选择实例规格
      <el-input
        style="width: 240px; margin-bottom: 16px; --el-color-danger: #c0c4cc"
        v-model="searchValue"
        placeholder="请输入关键字搜索"
        prefix-icon="Search"
        :validate-event="false"
      />
    </div>
    <el-radio-group v-model="_data" style="width: 100%">
      <el-table
        ref="singleTableRef"
        :data="filterList(formItem?.optionList)"
        highlight-current-row
        style="width: 100%"
        @current-change="handleCurrentChange"
        max-height="280px"
      >
        <el-table-column width="55">
          <template #default="scope">
            <el-radio :label="scope.row.id">
              <template #default>{{}}</template>
            </el-radio>
          </template>
        </el-table-column>
        <el-table-column property="name" label="名称" />
        <el-table-column
          property="vcpus"
          label="CPU(核)"
          :filters="cpuFilters"
          :filter-method="filterCpu"
        />
        <el-table-column
          label="内存(GB)"
          :filters="ramFilters"
          :filter-method="filterRam"
        >
          <template #default="scope">
            {{ scope.row.ram / 1024 }}
          </template>
        </el-table-column>
        <el-table-column
          label="硬盘(GB)"
          :filters="diskFilters"
          :filter-method="filterDisk"
        >
          <template #default="scope">
            {{ scope.row.disk }}
          </template>
        </el-table-column>
      </el-table>
    </el-radio-group>
    <span class="msg" v-show="currentRow">
      已选择:
      <span style="color: rgba(51, 112, 255, 1)" v-if="currentRow">
        {{ currentRow.name }}
        <span style="margin-left: 20px">
          cpu: {{ currentRow.vcpus }}核, 内存: {{ currentRow.ram / 1024 }}GB,
          硬盘: {{ currentRow.disk }}GB
        </span>
      </span>
    </span>
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

interface Flavor {
  id: string;
  name: string;
  vcpus: number;
  ram: number;
  disk: number;
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
const currentRow = computed<Flavor | undefined>({
  get() {
    return _.find(
      props.formItem?.optionList,
      (o: Flavor) => o.id === _data.value
    );
  },
  set(value) {
    _data.value = value?.id;
  },
});

const cpuFilters = computed(() => {
  return _.uniqBy(
    _.map(props.formItem?.optionList, (flavor: Flavor) => {
      return { text: flavor.vcpus, value: flavor.vcpus };
    }),
    "value"
  );
});
const filterCpu = (value: number, row: Flavor) => {
  return row.vcpus === value;
};
const ramFilters = computed(() => {
  return _.uniqBy(
    _.map(props.formItem?.optionList, (flavor: Flavor) => {
      return { text: flavor.ram / 1024, value: flavor.ram / 1024 };
    }),
    "value"
  );
});
const filterRam = (value: number, row: Flavor) => {
  return row.ram / 1024 === value;
};
const diskFilters = computed(() => {
  return _.uniqBy(
    _.map(props.formItem?.optionList, (flavor: Flavor) => {
      return { text: flavor.disk, value: flavor.disk };
    }),
    "value"
  );
});
const filterDisk = (value: number, row: Flavor) => {
  return row.disk === value;
};

function handleCurrentChange(val: Flavor | undefined) {
  currentRow.value = val;
}

const searchValue = ref<string>("");

function filterList(list: Array<any>) {
  if (searchValue.value == undefined || searchValue.value === "") {
    return list;
  } else {
    return _.filter(list, (o) => {
      return _.includes(_.lowerCase(o.name), _.lowerCase(searchValue.value));
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
</style>
