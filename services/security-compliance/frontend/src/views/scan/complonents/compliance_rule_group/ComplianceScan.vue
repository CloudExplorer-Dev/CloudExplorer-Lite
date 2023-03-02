<template>
  <!-- 创建 -->
  <el-dialog
    v-model="scanDialogVisible"
    title="扫描"
    width="60%"
    :before-close="close"
  >
    <el-form :model="scanFrom" label-width="60px">
      <el-form-item
        prop="cloudAccountId"
        v-for="item in supportCloudAccountResourceList"
        :key="item.cloudAccount.id"
      >
        <el-row style="width: 100%">
          <el-col :span="24">
            <el-checkbox
              v-model="scanCheckAll[item.cloudAccount.id]"
              :indeterminate="isIndeterminate[item.cloudAccount.id]"
              @change="handleCheckAllChange(item, $event)"
            >
              <div style="display: flex">
                <platform_icon :platform="item.cloudAccount.platform">
                </platform_icon>
                {{ item.cloudAccount.name }}
              </div>
            </el-checkbox>
          </el-col>
        </el-row>
        <el-row style="width: 100%; margin-left: 20px">
          <el-col :span="24">
            <el-checkbox-group
              v-model="scanFrom[item.cloudAccount.id]"
              @change="handleCheckedCitiesChange(item.cloudAccount.id, $event)"
            >
              <el-checkbox
                v-for="type in item.resourceTypes"
                :key="type.key"
                :label="type.value"
              >
                {{ type.key }}
              </el-checkbox>
            </el-checkbox-group>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="scanDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit"> 提交 </el-button>
      </span>
    </template>
  </el-dialog>
</template>
<script setup lang="ts">
import { ref, onMounted } from "vue";
import complianceScanApi from "@/api/compliance_scan";
import { ElMessage } from "element-plus";
import type { SupportCloudAccountResourceResponse } from "@/api/compliance_scan/type";
import type { SimpleMap } from "@commons/api/base/type";
import { platformIcon } from "@commons/utils/platform";
import platform_icon from "@commons/components/platform-icon/index.vue";
/**
 * 弹出框是否显示
 */
const scanDialogVisible = ref<boolean>();
/**
 * 扫描表单
 */
const scanFrom = ref<SimpleMap<Array<string>>>({});
/**
 * 全选中间状态
 */
const isIndeterminate = ref<SimpleMap<boolean>>({});
/**
 * 是否全选
 */
const scanCheckAll = ref<SimpleMap<boolean>>({});
/**
 * 支持的云账号
 */
const supportCloudAccountResourceList = ref<
  Array<SupportCloudAccountResourceResponse>
>([]);

const handleCheckAllChange = (
  supportCloudAccountResource: SupportCloudAccountResourceResponse,
  select: boolean
) => {
  if (select) {
    scanFrom.value[supportCloudAccountResource.cloudAccount.id] =
      supportCloudAccountResource.resourceTypes.map((r) => r.value);
    isIndeterminate.value[supportCloudAccountResource.cloudAccount.id] = false;
  } else {
    scanFrom.value[supportCloudAccountResource.cloudAccount.id] = [];
    isIndeterminate.value[supportCloudAccountResource.cloudAccount.id] = false;
  }
};

/**
 * 多选item改变处理器
 * @param cloudAccountId 云账号id
 * @param value   选中值
 */
const handleCheckedCitiesChange = (cloudAccountId: string, value: string[]) => {
  scanFrom.value[cloudAccountId] = value;
  if (
    value.length ===
    supportCloudAccountResourceList.value.find(
      (c) => c.cloudAccount.id === cloudAccountId
    )?.resourceTypes.length
  ) {
    isIndeterminate.value[cloudAccountId] = false;
    scanCheckAll.value[cloudAccountId] = true;
  } else {
    scanCheckAll.value[cloudAccountId] = false;
    if (value.length === 0) {
      isIndeterminate.value[cloudAccountId] = false;
    } else {
      isIndeterminate.value[cloudAccountId] = true;
    }
  }
};

/**
 * 关闭模态框
 */
const close = () => {
  supportCloudAccountResourceList.value.forEach((item) => {
    scanCheckAll.value[item.cloudAccount.id] = false;
  });
  isIndeterminate.value = {};
  scanFrom.value = {};
  scanDialogVisible.value = false;
};
/**
 * 打开模态框
 */
const open = () => {
  scanDialogVisible.value = true;
};
/**
 * 提交表单
 */
const submit = () => {
  // 必须选择一种资源进行同步扫描
  if (
    Object.keys(scanFrom.value).map((key) => [...scanFrom.value[key]])
      .length === 0
  ) {
    ElMessage.error("至少选择一种资源进行同步扫描");
    return;
  } else {
    complianceScanApi
      .syncScan({
        cloudAccountResources: Object.keys(scanFrom.value).map((key) => ({
          cloudAccountId: key,
          resourceType: scanFrom.value[key],
        })),
      })
      .then(() => {
        ElMessage.success("发送扫描任务成功");
        close();
      });
  }
};
onMounted(() => {
  complianceScanApi.listSupportCloudAccountResource().then((ok) => {
    supportCloudAccountResourceList.value = ok.data;
  });
});

defineExpose({ open, close });
</script>
<style lang="scss"></style>
