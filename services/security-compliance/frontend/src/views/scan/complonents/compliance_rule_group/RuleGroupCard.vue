<template>
  <el-card
    shadow="never"
    class="card_content"
    @click="emit('update:active', !active)"
    :class="active ? 'active' : ''"
  >
    <!-- title -->
    <el-row class="title_row">
      <el-col :span="17"
        ><span class="title">{{ props.ruleData.name }}</span></el-col
      >
      <el-col :span="7" v-if="runing">
        <ScanJobStatusIcon status="SYNCING"></ScanJobStatusIcon
      ></el-col>
    </el-row>
    <!-- content -->
    <el-row class="content_row">
      <el-col :span="24">
        <span class="desc">不合规规则</span>
        <!-- 不合规数量 -->
        <span class="not_compliance_num">{{
          props.ruleData.high + props.ruleData.middle + props.ruleData.low
        }}</span>
        <!-- 总数 -->
        <span class="compliance_num"
          >&nbsp;&nbsp;/&nbsp;{{ props.ruleData.total }}</span
        >
      </el-col>
    </el-row>
    <!-- 分割线 -->
    <el-divider class="divider_row" />
    <el-row class="proportion_row">
      <el-col :span="24"
        ><Proportion :data-list="proportions"></Proportion
      ></el-col>
    </el-row>
    <el-row class="desc_row">
      <el-col :span="8"
        ><span class="label">高:</span
        ><span class="value high">{{ props.ruleData.high }}</span></el-col
      >
      <el-col :span="8"
        ><span class="label">中:</span
        ><span class="value middle">{{ props.ruleData.middle }}</span></el-col
      >
      <el-col :span="8"
        ><span class="label">低:</span
        ><span class="value low">{{ props.ruleData.low }}</span></el-col
      >
    </el-row>
  </el-card>
</template>
<script setup lang="ts">
import Proportion from "@/views/scan/complonents/compliance_rule_group/Proportion.vue";
import ScanJobStatusIcon from "@/views/scan/complonents/compliance_rule/ScanJobStatusIcon.vue";
import { computed } from "vue";
const props = withDefaults(
  defineProps<{
    ruleData: {
      high: number;
      middle: number;
      low: number;
      total: number;
      name: string;
      resourceType: Array<string>;
      platform: Array<string>;
    };
    cloudAccountId: string;
    resourceTypes: Array<{
      resourceType: string;
      status: string;
      cloudAccountId: string;
      platform: string;
    }>;
    active: boolean;
  }>(),
  {
    ruleData: () => {
      return {
        high: 0,
        middle: 0,
        low: 0,
        total: 0,
        name: "",
        resourceType: [],
        platform: [],
      };
    },
    active: false,
  }
);

const emit = defineEmits(["update:active"]);

const runing = computed(() => {
  if (
    props.resourceTypes.some((item) => {
      if (!props.cloudAccountId || props.cloudAccountId !== "all") {
        return (
          props.ruleData.resourceType.includes(item.resourceType) &&
          props.ruleData.platform.includes(item.platform) &&
          item.status === "SYNCING" &&
          props.cloudAccountId === item.cloudAccountId
        );
      } else {
        return (
          props.ruleData.resourceType.includes(item.resourceType) &&
          props.ruleData.platform.includes(item.platform) &&
          item.status === "SYNCING"
        );
      }
    })
  ) {
    return true;
  }
  return false;
});
// 比例数据
const proportions = computed(() => {
  const colors = [
    { color: "rgba(247, 105, 100, 1)", num: props.ruleData.high },
    { color: "rgba(255, 165, 61, 1)", num: props.ruleData.middle },
    { color: "rgba(110, 116, 142, 1)", num: props.ruleData.low },
    {
      color: "rgba(222, 224, 227, 1)",
      num:
        props.ruleData.total -
        props.ruleData.high -
        props.ruleData.low -
        props.ruleData.middle,
    },
  ];

  return colors;
});
</script>
<style lang="scss" scoped>
:deep(.el-card__body) {
  padding: 16px;
}
.active {
  background: rgba(51, 112, 255, 0.1);
  border: 1px solid #3370ff;
}
.card_content {
  height: 161px;
  width: 100%;
  min-width: 248px;
  .title_row {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    .title {
      height: 22px;
      font-weight: 500;
      line-height: 22px;
      color: #1f2329;
      font-size: 14px;
    }
  }
  .desc_row {
    margin-top: 8px;
  }
  .proportion_row {
    margin-top: 16px;
  }
  .divider_row {
    margin-top: 19px;
    margin-bottom: 0;
  }
  .content_row {
    margin-top: 11px;
    .desc {
      color: #646a73;
      height: 22px;
      font-weight: 400;
      font-size: 14px;
      line-height: 22px;
    }
    .not_compliance_num {
      margin-left: 12px;
      color: #1f2329;
      font-size: 20px;
      line-height: 28px;
      font-weight: 500;
    }
    .compliance_num {
      color: rgba(100, 106, 115, 1);
    }
  }
  .desc_row {
    .label {
      height: 22px;
      font-size: 14px;
      color: #646a73;
      font-weight: 400;
      width: 28px;
      display: inline-block;
    }
    .value {
      font-weight: 400;
      font-size: 14px;
      line-height: 22px;
      height: 22px;
    }
    .high {
      color: #f54a45;
    }
    .middle {
      color: #ff8800;
    }
    .low {
      color: #1f2329;
    }
  }
}
</style>
