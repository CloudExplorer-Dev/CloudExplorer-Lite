<template>
  <div class="content">
    <el-row
      class="row"
      :gutter="16"
      v-for="(row, index) in rowCloudAccountList"
      :key="index"
    >
      <el-col :span="6" v-for="cloudAccount in row" :key="cloudAccount.id">
        <div
          class="item"
          :class="selected[cloudAccount.id] ? 'active' : ''"
          @click="changeValue(cloudAccount.id)"
        >
          <PlatformIcon
            style="margin-right: 0"
            class="icon"
            :platform="cloudAccount.platform"
          />
          <div class="text">{{ cloudAccount.name }}</div>
          <el-checkbox
            v-model="selected[cloudAccount.id]"
            @click.stop
            @change="changeValue(cloudAccount.id)"
            class="checkbox"
          />
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script setup lang="ts">
import { computed } from "vue";
import PlatformIcon from "@commons/components/platform-icon/index.vue";
import type { CloudAccount } from "@commons/api/cloud_account/type";
import { split } from "@commons/utils/commons";
import type { SimpleMap } from "@commons/api/base/type";
const props = defineProps<{
  /**
   * 云账号列表
   */
  cloudAccountList: Array<CloudAccount>;
  /**
   *选中数据
   */
  modelValue: Array<string>;
}>();
const emit = defineEmits(["update:modelValue"]);
const changeValue = (cloudAccountId: string) => {
  const active = !props.modelValue.includes(cloudAccountId);
  if (active) {
    if (!props.modelValue.includes(cloudAccountId)) {
      emit("update:modelValue", [...props.modelValue, cloudAccountId]);
    }
  } else {
    if (props.modelValue.includes(cloudAccountId)) {
      emit(
        "update:modelValue",
        props.modelValue.filter((id) => id !== cloudAccountId)
      );
    }
  }
};
const selected = computed(() => {
  const res: SimpleMap<boolean> = props.cloudAccountList
    .map((item) => {
      return { value: item.id, active: props.modelValue.includes(item.id) };
    })
    .reduce((pre, next) => {
      return { ...pre, [next.value]: next.active };
    }, {});
  return res;
});

//每一行的规则组数据
const rowCloudAccountList = computed(() => {
  if (!props.cloudAccountList) {
    return [];
  }
  const rowData = split(
    props.cloudAccountList,
    Math.ceil(props.cloudAccountList.length / 4)
  );
  return rowData;
});
</script>
<style lang="scss" scoped>
.content {
  display: flex;
  flex-wrap: wrap;
  width: 100%;
  .row {
    width: 100%;
    margin-top: 11px;
    &:first-child {
      margin-top: 16px;
    }
    .active {
      background: #f5f8ff;
      border: none !important;
    }
    .item {
      border: 1px solid #dee0e3;
      cursor: pointer;
      height: 56px;
      box-sizing: border-box;
      &:hover {
        background: #f5f8ff;
        border: none;
      }

      min-width: 186px;
      border-radius: 4px;
      display: flex;
      align-items: center;
      .icon {
        margin-left: 20px;
        width: 24px;
        height: 24px;
        margin-right: 0;
      }
      .text {
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        margin-left: 12px;
        width: calc(100% - 64px);
        color: #1f2329;
      }
      .checkbox {
        margin-right: 20px;
      }
    }
  }
}
</style>
