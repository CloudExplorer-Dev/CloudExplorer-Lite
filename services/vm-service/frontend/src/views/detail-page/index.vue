<script setup lang="ts">
import InstanceStatus from "./InstanceStatus.vue";
import PlatformIcon from "./PlatformIcon.vue";
import IpArray from "./IpArray.vue";
import SecurityGroup from "./SecurityGroup.vue";
import VmLink from "./VmLink.vue";
import { reactive, ref } from "vue";

const props = defineProps<{
  content: string;
  label: string;
  value: string;
}>();

const componentMap = ref({
  InstanceStatus: InstanceStatus,
  PlatformIcon: PlatformIcon,
  IpArray: IpArray,
  SecurityGroup: SecurityGroup,
  VmLink: VmLink,
});

const tooltipRef = ref();
const visible = ref(false); // 控制 tooltip 显示或者隐藏
const currentItem = ref(); // 鼠标选中元素的值
const spanRef = ref(); // 鼠标选中的元素
const divRef = ref();
const showTips = (index: number, e: Event) => {
  spanRef.value = e.currentTarget;
  divRef.value = divRefList[index];
  const spanWidth = spanRef.value.offsetWidth;
  const divWidth = divRef.value.offsetWidth;
  if (spanWidth >= Math.floor(divWidth * 0.9)) {
    visible.value = true;
  }
  currentItem.value = props.content[index];
};
const divRefList = reactive<any>([]);
const setDivRef = (el: any) => {
  if (el) {
    divRefList.push(el);
  }
};
</script>
<template>
  <div class="container">
    <div v-for="(item, index) in content" :key="index" class="item">
      <div>
        <p class="label">
          {{ item[label] }}
        </p>
        <div class="value" :ref="setDivRef">
          <span
            v-if="!item.hideValue"
            class="truncate"
            @mouseover="showTips(index, $event)"
            @mouseout="visible = false"
          >
            {{
              item[value] === null || item[value] === "null" ? "-" : item[value]
            }}
          </span>
          <component
            v-bind:is="componentMap[com]"
            v-for="(com, index) in item.components"
            :key="index"
            :instanceStatus="item.instanceStatus"
            :platform="item.platform"
            :remote-ip="item.remoteIp"
            :ip-array="item.value"
            :securityGroupIds="item.value"
            :serverId="item.serverId"
            :serverName="item.value"
          />
        </div>
      </div>
    </div>
    <el-tooltip
      v-if="currentItem && visible"
      ref="tooltipRef"
      :content="currentItem[value]"
      placement="top"
      effect="light"
      virtual-triggering
      :virtual-ref="spanRef"
    />
  </div>
</template>
<style lang="scss" scoped>
.container {
  display: flex;
  flex-wrap: wrap;
  width: 100%;

  .item {
    width: 25%;
    margin-bottom: 6px;

    .label {
      font-style: normal;
      bold: medium;
      font-weight: 400;
      font-size: 14px;
      line-height: 22px;
      color: #6c6c6c;
    }

    .value {
      display: flex;

      .truncate {
        display: inline-block;
        overflow: hidden;
        text-overflow: ellipsis;
        max-width: 90%;
        white-space: nowrap;
      }
    }
  }
}
</style>
