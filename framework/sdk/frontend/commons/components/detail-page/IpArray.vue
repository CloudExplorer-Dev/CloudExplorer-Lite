<script setup lang="ts">
import { classifyIP } from "@commons/utils/util";
import { useI18n } from "vue-i18n";

const { t } = useI18n();
const props = defineProps<{
  ipArray: string | Array<string>;
  remoteIp?: string;
}>();
</script>
<template>
  <div>
    <div v-for="(item, index) in classifyIP(ipArray, remoteIp)" :key="index">
      <div v-if="index < 2">
        <span>{{ item.ip }}</span>
        <span v-if="item.isPublicIp"> (公) </span>
      </div>
    </div>
    <el-dropdown
      :hide-on-click="false"
      v-if="JSON.parse(ipArray).length > 2"
      max-height="100px"
    >
      <span style="color: var(--el-color-primary); cursor: pointer">
        {{ t("commons.cloud_server.more", "更多")
        }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
      </span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item
            v-for="(item, index) in classifyIP(ipArray, remoteIp)"
            :key="index"
          >
            <span>{{ item.ip }}</span>
            <span v-if="item.isPublicIp"> (公) </span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>
