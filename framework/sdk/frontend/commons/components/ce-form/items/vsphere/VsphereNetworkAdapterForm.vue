<template v-loading="_loading">
  <template v-if="!confirm">
    <el-form
      ref="ruleFormRef"
      label-width="130px"
      label-suffix=":"
      label-position="left"
      style="margin-bottom: 18px"
      :model="_data"
    >
      <el-tabs v-model="activeTab" type="card">
        <el-tab-pane
          v-for="(item, index) in _data"
          :key="index"
          :label="'主机' + (index + 1)"
          :name="index"
        >
          <el-button
            class="el-button--primary add-button"
            @click="addAdapter(item)"
          >
            添加网卡
          </el-button>
          <div class="adapter-container">
            <el-card
              :body-style="{ position: 'relative' }"
              v-for="(adapter, i) in item"
              :key="i"
              class="card"
            >
              <div class="title">
                {{ "网卡" + (i + 1) }}
              </div>

              <el-form-item
                :rules="{
                  message: '网络' + '不能为空',
                  trigger: 'blur',
                  required: true,
                }"
                label="网络"
                :prop="'[' + index + '][' + i + '].vlan'"
              >
                <el-select class="m-2" filterable v-model="adapter.vlan">
                  <el-option
                    v-for="(o, j) in formItem?.ext?.network?.optionList"
                    :key="j"
                    :label="o.name"
                    :value="o.mor"
                  />
                </el-select>
              </el-form-item>

              <el-form-item
                :rules="{
                  message: 'IP分配类型' + '不能为空',
                  trigger: 'blur',
                  required: true,
                }"
                label="IP分配类型"
                :prop="'[' + index + '][' + i + '].dhcp'"
              >
                <el-radio-group
                  v-model="adapter.dhcp"
                  @change="dhcpChange(adapter, adapter.dhcp)"
                >
                  <el-radio-button :label="true">
                    DHCP自动分配
                  </el-radio-button>
                  <el-radio-button :label="false"> 手动分配 </el-radio-button>
                </el-radio-group>
              </el-form-item>

              <template v-if="!adapter.dhcp">
                <!--   //todo ipv6           -->

                <el-form-item
                  :rules="{
                    message: 'IP地址' + '不能为空',
                    trigger: 'blur',
                    required: true,
                  }"
                  label="IP地址"
                  :prop="'[' + index + '][' + i + '].ipAddr'"
                >
                  <el-input v-model="adapter.ipAddr" />
                </el-form-item>

                <el-form-item
                  :rules="{
                    message: '默认网关' + '不能为空',
                    trigger: 'blur',
                    required: true,
                  }"
                  label="默认网关"
                  :prop="'[' + index + '][' + i + '].gateway'"
                >
                  <el-input v-model="adapter.gateway" />
                </el-form-item>

                <el-form-item
                  :rules="{
                    message: '子网掩码' + '不能为空',
                    trigger: 'blur',
                    required: true,
                  }"
                  label="子网掩码"
                  :prop="'[' + index + '][' + i + '].netmask'"
                >
                  <el-input v-model="adapter.netmask" />
                </el-form-item>
              </template>

              <el-button
                v-if="i > 0"
                class="remove-button"
                @click="removeAdapter(item, i)"
                :icon="CloseBold"
                type="info"
                text
              ></el-button>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-form>
  </template>
  <template v-else>
    <div class="network-confirm-view">
      <el-card v-for="(s, i) in modelValue" :key="i">
        <div style="font-weight: bold; padding-bottom: 14px">
          {{ "主机" + (i + 1) }}
        </div>
        <div class="card-container">
          <el-card v-for="(p, j) in s" :key="j" class="card">
            <el-descriptions :title="'网卡' + (j + 1)" :column="1">
              <el-descriptions-item label="网络">
                {{
                  _.get(
                    _.find(
                      formItem?.ext?.network?.optionList,
                      (o) => o.mor === p.vlan
                    ),
                    "mor",
                    p.vlan
                  )
                }}
              </el-descriptions-item>
              <el-descriptions-item label="IP分配类型">
                {{ p.dhcp ? "DHCP自动分配" : "手动分配" }}
              </el-descriptions-item>
              <template v-if="!p.dhcp">
                <el-descriptions-item label="IP地址">
                  {{ p.ipAddr }}
                </el-descriptions-item>
                <el-descriptions-item label="默认网关">
                  {{ p.gateway }}
                </el-descriptions-item>
                <el-descriptions-item label="子网掩码">
                  {{ p.netmask }}
                </el-descriptions-item>
              </template>
            </el-descriptions>
          </el-card>
        </div>
      </el-card>
    </div>
  </template>
