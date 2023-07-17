<template>
  <div>
    <el-popover
      placement="top-start"
      :title="$attrs.title"
      :width="200"
      trigger="hover"
    >
      <template #reference>
        <el-progress v-bind="$attrs"></el-progress
      ></template>
      <div>
        <el-row
          ><el-col :span="6">总量</el-col
          ><el-col :span="18">
            <span class="value"
              >{{ DecimalFormat.format($attrs.total, 2) }}&nbsp;&nbsp;{{
                $attrs.unit
              }}</span
            ></el-col
          ></el-row
        >
        <el-divider border-style="dashed" />
        <el-row
          ><el-col :span="6">可用</el-col
          ><el-col :span="18">
            <span class="value"
              >{{ DecimalFormat.format($attrs.available, 2) }}&nbsp;&nbsp;{{
                $attrs.unit
              }}</span
            ></el-col
          ></el-row
        >
        <el-row style="margin-top: 8px"
          ><el-col :span="6">已用</el-col
          ><el-col :span="18">
            <span class="value">
              {{ DecimalFormat.format($attrs.used, 2) }}&nbsp;&nbsp;{{
                $attrs.unit
              }}</span
            >
          </el-col></el-row
        >
      </div>
    </el-popover>
  </div>
</template>
<script setup lang="ts">
import DecimalFormat from "@commons/utils/decimalFormat";
</script>
<style lang="scss" scoped>
@mixin valueScss() {
  color: rgba(31, 35, 41, 1);
  font-weight: 500;
  font-size: 12px;
  line-height: 22px;
  height: 22px;
}
.value {
  float: right;
  @include valueScss;
}
</style>
