<script setup lang="ts">
import { languages, useUserStore } from "@commons/stores/modules/user";

const userStore = useUserStore();
</script>

<template>
  <span style="padding-right: 6px">
    {{ languages[userStore.currentLang] }}
  </span>
  <!-- 目前不支持切换语言功能，暂时屏蔽-->
  <!--  <el-dropdown trigger="click">-->
  <!--    <el-button type="primary">-->
  <!--      {{ languages[userStore.currentLang]-->
  <!--      }}<el-icon class="el-icon&#45;&#45;right"><arrow-down /></el-icon>-->
  <!--    </el-button>-->
  <!--    <template #dropdown>-->
  <!--      <el-dropdown-menu>-->
  <!--        <el-dropdown-item-->
  <!--          v-for="(value, key) in languages"-->
  <!--          :key="key"-->
  <!--          :disabled="userStore.currentLang === key"-->
  <!--          ><span @click="userStore.changeLang(key)">{{-->
  <!--            value-->
  <!--          }}</span></el-dropdown-item-->
  <!--        >-->
  <!--      </el-dropdown-menu>-->
  <!--    </template>-->
  <!--  </el-dropdown>-->
</template>
