<template>
  <el-tabs
    id="tables"
    v-bind:modelValue="active"
    :before-leave="beforeLeave"
    @tab-change="tabsChange"
    style="
      width: 100%;
      margin-top: 16px;
      --el-color-primary: rgba(51, 112, 255, 1);
    "
  >
    <el-tab-pane
      v-for="rule in tabsRuleList"
      :key="rule[valueField]"
      :label="rule[labelField]"
      :name="rule[valueField]"
    >
    </el-tab-pane>
    <el-tab-pane label="other" name="other" v-if="tabs.length > maxNum">
      <template #label>
        <el-dropdown @command="dropdownChange" trigger="click">
          <span class="el-dropdown-link">
            更多
            <el-icon class="el-icon--right">
              <arrow-down />
            </el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item
                v-for="item in dropdownRuleList"
                :key="item[labelField]"
                :command="item[valueField]"
                :style="{
                  '--el-text-color-regular':
                    props.active === item[valueField]
                      ? 'rgba(51, 112, 255, 1)'
                      : 'rgba(31, 35, 41, 1)',
                }"
              >
                <div style="width: 120px; display: flex; align-items: center">
                  <div class="item">
                    {{ item[labelField] }}
                  </div>
                  <el-icon
                    style="width: 20%"
                    v-show="props.active === item[valueField]"
                    ><Check
                  /></el-icon>
                </div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template> </el-dropdown
      ></template>
    </el-tab-pane>
  </el-tabs>
</template>
<script setup lang="ts">
import { computed, watch } from "vue";
const prefix = "tabs_";
const props = withDefaults(
  defineProps<{
    // 排序本地缓存key 开启置顶数据后才会生效
    localKey?: string;
    // tab 数据
    tabs: Array<any>;
    // 标签字段
    labelField?: string;
    //值字段
    valueField?: string;
    // 一行最多显示多少
    maxNum?: number;
    // 当前选中的规则id
    active: string;
    // 是否将更多的数据置顶
    topping?: boolean;
  }>(),
  {
    topping: true,
    maxNum: 5,
    labelField: "name",
    valueField: "id",
  }
);

const emit = defineEmits(["update:active"]);

const active = computed({
  get: () => {
    if (
      tabsRuleList.value.some((item) => item[props.valueField] === props.active)
    ) {
      return props.active;
    } else {
      return "other";
    }
  },
  set: (event) => {
    if (event !== "other") {
      emit("update:active", event);
    }
  },
});

/**
 * 下拉选择的规则数据
 */
const dropdownRuleList = computed(() => {
  return props.tabs.filter((item, index) => index >= props.maxNum);
});
/**
 * tabs规则数据
 */
const tabsRuleList = computed(() => {
  return props.tabs.filter((item, index) => index < props.maxNum);
});
/**
 * tab变化事件
 * @param event 事件值
 */
const tabsChange = (event: string) => {
  if (event !== "other") {
    emit("update:active", event);
  }
};

/**
 * 下拉框改变
 * @param event 事件值
 */
const dropdownChange = (event: string) => {
  if (props.topping) {
    const end: number = props.tabs
      .map((item, index) => {
        if (item[props.valueField] === event) {
          return index;
        }
        return null;
      })
      .find((i) => i) as number;
    resetData(props.tabs, end);
  }
  emit("update:active", event);
};

/**
 * 重排数据
 * @param tabs
 * @param endIndex
 */
const resetData = (tabs: Array<any>, endIndex: number) => {
  const temp = tabs[endIndex];
  tabs.splice(endIndex, 1);
  tabs.unshift(temp);
  // 缓存排序
  cacheSort(tabs);
};

/**
 * 缓存排序
 * @param tabs
 */
const cacheSort = (tabs: Array<any>) => {
  if (props.localKey) {
    localStorage.setItem(
      prefix + props.localKey,
      JSON.stringify(tabs.map((item) => item[props.valueField]))
    );
  }
};

/**
 * 排序数据
 * @param tabs 数据
 */
const sortData = (tabs: Array<any>) => {
  if (props.localKey && props.topping) {
    const cacheTabsSort = localStorage.getItem(prefix + props.localKey);
    if (cacheTabsSort) {
      const cacheSort: Array<string> = JSON.parse(
        cacheTabsSort
      ) as Array<string>;

      const cacheData: any = cacheSort
        .map((item, index) => ({ [item]: index }))
        .reduce((pre, next) => ({ ...pre, ...next }), {});

      const cacheIds = Object.keys(cacheData);

      tabs.map((item) => {
        if (cacheIds.includes(item[props.valueField])) {
          item["order"] = cacheData[item[props.valueField]];
        } else {
          item["order"] = -1;
        }
      });
      tabs.sort((pre, next) => pre.order - next.order);
    }
    if (tabs && tabs.length > 0) {
      if (!props.active) {
        emit("update:active", tabs[0][props.valueField]);
      }
    }
  }
};
/**
 * tab切换 是否进行切换
 * @param event 事件值
 */
const beforeLeave = (event: string) => {
  if (event === "other") {
    if (
      dropdownRuleList.value.some(
        (item) => item[props.valueField] === props.active
      )
    ) {
      return Promise.resolve();
    }
    return Promise.reject();
  }
  return Promise.resolve();
};
const sortLocal = () => {
  sortData(props.tabs);
};
let initSortLoacl = false;
watch(props, () => {
  if (props.tabs && props.tabs.length > 0 && !initSortLoacl) {
    sortLocal();
    initSortLoacl = true;
  }
});
defineExpose({ sortLocal });
</script>
<style lang="scss" scoped>
@mixin tabs_text() {
  color: rgba(31, 35, 41, 1);
  height: 22px;
  font-weight: 400;
  font-size: 14px;
}
.selected {
  color: #409eff !important;
}
.item {
  width: 80%;
  text-overflow: ellipsis;
  overflow: hidden;
}
:deep(.el-tabs__item) {
  @include tabs_text;
  margin-bottom: 9px;
}
:deep(.el-dropdown-link) {
  @include tabs_text;
}
</style>
