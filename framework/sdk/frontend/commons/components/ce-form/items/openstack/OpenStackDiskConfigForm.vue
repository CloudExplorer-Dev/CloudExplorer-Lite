<template>
  <template v-if="!confirm">
    <el-form
      ref="ruleFormRef"
      label-width="76px"
      label-suffix=":"
      label-position="left"
      style="margin-bottom: 18px"
      :model="data"
    >
      <div v-for="(obj, index) in data" :key="index">
        <div class="disk-title">
          <span v-if="obj.boot"> 系统盘 <span class="unit"> (GB) </span></span>
          <span
            v-else-if="(_hasRoot && index === 1) || (!_hasRoot && index === 0)"
          >
            数据盘 <span class="unit"> (GB) </span>
          </span>
        </div>
        <div class="disk-row">
          <el-form-item label-width="0" :prop="'[' + index + '].volumeType'">
            <template #prefix>
              <div>磁盘类型</div>
            </template>
            <el-select
              filterable
              v-model="obj.volumeType"
              placeholder="磁盘类型"
              clearable
              style="width: 260px"
            >
              <el-option
                v-for="(item, index) in formItem?.ext?.volumeType?.optionList"
                :key="index"
                :label="item.name"
                :value="item.name"
              />
            </el-select>
          </el-form-item>

          <el-form-item
            style="margin-left: 12px"
            label-width="0"
            :rules="[
              {
                message: '磁盘大小' + '不能为空',
                trigger: 'blur',
                required: true,
              },
              {
                message: `磁盘大小最小值为${obj.minSize}`,
                trigger: ['change', 'blur'],
                type: 'number',
                min: obj.minSize,
              },
            ]"
            :prop="'[' + index + '].size'"
          >
            <LineNumber
              special-step="10"
              v-model="obj.size"
              :min="obj.minSize"
              :step="1"
              required
              style="width: 200px"
            >
              <template #perfix>
                <div>磁盘大小</div>
              </template>
            </LineNumber>
          </el-form-item>

          <CeIcon
            style="cursor: pointer; margin-left: 9px"
            size="16px"
            code="icon_delete-trash_outlined1"
            v-if="!obj.boot"
            @click="remove(index)"
            color="#6E748E"
          />
        </div>
      </div>
      <el-button link type="primary" @click="add">+ 添加磁盘 </el-button>
    </el-form>
  </template>
  <template v-else>
    <el-descriptions :column="3" direction="vertical">
      <el-descriptions-item
        :label="
          _hasRoot ? (i === 0 ? '系统盘' : '数据盘' + i) : '数据盘' + (i + 1)
        "
        v-for="(disk, i) in modelValue"
        :key="i"
        width="33.33%"
      >
        {{ disk.size }}GB [磁盘类型:
        {{ disk.volumeType ? disk.volumeType : "默认" }}]
      </el-descriptions-item>
      <el-descriptions-item
        width="33.33%"
        v-for="x in modelValue.length % 3"
        :key="x"
      />
    </el-descriptions>
  </template>
</template>
<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import _ from "lodash";
import type { FormView } from "@commons/components/ce-form/type";
import { CloseBold } from "@element-plus/icons-vue";
import { type FormInstance } from "element-plus";
import formApi from "@commons/api/form_resource_api";
import LineNumber from "@commons/components/ce-form/items/LineNumber.vue";
import CeIcon from "@commons/components/ce-icon/index.vue";

const props = defineProps<{
  modelValue: any;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  formItem: FormView;
  otherParams: any;
  confirm?: boolean;
}>();

const emit = defineEmits(["update:modelValue", "change"]);

interface Disk {
  minSize: number;
  size: number;
  deleteWithInstance: boolean;
  boot: boolean;
  volumeType?: string;
}

const _loading = ref<boolean>(false);

const _hasRoot = computed<boolean>(() => {
  return _.find(data, (a: any) => !!a.boot) !== undefined;
});

/**
 * 默认系统盘
 */
