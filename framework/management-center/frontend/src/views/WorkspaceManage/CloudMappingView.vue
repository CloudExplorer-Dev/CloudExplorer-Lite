<!--工作空间列表中云租户映射视图-->
<script setup>
//获取父组件参数
const props = defineProps({
  cloudIconUrl: String,
  cloudName: String,
  projectName: String,
});
</script>
<script>
export default {
  name: "CloudImage",
};
</script>
<template>
  <el-tooltip
    class="item"
    effect="dark"
    :content="cloudName + ': ' + projectName"
    placement="top"
  >
    <div>
      <el-row>
        <el-col :span="30"
          ><div class="image-size-class">
            <el-image :src="props.cloudIconUrl"></el-image>
          </div>
        </el-col>
        <el-col :span="30">
          <div class="text-overflow marginL5">{{ props.cloudName }}</div>
        </el-col>
        <el-col :span="2"> : </el-col>
        <el-col :span="30"
          ><div class="text-overflow">{{ props.projectName }}</div></el-col
        >
      </el-row>
    </div>
  </el-tooltip>
</template>

<style lang="scss" scoped>
.marginL5 {
  margin-left: 5px;
}
//图片大小
.image-size-class {
  .el-image {
    width: 21px;
    height: 21px;
  }
}
//过长字符样式
.text-overflow {
  max-width: 70px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
</style>
