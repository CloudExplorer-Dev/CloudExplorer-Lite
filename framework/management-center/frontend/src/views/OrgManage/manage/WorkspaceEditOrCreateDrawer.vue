<!--创建编辑工作空间-->
<script setup lang="ts">
import organizationApi from "@/api/organization";
import FormTitle from "@/componnets/form_title/FormTitle.vue";
import { ref, reactive, nextTick } from "vue";
import type { OrganizationTree } from "@/api/organization/type";
import WorkspaceApi from "@/api/workspace";
import type { FormInstance, FormRules } from "element-plus";
import { useI18n } from "vue-i18n";
import type { CreateWorkspaceForm } from "@/api/workspace/type";
import CeIcon from "@commons/components/ce-icon/index.vue";
import CeDrawer from "@commons/components/ce-drawer/index.vue";
import { ElMessage } from "element-plus";

const { t } = useI18n();
const loading = ref<boolean>(false);
//表单验证规则
const ruleFormRef = ref<FormInstance>();
//定义表单数据
const form = ref<CreateWorkspaceForm>({
  organizationId: undefined,
  workspaceDetails: [{ id: undefined, name: "", description: "" }],
});
//所属组织下拉框数据
const orgSelectData = ref<Array<OrganizationTree>>();
//定义组织树选择数据
const workspaceOrgSelectTree = ref();
//默认父级组织选中
const defaultCheckedKeys = ref<Array<string>>([]);

const workspaceDrawerRef = ref<InstanceType<typeof CeDrawer>>();

