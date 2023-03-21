<script lang="ts" setup>
import BillOverView from "./BillOverView.vue";
import BillCloudAccountDistribution from "./BillCloudAccountDistribution.vue";
import { computed, ref } from "vue";

const div1 = ref<InstanceType<typeof BillOverView> | null>(null);
const div2 = ref<InstanceType<typeof BillCloudAccountDistribution> | null>(
  null
);

const show = computed(() => {
  return div1.value?.show && div2.value?.show;
});
</script>
<template>
  <div :class="{ 'info-card no-padding': show }">
    <BillOverView class="no-bottom-border" ref="div1" />
    <div class="divider" v-if="show"></div>
    <BillCloudAccountDistribution sub-title class="no-top-border" ref="div2" />
  </div>
</template>

<style scoped lang="scss">
.chart {
  min-height: 200px;
  width: 100%;
}
.info-card {
  background: #ffffff;
  border-radius: 4px;
  padding: 24px;
  overflow: hidden;

  .divider {
    height: 1px;
    width: 90%;
    margin-left: auto;
    margin-right: auto;
    background-color: rgba(31, 35, 41, 0.15);
  }
}

.info-card.no-padding {
  padding: 0;
}

.info-card.no-bottom-border {
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
}
.info-card.no-top-border {
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}
</style>
