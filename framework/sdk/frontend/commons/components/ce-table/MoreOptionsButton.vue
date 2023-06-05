<script setup lang="ts">
import { computed } from "vue";
import { Button } from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
const { t } = useI18n();
import _ from "lodash";

const props = withDefaults(
  defineProps<{
    buttons: Array<Button>;
    name?: string;
    row?: any;
    trigger?: "hover" | "click";
    border?: boolean;
  }>(),
  { trigger: "hover", border: false }
);

const _buttons = computed(() => {
  return _.filter(props.buttons, (b) => {
    if (_.isFunction(b.show)) {
      return b.show(props.row);
    } else {
      return b.show;
    }
  }) as Array<Button>;
});

const disabled = computed(() => {
  return function (btn: any) {
    return _.isFunction(btn.disabled) ? btn.disabled(props.row) : btn.disabled;
  };
});
const dropdownDisabled = computed(() => {
  return _buttons?.value?.every((btn) => {
    return _.isFunction(btn.disabled) ? btn.disabled(props.row) : btn.disabled;
  });
});

const show = computed<boolean>(() => {
  return _buttons.value && _buttons.value.length > 0;
});

defineExpose({ show });

function handleCommand(btn: any) {
  btn.click(props.row);
}
</script>

<template>
  <el-dropdown
    :disabled="dropdownDisabled"
    :trigger="trigger"
    @command="handleCommand"
    v-if="show"
  >
    <div class="more-operation" :class="border ? 'border' : ''" v-if="!name">
      <slot name="icon">
        <el-icon>
          <MoreFilled />
        </el-icon>
      </slot>
    </div>
    <div
      class="more-operation-text"
      :class="border ? 'border' : ''"
      v-if="name"
    >
      {{ name }}&nbsp;
      <el-icon><ArrowDown /></el-icon>
    </div>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item
          v-for="(b, i) in _buttons"
          :key="i"
          :icon="b.icon"
          :disabled="disabled(b)"
          :command="b"
          :style="{ color: b.color }"
        >
          {{ b.label }}
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<style lang="scss" scoped>
.more-operation {
  cursor: pointer;
  color: #3370ff;
  width: 24px;
  height: 24px;
  border-radius: 4px;
  text-align: center;
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  justify-content: center;

  &.border {
    border: 1px solid #3370ff;
  }

  &:hover {
    background-color: rgba(51, 112, 255, 0.1);
  }
}

.more-operation-text {
  display: flex;
  cursor: pointer;
  color: #3370ff;
  height: 24px;
  border-radius: 4px;
  padding: 0 16px;
  text-align: center;
  border: #3370ff solid 1px;
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 22px;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;

  &.border {
    border: 1px solid #3370ff;
  }
  &:hover {
    background: linear-gradient(
        0deg,
        rgba(51, 112, 255, 0.15),
        rgba(51, 112, 255, 0.15)
      ),
      #ffffff;
  }
}
</style>
