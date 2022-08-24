<script setup lang="ts">
import {computed} from "vue";
import {useUserStore} from "@commons/stores/user";

const user = useUserStore();
const currentLanguage = computed<string>(() => user.language);

interface ILanguagesType {
  [key: string]: string;
}
const languages: ILanguagesType = {
  "zh-cn": "中文(简体)",
  "zh-tw": "中文(繁體)",
  en: "English",
};

const handleSwitchLanguage = (lang: string) => {
  user.setLanguage(lang);
};
</script>

<template>
  <el-dropdown trigger="click">
    <el-button type="primary">
      {{ languages[currentLanguage]
      }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
    </el-button>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item
          v-for="(value, key) in languages"
          :key="key"
          :disabled="currentLanguage == key"
          ><span @click="handleSwitchLanguage(key as string)">{{
            value
          }}</span></el-dropdown-item
        >
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>