function getDefaultSystemDisk(): Disk {
  const templateFormView = _.find(props.allFormViewData, (formViewData) => {
    return formViewData.field === "flavorId";
  });

  const option = _.find(templateFormView?.optionList, (obj) => {
    return obj.id === props.allData.flavorId;
  });
  return {
    minSize: option.disk,
    size: option.disk,
    deleteWithInstance: false,
    boot: true,
    volumeType: undefined,
  };
}

/**
 * 双向绑定更新到外面
 */
const data = computed<Array<any>>({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});
/**
 * 触发change事件
 */
watch(
  () => data.value,
  (data) => {
    emit("change");
  },
  { deep: true }
);

function add() {
  data.value?.push({
    minSize: 1,
    size: 20,
    deleteWithInstance: false,
    boot: false,
  });
}
function remove(index: number) {
  _.remove(data.value, (n, i) => index === i);
}

// 校验实例对象
const ruleFormRef = ref<FormInstance>();

/**
 * 校验方法
 */
function validate(): Promise<boolean> {
  if (ruleFormRef.value) {
    return ruleFormRef.value.validate();
  } else {
    return new Promise((resolve, reject) => {
      return reject(true);
    });
  }
  /*return new Promise((resolve, reject) => {
    if (props.allData.bootFormVolume && data.value.length === 0) {
      return reject(false);
    }
    if (!props.allData.bootFormVolume && data.value.length === 0) {
      return resolve(true);
    }
    return _.every(data.value, (disk) => disk.size > 0 && disk.volumeType)
      ? resolve(true)
      : reject(false);
  });*/
}

function setDisks() {
  const sysDisk = _.find(data.value, (d) => d.boot);
  const defaultSysDisk = getDefaultSystemDisk();
  const list = _.filter(data.value, (d) => !d.boot);
  if (props.allData.bootFormVolume) {
    if (sysDisk != undefined) {
      defaultSysDisk.deleteWithInstance = sysDisk.deleteWithInstance;
      defaultSysDisk.volumeType = sysDisk.volumeType;
    }
    list.unshift(defaultSysDisk);
  }
  data.value = list;
}

/**
 * 监听模板变化，获取值
 */
if (!props.confirm) {
  watch(
    () => props.allData.bootFormVolume,
    (_data) => {
      setDisks();
    }
  );
  watch(
    () => props.allData.flavorId,
    (_data) => {
      setDisks();
    }
  );
}

function getTempRequest() {
  return _.assignWith(
    {},
    props.otherParams,
    props.allData,
    (objValue, srcValue) => {
      return _.isUndefined(objValue) ? srcValue : objValue;
    }
  );
}

function getVolumeTypes() {
  const _temp = getTempRequest();
  formApi
    .getResourceMethod(
      false,
      "com.fit2cloud.provider.impl.openstack.OpenStackCloudProvider",
      "listVolumeType",
      _temp,
      _loading
    )
    .then((ok) => {
      _.set(props.formItem, "ext.volumeType.optionList", ok.data);
    });
}

onMounted(() => {
  if (!props.confirm) {
    getVolumeTypes();
  }
});

defineExpose({
  validate,
  field: props.field,
});
</script>
<style lang="scss" scoped>
.vs-disk-config-card {
  height: 190px;
  width: 200px;
  margin-right: 20px;
  margin-bottom: 20px;

  .card {
    height: 160px;
    .title {
      font-size: 14px;
      font-weight: bold;
    }
    .remove-button {
      position: absolute;
      top: 0;
      right: 0;
    }
  }
  .add-card {
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

.disk-title {
  padding-top: 6px;
  padding-bottom: 6px;
  .unit {
    font-size: smaller;
    color: var(--el-text-color-secondary);
  }
}

.disk-row {
  width: 100%;
  background: #f7f9fc;
  border-radius: 4px;
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  height: 46px;
  padding-left: 12px;
  padding-right: 12px;
  margin-bottom: 6px;

  :deep(.el-form-item) {
    margin-bottom: 0;
  }
}
</style>
