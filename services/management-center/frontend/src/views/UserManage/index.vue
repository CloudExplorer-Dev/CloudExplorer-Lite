<script setup lang="ts"></script>
<template lang="">
  <div class="contentContainer">
    <div class="routeList">
      <div>
        <el-breadcrumb>
          <el-breadcrumb-item style="">管理中心</el-breadcrumb-item>
          <el-breadcrumb-item>用户与租户</el-breadcrumb-item>
          <el-breadcrumb-item>用户</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
    </div>
    <div class="content"></div>
  </div>
</template>

<style lang="css">
.routeList {
  height: 35px;
  width: 100%;
  justify-content: flex-start;
  margin-left: 10px;
  align-content: center;
  align-items: flex-start;
  display: flex;
}

.routeList div {
  line-height: 35px;
}

.content {
  height: calc(100% - 60px);
  width: calc(100% - 20px);
  background-color: #fff;
  margin: 10px;
}

.contentContainer {
  height: 100%;
  width: 100%;
}
</style>
