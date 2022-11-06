<template v-loading="_loading">
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
          :rules="{
            message: '云主机名称' + '不能为空',
            trigger: 'blur',
            required: true,
          }"
          label="云主机名称"
          :prop="'[' + index + '].name'"
        >
          <el-input v-model="item.name" />
        </el-form-item>
      </el-tab-pane>
    </el-tabs>
  </el-form>
</template>
<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue";
import { type FormInstance } from "element-plus";
import _ from "lodash";
import type { FormView } from "@commons/components/ce-form/type";

interface ServerInfo {
  name?: string;
}

const props = defineProps<{
  modelValue?: Array<ServerInfo>;
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  otherParams: any;
  formItem: FormView;
}>();

const emit = defineEmits(["update:modelValue", "change"]);

// 校验实例对象
const ruleFormRef = ref<FormInstance>();
const _loading = ref<boolean>(false);

const _data = computed({
  get() {
    return props.modelValue ? props.modelValue : [];
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

function setServers(count: number | undefined) {
  if (count !== undefined) {
    if (_data.value.length > count) {
      _data.value = _.slice(_data.value, 0, count - _data.value.length);
    } else if (_data.value.length < count) {
      const addCount = count - _data.value.length;
      for (let i = 0; i < addCount; i++) {
        const list = _.clone(_data.value);
        list.push({});
        _data.value = list;
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
<style lang="scss">
.add-button {
  margin: 10px;
}
</style>
