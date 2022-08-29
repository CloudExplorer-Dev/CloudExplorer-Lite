<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import { useI18n } from "vue-i18n";
import roleConst from "@commons/utils/constants";

interface Role {
  id: string;
  name: string;
  type: string;
  parentId: string;
}

interface RoleInfo {
  roleId: string;
  roleType: string;
  selects: [];
}

const { t } = useI18n();

const form = reactive<any>({
  id: "",
  username: "",
  email: "",
  phone: "",
  password: "",
  confirmPassword: "",
  roleInfoList: [],
});

const roles = ref<Array<Role>>([
  { id: "admin", name: "系统管理员", type: "original", parentId: "" },
  { id: "org", name: "组织管理员", type: "original", parentId: "" },
  { id: "user", name: "工作空间用户", type: "original", parentId: "" },
]);

const isAddLineAble = ref(true);

const addLine = () => {
  const roleInfo: any = {
    roleId: "",
    selects: [],
  };
  form.roleInfoList.forEach((item: any) => {
    roleInfo.selects.push(item.roleId);
  });
  form.roleInfoList.push(roleInfo);
  if (form.roleInfoList && roles) {
    if (form.roleInfoList.length === roles.value.length) {
      isAddLineAble.value = false;
    }
  }
};

onMounted(() => {
  addLine();
});
</script>

<template>
  <div style="height: 100%">
    <layout-content>
      <template #breadcrumb>
        <breadcrumb
          :breadcrumbs="[
            { to: { name: 'user' }, title: '用户管理' },
            { to: {}, title: '创建' },
          ]"
        ></breadcrumb>
      </template>

      <el-form :model="form" label-width="100px" label-position="right">
        <layout-container>
          <template #header>
            <span>基本信息</span>
          </template>
          <template #content>
            <el-row>
              <el-col :span="10">
                <el-form-item label="ID">
                  <el-input
                    v-model="form.id"
                    type="text"
                    maxlength="30"
                    show-word-limit
                  />
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item :label="$t('commons.personal.username')">
                  <el-input
                    v-model="form.username"
                    type="text"
                    maxlength="30"
                    show-word-limit
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="10">
                <el-form-item label="Email">
                  <el-input
                    v-model="form.email"
                    type="text"
                    maxlength="50"
                    show-word-limit
                  />
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item :label="$t('commons.personal.phone')">
                  <el-input v-model="form.phone" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="10">
                <el-form-item
                  :label="$t('commons.personal.new_password')"
                  prop="newPassword"
                >
                  <el-input
                    v-model="form.password"
                    type="password"
                    clearable
                    show-password
                    autocomplete="new-password"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="10">
                <el-form-item
                  :label="$t('commons.personal.confirm_password')"
                  prop="confirmPassword"
                >
                  <el-input
                    v-model="form.confirmPassword"
                    type="password"
                    clearable
                    show-password
                    autocomplete="new-password"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </template>
        </layout-container>

        <layout-container>
          <template #header>
            <span>设置角色</span>
          </template>
          <template #content>
            <div v-for="(roleInfo, index) in form.roleInfoList">
              <!-- 用户类型 -->
              <el-row>
                <el-col :span="10">
                  <el-form-item label="用户类型" prop="region">
                    <el-select
                      style="width: 100%"
                      v-model="roleInfo.roleId"
                      placeholder="用户类型"
                    >
                      <el-option
                        v-for="role in roles"
                        :key="role.id"
                        :label="role.name"
                        :value="role.id"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="6" style="padding-left: 60px">
                  <el-tooltip
                    class="box-item"
                    effect="dark"
                    content="删除角色"
                    placement="bottom"
                  >
                    <el-button
                      v-if="form.roleInfoList.length > 1"
                      type="danger"
                      icon="minus"
                      circle
                    ></el-button>
                  </el-tooltip>
                  <el-tooltip
                    class="box-item"
                    effect="dark"
                    content="添加角色"
                    placement="bottom"
                  >
                    <el-button
                      v-if="form.roleInfoList.length === index + 1"
                      type="primary"
                      @click="addLine"
                      :disabled="!isAddLineAble"
                      icon="plus"
                      circle
                    ></el-button>
                  </el-tooltip>
                </el-col>
              </el-row>

              <!-- 选择组织 -->
              <el-row
                v-if="
                  roleInfo.roleType === roleConst.user && !roleInfo.workspace
                "
              >
                <el-tree-select
                  v-model="roleInfo.selectOrganizationId"
                  :data="data"
                  :render-after-expand="false"
                  show-checkbox
                />
              </el-row>
              <!-- 选择工作空间 -->
              <el-row> </el-row>
            </div>
          </template>
        </layout-container>
        <layout-container>
          <el-button>取消</el-button>
          <el-button type="primary">创建</el-button>
        </layout-container>
      </el-form>
    </layout-content>
  </div>
</template>

<style lang="scss"></style>
