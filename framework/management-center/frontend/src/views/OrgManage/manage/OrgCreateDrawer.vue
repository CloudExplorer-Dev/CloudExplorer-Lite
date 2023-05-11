<template>
  <CeDrawer
    ref="addOrgDrawerRef"
    title="创建组织"
    confirm-btn-name="创建"
    @confirm="submitForm"
    @cancel="cancelAddOrg"
    :disable-btn="loading"
  >
    <el-form
      :model="form"
      :rules="rules"
      ref="ruleFormRef"
      status-icon
      scroll-to-error
      v-loading="loading"
    >
      <FormTitle>{{ t("commons.basic_info", "基本信息") }}</FormTitle>
      <div style="margin-bottom: 32px">
        <template v-for="(org, index) in form.orgDetails" :key="index">
          <div class="add-org-form-item">
            <el-form-item
              class="form-item"
              :prop="'orgDetails[' + index + '].name'"
              :rules="rules.name"
              :label="t('commons.name', '名称')"
            >
              <el-input v-model="org.name" style="width: 280px" />
            </el-form-item>
            <el-form-item
              class="form-item"
              :prop="'orgDetails[' + index + '].description'"
              :rules="rules.description"
              :label="t('commons.description', '描述')"
            >
              <el-input v-model="org.description" style="width: 320px" />
            </el-form-item>

            <div
              v-if="form.orgDetails.length <= 1"
              style="width: 16px; height: 16px"
            ></div>
            <CeIcon
              style="cursor: pointer"
              size="16px"
              code="icon_delete-trash_outlined1"
              v-if="form.orgDetails.length > 1"
              @click="deleteOrgItem(ruleFormRef, org, index)"
            />
          </div>
        </template>

        <el-button @click="addOrgItem(ruleFormRef)" link type="primary">
          + 添加组织
        </el-button>
      </div>

      <FormTitle>
        {{ t("org_manage.affiliated_organization", "所属组织") }}
      </FormTitle>

      <div style="margin-bottom: 8px">{{ t("commons.org", "组织") }}</div>
      <el-form-item>
        <el-tree-select
          ref="workspaceOrgSelectTree"
          filterable
          :filter-method="filterMethod"
          :props="{ label: 'name' }"
          node-key="id"
          v-model="form.pid"
          :data="orientationData"
          show-checkbox
          check-strictly
          :render-after-expand="false"
          :default-checked-keys="defaultCheckedKeys"
          :default-expanded-keys="defaultCheckedKeys"
          style="width: 100%"
        />
      </el-form-item>
    </el-form>
  </CeDrawer>
</template>
<script setup lang="ts">
import { ElMessage } from "element-plus";
import { ref, reactive, nextTick } from "vue";
import organizationApi from "@/api/organization";
import type { OrganizationTree, CreateOrgForm } from "@/api/organization/type";
import type { FormInstance, FormRules } from "element-plus";
import CeIcon from "@commons/components/ce-icon/index.vue";
import { useI18n } from "vue-i18n";
import FormTitle from "@/componnets/form_title/FormTitle.vue";
import CeDrawer from "@commons/components/ce-drawer/index.vue";

const loading = ref<boolean>(false);
const defaultCheckedKeys = ref<Array<string>>([]);
const workspaceOrgSelectTree = ref();

const { t } = useI18n();
// 校验实例对象
const ruleFormRef = ref<FormInstance>();
// 组织树数据
const orientationData = ref<Array<OrganizationTree>>();
//元数据
const sourceData = ref<Array<OrganizationTree>>();
//校验对象
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
// 表单数据初始化
const form = ref<CreateOrgForm>({
  pid: undefined,
  orgDetails: [{ name: "", description: "" }],
});

function clear() {
  form.value = {
    pid: undefined,
    orgDetails: [{ name: "", description: "" }],
  };
}

const addOrgDrawerRef = ref<InstanceType<typeof CeDrawer>>();

const emit = defineEmits(["submit"]);

/**
 * 保存掉用函数
 * @param formEl 校验对象
 */
const submitForm = () => {
  if (!ruleFormRef.value) return;
  ruleFormRef.value?.validate((valid) => {
    if (valid) {
      organizationApi.batchSave(form.value, loading).then(() => {
        ElMessage.success(t("commons.msg.save_success"));
        cancelAddOrg();
        emit("submit");
      });
    }
  });
};

function open() {
  addOrgDrawerRef.value?.open();
  organizationApi.tree().then((data) => {
    orientationData.value = data.data;
    sourceData.value = [...orientationData.value];
  });
}

function cancelAddOrg() {
  addOrgDrawerRef.value?.close();
  clear();
}
/**
 * 从form表单里面删除一个组织
 * @param formEl 校验对象
 * @param org    组织数据
 * @param index  当前组织所在下标
 */
const deleteOrgItem = (
  formEl: FormInstance | undefined,
  org: { name: string; description: string },
  index: number
) => {
  form.value.orgDetails.splice(index, 1);
};
/**
 * 在form表单里面添加一个组织
 * @param formEl 校验对象
 */
const addOrgItem = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid) => {
    if (valid) {
      form.value.orgDetails.push({ name: "", description: "" });
    }
  });
};
/**
 * 从原数据里面过滤出于value相同的数据
 * @param value   value
 */
const filterMethod = (value: string) => {
  if (orientationData.value) {
    orientationData.value = sourceData.value?.filter((item) => {
      return item.name.includes(value);
    });
  }
};

function setPid(id?: string) {
  if (id) {
    form.value.pid = id;
    defaultCheckedKeys.value = [form.value.pid];

    nextTick(() => {
      workspaceOrgSelectTree.value?.setCheckedKeys(
        defaultCheckedKeys.value,
        true
      );
    });
  }
}

defineExpose({ open, clear, setPid });
</script>
<style lang="scss" scoped>
.add-org-form-item {
  width: calc(100% - 24px);
  background: #f7f9fc;
  border-radius: 4px;
  padding: 12px;
  margin-top: 28px;
  margin-bottom: 14px;
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: center;
  justify-content: space-between;

  .form-item {
    margin-bottom: 0;
  }

  &:first-child {
    margin-top: 0;
  }
}

.label-required:after {
  content: "*";
  color: var(--el-color-danger);
  margin-left: 4px;
}
</style>
