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
    <el-form :model="from" :inline="true" ref="ruleFormRef" status-icon>
      <layout-container :border="false">
        <template #content>
          <layout-container>
            <template #header><h4>基本信息</h4></template>
            <template #content>
              <div v-for="(org, index) in from.orgDetails" :key="index">
                <el-form-item
                  label="名称"
                  :prop="'orgDetails[' + index + '].name'"
                  style="width: 40%"
                  :rules="rules.name"
                >
                  <el-input v-model="org.name" />
                </el-form-item>
                <el-form-item
                  label="描述"
                  :prop="'orgDetails[' + index + '].description'"
                  style="width: 40%"
                  :rules="rules.description"
                >
                  <el-input v-model="org.description" />
                </el-form-item>
                <el-form-item v-if="index === from.orgDetails.length - 1">
                  <ce-icon
                    style="cursor: pointer; height: 20px; width: 20px"
                    code="Plus"
                    @click="addOrgItem(ruleFormRef)"
                  ></ce-icon>
                </el-form-item>
                <el-form-item v-if="from.orgDetails.length > 1">
                  <ce-icon
                    style="cursor: pointer; height: 20px; width: 20px"
                    code="Minus"
                    @click="deleteOrgItem(ruleFormRef, org, index)"
                  ></ce-icon>
                </el-form-item>
              </div>
            </template>
          </layout-container>
          <layout-container>
            <template #header><h4>所属组织</h4></template>
            <template #content>
              <el-form-item label="组织" style="width: 80%">
                <el-tree-select
                  filterable
                  :filter-method="filterMethod"
                  :props="{ label: 'name' }"
                  node-key="id"
                  v-model="from.pid"
                  :data="orientationData"
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
import { ElMessage } from "element-plus";
import { ref, onMounted, reactive } from "vue";
import { tree, OrganizationTree, batch } from "@/api/organization";
import { useRouter } from "vue-router";
import type { FormInstance, FormRules } from "element-plus";
import type { CreateOrgFrom } from "./type";
import { useI18n } from "vue-i18n";
const { t } = useI18n();
const router = useRouter();
const ruleFormRef = ref<FormInstance>();
/**
 *组织树数据
 */
const orientationData = ref<Array<OrganizationTree>>();
/**
 *元数据
 */
const sourceData = ref<Array<OrganizationTree>>();
onMounted(() => {
  tree().then((data) => {
    orientationData.value = data.data;
    sourceData.value = [...orientationData.value];
  });
});

const rules = reactive<FormRules>({
  name: [
    {
      message: "组织名称不为空",
      trigger: "blur",
      type: "string",
      required: true,
    },
  ],
  description: [
    {
      message: "组织描述不为空",
      trigger: "blur",
      type: "string",
      required: true,
    },
  ],
});

const submitForm = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid, fields) => {
    if (valid) {
      batch(from.value).then(() => {
        router.push({ name: "org" });
        ElMessage.success(t("commons.msg.save_success"));
      });
    }
  });
};
const from = ref<CreateOrgFrom>({
  pid: undefined,
  orgDetails: [{ name: "", description: "" }],
});

/**
 *删除一个组织详情对象
 */
const deleteOrgItem = (
  formEl: FormInstance | undefined,
  org: { name: string; description: string },
  index: number
) => {
  from.value.orgDetails.splice(index, 1);
};
/**
 * 添加一个组织详情对象
 */
const addOrgItem = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid, fields) => {
    if (valid) {
      from.value.orgDetails.push({ name: "", description: "" });
    }
  });
};
const filterMethod = (value: string) => {
  if (orientationData.value) {
    orientationData.value = sourceData.value?.filter((item) => {
      return item.name.includes(value);
    });
  }
};
</script>
<style lang="scss"></style>
