<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { $t, $tm } from "ce-base/commons/base-locales";
import { ElMessage } from "element-plus";
import { computed } from "vue";
// import { useUserStore } from "@/stores/user";

// const { t } = useI18n();

// const user = useUserStore();
// const currentLanguage = computed(() => user.language);
const currentLanguage = "zh-cn";
const languageMap = {
  "zh-cn": "中文(简体)",
  "zh-tw": "中文(繁體)",
  "en-us": "English",
};

function handleSwitchLanguage(lang) {
  // TODO 此处通过状态管理设置用户语言和浏览器缓存之类
  // user.setLanguage(lang).then(() => {
  ElMessage({
    message: $t("commons.btn.switch_lang") + $t("commons.msg.success"),
    type: "success",
  });
  // });
}
</script>

<template>
  <el-dropdown trigger="click">
    <el-button type="primary">
      {{ languageMap[currentLanguage]
      }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
    </el-button>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item
          v-for="(value, key) in languageMap"
          :key="key"
          :disabled="currentLanguage == key"
          ><span @click="handleSwitchLanguage(key)">{{
            value
          }}</span></el-dropdown-item
        >
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>
