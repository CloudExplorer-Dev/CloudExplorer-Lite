<template>
  <el-container class="create-catalog-container">
    <el-main ref="create-catalog-container">
      <div class="form-div">
        <el-form
          :model="from"
          ref="ruleFormRef"
          label-width="80px"
          status-icon
          label-position="top"
          require-asterisk-position="right"
        >
          <el-row>
            <el-col span="24">
              <FormTitle>{{ t("commons.basic_info", "基本信息") }}</FormTitle>
            </el-col>
          </el-row>
          <el-row
            :gutter="10"
            v-for="(org, index) in from.orgDetails"
            :key="index"
          >
            <el-col :span="11">
              <el-form-item
                :label="t('commons.name', '名称')"
                :prop="'orgDetails[' + index + '].name'"
                :rules="rules.name"
              >
                <el-input v-model="org.name" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                :label="t('commons.description', '描述')"
                :prop="'orgDetails[' + index + '].description'"
                :rules="rules.description"
              >
                <el-input v-model="org.description" />
              </el-form-item>
            </el-col>
            <el-col :span="1" class="padding-top-30">
              <el-form-item v-if="from.orgDetails.length > 1">
                <div
                  class="delete-button-class"
                  @click="deleteOrgItem(ruleFormRef, org, index)"
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
              <el-form-item>
                <!--                <el-form-item v-if="index === from.orgDetails.length - 1">-->
                <div class="add-button-class" @click="addOrgItem(ruleFormRef)">
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
            <el-col span="24">
              <FormTitle>{{
                t("org_manage.affiliated_organization", "所属组织")
              }}</FormTitle>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="23">
              <el-form-item :label="t('commons.org', '组织')">
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
            </el-col>
          </el-row>
        </el-form>
      </div>
    </el-main>
    <el-footer>
      <div class="footer">
        <div class="form-div">
          <div class="footer-btn">
            <el-button
              class="cancel-btn"
              @click="() => router.push({ name: 'org_list' })"
              >{{ t("commons.btn.cancel", "取消") }}</el-button
            >
            <el-button
              class="save-btn"
              type="primary"
              @click="submitForm(ruleFormRef)"
              >{{ t("commons.btn.save", "保存") }}</el-button
            >
          </div>
        </div>
      </div>
    </el-footer>
  </el-container>
</template>
<script setup lang="ts">
import { ElMessage } from "element-plus";
import { ref, onMounted, reactive } from "vue";
import organizationApi from "@/api/organization";
import type { OrganizationTree, CreateOrgFrom } from "@/api/organization/type";
import { useRouter } from "vue-router";
import type { FormInstance, FormRules } from "element-plus";
import CeIcon from "@commons/components/ce-icon/index.vue";
import { useI18n } from "vue-i18n";
import FormTitle from "@/componnets/from_title/FormTitle.vue";
const { t } = useI18n();
// 路由对象
const router = useRouter();
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
const from = ref<CreateOrgFrom>({
  pid: undefined,
  orgDetails: [{ name: "", description: "" }],
});

// 组建挂载生命周期钩子
onMounted(() => {
  organizationApi.tree().then((data) => {
    orientationData.value = data.data;
    sourceData.value = [...orientationData.value];
  });
});
/**
 * 保存掉用函数
 * @param formEl 校验对象
 */
const submitForm = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid) => {
    if (valid) {
      organizationApi.batchSave(from.value).then(() => {
        router.push({ name: "org" });
        ElMessage.success(t("commons.msg.save_success"));
      });
    }
  });
};
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
  from.value.orgDetails.splice(index, 1);
};
/**
 * 在form表单里面添加一个组织
 * @param formEl 校验对象
 */
const addOrgItem = (formEl: FormInstance | undefined) => {
  formEl?.validate((valid) => {
    if (valid) {
      from.value.orgDetails.push({ name: "", description: "" });
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
</script>
<style lang="scss"></style>
