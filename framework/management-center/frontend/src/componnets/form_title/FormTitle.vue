<template>
  <div class="title">
    <p class="title-left"></p>
    <p class="title-right">
      <slot></slot>
    </p>
  </div>
</template>
<style scoped lang="scss">
//表单标题
.title {
  display: flex;
  margin-top: -18px;
  .title-left {
    margin-top: 20px;
    height: 16px;
    float: left;
    text-align: left;
    border-radius: 0;
    width: 2px;
    background: #3370ff;
    flex: none;
    order: 1;
    flex-grow: 0;
  }
  .title-right {
    height: 24px;
    margin-left: 8px;
    float: right;
    width: 64px;
    font-style: normal;
    font-weight: 500;
    font-size: 16px;
    line-height: 24px;
    color: #1f2329;
    flex: none;
    order: 2;
    flex-grow: 0;
  }
}
</style>
