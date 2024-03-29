<template>
  <el-form
    :rules="rules"
    :model="_roleFormData"
    ref="_ruleFormRef"
    status-icon
    label-position="top"
    require-asterisk-position="right"
    style="width: 100%"
  >
    <el-row :gutter="10">
      <el-col :span="24">
        <el-form-item label="名称" label-width="100px" prop="name">
          <el-input v-model="_roleFormData.name" v-if="editInfo" />
          <span v-if="!editInfo">{{ _roleData.name }}</span>
        </el-form-item>
      </el-col> </el-row
    ><!--    <el-row :gutter="10">
      <el-col :span="24">
        <el-form-item label="描述" label-width="100px" prop="description">
          <el-input v-model="_roleFormData.description" v-if="editInfo" />
          <span v-if="!editInfo">{{ _roleData.description }}</span>
        </el-form-item>
      </el-col>
    </el-row>-->

    <el-row :gutter="10" v-if="createNew">
      <el-col :span="24">
        <el-form-item label="继承角色" label-width="100px" prop="parentRoleId">
          <el-select style="width: 100%" v-model="_roleData.parentRoleId">
            <el-option
              v-for="baseRole in originRoles"
              :key="baseRole.id"
              :label="baseRole.name"
              :value="baseRole.id"
              :disabled="!createNew && baseRole.id !== _roleData.parentRoleId"
            >
              {{ baseRole.name }}
            </el-option>
          </el-select>
        </el-form-item>
      </el-col>
    </el-row>
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
      //创建时默认值
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

defineExpose({ init });
</script>
<style lang="scss"></style>
