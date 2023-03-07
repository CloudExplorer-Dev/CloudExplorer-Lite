<!--云账号显示组件，图标 账号名称: 项目名称-->
<template>
  <el-form :model="formData" label-width="120px">
    <div v-for="item in formData" :key="item.cloud_account_id">
      <el-row style="flex-wrap: wrap; flex-direction: row">
        <el-col span="8" class="cloud-project-box">
          <el-image :src="item.cloud_account_icon_url"></el-image>
        </el-col>
        <el-col span="16">
          <el-form-item :label="item.cloud_account_name">
            <el-select
              style="width: 100%"
              @change="changeProject"
              placeholder="请选择项目"
              v-model="item.project_id"
            >
              <el-option
                v-for="p in item.cloud_projects"
                :key="p.id"
                :label="p.name"
                :value="p.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </div>
  </el-form>
</template>

<script setup lang="ts">
import type { CloudAccount } from "@/api/cloud_account/type";
import { ref, onMounted, defineEmits } from "vue";

interface CloudAccountProject {
  [key: string]: any;
}

//定义自定义事件
const emits = defineEmits<{
  (event: "cloudMapping", cloudMppingData: any): void;
}>();
//表单数据对象
interface FormPerson {
  cloud_account_icon_url: string;
  cloud_account_id: string;
  cloud_account_name: string;
  project_id: string;
  cloud_projects: Array<CloudAccountProject>;
}
//表单数据
const formData = ref<Array<FormPerson>>();

//所有云账号数据
const cloudAccountData = ref<Array<CloudAccount>>();

// 改变下拉框
const changeProject = () => {
  console.log(1123);
  emits("cloudMapping", formData);
};
//初始化云账号相关数据
onMounted(() => {
  //查询云账号，带云账号项目
  // listAllCloudAccount().then((data): void => {
  //   cloudAccountData.value = data.data;
  // });
  //测试数据
  cloudAccountData.value = [];
});
</script>
<style lang="scss" scoped>
//图标容器
.cloud-project-box {
  .el-image {
    width: 21px;
    height: 21px;
    top: 5px;
    padding-right: 5px;
  }
}
</style>
