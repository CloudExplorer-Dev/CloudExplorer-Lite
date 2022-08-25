<script setup lang="ts">
import SwitchLang from "@commons/business/switch-lang/index.vue";
import PersonSetting from "@commons/business/person-setting/index.vue";
import ToDo from "@commons/business/to-do/index.vue";
import Notification from "@commons/business/notification/index.vue";
</script>

<template>
  <div class="top-nav">
    <div class="logo">
      <img class="img" src="@commons/assets/white-logo.png" alt="" />
    </div>
    <h2>云服务平台</h2>
    <div class="role">系统管理员</div>
    <div class="flex-auto"></div>
    <SwitchLang />
    <ToDo />
    <Notification />
    <PersonSetting />
  </div>
</template>

<style lang="scss">
.top-nav {
  height: 100%;
  display: flex;
  color: white;
  align-items: center;
  .logo {
    height: 30px;
    margin-right: 8px;
    .img {
      height: 100%;
    }
  }
  .role {
    padding: 0 20px;
  }
  .flex-auto {
    flex: 1 1 auto;
  }
}
</style>
