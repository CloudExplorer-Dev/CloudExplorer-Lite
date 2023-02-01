<template>
  <layout-auto-height-content>
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <el-tabs v-model="activeCompliance" class="demo-tabs">
      <el-tab-pane label="合规规则" name="compliance_rule"
        ><compliance_rule ref="compliance_rule_ref"></compliance_rule>
      </el-tab-pane>
      <el-tab-pane label="漏洞规则" name="leak_hole"> </el-tab-pane>
      <el-tab-pane label="规则组" name="rule_group">
        <compliance_rule_group></compliance_rule_group
      ></el-tab-pane>
    </el-tabs>
  </layout-auto-height-content>
</template>
<script setup lang="ts">
import { ref, watch, onMounted } from "vue";
import compliance_rule from "@/views/rule/ComplianceRule.vue";
import compliance_rule_group from "@/views/rule/ComplianceRuleGroup.vue";
const activeCompliance = ref<string>("compliance_rule");
const compliance_rule_ref = ref<InstanceType<typeof compliance_rule>>();
watch(activeCompliance, (new_value: string) => {
  if (new_value === "compliance_rule") {
    compliance_rule_ref.value?.refresh();
  }
});
onMounted(() => {
  compliance_rule_ref.value?.refresh();
});
</script>
<style lang="scss"></style>
