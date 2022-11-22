<template>
  <template v-if="!confirm">
    <div style="display: flex; flex-direction: row; flex-wrap: wrap">
      <div
        v-for="(obj, index) in data"
        :key="index"
        class="vs-disk-config-card"
      >
        <el-card
          class="card"
          :body-style="{
            padding: 0,
            'text-align': 'center',
            display: 'flex',
            'flex-direction': 'column',
            'flex-wrap': 'nowrap',
            'align-items': 'center',
            'justify-content': 'space-evenly',
            height: '100%',
            position: 'relative',
          }"
        >
          <span class="title">
            {{ index === 0 ? "系统盘" : "数据盘 " + index }}
          </span>

          <el-form
            ref="ruleFormRef"
            label-suffix=":"
            label-position="left"
            :model="_data"
            size="small"
          >
            <el-form-item label="磁盘类型" prop="diskType">
              <el-select filterable v-model="obj.diskType" style="width: 150px">
                <el-option
                  v-for="(item, id) in props.formItem?.ext?.diskConfig
                    ?.diskTypes"
                  :key="id"
                  :label="item.name"
                  :value="item.id"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="磁盘大小" prop="size">
              <el-input-number
                v-model="obj.size"
                :min="_.defaultTo(defaultDisks[index]?.size, 1)"
                :step="1"
                required
              />
            </el-form-item>
            <el-form-item label="预估费用" prop="obj.amountText">
              <span>112.19</span>
            </el-form-item>
          </el-form>
          <el-button
            v-if="index != 0"
            class="remove-button"
            @click="remove(index)"
            :icon="CloseBold"
            type="info"
            text
          ></el-button>
        </el-card>

        <div style="width: 100%; height: 30px; text-align: center">
          <el-checkbox v-model="obj.deleteWithInstance">随实例删除</el-checkbox>
        </div>
      </div>
      <div class="vs-disk-config-card">
        <el-card class="card add-card">
          <el-button
            style="margin: auto"
            class="el-button--primary"
            @click="add"
            >添加磁盘</el-button
          >
        </el-card>
      </div>
    </div>
  </template>
  <template v-else>
    <el-descriptions>
      <el-descriptions-item
        :label="i === 0 ? '系统盘' : '数据盘' + (i + 1)"
        v-for="(disk, i) in modelValue"
        :key="i"
      >
        {{ disk.size }}GB{{ disk.deleteWithInstance ? " (随实例删除)" : "" }}
      </el-descriptions-item>
    </el-descriptions>
  </template>
</template>
<script setup lang="ts">
import type { FormInstance } from "element-plus";
import { computed, watch, onMounted, ref } from "vue";
import _ from "lodash";
import type { FormView } from "@commons/components/ce-form/type";
import { CloseBold } from "@element-plus/icons-vue";
import formApi from "@commons/api/form_resource_api";
const props = defineProps<{
  modelValue: any;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  formItem: FormView;
  otherParams: any;
  confirm?: boolean;
}>();
const diskTypes = ref<any>([]);
const _data = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});
// 校验实例对象
const ruleFormRef = ref<FormInstance>();
/**
 * 默认展示的盘
 */
const defaultDisks = computed(() => {
  return [{ size: 1, deleteWithInstance: true }];
});

watch(
  () => props.allData.availabilityZone,
  (count) => {
    getDiskType();
  }
);
const emit = defineEmits(["update:modelValue", "change"]);
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

function add() {
  data.value?.push({
    size: 1,
    diskType: props.formItem?.ext?.diskConfig?.diskTypes?.[0].id,
    amountText: "",
    deleteWithInstance: true,
  });
}
function remove(index: number) {
  _.remove(data.value, (n, i) => index === i);
}

function getTempRequest() {
  return _.assignWith(
    {},
    { computeConfig: _data.value },
    props.otherParams,
    props.allData,
    (objValue, srcValue) => {
      return _.isUndefined(objValue) ? srcValue : objValue;
    }
  );
}
onMounted(() => {
  getDiskType();
});

function getDiskType() {
  const _temp = getTempRequest();
  const clazz = "com.fit2cloud.provider.impl.huawei.HuaweiCloudProvider";
  const method = "getAllDiskTypes";
  formApi.getResourceMethod(false, clazz, method, _temp).then((ok) => {
    _.set(props.formItem, "ext.diskConfig.diskTypes", ok.data);
    diskTypes.value = ok.data;
    data.value.length = 0;
    data.value?.push({
      size: 40,
      diskType: diskTypes?.value[0].id,
      amountText: "",
      deleteWithInstance: true,
    });
  });
}
</script>
<style lang="scss" scoped>
.vs-disk-config-card {
  height: 180px;
  width: 250px;
  margin-right: 20px;
  margin-bottom: 40px;
  .card {
    height: 180px;
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
</style>
