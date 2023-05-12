<template>
  <CeDrawer
    ref="editOrgDrawerRef"
    title="编辑组织"
    confirm-btn-name="保存"
    @confirm="submitForm"
    @cancel="cancelEditOrg"
    :disable-btn="loading"
  >
    <el-form
      :rules="rules"
      :model="form"
      ref="ruleFormRef"
      status-icon
      scroll-to-error
      v-loading="loading"
    >
      <FormTitle>{{ t("commons.basic_info", "基本信息") }}</FormTitle>

      <div style="margin-bottom: 32px">
        <div class="edit-org-form-item">
          <el-form-item
            :prop="'name'"
            class="form-item"
            :label="t('commons.name', '名称')"
          >
            <el-input v-model="form.name" style="width: 280px" />
          </el-form-item>

          <el-form-item
            :prop="'description'"
            :rules="rules.description"
            class="form-item"
            :label="t('commons.description', '描述')"
          >
            <el-input v-model="form.description" style="width: 320px" />
          </el-form-item>

          <div style="width: 16px; height: 16px"></div>
        </div>
      </div>

      <FormTitle>
        {{ t("org_manage.affiliated_organization", "所属组织") }}
      </FormTitle>

      <div style="margin-bottom: 8px">{{ t("commons.org", "组织") }}</div>
      <el-form-item>
        <el-tree-select
          filterable
          :filter-method="filterMethod"
          :props="{ label: 'name' }"
          node-key="id"
          v-model="form.pid"
          :data="orientationData"
          show-checkbox
          style="width: 100%"
          check-strictly
          :render-after-expand="false"
        />
      </el-form-item>
    </el-form>
  </CeDrawer>
</template>
<script setup lang="ts">
import { reactive, ref } from "vue";
import type { FormRules, FormInstance } from "element-plus";
import { ElMessage } from "element-plus";
import organizationApi from "@/api/organization";
import type { OrganizationTree, OrgUpdateForm } from "@/api/organization/type";
import { useI18n } from "vue-i18n";
import FormTitle from "@/componnets/form_title/FormTitle.vue";
import CeDrawer from "@commons/components/ce-drawer/index.vue";

// 国际化实例
const { t } = useI18n();
// 校验实例对象
const ruleFormRef = ref<FormInstance>();

const loading = ref<boolean>(false);

const editOrgDrawerRef = ref<InstanceType<typeof CeDrawer>>();

const emit = defineEmits(["submit"]);
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
});
// 表单数据
const form = ref<OrgUpdateForm>({
  pid: undefined,
  name: "",
  id: "",
  description: "",
});

function clear() {
  form.value = {
    pid: undefined,
    name: "",
    id: "",
    description: "",
  };
}

function setData(id: string) {
  organizationApi.getOrgById(id as string).then((data) => {
    form.value = data.data;
  });
  organizationApi.tree().then((data) => {
    orientationData.value = resetData(data.data, id as string, false);
    sourceData.value = [...orientationData.value];
  });
}

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
const submitForm = () => {
  if (!ruleFormRef.value) return;
  ruleFormRef.value?.validate((valid) => {
    if (valid) {
      organizationApi.updateOrg(form.value).then(() => {
        ElMessage.success(t("commons.msg.save_success"));
        cancelEditOrg();
        emit("submit");
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

function open() {
  editOrgDrawerRef.value?.open();
}

function cancelEditOrg() {
  editOrgDrawerRef.value?.close();
  clear();
}

defineExpose({ open, clear, setData });
</script>
<style lang="scss" scoped>
.edit-org-form-item {
  width: calc(100% - 24px);
  background: #f7f9fc;
  border-radius: 4px;
  padding: 12px;
  margin-top: 0;
  margin-bottom: 14px;
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  justify-content: space-between;

  .form-item {
    margin-bottom: 0;
  }
}
</style>
