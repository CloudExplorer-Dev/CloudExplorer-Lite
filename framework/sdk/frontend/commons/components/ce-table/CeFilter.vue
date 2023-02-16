<template>
  <el-tag
    v-for="condition in props.allConditions"
    :key="condition.field"
    closable
    :disable-transitions="false"
    @close="clearOne(condition.field)"
    type="info"
    round
  >
    {{ condition.label }}：{{ condition.valueLabel }}
  </el-tag>
  <span class="fu-search-board" v-show="showClearAllButton" @click="clearAll">{{
    t("commons.btn.clear", "清空")
  }}</span>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const props = defineProps<{
  allConditions: {};
}>();

const emit = defineEmits(["clearOne", "clearAll"]);

/**
 * 是否展示清空所有筛选项的按钮
 */
const showClearAllButton = computed(() => {
  return Object.keys(props.allConditions).length !== 0;
});

/**
 * 清除单个筛选项
 */
const clearOne = (field: string) => {
  emit("clearOne", field);
};

/**
 *  清空所有筛选项
 */
const clearAll = () => {
  emit("clearAll");
};
</script>

<style lang="scss" scoped>
.fu-search-board {
  font-size: 12px;
  color: #999;
  cursor: pointer;
  padding-left: 5px;
}

.fu-search-board:hover {
  color: #333;
}
</style>
