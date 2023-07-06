<template>
  {{ formItem.label }}
  <el-tooltip effect="dark" v-if="formItem.hint" :key="formItem.hint">
    <template #content>
      <div class="tooltip-title" v-if="hint.title">
        {{ hint.title }}
      </div>
      <pre class="tooltip-content" v-if="hint.content">{{ hint.content }}</pre>
    </template>
    <CeIcon code="icon-maybe_outlined" size="1em" />
  </el-tooltip>
  <div
    style="float: right; color: var(--el-color-primary); cursor: pointer"
    v-if="formItem.extraInfo"
    :key="formItem.extraInfo"
    @click="jumpTo(extraInfo?.url)"
  >
    {{ extraInfo?.text }}
  </div>
</template>
<script setup lang="ts">
import { computed } from "vue";
import CeIcon from "@commons/components/ce-icon/index.vue";
import type { FormView } from "@commons/components/ce-form/type";
const props = defineProps<{
  // 表单Item
  formItem: FormView;
}>();
const hint = computed(() => {
  if (props.formItem.hint) {
    return JSON.parse(props.formItem.hint);
  }
  return {};
});
const extraInfo = computed(() => {
  if (props.formItem.extraInfo) {
    return JSON.parse(props.formItem.extraInfo);
  }
  return {};
});

function jumpTo(url: string) {
  if (url) {
    window.open(url, "_blank");
  }
}
</script>
<style lang="scss" scoped></style>
