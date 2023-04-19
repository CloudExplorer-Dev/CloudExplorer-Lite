<template v-loading="_loading">
  <template v-if="!confirm">
    <el-form
      :inline-message="true"
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
          <el-form-item
            :rules="{
              message: '云主机名称' + '不能为空',
              trigger: 'blur',
              required: true,
            }"
            label="云主机名称"
            :prop="'[' + index + '].name'"
          >
            <el-input v-model.trim="item.name" style="width: 100%" />
          </el-form-item>

          <el-form-item
            :rules="rules()"
            label="Hostname"
            :prop="'[' + index + '].hostName'"
          >
            <el-input
              v-model.trim="item.hostName"
              minlength="1"
              maxlength="65"
            />
          </el-form-item>
        </el-tab-pane>
      </el-tabs>
    </el-form>
  </template>
  <template v-else>
    <template v-for="(o, i) in modelValue" :key="i">
      <el-descriptions :title="'主机' + (i + 1)">
        <el-descriptions-item label="云主机名称">
          {{ o.name }}
        </el-descriptions-item>
        <el-descriptions-item label="Hostname">
          {{ o.hostName }}
        </el-descriptions-item>
      </el-descriptions>
    </template>
  </template>
</template>
<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import type { FormInstance } from "element-plus";
import _ from "lodash";
import type { FormView } from "@commons/components/ce-form/type";

interface ServerInfo {
  name?: string;
  hostName?: string;
  authReboot?: boolean;
}

const props = defineProps<{
  modelValue?: Array<ServerInfo>;
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
    activeTab.value = count - 1;
    setServers(count);
  }
);

function setServers(count: number | undefined) {
  if (count !== undefined) {
    if (_data.value) {
      if (_data.value.length > count) {
        _data.value = _.slice(_data.value, 0, count - _data.value.length);
      } else if (_data.value.length < count) {
        const temp = [];
        for (let i = 0; i < count - _data.value.length; i++) {
          temp.push({ authReboot: true });
        }
        _data.value = _.concat(_data.value, temp);
      }
    }
  }
}

function rules() {
  const rules = [
    {
      message: "Hostname" + "不能为空",
      trigger: "blur",
      required: true,
    },
    {
      message: "Hostname" + "长度为1-64",
      trigger: "blur",
      pattern: "^.{1,64}$",
    },
  ];
  if (props.formItem.regexp) {
    const regexpObj = {
      message: props.formItem.regexpDescription as string,
      trigger: "blur",
      required: true,
    };
    _.set(regexpObj, "pattern", props.formItem.regexp);
    rules.push(regexpObj);
  }
  return rules;
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
});

defineExpose({
  validate,
  field: props.field,
});
</script>
<style lang="scss" scoped>
.add-button {
  margin: 10px;
}
</style>
