<template>
  <template v-if="!confirm">
    <div style="margin-bottom: 10px">
      <el-input
        v-model="searchName"
        placeholder="输入关键字搜索"
        style="width: 20%"
        @keyup="handleQueryClick"
      >
        <template #append>
          <el-button style="color: var(--el-color-primary)" icon="Search" />
        </template>
      </el-input>
    </div>
    <el-form
      ref="ruleFormRef"
      label-suffix=":"
      label-position="left"
      :model="_data"
      size="small"
    >
      <el-form-item>
        <el-radio-group v-model="selectRowId" style="width: 100%">
          <el-table
            ref="multipleTableRef"
            :data="props.formItem?.ext?.instanceConfig?.searchTableData"
            highlight-current-row
            style="width: 100%"
            max-height="240px"
            @current-change="handleCurrentChange"
            v-loading="instanceTypeLoading"
            border
          >
            <template #empty>
              <span> 所选可用区暂无可用实例规格</span>
            </template>
            <el-table-column width="55">
              <template #default="scope">
                <el-radio :label="scope.row.specName">
                  <template #default>{{}}</template>
                </el-radio>
              </template>
            </el-table-column>
            <el-table-column property="specType" label="规格类型" />
            <el-table-column property="specName" label="规格名称" />
            <el-table-column property="instanceSpec" label="实例规格" />
          </el-table>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <span style="font-size: 12px; color: #606266"
      >当前选择实例 {{ selectRowId }}</span
    >
  </template>
  <template v-else>
    <el-descriptions>
      <el-descriptions-item label="实例规格">
        {{ modelValue?.instanceSpec }}
      </el-descriptions-item>
    </el-descriptions>
  </template>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import formApi from "@commons/api/form_resource_api";
import { computed, onMounted, ref, watch } from "vue";
import _ from "lodash";
import type { ElTable, FormInstance } from "element-plus";

const props = defineProps<{
  modelValue?: InstanceSpec;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  otherParams: any;
  formItem: FormView;
  confirm?: boolean;
}>();

/**
 *
 */
interface InstanceSpec {
  specName: string;
  specType: string;
  instanceSpec: string;
  amount: number;
  vcpus: string;
  ram: number;
  disk: string;
  amountText: string;
  randomImageId: string;
}
const selectRowId = ref<any>("");

const searchName = ref("");

const emit = defineEmits(["update:modelValue", "change"]);

const _data = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

const instanceTypeLoading = ref<boolean>(false);

// 校验实例对象
const ruleFormRef = ref<FormInstance>();

/**
 * 列表选中
 */
const currentRow = computed<InstanceSpec | undefined>({
  get() {
    return _.find(
      props.formItem?.ext?.instanceConfig?.searchTableData,
      (o: InstanceSpec) => o.specName === _data.value?.specName
    );
  },
  set(value) {
    _data.value = value;
  },
});

function handleCurrentChange(val: InstanceSpec | undefined) {
  currentRow.value = val;
  selectRowId.value = val?.specName;
  emit("change");
}

const singleTableRef = ref<InstanceType<typeof ElTable>>();

function getTempRequest() {
  return _.assignWith(
    {},
    {},
    props.otherParams,
    props.allData,
    (objValue, srcValue) => {
      return _.isUndefined(objValue) ? srcValue : objValue;
    }
  );
}

function getList() {
  const _temp = getTempRequest();
  const clazz = "com.fit2cloud.provider.impl.huawei.HuaweiCloudProvider";
  const method = "getInstanceSpecTypes";
  formApi
    .getResourceMethod(false, clazz, method, _temp, instanceTypeLoading)
    .then((ok) => {
      _.set(props.formItem, "ext.instanceConfig", ok.data);
      _.set(
        props.formItem,
        "ext.instanceConfig.searchTableData",
        ok.data.tableData
      );
      if (ok.data.tableData) {
        if (currentRow.value === undefined) {
          currentRow.value = ok.data.tableData[0];
          selectRowId.value = ok.data.tableData[0].specName;
        } else {
          //设置界面默认选中
          singleTableRef.value?.setCurrentRow(currentRow.value);
          selectRowId.value = currentRow.value.specName;
        }
        emit("change");
      }
    });
}

onMounted(() => {
  if (!props.confirm) {
    getList();
  }
});

watch(
  () => props.allData.billingMode,
  () => {
    getList();
  }
);
watch(
  () => props.allData.availabilityZone,
  () => {
    getList();
  }
);

const handleQueryClick = () => {
  if (!props.formItem?.ext?.instanceConfig?.tableData) {
    return;
  }
  let arr = [...props.formItem?.ext?.instanceConfig?.tableData];
  if (searchName.value.trim() && arr.length > 0) {
    arr = _.filter(
      props.formItem?.ext?.instanceConfig?.tableData,
      function (v) {
        const columnNames = Object.keys(arr[0]);
        let isShow = false;
        for (let i = 0; i < columnNames.length; i++) {
          if (v[columnNames[i]]?.toString().indexOf(searchName.value) > -1) {
            isShow = true;
          }
        }
        return isShow;
      }
    );
  }
  _.set(props.formItem, "ext.instanceConfig.searchTableData", arr);
};
</script>
<style lang="scss" scoped>
.usage-bar-top-text {
  display: flex;
  flex-direction: row-reverse;
  justify-content: space-between;
  font-size: smaller;
  font-weight: bold;
}

.usage-bar-bottom-text {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  font-size: smaller;
}
</style>
