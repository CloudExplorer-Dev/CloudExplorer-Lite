<script lang="ts" setup>
import { getCurrentInstance, onMounted, ref } from "vue";

const options = defineProps<{
  url: string;
  baseRoute: string;
  name: string;
  moduleName: string;
}>();

const lastName = ref<string>();

const handleCreate = (): void => {
  console.log(`child-vite [${options.name}] url: ${options.url}`);
  lastName.value = options.name;
  console.log(`child-vite [${options.name}]  创建了`);
};

const handleBeforeMount = (): void => {
  console.log(`child-vite [${options.name}]  即将被渲染`);
};

const handleMount = (): void => {
  console.log(`child-vite [${options.name}] 已经渲染完成`);
  console.log("options.moduleName", options.moduleName);
};

const handleUnmount = (): void => {
  console.log(`child-vite [${lastName.value}] 卸载了`);
};

const handleError = (): void => {
  console.log(`child-vite [${options.name}] 加载出错了`);
};

const handleDataChange = (e: CustomEvent): void => {
  console.log("来自子应用 child-vite 的数据:", e.detail.data);
};
</script>
<template>
  <micro-app
    :name="options.name"
    :url="options.url"
    @created="handleCreate"
    @beforemount="handleBeforeMount"
    @mounted="handleMount"
    @unmount="handleUnmount"
    @error="handleError"
    @datachange="handleDataChange"
    iframe
  ></micro-app>
</template>

<style lang=""></style>
