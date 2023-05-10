<script setup lang="ts">
import { ref } from "vue";

const props = withDefaults(
  defineProps<{
    direction?: "rtl" | "ltr" | "ttb" | "btt";
    showClose?: boolean;
    showCancel?: boolean;
    confirmBtnName?: string;
    title?: string;
    size?: string | number;
    disableBtn?: boolean;
  }>(),
  {
    direction: "rtl",
    showClose: true,
    showCancel: true,
    confirmBtnName: "确认",
    size: 840,
    disableBtn: false,
  }
);

const emit = defineEmits(["update:show", "cancel", "confirm"]);

const _show = ref<boolean>(false);

function cancel() {
  if (props.disableBtn) {
    return;
  }
  close();
  emit("cancel");
}

function open() {
  _show.value = true;
}
function close() {
  _show.value = false;
}

function confirm() {
  emit("confirm");
}

defineExpose({ open, close });
</script>

<template>
  <div class="ce-drawer">
    <el-drawer
      v-model="_show"
      :direction="direction"
      :showClose="false"
      :size="size"
      destroy-on-close
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <template #header>
        <div class="header">
          <slot name="header">
            <div class="title">{{ title }}</div>
          </slot>
          <div></div>
          <el-icon
            class="close-btn"
            size="16px"
            v-if="showClose"
            @click="cancel"
          >
            <Close />
          </el-icon>
        </div>
      </template>
      <template #default>
        <slot name="default"></slot>
      </template>
      <template #footer>
        <slot name="footer">
          <div style="flex: auto">
            <el-button @click="cancel" v-if="showCancel" :disabled="disableBtn">
              取消
            </el-button>
            <el-button type="primary" @click="confirm" :disabled="disableBtn">
              {{ confirmBtnName }}
            </el-button>
          </div>
        </slot>
      </template>
    </el-drawer>
  </div>
</template>

<style lang="scss" scoped>
.ce-drawer {
  --el-drawer-padding-primary: 24px;

  :deep(.el-drawer__header) {
    //border-bottom: #d5d6d8 solid 1px;
    margin-bottom: 0;
    padding-top: 0;
    height: 56px;

    &::after {
      content: "";
      position: absolute;
      top: 56px;
      bottom: auto;
      right: auto;
      left: var(--el-drawer-padding-primary);
      height: 1px;
      width: calc(
        100% - var(--el-drawer-padding-primary) -
          var(--el-drawer-padding-primary)
      );
      background-color: #d5d6d8;
    }
  }

  :deep(.el-drawer__footer) {
    box-shadow: 0px -1px 4px rgba(31, 35, 41, 0.1);
    height: 80px;
    padding-top: 0;
    padding-bottom: 0;
    display: flex;
    align-items: center;
    flex-direction: row;
    flex-wrap: nowrap;
  }
}

.header {
  display: flex;
  align-items: center;
  flex-direction: row;
  flex-wrap: nowrap;
  justify-content: space-between;

  .title {
    font-style: normal;
    font-weight: 500;
    font-size: 16px;
    line-height: 24px;

    color: #1f2329;
  }

  .close-btn {
    cursor: pointer;
  }
}
</style>
