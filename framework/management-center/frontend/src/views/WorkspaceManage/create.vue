<!--创建编辑工作空间-->
<script setup lang="ts">
import organizationApi from "@/api/organization";
import FormTitle from "@/componnets/form_title/FormTitle.vue";

const props = defineProps<{
  id: string;
}>();
import { ref, onMounted, reactive, nextTick } from "vue";
import type { OrganizationTree } from "@/api/organization/type";
import WorkspaceApi from "@/api/workspace";
import type { FormInstance, FormRules } from "element-plus";
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import type { CreateWorkspaceForm } from "@/api/workspace/type";
import CeIcon from "@commons/components/ce-icon/index.vue";
const { t } = useI18n();
const route = useRouter();
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

//页面初始化
onMounted(() => {
  //查询组织树
  organizationApi.tree().then((data) => {
    orgSelectData.value = data.data;
    sourceData.value = [...orgSelectData.value];
  });
  //工作空间ID
  const workspaceId = props.id;
  if (workspaceId) {
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
        defaultCheckedKeys.value.push(form.value.organizationId);
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
});

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

//提交表单
const submitForm = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid, fields) => {
    console.log("submit form data:", form);

    if (valid) {
      console.log("submit data:", form);
      if (form.value.workspaceDetails[0].id) {
        //修改了父级组织
        form.value.workspaceDetails[0].organizationId =
          form.value.organizationId;
        WorkspaceApi.update(form.value.workspaceDetails[0], loading)
          .then((res) => {
            console.log("编辑成功", res);
            route.push({ name: "workspace" });
          })
          .catch((err) => {
            console.log("编辑失败:", err);
          });
      } else {
        WorkspaceApi.batch(form.value, loading)
          .then((res) => {
            console.log("保存成功", res);
            route.push({ name: "workspace" });
          })
          .catch((err) => {
            console.log("保存成功:", err);
          });
      }
    } else {
      console.log("error submit!", fields);
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
  // formEl?.validate((valid) => {
  //   if (valid) {
  //     form.value.workspaceDetails.push({ name: "", description: "" });
  //   }
  // });
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
</script>
<template>
  <el-container class="create-catalog-container" v-loading="loading">
    <el-main ref="create-catalog-container">
      <div class="form-div">
        <el-form
          :model="form"
          :rules="rules"
          ref="ruleFormRef"
          status-icon
          label-width="80px"
          label-position="top"
          require-asterisk-position="right"
        >
          <el-row>
            <el-col>
              <FormTitle>{{ t("commons.basic_info", "基本信息") }}</FormTitle>
            </el-col>
          </el-row>
          <el-row
            :gutter="10"
            v-for="(item, index) in form.workspaceDetails"
            :key="index"
          >
            <el-col :span="11">
              <el-form-item
                :label="$t('commons.name')"
                :prop="'workspaceDetails[' + index + '].name'"
                :rules="rules.name"
              >
                <el-input v-model="item.name" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                :label="$t('commons.description')"
                :prop="'workspaceDetails[' + index + '].description'"
                :rules="rules.description"
              >
                <el-input v-model="item.description" />
              </el-form-item>
            </el-col>
            <el-col :span="1" class="padding-top-30">
              <el-form-item v-if="form.workspaceDetails.length > 1">
                <div
                  class="delete-button-class"
                  @click="deleteItem(ruleFormRef, item, index)"
                >
                  <CeIcon
                    size="var(--ce-star-menu-icon-width,13.33px)"
                    code="icon_delete-trash_outlined1"
                  ></CeIcon>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="10">
            <el-col :span="24">
              <el-form-item v-if="props.id === undefined">
                <div class="add-button-class" @click="addItem(ruleFormRef)">
                  <CeIcon
                    size="var(--ce-star-menu-icon-width,13.33px)"
                    code="icon_add_outlined"
                  ></CeIcon>
                  <span class="span-class">
                    {{ t("commons.btn.add", "添加") }}
                  </span>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <FormTitle>{{ t("workspace.org") }}</FormTitle>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="23">
              <el-form-item :label="$t('commons.org')" prop="org">
                <el-tree-select
                  style="width: 100%"
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
                  ref="workspaceOrgSelectTree"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-main>
    <el-footer>
      <div class="footer">
        <div class="form-div">
          <div class="footer-btn">
            <el-button class="cancel-btn" @click="route.back">{{
              t("commons.btn.cancel")
            }}</el-button>
            <el-button
              class="save-btn"
              type="primary"
              @click="submitForm(ruleFormRef)"
              >{{ t("commons.btn.save") }}</el-button
            >
          </div>
        </div>
      </div>
    </el-footer>
  </el-container>
</template>
<style lang="scss" scoped>
//描述文字样式
.desc-text-class {
  font-size: 10px;
  color: darkgray;
}
</style>
