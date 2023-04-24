<script setup lang="ts">
import { computed, ref } from "vue";
import { type UpdateUserStatusRequest, User } from "@/api/user/type";
import { changeUserStatus } from "@/api/user";
import { ElMessage } from "element-plus";
import { useI18n } from "vue-i18n";
const { t } = useI18n();

const props = defineProps<{
  finalFunction: any;
  functionProps: User;
}>();

const emit = defineEmits(["update:functionProps"]);

const value = computed({
  get() {
    return !!props.functionProps.enabled;
  },
  set(value) {
    emit("update:functionProps", value);
  },
});

const loading = ref<boolean>(false);

/**
 * 启停用户
 */
const handleSwitchStatus = (row: User) => {
  loading.value = true;
  changeUserStatus(row as UpdateUserStatusRequest)
    .then((res) => {
      ElMessage.success(t("commons.msg.op_success"));
    })
    .catch((e) => {
      console.error(e);
      props.finalFunction();
    })
    .finally(() => {
      loading.value = false;
    });
};
</script>

<template>
  <el-switch
    v-model="value"
    :loading="loading"
    @click.prevent="handleSwitchStatus(functionProps)"
  />
</template>

<style lang="scss" scoped></style>
