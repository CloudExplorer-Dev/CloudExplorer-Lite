<template>
  <el-form
    :rules="rules"
    label-position="right"
    :model="_roleFormData"
    :inline="false"
    ref="_ruleFormRef"
    status-icon
  >
    <el-form-item label="名称" label-width="100px" prop="name">
      <el-col :span="14">
        <el-input v-model="_roleFormData.name" v-if="editInfo" />
        <span v-if="!editInfo">{{ _roleData.name }}</span>
      </el-col>
    </el-form-item>

    <el-form-item label="描述" label-width="100px" prop="description">
      <el-col :span="14">
        <el-input v-model="_roleFormData.description" v-if="editInfo" />
        <span v-if="!editInfo">{{ _roleData.description }}</span>
      </el-col>
    </el-form-item>

    <el-form-item label="继承角色" label-width="100px" prop="parentRoleId">
      <el-radio-group v-model="_roleData.parentRoleId">
        <el-radio-button
          v-for="baseRole in originRoles"
          :key="baseRole.id"
          :label="baseRole.id"
          :disabled="!createNew && baseRole.id !== _roleData.parentRoleId"
        >
          {{ baseRole.name }}
        </el-radio-button>
      </el-radio-group>
    </el-form-item>
  </el-form>
</template>
<script setup lang="ts">
import RoleApi from "@/api/role";

const props = defineProps<{
  id?: string;
  loading: boolean;
  editInfo: boolean;
  roleData: Role;
  roleFormData: UpdateRoleRequest;
  ruleFormRef: FormInstance;
  createNew?: boolean;
}>();

const emit = defineEmits([
  "update:roleFormData",
  "update:ruleFormRef",
  "update:roleData",
  "update:loading",
]);

import { computed, onMounted, reactive, ref, watch } from "vue";
import type { FormRules, FormInstance } from "element-plus";
import { Role } from "@commons/api/role/type";
import { UpdateRoleRequest } from "@/api/role/type";
import { useI18n } from "vue-i18n";

const { t } = useI18n();

const originRoles = ref<Array<Role>>([]);

const _loading = ref(props.loading);

const _roleData = computed({
  get() {
    return props.roleData;
  },
  set(value) {
    emit("update:roleData", value);
  },
});

const _roleFormData = computed({
  get() {
    return props.roleFormData;
  },
  set(value) {
    emit("update:roleFormData", value);
  },
});

const _ruleFormRef = computed({
  get() {
    return props.ruleFormRef;
  },
  set(value) {
    emit("update:ruleFormRef", value);
  },
});

const isEdit = computed(() => {
  return props.editInfo;
});

watch(
  isEdit,
  (isEdit) => {
    //恢复表单内输入框值
    _roleFormData.value = new UpdateRoleRequest(
      _roleData.value.id,
      _roleData.value.name,
      _roleData.value.description
    );
  },
  { immediate: true }
);

const rules = reactive<FormRules>({
  name: [
    {
      message: "名称不为空",
      trigger: "blur",
      type: "string",
      required: true,
    },
  ],
});

onMounted(() => {
  init();
});

function init() {
  RoleApi.listRoles({ type: "origin" }, _loading).then((ok) => {
    originRoles.value = ok.data;
    if (props.createNew) {
      console.log("create new");
      //新建时默认值
      _roleData.value.parentRoleId = originRoles.value[0]?.parentRoleId;
    }
  });

  //当前角色
  if (props.id) {
    RoleApi.getRoleById(props.id, _loading).then((ok) => {
      _roleData.value = ok.data;
    });
  }
}
</script>
<style lang="scss"></style>
