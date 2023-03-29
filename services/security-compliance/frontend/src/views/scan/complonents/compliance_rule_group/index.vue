<template>
  <div class="rule_group_content" v-loading="loading">
    <el-row
      class="rule_group_row"
      :gutter="16"
      v-for="(row, index) in rowRuleGroupList"
      :key="index"
    >
      <el-col :span="6" v-for="item in row" :key="item.id"
        ><RuleGroupCard
          :cloud-account-id="cloudAccountId"
          :resource-types="resourceTypeStatus"
          :rule-data="item"
          @update:active="selected($event, item)"
          :active="activeRuleGroup ? activeRuleGroup.id === item.id : false"
        ></RuleGroupCard
      ></el-col>
    </el-row>
    <div class="operate" @click="expand = !expand">
      <span v-if="!expand" class="expand">展开</span
      ><span v-else class="close">收起</span> &nbsp;<ce-icon
        :code="expand ? 'icon_up_outlined' : 'icon_down_outlined'"
        class="icon"
        size="12px"
      ></ce-icon>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, computed, watch } from "vue";
import sacnResultApi from "@/api/compliance_scan_result/index";
import type { ComplianceRuleGroupCountResponse } from "@/api/compliance_scan_result/type";
import RuleGroupCard from "@/views/scan/complonents/compliance_rule_group/RuleGroupCard.vue";
import { splitArray } from "@commons/utils/commons.ts";
import bus from "@commons/bus";
import { useRoute } from "vue-router";
const route = useRoute();
const ruleGroupCountList = ref<Array<ComplianceRuleGroupCountResponse>>([]);
// 选中的规则组
const activeRuleGroup = ref<ComplianceRuleGroupCountResponse>();
// 加载器
const loading = ref<boolean>(false);
const props = defineProps<{
  cloudAccountId: string;
}>();
const resourceTypeStatus = ref<
  Array<{
    cloudAccountId: string;
    resourceType: string;
    status: string;
    platform: string;
  }>
>([]);

onMounted(() => {
  bus.on(
    "sync_status",
    (
      row: Array<{
        cloudAccountId: string;
        resourceType: string;
        status: string;
        platform: string;
      }>
    ) => {
      resourceTypeStatus.value = row;
      const sync = resourceTypeStatus.value.filter(
        (item) => item.status === "SYNCING"
      );
      if (sync) {
        ruleGroupCountList.value.sort((pre, next) => {
          if (
            sync.some((s) => {
              return (
                pre.resourceType.includes(s.resourceType) &&
                pre.platform.includes(s.platform)
              );
            })
          ) {
            if (
              sync.some((s) => {
                return (
                  next.resourceType.includes(s.resourceType) &&
                  next.platform.includes(s.platform)
                );
              })
            ) {
              return next.high - pre.high;
            } else {
              return -1;
            }
          } else {
            return 1;
          }
        });
      }
    }
  );
  search().then((ruleGroupList) => {
    if (route.query.ruleGroup) {
      activeRuleGroup.value = ruleGroupList.find(
        (item) => item.id === route.query.ruleGroup
      );
      bus.emit("update:compliance_rule_group", activeRuleGroup.value);
    }
  });
});
/**
 * 查询规则组数据
 */
const search = () => {
  return sacnResultApi
    .listRuleGroupCount(
      props.cloudAccountId === "all" ? undefined : props.cloudAccountId,
      loading
    )
    .then((ok) => {
      ruleGroupCountList.value = ok.data;
      ruleGroupCountList.value.sort((pre, next) => next.high - pre.high);
      return ok.data;
    });
};
watch(
  () => props.cloudAccountId,
  () => {
    search();
  }
);
const rule = ref<{}>();
/**
 * 规则组选中状态
 * @param active 是否选中
 * @param row    规则组对象
 */
const selected = (active: boolean, row: ComplianceRuleGroupCountResponse) => {
  if (active) {
    activeRuleGroup.value = row;
  } else {
    activeRuleGroup.value = undefined;
  }
  bus.emit("update:compliance_rule_group", activeRuleGroup.value);
};
// 是否展开
const expand = ref<boolean>(false);
//每一行的规则组数据
const rowRuleGroupList = computed(() => {
  const rowData = splitArray(ruleGroupCountList.value, 4);
  if (expand.value) {
    return rowData;
  } else {
    return [rowData[0]];
  }
});
</script>
<style lang="scss">
.rule_group_content {
  .operate {
    display: flex;
    justify-content: center;
    cursor: pointer;
    margin-top: 16px;
    height: 22px;

    span {
      color: #646a73;
      height: 22px;
      line-height: 22px;
      font-weight: 400;
      font-size: 14px;
    }
  }
  .rule_group_row {
    margin-top: 10px;
    &:first-child {
      margin-top: 0;
    }
  }
}
</style>
