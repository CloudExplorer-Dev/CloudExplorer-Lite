<template v-loading="_loading">
  <div style="width: 100%">
    <template v-if="!confirm">
      <el-form-item :rules="rules" :prop="props.field">
        <ce-regex-tooltip
          :description="props.formItem.description"
          :model-value="_data"
          :rules="rules"
        >
          <el-input
            v-bind="$attrs"
            :show-password="props.formItem.encrypted"
            v-model="_data"
          />
        </ce-regex-tooltip>
      </el-form-item>
    </template>
    <template v-else>
      <div class="detail-box">
        <el-descriptions :column="3" direction="vertical">
          <el-descriptions-item width="33.33%" />
        </el-descriptions>
      </div>
    </template>
  </div>
</template>
<script setup lang="ts">
import { computed, ref } from "vue";
import { type FormInstance } from "element-plus";
import type { FormView } from "@commons/components/ce-form/type";
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

// 校验实例对象
const ruleFormRef = ref<FormInstance>();
const _loading = ref<boolean>(false);
const defaultRules = ref<Array<any>>([{ regex: /\S/, message: "不能为空" }]);
const rules = computed<Array<any>>(() => {
  if (props.formItem.propsInfo?.rules) {
    return props.formItem.propsInfo?.rules;
  } else {
    return defaultRules;
  }
});

const _data = computed({
  get() {
    return props.modelValue;
  },
  set(value) {
    emit("update:modelValue", value);
  },
});
</script>
<style lang="scss" scoped>
.box-content {
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  padding: 18px;
  background-color: #f7f9fc;
  border-radius: 4px;
  .el-form-item {
    margin-bottom: 0;
    margin-right: 0;
    padding: 0px 2px 0px 8px;
    width: 100%;
    .el-form-item__content .custom-popover {
      width: 100%;
    }
  }

  .delete-icon {
    cursor: pointer;
    margin-left: 8px;
  }
}
.add-button {
  margin: 10px;
}
.detail-box {
  margin-top: 8px;
  :deep(.el-descriptions__header) {
    margin-bottom: 0 !important;
  }
}
</style>
