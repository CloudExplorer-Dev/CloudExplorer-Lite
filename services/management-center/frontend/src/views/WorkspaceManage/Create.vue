<!--创建编辑工作空间-->
<template>
  <layout-content>
    <template #breadcrumb>
      <breadcrumb
        :breadcrumbs="[
          { to: { name: 'workspace' }, title: '工作空间' },
          { to: {}, title: viewText },
        ]"
      ></breadcrumb>
    </template>
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
              <el-form-item label="名称" prop="name" style="width: 100%">
                <el-input v-model="form.name" />
                <div class="desc-text-class">长度限制为2～128个字符。</div>
              </el-form-item>
              <el-form-item label="描述" prop="description" style="width: 100%">
                <el-input v-model="form.description" />
                <div class="desc-text-class">长度限制为2～128个字符。</div>
              </el-form-item>
              <el-form-item label="父级组织" prop="org" style="width: 100%">
                <el-tree-select
                  style="width: 100%"
                  :props="{ label: 'name' }"
                  node-key="id"
                  v-model="form.organizationId"
                  :data="orgSeleteData"
                  check-strictly
                  show-checkbox
                  ref="workspaceOrgSelectTree"
                  :default-checked-keys="defaultCheckedKeys"
                  :default-expanded-keys="defaultCheckedKeys"
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
  </layout-content>
</template>
<script setup lang="ts">
import { ref, onMounted, reactive, nextTick } from "vue";
import { tree } from "@/api/organization";
import { create, update, getWorksapceById } from "@/api/workspace";
import type { FormInstance, FormRules } from "element-plus";
import { useRouter } from "vue-router";
const route = useRouter();
//面包屑文本
const viewText = ref("");
//表单验证规则
const ruleFormRef = ref<FormInstance>();
//定义表单数据
const form = ref({
  id: null,
  name: null,
  organizationId: null,
  description: null,
  cloudAccounts: null,
});
//所属组织下拉框数据
const orgSeleteData = ref();
//定义组织树选择数据
const workspaceOrgSelectTree = ref();
//默认父级组织选中
const defaultCheckedKeys = ref<Array<string>>();

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
  });
  //工作空间ID
  const workspaceId = route.currentRoute.value.params.id;
  if (!workspaceId) {
    viewText.value = "创建";
  } else {
    viewText.value = "编辑";
    //查询工作空间信息
    getWorksapceById(workspaceId as string)
      .then((res) => {
        //请求成功给form设置数据
        form.value = {
          id: res.data.id,
          name: res.data.name,
          organizationId: res.data.organizationId,
          description: res.data.description,
          cloudAccounts: null,
        };
        //设置默认选中组织
        defaultCheckedKeys.value = [res.data.organizationId];
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
      if (form.value.id) {
        update(form.value)
          .then((res) => {
            console.log("创建成功", res);
            route.push({ name: "workspace" });
          })
          .catch((err) => {
            console.log("创建失败:", err);
          });
      } else {
        create(form.value)
          .then((res) => {
            console.log("编辑成功", res);
            route.push({ name: "workspace" });
          })
          .catch((err) => {
            console.log("编辑失败:", err);
          });
      }
    } else {
      console.log("error submit!", fields);
    }
  });
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
  .el-form-item {
    margin-bottom: 25px;
  }
  .el-form-item:first-child {
    margin-top: 5px;
    margin-bottom: 25px;
  }
  .el-form-item:last-child {
    margin-bottom: 5px;
  }
}
</style>
