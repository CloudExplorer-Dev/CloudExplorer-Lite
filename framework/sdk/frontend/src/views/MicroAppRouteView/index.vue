<script lang="ts" setup>
import { getCurrentInstance, onMounted, ref } from "vue";
import { EventCenterForMicroApp } from "@micro-zoe/micro-app";
import Config from "@commons/utils/constants";

const options = defineProps<{
  url: string;
  baseroute: string;
  name: string;
  moduleName: string;
}>();

const lastName = ref<string>();

const data = ref({ path: "" });
const $bus = getCurrentInstance()?.appContext.config.globalProperties.$bus;
onMounted(() => {
  $bus.on("changePath", (option: any) => {
    console.log("changePath", option);
    data.value = option;
  });
});

const handleCreate = (): void => {
  console.log(`child-vite [${options.name}] url: ${options.url}`);
  lastName.value = options.name;
  console.log(`child-vite [${options.name}]  创建了`);
};

const handleBeforeMount = (): void => {
  console.log(`child-vite [${options.name}]  即将被渲染`);
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore 因为vite子应用关闭了沙箱，我们需要为子应用appname-vite创建EventCenterForMicroApp对象来实现数据通信
  //window.eventCenterForAppNameVite = new EventCenterForMicroApp(options.name);
  window[`${Config.MICRO_APP_EVENTCENTER_PREFIX}${options.name}`] =
    new EventCenterForMicroApp(options.name);
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
    inline
    disableSandbox
    @created="handleCreate"
    @beforemount="handleBeforeMount"
    @mounted="handleMount"
    @unmount="handleUnmount"
    @error="handleError"
    @datachange="handleDataChange"
    :data="data"
  ></micro-app>
</template>

<style lang=""></style>
