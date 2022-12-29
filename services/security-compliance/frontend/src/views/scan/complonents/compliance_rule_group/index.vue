<template>
  <div class="scan_compliance_rule_group_wapper">
    <div class="scan_compliance_rule_group">
      <swiper
        style="height: 110px"
        class="swiper"
        :modules="[Pagination, Navigation, Autoplay]"
        :space-between="10"
        :slides-per-view="4"
        :pagination="{
          clickable: true,
        }"
        :autoplay="{
          delay: 2500,
          disableOnInteraction: false,
        }"
      >
        <swiper-slide
          class="slide"
          v-for="ruleGroup in complianceRuleGroups"
          :key="ruleGroup.id"
        >
          <compliance_rule_cord
            :complianceRuleId="ruleGroup.id"
          ></compliance_rule_cord
        ></swiper-slide>
      </swiper>
    </div>
    <div class="scan"><el-button type="primary">一键扫描</el-button></div>
  </div>
</template>
<script setup lang="ts">
import complianceRuleGroupApi from "@/api/rule_group";
import compliance_rule_cord from "@/views/scan/complonents/compliance_rule_group/ComplianceRuleCard.vue";
import { onMounted, ref } from "vue";
import type { ComplianceRuleGroup } from "@/api/rule_group/type";
import { Pagination, Navigation, Autoplay } from "swiper";
import { Swiper, SwiperSlide } from "swiper/vue";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";

const complianceRuleGroups = ref<Array<ComplianceRuleGroup>>([]);
onMounted(() => {
  complianceRuleGroupApi.list().then((ok) => {
    complianceRuleGroups.value = ok.data;
  });
});
</script>
<style lang="scss" scoped>
.scan_compliance_rule_group_wapper {
  height: 120px;
  min-width: 1100px;
  display: flex;
  align-items: center;
  .scan_compliance_rule_group {
    width: 85%;
    min-width: 1000px;
    height: 100px;
  }
  .scan {
    min-width: 100px;
    width: 15%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
}

:deep(.swiper-pagination) {
  bottom: 0px !important;
}
</style>
