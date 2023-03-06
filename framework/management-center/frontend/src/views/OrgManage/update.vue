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
          <template #header
            ><h4>{{ t("commons.basic_info", "基本信息") }}</h4></template
          >
          <template #content>
            <el-form-item
              :label="t('commons.name', '名称')"
              :prop="'name'"
              style="width: 40%"
            >
              <el-input v-model="from.name" />
            </el-form-item>
            <el-form-item
              :label="t('commons.description', '描述')"
              :prop="'description'"
              style="width: 40%"
              :rules="rules.description"
            >
              <el-input v-model="from.description" />
            </el-form-item>
          </template>
        </layout-container>
        <layout-container>
          <template #header
            ><h4>
              {{ t("org_manage.affiliated_organization", "所属组织") }}
            </h4></template
          >
          <template #content>
            <el-form-item :label="t('commons.org', '组织')" style="width: 80%">
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
          <el-button @click="() => route.push({ name: 'org_list' })">{{
            t("commons.btn.cancel", "取消")
          }}</el-button>
          <el-button type="primary" @click="submitForm(ruleFormRef)">{{
            t("commons.btn.save", "保存")
          }}</el-button></layout-container
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
import organizationApi from "@/api/organization";
import type { OrganizationTree, OrgUpdateForm } from "@/api/organization/type";
import { useI18n } from "vue-i18n";
// 国际化实例
const { t } = useI18n();
// 校验实例对象
const ruleFormRef = ref<FormInstance>();
// 路由实例对象
const route = useRouter();
// 组织是否禁用接口
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
// 校验规则对象
const rules = reactive<FormRules>({
  name: [
    {
      message: t("org_manage.organization_name_is_not_empty", "组织名称不为空"),
      trigger: "blur",
      type: "string",
      required: true,
    },
  ],
  description: [
    {
      message: t(
        "org_manage.organization_description_is_not_empty",
        "组织描述不为空"
      ),
      trigger: "blur",
      type: "string",
      required: true,
    },
  ],
});
// 表单数据
const from = ref<OrgUpdateForm>({
  pid: undefined,
  name: "",
  id: "",
  description: "",
});
// 挂载生命周期钩子
onMounted(() => {
  const id: unknown = route.currentRoute.value.query.id;
  organizationApi.getOrgById(id as string).then((data) => {
    from.value = data.data;
  });
  organizationApi.tree().then((data) => {
    orientationData.value = resetData(data.data, id as string, false);
    sourceData.value = [...orientationData.value];
  });
});

/**
 * 设置不能修改的数据
 * @param organizationTree     组织树
 * @param updateOrganizationId 需要更新的组织id
 * @param disabled             是否禁止修改
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

/**
 * 提交表单
 * @param formEl 表单校验对象
 */
const submitForm = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid) => {
    if (valid) {
      organizationApi.updateOrg(from.value).then(() => {
        route.push({ name: "org" });
        ElMessage.success(t("commons.msg.save_success"));
      });
    }
  });
};
/**
 * 过滤出指定value的数据
 * @param value value
 */
const filterMethod = (value: string) => {
  if (orientationData.value) {
    orientationData.value = sourceData.value?.filter((item) => {
      return item.name.includes(value);
    });
  }
};
</script>
<style lang="scss"></style>
