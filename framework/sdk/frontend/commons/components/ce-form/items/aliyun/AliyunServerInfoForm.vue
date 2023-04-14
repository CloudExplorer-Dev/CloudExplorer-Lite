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
          <el-form-item
            :rules="[
              {
                message: '云主机名称' + '不能为空',
                trigger: 'blur',
                required: true,
              },
              {
                message:
                  '2～128个字符，以大小写字母或中文开头，可包含数字、点号（.）、下划线（_）、半角冒号（:）或连字符（-）',
                trigger: 'blur',
                pattern:
                  '(^[A-Za-z\u4e00-\u9fa5]{1}[A-Za-z0-9_\\-\\.\\:\u4e00-\u9fa5]{1,128}$)|(^\\s*$)',
              },
            ]"
            label="云主机名称"
            :prop="'[' + index + '].name'"
          >
            <el-input v-model.trim="item.name" />
          </el-form-item>

          <el-form-item
            :rules="[
              {
                message: 'Hostname' + '不能为空',
                trigger: 'blur',
                required: true,
              },
              {
                message:
                  '2～64个字符,只能包含小写字母、大写字母、数字、点或横线.不能出现连续的特殊字符，不能以特殊字符开头结尾。',
                trigger: 'blur',
                pattern: /^([0-9a-zA-Z]+[\.\-])*[0-9a-zA-Z]{2,64}$/,
              },
            ]"
            label="Hostname"
            :prop="'[' + index + '].hostname'"
          >
            <el-input v-model.trim="item.hostname" />
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
          {{ o.hostname }}
        </el-descriptions-item>
      </el-descriptions>
    </template>
  </template>
</template>
<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import { type FormInstance } from "element-plus";
import _ from "lodash";
import type { FormView } from "@commons/components/ce-form/type";

interface ServerInfo {
  name?: string;
  hostname?: string;
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
          temp.push({});
        }
        _data.value = _.concat(_data.value, temp);
      }
    }
  }
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
