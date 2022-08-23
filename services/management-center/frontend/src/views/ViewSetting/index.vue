<template>
  <layout-content>
    <template #breadcrumb
      ><breadcrumb :breadcrumbs="[{ to: {}, title: '显示设置' }]"></breadcrumb
    ></template>
    <el-form :model="from" :label-position="'top'">
      <layout-container :border="false">
        <template #content>
          <layout-container>
            <template #header><h4>基本信息</h4></template>
            <template #content>
              <el-form-item label="系统颜色">
                <el-input v-model="from.name" />
              </el-form-item>
              <el-form-item label="登录页面标示">
                <el-input v-model="from.name" />
              </el-form-item>
            </template>
          </layout-container>
          <layout-container>
            <el-button>取消</el-button>
            <el-button type="primary">保存</el-button></layout-container
          >
        </template>
      </layout-container>
    </el-form>
  </layout-content>
</template>
<script setup lang="ts">
import { ref } from "vue";
const from = ref({ name: "" });
</script>
<style lang="scss"></style>