</template>
<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import { type FormInstance } from "element-plus";
import _ from "lodash";
import type { FormView } from "@commons/components/ce-form/type";
import { CloseBold } from "@element-plus/icons-vue";
import formApi from "@commons/api/form_resource_api";

interface NetWorkAdapter {
  vlan?: string;

  dhcp: boolean;

  ipAddr?: string;

  gateway?: string;

  netmask?: string;
}

const props = defineProps<{
  modelValue?: Array<Array<NetWorkAdapter>>;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  otherParams: any;
  formItem: FormView;
  confirm?: boolean;
}>();

const emit = defineEmits(["update:modelValue", "change"]);

// 校验实例对象
const ruleFormRef = ref<FormInstance>();
const _loading = ref<boolean>(false);

const _data = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});

const activeTab = ref(0);

/**
 * 监听数量变化，获取值
 */
watch(
  () => props.allData.count,
  (count) => {
    setServers(count);
  }
);

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

function getList() {
  const _temp = getTempRequest();
  const clazz = "com.fit2cloud.provider.impl.vsphere.VsphereCloudProvider";
  const method = "getNetworks";
  formApi
    .getResourceMethod(false, clazz, method, _temp, _loading)
    .then((ok) => {
      _.set(props.formItem, "ext.network.optionList", ok.data);
    });
}

function setServers(count: number | undefined) {
  if (count !== undefined) {
    if (_data.value) {
      if (_data.value.length > count) {
        _data.value = _.slice(_data.value, 0, count - _data.value.length);
      } else if (_data.value.length < count) {
        const temp = [];
        for (let i = 0; i < count - _data.value.length; i++) {
          temp.push([{ dhcp: true }]);
        }
        _data.value = _.concat(_data.value, temp);
      }
    }
  }
}

function dhcpChange(adapter: NetWorkAdapter, dhcp: boolean) {
  if (dhcp) {
    adapter.gateway = undefined;
    adapter.netmask = undefined;
    adapter.ipAddr = undefined;
  }
}

function addAdapter(list: Array<NetWorkAdapter>) {
  list.push({ dhcp: true });
}

function removeAdapter(list: Array<NetWorkAdapter>, index: number) {
  _.remove(list, (o, i) => index === i);
}

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

onMounted(() => {
  setServers(props.allData.count);
  if (!props.confirm) {
    getList();
  }
});

defineExpose({
  validate,
  field: props.field,
});
</script>
<style lang="scss" scoped>
.adapter-container {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  align-items: stretch;
  justify-content: flex-start;

  .card {
    min-height: 100px;
    width: 446px;
    margin: 10px;
    .title {
      font-size: 14px;
      font-weight: bold;
      line-height: 44px;
      vertical-align: center;
      height: 44px;
      margin-bottom: 20px;
    }
    .remove-button {
      position: absolute;
      top: 20px;
      right: 20px;
    }
  }
}
.add-button {
  margin: 10px;
}
.network-confirm-view {
  margin-bottom: 20px;
  .card-container {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    align-items: stretch;
    justify-content: flex-start;

    .card {
      width: 300px;
      margin-right: 20px;
    }
  }
}
</style>
