<template>
  <div class="scan_compliance_rule_group_wapper" v-resize="reSize">
    <div class="scan_compliance_rule_group">
      <swiper
        style="height: 110px"
        class="swiper"
        :modules="[Pagination, Navigation, Autoplay]"
        :space-between="10"
        :slides-per-view="slidesPerView"
        :pagination="{
          clickable: true,
        }"
      >
        <swiper-slide
          class="slide"
          v-for="ruleGroup in complianceRuleGroups"
          :key="ruleGroup.id"
        >
          <compliance_rule_cord
            :class="
              activeComplianceRuleGroupId === ruleGroup.id ? 'active' : ''
            "
            @click="selectComplianceRuleGroup(ruleGroup.id)"
            :complianceRuleId="ruleGroup.id"
          ></compliance_rule_cord>
        </swiper-slide>
      </swiper>
    </div>

    <div class="scan">
      <el-button type="primary" @click="openScanView">一键扫描</el-button>
      <div style="margin-top: 10px" v-if="scanStatus === 'SYNCING'">
        扫描中...<el-icon class="is-loading">
          <Loading />
        </el-icon>
      </div>
    </div>
    <compliance_scan ref="scan"></compliance_scan>
  </div>
</template>
<script setup lang="ts">
import complianceRuleGroupApi from "@/api/rule_group";
import compliance_rule_cord from "@/views/scan/complonents/compliance_rule_group/ComplianceRuleCard.vue";
import compliance_scan from "@/views/scan/complonents/compliance_rule_group/ComplianceScan.vue";
import { onMounted, ref } from "vue";
import type { ComplianceRuleGroup } from "@/api/rule_group/type";
import { Pagination, Navigation, Autoplay } from "swiper";
import { Swiper, SwiperSlide } from "swiper/vue";
import bus from "@commons/bus";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";
import { useRoute } from "vue-router";
const route = useRoute();
// 规则组列表
const complianceRuleGroups = ref<Array<ComplianceRuleGroup>>([]);
onMounted(() => {
  // 查询规则组
  complianceRuleGroupApi.list().then((ok) => {
    complianceRuleGroups.value = ok.data;
  });
  if (route.query.ruleGroup) {
    activeComplianceRuleGroupId.value = route.query.ruleGroup as string;
  }
});
const scan = ref<InstanceType<typeof compliance_scan>>();
// 选中的规则组
const activeComplianceRuleGroupId = ref<string>("");
const slidesPerView = ref<number>(4);
/**
 * 选中规则组执行函数
 * @param complianceRuleGroup 规则组id
 */
const selectComplianceRuleGroup = (complianceRuleGroup: string) => {
  if (activeComplianceRuleGroupId.value === complianceRuleGroup) {
    activeComplianceRuleGroupId.value = "";
  } else {
    activeComplianceRuleGroupId.value = complianceRuleGroup;
  }
  bus.emit(
    "update:compliance_rule_group_id",
    activeComplianceRuleGroupId.value
  );
};

const scanStatus = ref<"SYNCING" | "SUCCESS" | "INIT">("INIT");

bus.on("scanStatus", (status: "SYNCING" | "SUCCESS") => {
  scanStatus.value = status;
});
const openScanView = () => {
  scan.value?.open();
};

const reSize = (wh: { width: string }) => {
  slidesPerView.value = Math.floor(
    (Number.parseInt(wh.width.replace("px", "")) - 85) / 250
  );
};
</script>
<style lang="scss" scoped>
.active {
  border: 1px solid var(--el-color-primary);
  box-shadow: var(--el-box-shadow-lighter);
}
.scan_compliance_rule_group_wapper {
  height: 120px;
  width: 100%;
  display: flex;
  align-items: center;
  .scan_compliance_rule_group {
    width: calc(100% - 85px);
    height: 100px;
  }
  .scan {
    flex-wrap: wrap;
    width: 85px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
}

:deep(.swiper-pagination) {
  bottom: 0px !important;
}
</style>
