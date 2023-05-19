<template v-loading="_loading">
  <template v-if="!confirm">
    <el-form
      ref="ruleFormRef"
      label-width="130px"
      label-position="left"
      require-asterisk-position="right"
      :model="_data"
    >
      <el-tabs v-model="activeTab" type="card">
        <el-tab-pane
          v-for="(item, index) in _data"
          :key="index"
          :label="'主机' + (index + 1)"
          :name="index"
        >
          <div class="adapter-container">
            <div v-for="(adapter, i) in item.adapters" :key="i" class="card">
              <div class="title">
                {{ "网卡" + (i + 1) }}

                <el-icon
                  style="cursor: pointer"
                  v-if="item.adapters.length > 1 || i > 0"
                  @click="removeAdapter(item.adapters, i)"
                >
                  <CloseBold />
                </el-icon>
              </div>

              <div style="padding: 16px">
                <el-form-item
                  :rules="{
                    message: '网络' + '不能为空',
                    trigger: ['blur', 'change'],
                    required: true,
                  }"
                  label="网络"
                  :prop="'[' + index + '].adapters[' + i + '].vlan'"
                >
                  <el-select
                    class="m-2"
                    filterable
                    v-model="adapter.vlan"
                    style="width: 100%"
                  >
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
                  :prop="'[' + index + '].adapters[' + i + '].dhcp'"
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
                    :rules="getIpCheckRules('IP地址')"
                    label="IP地址"
                    :prop="'[' + index + '].adapters[' + i + '].ipAddr'"
                  >
                    <el-input v-model="adapter.ipAddr" />
                  </el-form-item>

                  <el-form-item
                    :rules="getIpCheckRules('默认网关')"
                    label="默认网关"
                    :prop="'[' + index + '].adapters[' + i + '].gateway'"
                  >
                    <el-input v-model="adapter.gateway" />
                  </el-form-item>

                  <el-form-item
                    :rules="getMaskCheckRules('子网掩码')"
                    label="子网掩码"
                    :prop="'[' + index + '].adapters[' + i + '].netmask'"
                  >
                    <el-input v-model="adapter.netmask" />
                  </el-form-item>
                </template>
              </div>
            </div>

            <div class="add-card" @click="addAdapter(item.adapters)">
              + 添加网卡
            </div>
          </div>
          <el-row :gutter="12">
            <el-col :span="12">
              <el-form-item
                :rules="getIpCheckRules('DNS1', false)"
                label="DNS1"
                :prop="'[' + index + '].dns1'"
              >
                <el-input v-model="item.dns1" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                :rules="getIpCheckRules('DNS2', false)"
                label="DNS2"
                :prop="'[' + index + '].dns2'"
              >
                <el-input v-model="item.dns2" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-form>
  </template>
  <template v-else>
    <el-tabs v-model="activeTab" type="card">
      <el-tab-pane
        v-for="(item, index) in _data"
        :key="index"
        :label="'主机' + (index + 1)"
        :name="index"
      >
        <div class="adapter-container">
          <div
            style="
              width: 239px;
              background: #ffffff;
              border: 1px solid #bbbfc4;
              border-radius: 4px;
              padding: 16px;
              position: relative;
              margin: 10px;
            "
            v-for="(p, j) in item.adapters"
            :key="j"
          >
            <div
              style="
                position: absolute;
                right: 0;
                top: 0;
                padding: 1px 6px;
                height: 24px;
                background: #ebf1ff;
                border-radius: 2px 4px 0px 2px;
                font-style: normal;
                font-weight: 400;
                font-size: 14px;
                line-height: 22px;
                color: #3370ff;
              "
            >
              {{ "网卡" + (j + 1) }}
            </div>
            <el-descriptions :column="1" direction="vertical">
              <el-descriptions-item label="网络">
                {{
                  _.get(
                    _.find(
                      formItem?.ext?.network?.optionList,
                      (o) => o.mor === p.vlan
                    ),
                    "name",
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
          </div>
        </div>
        <el-descriptions
          :column="3"
          direction="vertical"
          style="margin-top: 20px"
        >
          <el-descriptions-item label="DNS1" width="33.33%">
            {{ item?.dns1 }}
            <span v-if="!item?.dns1"> - </span>
          </el-descriptions-item>
          <el-descriptions-item label="DNS2" width="33.33%">
            {{ item?.dns2 }}
            <span v-if="!item?.dns2"> - </span>
          </el-descriptions-item>
          <el-descriptions-item width="33.33%" />
        </el-descriptions>
      </el-tab-pane>
    </el-tabs>
  </template>
</template>
<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import { type FormInstance } from "element-plus";
import _ from "lodash";
import type { FormView } from "@commons/components/ce-form/type";
import { CloseBold } from "@element-plus/icons-vue";
import formApi from "@commons/api/form_resource_api";
import IpChecker from "@commons/utils/IpChecker";

interface NetworkConfig {
  adapters?: Array<NetWorkAdapter>;
  dns1?: string;
  dns2?: string;
}

interface NetWorkAdapter {
  vlan?: string;

  dhcp: boolean;

  ipAddr?: string;

  gateway?: string;

  netmask?: string;
}

const props = defineProps<{
  modelValue?: Array<NetworkConfig>;
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

function getIpCheckRules(columnName: string, required?: boolean) {
  const rules: Array<any> = [];
  if (required === undefined || required) {
    rules.push({
      message: columnName + "不能为空",
      trigger: "blur",
      required: true,
    });
  }
  /* rules.push({
    message: columnName + "格式不正确",
    trigger: ["blur", "change"],
    pattern: IpChecker.RegExpPatterns.ipv4,
  });*/
  rules.push({
    message: columnName + "格式不正确",
    trigger: ["blur", "change"],
    validator: IpChecker.ruleIpIsValid,
  });

  return rules;
}

function getMaskCheckRules(columnName: string) {
  const rules: Array<any> = [
    {
      message: columnName + "不能为空",
      trigger: "blur",
      required: true,
    },
    {
      message: columnName + "格式不正确",
      trigger: ["blur", "change"],
      validator: IpChecker.ruleMaskIsValidV6,
    },
  ];
  return rules;
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
          temp.push({ adapters: [{ dhcp: true }] });
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
  margin-bottom: 18px;

  .dns-input {
    width: 75%;
  }

  .card {
    min-height: 240px;
    width: 334px;
    margin: 10px;
    padding: 0;

    background: linear-gradient(
      180deg,
      #f7f9fc 0%,
      rgba(247, 249, 252, 0) 100%
    );
    border: 1px solid #ffffff;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.04);
    border-radius: 4px;

    .title {
      font-size: 14px;
      font-weight: bold;
      line-height: 22px;
      vertical-align: center;
      height: 22px;
      background: #f5f8ff;
      color: #3370ff;
      padding: 12px;
      display: flex;
      flex-direction: row;
      flex-wrap: nowrap;
      justify-content: space-between;
      align-items: center;
    }
  }

  .add-card {
    min-height: 240px;
    width: 334px;
    margin: 10px;
    padding: 0;
    background: #ffffff;
    border: 1px dashed #3370ff;
    border-radius: 4px;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
    color: #3370ff;
    cursor: pointer;
  }
}
.add-button {
  margin: 10px;
}
.network-confirm-view {
  .card-container {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    align-items: stretch;
    justify-content: flex-start;
    margin-bottom: 18px;

    .card {
      width: 300px;
      margin-right: 20px;
    }
  }
}
</style>
