<template>
  <div style="width: 100%">
    <template v-if="!confirm">
      <el-form
        ref="ruleFormRef"
        label-suffix=":"
        label-position="left"
        :model="_data"
        v-loading="_loading"
      >
        <div
          style="
            display: flex;
            flex-direction: row;
            flex-wrap: nowrap;
            align-items: center;
            justify-content: space-between;
          "
        >
          <el-form-item
            :rules="{
              message: '计算资源类型' + '不能为空',
              trigger: 'blur',
              required: true,
            }"
            label="计算资源类型"
            prop="location"
          >
            <el-radio-group
              v-model="_data.location"
              @change="locationChange(_data.location)"
            >
              <el-radio-button
                v-for="(item, index) in formItem?.ext?.location?.optionList"
                :key="index"
                :label="item['value']"
              >
                {{ item["name"] }}
              </el-radio-button>
            </el-radio-group>
          </el-form-item>

          <el-input
            v-if="label"
            style="width: 240px"
            v-model="searchValue"
            placeholder="请输入关键字搜索"
            prefix-icon="Search"
          />
        </div>

        <el-form-item
          v-if="_data.location === 'host' || _data.location === 'pool'"
          :rules="{
            message: label + '不能为空',
            trigger: 'blur',
            required: true,
          }"
          prop="name"
        >
          <el-radio-group v-model="_data.name" style="width: 100%">
            <el-table
              ref="singleTableRef"
              :data="filterList(formItem?.ext?.name?.optionList)"
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
              <el-table-column property="name" :label="label" />
              <el-table-column label="CPU使用量">
                <template #default="scope">
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
                              (scope.row.usedCpu / scope.row.totalCpu) *
                              100
                            ).toFixed(2)
                          )
                        "
                        :stroke-width="7"
                        striped
                      />
                    </template>
                    <div>
                      <el-row>
                        <el-col :span="6"> 总量 </el-col>
                        <el-col :span="18">
                          <span class="value">
                            {{ scope.row.totalCpu }}GHz
                          </span>
                        </el-col>
                      </el-row>
                      <el-divider border-style="dashed" />
                      <el-row>
                        <el-col :span="6"> 可用 </el-col>
                        <el-col :span="18">
                          <span class="value">
                            {{
                              DecimalFormat.format(
                                scope.row.totalCpu - scope.row.usedCpu,
                                2
                              )
                            }}GHz
                          </span>
                        </el-col>
                      </el-row>
                      <el-row style="margin-top: 8px">
                        <el-col :span="6"> 已用 </el-col>
                        <el-col :span="18">
                          <span class="value">
                            {{ scope.row.usedCpu }}GHz
                          </span>
                        </el-col>
                      </el-row>
                    </div>
                  </el-popover>
                </template>
              </el-table-column>
              <el-table-column label="内存使用量">
                <template #default="scope">
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
                              (scope.row.usedMemory / scope.row.totalMemory) *
                              100
                            ).toFixed(2)
                          )
                        "
                        :stroke-width="7"
                        striped
                      />
                    </template>
                    <div>
                      <el-row>
                        <el-col :span="6"> 总量 </el-col>
                        <el-col :span="18">
                          <span class="value">
                            {{ scope.row.totalMemory }}GB
                          </span>
                        </el-col>
                      </el-row>
                      <el-divider border-style="dashed" />
                      <el-row>
                        <el-col :span="6"> 可用 </el-col>
                        <el-col :span="18">
                          <span class="value">
                            {{
                              DecimalFormat.format(
                                scope.row.totalMemory - scope.row.usedMemory,
                                2
                              )
                            }}GB
                          </span>
                        </el-col>
                      </el-row>
                      <el-row style="margin-top: 8px">
                        <el-col :span="6"> 已用 </el-col>
                        <el-col :span="18">
                          <span class="value">
                            {{ scope.row.usedMemory }}GB
                          </span>
                        </el-col>
                      </el-row>
                    </div>
                  </el-popover>
                </template>
              </el-table-column>
            </el-table>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span class="msg" v-show="label && props.modelValue?.name">
        当前选择{{ label }}
        <span class="active"> {{ props.modelValue?.name }}</span>
      </span>
    </template>
    <template v-else>
      <el-descriptions>
        <el-descriptions-item label="计算资源类型">
          {{
            _.get(
              _.find(
                formItem?.ext?.location?.optionList,
                (o) => o.value === modelValue.location
              ),
              "name",
              modelValue.location
            )
          }}
        </el-descriptions-item>
        <el-descriptions-item
          :label="label"
          v-if="_data.location === 'host' || _data.location === 'pool'"
        >
          {{
            _.get(
              _.find(
                formItem?.ext?.name?.optionList,
                (o) => o.name === modelValue.name
              ),
              "name",
              modelValue.name
            )
          }}
        </el-descriptions-item>
      </el-descriptions>
    </template>
  </div>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import formApi from "@commons/api/form_resource_api";
