<template>
  <layout-auto-height-content>
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <compliance_rule_group></compliance_rule_group>
    <compliance_rule></compliance_rule>
  </layout-auto-height-content>
</template>
<script setup lang="ts">
import compliance_rule_group from "@/views/scan/complonents/compliance_rule_group/index.vue";
import compliance_rule from "@/views/scan/complonents/compliance_rule/index.vue";
</script>
<style lang="scss"></style>
