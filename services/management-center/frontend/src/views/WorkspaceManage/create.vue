<!--创建编辑工作空间-->
<template>
  <el-form
    :model="form"
    :inline="true"
    :rules="rules"
    ref="ruleFormRef"
    status-icon
    class="form-class"
    label-width="80px"
  >
    <layout-container :border="false">
      <template #content>
        <layout-container>
          <template #header><h4>基本信息</h4></template>
          <template #content>
            <div v-for="(item, index) in form.workspaceDetails" :key="index">
              <el-form-item
                label="名称"
                :prop="'workspaceDetails[' + index + '].name'"
                style="width: 40%"
                :rules="rules.name"
              >
                <el-input v-model="item.name" />
              </el-form-item>
              <el-form-item
                label="描述"
                :prop="'workspaceDetails[' + index + '].description'"
                style="width: 40%"
                :rules="rules.description"
              >
                <el-input v-model="item.description" />
              </el-form-item>
              <el-form-item
                v-if="
                  index === form.workspaceDetails.length - 1 &&
                  item.id === undefined
                "
              >
                <ce-icon
                  style="cursor: pointer; height: 20px; width: 20px"
                  code="Plus"
                  @click="addItem(ruleFormRef)"
                ></ce-icon>
              </el-form-item>
              <el-form-item v-if="form.workspaceDetails.length > 1">
                <ce-icon
                  style="cursor: pointer; height: 20px; width: 20px"
                  code="Minus"
                  @click="deleteItem(ruleFormRef, item, index)"
                ></ce-icon>
              </el-form-item>
            </div>
          </template>
        </layout-container>
        <layout-container>
          <template #header><h4>所属组织</h4></template>
          <template #content>
            <el-form-item label="组织" prop="org" style="width: 80%">
              <el-tree-select
                :props="{ label: 'name' }"
                node-key="id"
                v-model="form.organizationId"
                :data="orgSeleteData"
                show-checkbox
                style="width: 100%"
                check-strictly
                :render-after-expand="false"
                :default-checked-keys="defaultCheckedKeys"
                ref="workspaceOrgSelectTree"
              />
            </el-form-item>
          </template>
        </layout-container>
        <!--先屏蔽云租户映射-->
        <!-- <layout-container style="display: none">
            <template #header>
              <h4>云租户映射</h4>
              <div class="desc-text-class" style="padding-left: 10px">
                各云平台上所有资源的企业项目属性都会按照映射关系设定，修改后可能会导致原工作空间下的资源授权变更，请勿随意修改映射。
              </div>
            </template>
            <template #content>
              <CloudMapping @cloudMapping="getCloudMapping"></CloudMapping>
            </template>
          </layout-container> -->
        <!--先屏蔽云租户映射-->
        <layout-container>
          <el-button @click="route.back">取消</el-button>
          <el-button type="primary" @click="submitForm(ruleFormRef)"
            >保存</el-button
          ></layout-container
        >
      </template>
    </layout-container>
  </el-form>
</template>
<script setup lang="ts">
import { ref, onMounted, reactive, nextTick } from "vue";
import type { OrganizationTree } from "@/api/organization/type";
import { tree } from "@/api/organization";
import { batch, update, getWorksapceById } from "@/api/workspace";
import type { FormInstance, FormRules } from "element-plus";
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import type { CreateWorkspaceForm } from "./type";
const { t } = useI18n();
const route = useRouter();

//表单验证规则
const ruleFormRef = ref<FormInstance>();
//定义表单数据
const form = ref<CreateWorkspaceForm>({
  organizationId: undefined,
  workspaceDetails: [{ id: undefined, name: "", description: "" }],
});
//所属组织下拉框数据
const orgSeleteData = ref<Array<OrganizationTree>>();
//定义组织树选择数据
const workspaceOrgSelectTree = ref();
//默认父级组织选中
const defaultCheckedKeys = ref();

//设置云租户映射
// const getCloudMapping = (cloudMappingData: any) => {
//   form.value.cloudAccounts = cloudMappingData;
// };

//页面初始化
onMounted(() => {
  //查询组织树
  tree().then((data) => {
    console.log("org 数据:", data.data);
    orgSeleteData.value = data.data;
    sourceData.value = [...orgSeleteData.value];
  });
  //工作空间ID
  const workspaceId = route.currentRoute.value.query.id;
  if (workspaceId) {
    //查询工作空间信息
    getWorksapceById(workspaceId as string)
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
          workspaceOrgSelectTree.value.setCheckedKeys(
            defaultCheckedKeys.value,
            true
          );
        });
      })
      .catch((err) => {
        console.log("请求失败:", err);
      });
  }
});

//表单验证
const rules = reactive<FormRules>({
  name: [
    { message: "工作空间名称不为空", trigger: "blur", required: true },
    {
      pattern: /^.{2,128}$/,
      message: "长度限制为2～128个字符",
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
          callback(new Error("名称重复"));
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
      message: "长度限制为2～128个字符",
      trigger: "blur",
    },
  ],
  org: [
    {
      asyncValidator: (rule, value, callback) => {
        // 在此获取选中的树形数据
        const arr = workspaceOrgSelectTree.value.getCheckedKeys();
        if (arr.length == 0 || !arr) {
          callback(new Error("请选择父级组织"));
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
        update(form.value.workspaceDetails[0])
          .then((res) => {
            console.log("编辑成功", res);
            route.push({ name: "workspace" });
          })
          .catch((err) => {
            console.log("编辑失败:", err);
          });
      } else {
        batch(form.value)
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
  formEl?.validate((valid) => {
    if (valid) {
      form.value.workspaceDetails.push({ name: "", description: "" });
    }
  });
};

/**
 * 组织下拉框过滤
 * 感觉这个搜索有问题，添加之后，checked有问题，先不加吧
 */
const sourceData = ref<Array<OrganizationTree>>();
const filterMethod = (value: string) => {
  if (orgSeleteData.value) {
    orgSeleteData.value = sourceData.value?.filter((item) => {
      return item.name.includes(value);
    });
  }
};
</script>
<style lang="scss" scoped>
//描述文字样式
.desc-text-class {
  font-size: 10px;
  color: darkgray;
}

//设置form表单组件样式
.form-class {
  // .el-form-item {
  //   margin-bottom: 25px;
  // }
  // .el-form-item:first-child {
  //   margin-top: 5px;
  //   margin-bottom: 25px;
  // }
  // .el-form-item:last-child {
  //   margin-bottom: 5px;
  // }
}
</style>
