<script setup lang="ts">
import { computed } from "vue";
import { Button } from "@commons/components/ce-table/type";
import { useI18n } from "vue-i18n";
const { t } = useI18n();
import _ from "lodash";

const props = defineProps<{
  buttons: Array<Button>;
  row?: any;
}>();

const _buttons = computed(() => {
  return _.filter(props.buttons, (b) => b.show) as Array<Button>;
});

const disabled = computed(() => {
  return function (btn: any) {
    return typeof btn.disabled === "function"
      ? btn.disabled(props.row)
      : btn.disabled;
  };
});

function handleCommand(btn: any) {
  btn.click(props.row);
}
</script>

<template>
  <el-dropdown @command="handleCommand">
    <div class="more-operation">
      <el-icon>
        <MoreFilled />
      </el-icon>
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
  padding-top: 3px;
  text-align: center;
}

.more-operation:hover {
  background-color: rgba(51, 112, 255, 0.1);
}
</style>
