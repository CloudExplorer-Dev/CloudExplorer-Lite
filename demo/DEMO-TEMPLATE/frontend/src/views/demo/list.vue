<script setup lang="ts">
import DemoApi from "@/api/demo";
import { onMounted, ref } from "vue";
import type { DemoObject } from "@/api/demo/type";

const demoObject = ref<DemoObject>();
const loading = ref<boolean>(false);

function getDemo() {
  DemoApi.getDemo(loading).then((res) => {
    demoObject.value = res.data;
  });
}
const form: any = ref({
  value: "",
});
function updateValue() {
  DemoApi.updateDemo(form.value.value, loading).then((res) => {
    demoObject.value = res.data;
    dialogFormVisible.value = false;
  });
}

const dialogFormVisible = ref(false);

function openEdit() {
  form.value.value = demoObject.value?.value;
  dialogFormVisible.value = true;
}

onMounted(() => {
  getDemo();
});
</script>
<template>
  <base-container v-loading="loading">
    <template #header>
      <span>{{ $t("commons.basic_info", "基本信息") }}</span>
    </template>
    <template #content>
      <div>{{ $t("demo.label.text", "获取到Demo对象") }}：{{ demoObject }}</div>
      <el-button @click="openEdit">{{
        $t("demo.label.edit", "修改")
      }}</el-button>

      <el-dialog
        v-model="dialogFormVisible"
        :title="$t('demo.label.edit', '修改')"
      >
        <el-form :model="form">
          <el-form-item label="Value">
            <el-input v-model="form.value" autocomplete="off" />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogFormVisible = false">Cancel</el-button>
            <el-button type="primary" @click="updateValue"> Confirm </el-button>
          </span>
        </template>
      </el-dialog>
    </template>
  </base-container>
</template>
<style lang="scss" scoped></style>
