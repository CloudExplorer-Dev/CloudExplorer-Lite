<script setup lang="ts">
import { computed, getCurrentInstance } from "vue";
import "@commons/font/iconfont";

const components: any = getCurrentInstance()?.appContext.components;

const $antIcons: any =
  getCurrentInstance()?.appContext.config.globalProperties.$antIcons;
const props = defineProps({
  code: {
    type: String,
    required: true,
  },
  cssPrefixText: {
    type: String,
    default: "ali-icon-",
  },
  type: {
    type: String,
    default: "code",
  },
  size: {
    type: String,
    default: "25px",
  },
  color: {
    type: String,
  },
});

/**
 * 拼接样式
 */
const getCode = computed(() => {
  return props.cssPrefixText + props.code;
});
</script>
<template>
  <el-icon>
    <svg
      v-if="props.type === 'svg'"
      class="icon svg-icon"
      preserveAspectRatio="none"
      style="height: 100%; width: 100%"
    >
      <use :xlink:href="'#' + getCode"></use>
    </svg>
    <component
      v-else-if="Object.keys(components).includes(props.code)"
      :is="$antIcons[props.code]"
    ></component>
    <span
      :style="{ fontSize: props.size, color: props.color }"
      v-else
      class="icon iconfont"
      :class="getCode"
    ></span>
  </el-icon>
</template>

<style lang="scss" scoped>
@import url(../../font/iconfont.css);
.icon {
  display: block;
  -webkit-transition: font-size 0.25s linear, width 0.25s linear;
  -moz-transition: font-size 0.25s linear, width 0.25s linear;
  transition: font-size 0.25s linear, width 0.25s linear;
}
.svg-icon {
  /* 通过设置 font-size 来改变图标大小 */
  width: 1em;
  /* 图标和文字相邻时，垂直对齐 */
  vertical-align: -0.15em;
  /* 通过设置 color 来改变 SVG 的颜色/fill */
  fill: currentColor;
  /* path 和 stroke 溢出 viewBox 部分在 IE 下会显示
      normalize.css 中也包含这行 */
  overflow: hidden;
}
</style>
