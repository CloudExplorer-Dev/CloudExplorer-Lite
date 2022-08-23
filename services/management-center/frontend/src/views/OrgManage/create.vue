<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb
        :breadcrumbs="[
          { to: { name: 'org' }, title: '组织管理' },
          { to: {}, title: '创建' },
        ]"
      ></breadcrumb>
    </template>
    <el-form
      :model="from"
      :inline="true"
      :rules="rules"
      ref="ruleFormRef"
      status-icon
    >
      <layout-container :border="false">
        <template #content>
          <layout-container>
            <template #header><h4>基本信息</h4></template>
            <template #content>
              <el-form-item label="名称" prop="name" style="width: 40%">
                <el-input v-model="from.name" />
              </el-form-item>
              <el-form-item label="描述" prop="description" style="width: 40%">
                <el-input v-model="from.description" />
              </el-form-item>
            </template>
          </layout-container>
          <layout-container>
            <template #header><h4>所属组织</h4></template>
            <template #content>
              <el-form-item label="组织" style="width: 80%">
                <el-tree-select
                  :props="{ label: 'name' }"
                  node-key="id"
                  v-model="from.org"
                  :data="tableData"
                  show-checkbox
                  style="width: 100%"
                  check-strictly
                  :render-after-expand="false"
                />
              </el-form-item>
            </template>
          </layout-container>
          <layout-container>
            <el-button>取消</el-button>
            <el-button type="primary" @click="submitForm(ruleFormRef)"
              >保存</el-button
            ></layout-container
          >
        </template>
      </layout-container>
    </el-form>
  </layout-content>
</template>
<script setup lang="ts">
import { ref, onMounted, reactive } from "vue";
import { listAllOrganization, Organization } from "@/api/organization";
import type { FormInstance, FormRules } from "element-plus";
const ruleFormRef = ref<FormInstance>();

interface OrganizationTree extends Organization {
  children: Array<OrganizationTree>;
}
const tableData = ref<Array<OrganizationTree>>();
/**
 * 将数据转换为树状
 * @param organizations
 */
const resetData = (organizations: Array<Organization>) => {
  const newOrganizations: Array<OrganizationTree> = organizations.map((org) => {
    return { ...org, children: [] };
  });
  const result: Array<OrganizationTree> = [];
  newOrganizations.forEach((item) => {
    if (item.pid) {
      const parentOrganization = newOrganizations.find((org) => {
        return org.id === item.pid;
      });
      if (parentOrganization) {
        parentOrganization.children.push(item);
      }
    } else {
      result.push(item);
    }
  });
  return result;
};

onMounted(() => {
  listAllOrganization().then((data) => {
    tableData.value = resetData(data.data);
  });
});

const rules = reactive<FormRules>({
  name: [{ message: "组织名称不为空", trigger: "blur", required: true }],
  description: [{ message: "组织描述不为空", trigger: "blur", required: true }],
});

const submitForm = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid, fields) => {
    if (valid) {
      console.log("submit!");
    } else {
      console.log("error submit!", fields);
    }
  });
};
const from = ref({ name: null, org: null, description: null });
</script>
<style lang="scss"></style>