//表单验证
const rules = reactive<FormRules>({
  name: [
    {
      message: t("commons.validate.required", [t("workspace.workspace_name")]),
      trigger: "blur",
      required: true,
    },
    {
      pattern: /^.{2,128}$/,
      message: t("commons.validate.limit", ["2", "128"]),
      trigger: "blur",
    },
    {
      //验证名字是否重复输入
      asyncValidator: (rule, value, callback) => {
        let repetition = 0;
        form.value.workspaceDetails.forEach((w) => {
          if (w.name === value) {
            repetition += 1;
          }
        });
        if (repetition > 1) {
          callback(
            new Error(
              t("workspace.validate.repeat", [t("workspace.workspace_name")])
            )
          );
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
  description: [
    {
      pattern: /^.{2,128}$/,
      message: t("commons.validate.limit", ["2", "128"]),
      trigger: "blur",
    },
  ],
  org: [
    {
      asyncValidator: (rule, value, callback) => {
        // 在此获取选中的树形数据
        const arr = workspaceOrgSelectTree.value.getCheckedKeys();
        if (arr.length == 0 || !arr) {
          callback(
            new Error(t("commons.validate.select", [t("workspace.org")]))
          );
        } else {
          callback();
        }
      },
      trigger: "blur",
      required: true,
    },
  ],
});

const emit = defineEmits(["submit"]);

//提交表单
const submitForm = () => {
  if (!ruleFormRef.value) return;
  ruleFormRef.value?.validate((valid, fields) => {
    console.log("submit form data:", form);

    if (valid) {
      console.log("submit data:", form);
      if (form.value.workspaceDetails[0].id) {
        //修改了父级组织
        form.value.workspaceDetails[0].organizationId =
          form.value.organizationId;
        WorkspaceApi.update(form.value.workspaceDetails[0], loading).then(
          (res) => {
            ElMessage.success(t("commons.msg.save_success"));
            cancelWorkspace();
            emit("submit");
          }
        );
      } else {
        WorkspaceApi.batch(form.value, loading).then((res) => {
          ElMessage.success(t("commons.msg.save_success"));
          cancelWorkspace();
          emit("submit");
        });
      }
    }
  });
};

/**
 *删除一个详情对象
 */
const deleteItem = (
  formEl: FormInstance | undefined,
  org: { name: string; description: string },
  index: number
) => {
  form.value.workspaceDetails.splice(index, 1);
};
/**
 * 添加一个详情对象
 */
const addItem = (formEl: FormInstance | undefined) => {
  //不验证直接添加
  form.value.workspaceDetails.push({ name: "", description: "" });
};

/**
 * 从原数据里面过滤出于value相同的数据
 * @param value   value
 */
const filterMethod = (value: string) => {
  if (orgSelectData.value) {
    orgSelectData.value = sourceData.value?.filter((item) => {
      return item.name.includes(value);
    });
  }
};

/**
 * 组织下拉框过滤
 */
const sourceData = ref<Array<OrganizationTree>>();

function clear() {
  form.value = {
    organizationId: undefined,
    workspaceDetails: [{ id: undefined, name: "", description: "" }],
  };
}

const isEdit = ref<boolean>(false);

function open(workspaceId?: string) {
  isEdit.value = false;
  workspaceDrawerRef.value?.open();

  //查询组织树
  organizationApi.tree().then((data) => {
    orgSelectData.value = data.data;
    sourceData.value = [...orgSelectData.value];
  });

  //工作空间ID
  if (workspaceId) {
    isEdit.value = true;
    //查询工作空间信息
    WorkspaceApi.getWorkspaceById(workspaceId as string, loading)
      .then((res) => {
        //置空只保留要编辑的工作空间
        form.value.workspaceDetails = [];
        //请求成功给form设置数据
        form.value.workspaceDetails.push({
          id: res.data.id,
          name: res.data.name,
          organizationId: res.data.organizationId,
          description: res.data.description,
        });
        form.value.organizationId = res.data.organizationId;
        //设置默认选中组织
        defaultCheckedKeys.value = [form.value.organizationId];
        nextTick(() => {
          workspaceOrgSelectTree.value?.setCheckedKeys(
            defaultCheckedKeys.value,
            true
          );
        });
      })
      .catch((err) => {
        console.log(err);
      });
  }
}

function setOrgId(id: string) {
  form.value.organizationId = id;

  defaultCheckedKeys.value = [form.value.organizationId];
  nextTick(() => {
    workspaceOrgSelectTree.value?.setCheckedKeys(
      defaultCheckedKeys.value,
      true
    );
  });
}

function cancelWorkspace() {
  workspaceDrawerRef.value?.close();
  clear();
  isEdit.value = false;
}

defineExpose({ open, clear, setOrgId });
</script>
<template>
  <CeDrawer
    ref="workspaceDrawerRef"
    :title="(isEdit ? '编辑' : '创建') + '工作空间'"
    :confirm-btn-name="isEdit ? '保存' : '创建'"
    @confirm="submitForm"
    @cancel="cancelWorkspace"
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
        <template v-for="(item, index) in form.workspaceDetails" :key="index">
          <div class="add-workspace-form-item">
            <el-form-item
              class="form-item"
              :prop="'workspaceDetails[' + index + '].name'"
              :rules="rules.name"
              :label="t('commons.name', '名称')"
            >
              <el-input v-model="item.name" style="width: 280px" />
            </el-form-item>
            <el-form-item
              class="form-item"
              :prop="'workspaceDetails[' + index + '].description'"
              :rules="rules.description"
              :label="t('commons.description', '描述')"
            >
              <el-input v-model="item.description" style="width: 320px" />
            </el-form-item>

            <div
              v-if="form.workspaceDetails.length <= 1"
              style="width: 16px; height: 16px"
            ></div>
            <CeIcon
              style="cursor: pointer"
              size="16px"
              code="icon_delete-trash_outlined1"
              v-if="form.workspaceDetails.length > 1"
              @click="deleteItem(ruleFormRef, item, index)"
            />
          </div>
        </template>
        <el-button
          @click="addItem(ruleFormRef)"
          link
          type="primary"
          v-if="!isEdit"
        >
          + 添加工作空间
        </el-button>
      </div>

      <FormTitle>{{ t("workspace.org") }}</FormTitle>

      <div style="margin-bottom: 8px" class="label-required">
        {{ t("commons.org", "组织") }}
      </div>
      <el-form-item prop="org">
        <el-tree-select
          ref="workspaceOrgSelectTree"
          filterable
          :props="{ label: 'name' }"
          node-key="id"
          v-model="form.organizationId"
          :data="orgSelectData"
          collapse-tags
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
<style lang="scss" scoped>
.add-workspace-form-item {
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
