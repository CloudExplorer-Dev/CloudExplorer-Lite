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
import { CloudAccount } from "@/api/cloud_account/type";
import { ref, reactive, onMounted, defineEmits } from "vue";

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
const formData = reactive(Array<FormPerson>());

//所有云账号数据
const cloudAccountData = ref(Array<CloudAccount>());

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
  const cloudTmpData = reactive([
    {
      id: "aliyun123",
      name: "南区阿里云账号",
      description: "测试账号",
      createTime: "2022-08-29 10:20:30",
      cloud_icon_url:
        "https://img.alicdn.com/tfs/TB1_ZXuNcfpK1RjSZFOXXa6nFXa-32-32.ico",
      projects: [
        {
          id: "123",
          uuid: "dfasdfasd23",
          name: "默认项目",
          cloud_account_id: "aliyun123",
        },
        {
          id: "1234",
          uuid: "dfasdfasd234",
          name: "企业项目",
          cloud_account_id: "aliyun123",
        },
      ],
    },
    {
      id: "huawei123",
      name: "南区华为账号",
      description: "测试账号",
      createTime: "2022-08-29 10:20:30",
      cloud_icon_url: "/management-center/src/assets/img/huawei.ico",
      projects: [
        {
          id: "123",
          uuid: "dfasdfasd23",
          name: "默认项目",
          cloud_account_id: "aliyun123",
        },
        {
          id: "1234",
          uuid: "dfasdfasd234",
          name: "企业项目",
          cloud_account_id: "aliyun123",
        },
      ],
    },
  ]);
  cloudAccountData.value = cloudTmpData;

  //转换成表单数据
  let cloudAccount;
  for (cloudAccount of cloudAccountData.value) {
    const projects = ref(Array<CloudAccountProject>());
    if (cloudAccount.projects && cloudAccount.projects.length > 0) {
      projects.value = cloudAccount.projects;
    }
    const fdp: any = {
      cloud_account_icon_url: cloudAccount.cloud_icon_url,
      loud_account_id: cloudAccount.id,
      cloud_account_name: cloudAccount.name,
      project_id: projects.value[0].id,
      cloud_projects: projects,
    };
    formData.push(fdp);
  }
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