import { computed, onMounted, ref, watch } from "vue";
import _ from "lodash";
import { ElTable, type FormInstance } from "element-plus";
import DecimalFormat from "@commons/utils/decimalFormat";

interface ComputeConfig {
  location: string;
  //mor?: string;
  name?: string; //由于vc还是拿name作为快速索引，所以不用mor作为查询值
}

interface HostOrResourcePool {
  mor: string;
  name: string;
  totalCpu: number;
  totalMemory: number;
  usedCpu: number;
  usedMemory: number;
}

const props = defineProps<{
  modelValue?: ComputeConfig;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  otherParams: any;
  formItem: FormView;
  confirm?: boolean;
}>();

const emit = defineEmits(["update:modelValue", "change"]);

const _data = computed({
  get() {
    return props.modelValue ? props.modelValue : { location: "host" };
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

const label = computed<string | null>(() => {
  if (_data.value.location === "host") {
    return "宿主机";
  }
  if (_data.value.location === "pool") {
    return "资源池";
  }
  return null;
});

// 校验实例对象
const ruleFormRef = ref<FormInstance>();

/**
 * 列表选中
 */
const currentRow = computed<HostOrResourcePool | undefined>({
  get() {
    return _.find(
      props.formItem?.ext?.name?.optionList,
      (o: HostOrResourcePool) => o.name === _data.value.name
    );
  },
  set(value) {
    _data.value.name = value?.name;
  },
});

function handleCurrentChange(val: HostOrResourcePool | undefined) {
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

const _loading = ref<boolean>(false);

const singleTableRef = ref<InstanceType<typeof ElTable>>();

/**
 * 主机资源类型变化
 * @param value
 */
function locationChange(value?: string) {
  //清空
  _data.value.name = undefined;
  searchValue.value = "";
  _.set(props.formItem, "ext.name.optionList", []);
  //重新获取主机/资源池列表
  getList();
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

function getComputeTypes() {
  const _temp = getTempRequest();
  formApi
    .getResourceMethod(
      false,
      "com.fit2cloud.provider.impl.vsphere.VsphereCloudProvider",
      "getLocations",
      _temp,
      _loading
    )
    .then((ok) => {
      _.set(props.formItem, "ext.location.optionList", ok.data);
    });
}

function getList() {
  const _temp = getTempRequest();
  let clazz = "";
  let method = "";
  if (_data.value.location === "host") {
    clazz = "com.fit2cloud.provider.impl.vsphere.VsphereCloudProvider";
    method = "getHosts";
  } else if (_data.value.location === "pool") {
    clazz = "com.fit2cloud.provider.impl.vsphere.VsphereCloudProvider";
    method = "geResourcePools";
  } else {
    _.set(props.formItem, "ext.name.optionList", []);
    _data.value.name = undefined;
    return;
  }
  formApi
    .getResourceMethod(false, clazz, method, _temp, _loading)
    .then((ok) => {
      _.set(props.formItem, "ext.name.optionList", ok.data);
      if (currentRow.value === undefined) {
        _data.value.name = undefined;
      } else {
        //设置界面默认选中
        //singleTableRef.value?.setCurrentRow(currentRow.value);
      }
    });
}

const searchValue = ref<string>("");

function filterList(list: Array<any>) {
  if (searchValue.value == undefined || searchValue.value === "") {
    return list;
  } else {
    return _.filter(list, (o) => {
      return _.includes(_.lowerCase(o.name), searchValue.value);
    });
  }
}

/**
 * 触发change事件
 */
watch(
  () => _data.value.location,
  (newLocation, oldLocation) => {
    if ("drs-flag" === newLocation || "drs-flag" === oldLocation) {
      emit("change");
    }
  }
);
watch(
  () => _data.value.name,
  (value) => {
    if (value !== undefined) {
      emit("change");
    }
  }
);

onMounted(() => {
  if (!props.confirm) {
    getComputeTypes();
    getList();
  }
});

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
}

defineExpose({
  validate,
  field: props.field,
});
</script>
<style lang="scss" scoped>
@mixin valueScss() {
  color: rgba(31, 35, 41, 1);
  font-weight: 500;
  font-size: 12px;
  line-height: 22px;
  height: 22px;
}
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

:deep(.el-table th.el-table__cell) {
  background-color: #f5f6f7;
}
</style>
