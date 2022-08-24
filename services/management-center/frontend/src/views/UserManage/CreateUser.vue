<script setup lang="ts">
import { reactive } from "vue";
import { useI18n } from "vue-i18n";
import CardLayout from "ce-base/commons/components/card-layout/index.vue";

const { t } = useI18n();

const form = reactive({
  id: "",
  username: "",
  email: "",
  phone: "",
  password: "",
});
</script>

<template>
  <div style="height: 100%">
    <div
      style="
        height: 49px;
        display: flex;
        align-items: center;
        border: 1px solid #cccccc;
      "
    >
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">用户</el-breadcrumb-item>
        <el-breadcrumb-item><a href="/">创建用户</a></el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="main-contianer">
      <el-form :model="form" label-width="100px" label-position="right">
        <card-layout>
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
                    v-model="form.newPassword"
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
        </card-layout>

        <card-layout>
          <template #header>
            <span>设置角色</span>
          </template>
          <template #content>
            <el-form-item label="用户角色" prop="region">
              <el-select
                style="width: 40%"
                v-model="form.userType"
                placeholder="用户类型"
              >
                <el-option label="系统管理员" value="admin" />
                <el-option label="组织管理员" value="org" />
                <el-option label="普通用户" value="user" />
              </el-select>
            </el-form-item>
          </template>
        </card-layout>

        <card-layout>
          <el-button>取消</el-button>
          <el-button type="primary">创建</el-button>
        </card-layout>
      </el-form>
    </div>
  </div>
</template>

<style lang="scss">
.main-contianer {
  background-color: white;
  height: 100%;
  margin: 20px;
}
</style>
