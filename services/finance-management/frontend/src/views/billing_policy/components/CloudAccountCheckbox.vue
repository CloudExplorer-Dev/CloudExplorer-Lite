<template>
  <div class="cloud-account-content">
    <el-row
      class="row"
      :gutter="16"
      v-for="(row, index) in rowCloudAccountList"
      :key="index"
    >
      <el-col
        class="col"
        :span="24 / rowNumber"
        v-for="cloudAccount in row"
        :key="cloudAccount.id"
      >
        <el-popover
          v-if="cloudAccount.billPolicy && !cloudAccount.selected"
          placement="top-start"
          :width="350"
          trigger="hover"
        >
          <div style="display: flex">
            <InfoIcon></InfoIcon>
            <div style="margin-left: 8px">
              <span
                style="
                  color: rgba(50, 50, 51, 1);
                  font-size: 14px;
                  font-weight: 500;
                "
                >已关联:{{ cloudAccount.billPolicy.name }}</span
              >
              <div style="font-size: 12px; font-weight: 400">
                勾选后该云账号会使用当前计费策略
              </div>
            </div>
          </div>

          <template #reference>
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
              <div class="text">
                {{ cloudAccount.name }}
              </div>
              <el-icon
                v-if="cloudAccount.billPolicy"
                style="margin: 0 4px 0 4px; color: rgba(51, 112, 255, 1)"
                ><Connection
              /></el-icon>
              <el-checkbox
                v-model="selected[cloudAccount.id]"
                @click.stop
                @change="changeValue(cloudAccount.id)"
                class="checkbox"
              />
            </div>
          </template>
        </el-popover>
        <div
          v-else
          class="item"
          :class="selected[cloudAccount.id] ? 'active' : ''"
          @click="changeValue(cloudAccount.id)"
        >
          <PlatformIcon
            style="margin-right: 0"
            class="icon"
            :platform="cloudAccount.platform"
          />
          <div class="text">
            {{ cloudAccount.name }}
          </div>
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
import type { CloudAccountResponse } from "@/api/billing_policy/type";
import { splitArray } from "@commons/utils/commons";
import type { SimpleMap } from "@commons/api/base/type";
import InfoIcon from "@/views/billing_policy/components/InfoIcon.vue";
const props = withDefaults(
  defineProps<{
    /**
     * 云账号列表
     */
    cloudAccountList: Array<CloudAccountResponse>;
    /**
     *选中数据
     */
    modelValue: Array<string>;

    rowNumber?: number;
  }>(),
  {
    rowNumber: 3,
  }
);
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
  return splitArray(props.cloudAccountList, props.rowNumber);
});
</script>
<style lang="scss" scoped>
.cloud-account-content {
  display: flex;
  flex-wrap: wrap;
  width: 100%;
  .row {
    width: 100%;

    .col {
      margin-top: 10px;
      .popover-title {
        display: flex;
        flex-wrap: nowrap;
      }

      margin-top: 11px;
    }
    &:first-child {
      margin-top: 10px;
      .col {
        margin-top: 0;
      }
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

      min-width: 173px;
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
        width: calc(100% - 64px - 24px);
        color: #1f2329;
      }
      .checkbox {
        margin-right: 20px;
      }
    }
  }
}
</style>
