<template>
  <template v-if="!confirm">
    <div>
      <el-form-item label="登录方式">
        <el-radio-group v-model="type">
          <el-radio-button label="default">默认密码</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="登录名"> 默认为模板的用户名 </el-form-item>
      <el-form-item label="密码"> 默认为模板的密码 </el-form-item>
    </div>
  </template>
  <template v-else>
    <el-descriptions direction="vertical" :column="3">
      <el-descriptions-item label="登录方式" width="33.33%">
        默认密码
      </el-descriptions-item>
      <el-descriptions-item label="登录名" width="33.33%">
        默认为模板的用户名
      </el-descriptions-item>
      <el-descriptions-item label="密码" width="33.33%">
        默认为模板的密码
      </el-descriptions-item>
    </el-descriptions>
  </template>
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
