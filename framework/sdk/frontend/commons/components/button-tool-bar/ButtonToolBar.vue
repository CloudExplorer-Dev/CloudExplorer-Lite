<template>
  <template v-for="(item, index) in baseActions" :key="index">
    <el-button
      @click="item.fn(item.arg)"
      v-if="item.show"
      :type="item.type"
      :disabled="item.disabled"
    >
      {{ item.text }}
    </el-button>
  </template>
  <el-dropdown
    @command="handleAction"
    trigger="click"
    style="margin-left: 12px"
    v-if="moreActions.length > 0 && _.some(moreActions, 'show')"
  >
    <el-button>
      {{ t("commons.cloud_server.more", "更多") }}
      <el-icon class="el-icon--right"><arrow-down /></el-icon>
    </el-button>
    <template #dropdown>
      <el-dropdown-menu>
        <template v-for="(item, index) in moreActions" :key="index">
          <el-dropdown-item
            :command="{ arg: item.arg, fn: item.fn }"
            v-if="item.show"
            :disabled="item.disabled"
          >
            {{ item.text }}
          </el-dropdown-item>
        </template>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import _ from "lodash";
import { computed } from "vue";
import type { ButtonAction } from "@commons/components/button-tool-bar/type";

const props = withDefaults(
  defineProps<{
    ellipsis?: number;
    actions: Array<ButtonAction>;
  }>(),
  {
    ellipsis: 2,
  }
);

const { t } = useI18n();

const ellipsis = computed(() => {
  return props.ellipsis <= 0 ? 2 : props.ellipsis;
});

const baseActions = computed<Array<ButtonAction>>(() => {
  return _.slice(
    _.filter(props.actions, (action) => action.show),
    0,
    props.actions.length <= ellipsis.value
      ? props.actions.length
      : ellipsis.value
  ) as Array<ButtonAction>;
});

const moreActions = computed<Array<ButtonAction>>(() => {
  return _.slice(
    _.filter(props.actions, (action) => action.show),
    props.actions.length <= ellipsis.value
      ? props.actions.length
      : ellipsis.value
  ) as Array<ButtonAction>;
});

//触发事件
const handleAction = (actionObj: any) => {
  const { arg, fn } = actionObj;
  if (fn) {
    fn(arg);
  }
};
</script>

<style lang="scss" scoped></style>
