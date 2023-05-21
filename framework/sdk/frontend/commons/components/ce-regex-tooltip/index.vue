<template>
  <div class="custom-popover">
    <el-popover
      :width="props.width"
      :content="props.content"
      :placement="props.placement"
      :trigger="props.trigger"
      :disabled="props.disabled"
      :offset="props.offset"
      :show-arrow="false"
    >
      <template #reference>
        <div style="width: 100%">
          <slot></slot>
        </div>
      </template>
      <div class="regex-input tooltip">
        <div>
          <span class="input-desc">{{ props.description }}</span>
        </div>
        <div v-for="(rule, index) in _rules" :key="index">
          <ce-icon
            :class="rule.status ? 'done' : 'fail'"
            style="padding: 0 4px 0 0"
            size="14px"
            :code="rule.status ? 'icon_yes_outlined' : 'icon_yes_outlined'"
          />
          <span>{{ rule.message }}</span>
        </div>
      </div>
    </el-popover>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
const props = withDefaults(
  defineProps<{
    content?: string;
    placement?: string;
    trigger?: string;
    disabled?: boolean;
    width?: string;
    modelValue?: string;
    offset?: number;
    description?: string;
    rules: any;
  }>(),
  {
    placement: "bottom-start",
    trigger: "click",
    disabled: false,
    width: "auto",
    offset: 0,
    rules: [{ regex: /\S/, message: "不能为空" }],
    modelValue: "",
  }
);
const _rules = computed(() => {
  const result: Array<any> = [];
  if (props.rules) {
    props.rules.forEach((rule: any) => {
      if (rule?.regex) {
        result.push({
          regex: rule?.regex,
          message: rule?.regexMessage,
          status: isMatch(rule?.regex),
        });
      } else if (rule?.pattern) {
        result.push({
          regex: rule?.pattern,
          message: rule?.regexMessage,
          status: isMatch(rule?.pattern),
        });
      }
    });
  }
  return result;
});
const isMatch = (regex: string) => {
  return props.modelValue
    ? new RegExp(regex).test(props.modelValue as string)
    : false;
};
</script>

<style lang="scss" scoped>
.custom-popover {
  display: inline-block;
  margin-right: 5px;
  margin-bottom: 5px;
  width: 100%;
}
.tooltip {
  width: 100%;
  box-sizing: border-box;
  .input-desc {
    height: 22px;
    left: 11px;
    right: 35px;
    top: calc(50% - 22px / 2);

    font-family: "PingFang SC";
    font-style: normal;
    font-weight: 500;
    font-size: 14px;
    line-height: 22px;
    color: #000000;
  }
  .fail {
    color: #f54a45;
  }
  .done {
    color: #34c724;
  }
  .show {
    display: block;
  }
}
</style>
