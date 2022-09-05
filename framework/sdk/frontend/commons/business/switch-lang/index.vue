<script setup lang="ts">
import { languages, useUserStore } from "@commons/stores/modules/user";

const userStore = useUserStore();
</script>

<template>
  <el-dropdown trigger="click">
    <el-button type="primary">
      {{ languages[userStore.currentLang]
      }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
    </el-button>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item
          v-for="(value, key) in languages"
          :key="key"
          :disabled="userStore.currentLang === key"
          ><span @click="userStore.changeLang(key)">{{
            value
          }}</span></el-dropdown-item
        >
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>
