<template>
  <template v-if="!confirm">
    <el-form-item label="登录方式">
      <el-radio-group v-model="type">
        <el-radio-button label="default">默认密码</el-radio-button>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="登录名"> 默认为模板的用户名 </el-form-item>
    <el-form-item label="密码"> 默认为模板的密码 </el-form-item>
  </template>
  <template v-else> </template>
</template>
<script setup lang="ts">
import type { FormView } from "@commons/components/ce-form/type";
import { ref } from "vue";

const props = defineProps<{
  allData?: any;
  allFormViewData?: Array<FormView>;
  field: string;
  otherParams: any;
  formItem: FormView;
  confirm?: boolean;
}>();

const type = ref<string>("default");
</script>
<style lang="scss" scoped></style>
