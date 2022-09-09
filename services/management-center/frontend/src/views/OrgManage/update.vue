<template>
  <el-form
    :rules="rules"
    :model="from"
    :inline="true"
    ref="ruleFormRef"
    status-icon
  >
    <layout-container :border="false">
      <template #content>
        <layout-container>
          <template #header><h4>基本信息</h4></template>
          <template #content>
            <el-form-item label="名称" :prop="'name'" style="width: 40%">
              <el-input v-model="from.name" />
            </el-form-item>
            <el-form-item
              label="描述"
              :prop="'description'"
              style="width: 40%"
              :rules="rules.description"
            >
              <el-input v-model="from.description" />
            </el-form-item>
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
</template>
<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import type { FormRules, FormInstance } from "element-plus";
import { ElMessage } from "element-plus";
import type { UpdateForm } from "./type";
import {
  tree,
  type OrganizationTree,
  getOrgById,
  updateOrg,
} from "@/api/organization";
import { useI18n } from "vue-i18n";
const { t } = useI18n();
const ruleFormRef = ref<FormInstance>();
const route = useRouter();
interface OrganizationTreeDisabled extends OrganizationTree {
  disabled?: boolean;
}
/**
 *组织数据
 */
const orientationData = ref<Array<OrganizationTreeDisabled>>();
/**
 *元数据
 */
const sourceData = ref<Array<OrganizationTreeDisabled>>();
onMounted(() => {
  const id: unknown = route.currentRoute.value.query.id;
  getOrgById(id as string).then((data) => {
    from.value = data.data;
  });
  tree().then((data) => {
    orientationData.value = resetData(data.data, id as string, false);
    sourceData.value = [...orientationData.value];
  });
});

/**
 *添加 disabled
 * 修改的时候不能修改到
 */
const resetData: (
  organizationTree: Array<OrganizationTreeDisabled>,
  updateOrganizationId: string,
  disabled: boolean
) => Array<OrganizationTreeDisabled> = (
  organizationTree,
  updateOrganizationId,
  disabled
) => {
  organizationTree.forEach((item) => {
    item.disabled = !disabled ? updateOrganizationId === item.id : disabled;
    if (item.children) {
      resetData(
        item.children,
        updateOrganizationId,
        !disabled ? updateOrganizationId === item.id : disabled
      );
    }
  });
  return organizationTree;
};

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

const from = ref<UpdateForm>({
  pid: undefined,
  name: "",
  id: "",
  description: "",
});

const submitForm = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid) => {
    if (valid) {
      updateOrg(from.value).then((ok) => {
        route.push({ name: "org" });
        ElMessage.success(t("commons.msg.save_success"));
      });
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
