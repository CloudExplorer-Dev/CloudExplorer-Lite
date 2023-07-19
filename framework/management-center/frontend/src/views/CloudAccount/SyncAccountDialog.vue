<script setup lang="ts">
import { computed, ref } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import {
  type CloudAccount,
  type Region,
  type ResourceSync,
  type SyncRequest,
} from "@/api/cloud_account/type";
import cloudAccountApi from "@/api/cloud_account";
import { ElMessage } from "element-plus";
import { useModuleStore } from "@commons/stores/modules/module";
import { usePermissionStore } from "@commons/stores/modules/permission";
import { useI18n } from "vue-i18n";
const moduleStore = useModuleStore();
const permissionStore = usePermissionStore();
const { t } = useI18n();

const loading = ref<boolean>(false);
const _visible = ref<boolean>(false);
const _id = ref<string>();
const _account = ref<CloudAccount>();

const tabs = computed(() => {
  const list = [
    {
      name: "资源",
      id: "resource",
    },
  ];
  if (billSyncShow.value) {
    list.push({
      name: "账单",
      id: "bill",
    });
  }
  return list;
});
const billSyncShow = ref<boolean>(false);

const activeTab = ref<string>("resource");

function close() {
  _visible.value = false;
}

function open(account?: CloudAccount) {
  if (!account) {
    return;
  }
  activeTab.value = "resource";
  _id.value = account.id;
  _account.value = account;
  _visible.value = true;
  regions.value = [];
  resources.value = [];

  syncForm.value = {
    checkedResources: [],
    checkedRegions: [],
  };

  billSyncForm.value = {
    months: [],
    syncBillType: "api",
    bucketSyncCycle: "current",
  };
  loading.value = true;
  // 获取当前云账号区域
  const region = cloudAccountApi.getRegions(_id.value).then((ok) => {
    regions.value = ok.data;
  });
  // 获取同步的资源
  const resource = cloudAccountApi.getResourceSync(_id.value).then((ok) => {
    resources.value = ok.data;
  });
  const jobs = cloudAccountApi.getJobs(_id.value).then((ok) => {
    const financeManagement = ok.data.cloudAccountModuleJobs.find(
      (m) => m.module === "finance-management"
    );
    if (financeManagement) {
      billSyncShow.value = true;
      if (
        financeManagement.jobDetailsList &&
        financeManagement.jobDetailsList.length > 0
      ) {
        billSyncForm.value.syncBillType =
          financeManagement.jobDetailsList[0].params["BILL_SETTING"][
            "syncMode"
          ];
      }
    } else {
      billSyncShow.value = false;
    }
  });
  Promise.all([region, resource, jobs]).finally(() => {
    loading.value = false;
  });
}

defineExpose({ open, close });

const ruleFormRef = ref<FormInstance>();
// 校验规则
const syncRules = ref<FormRules>({
  checkedResources: [
    {
      required: true,
      message: "同步资源必须选择",
      trigger: "change",
    },
  ],
  checkedRegions: [
    {
      required: true,
      message: "同步区域必须选择",
      trigger: "change",
    },
  ],
});

const syncForm = ref<{
  /**
   * 选中的资源
   */
  checkedResources: Array<string>;
  /**
   * 选中的区域
   */
  checkedRegions: Array<string>;
}>({
  checkedResources: [],
  checkedRegions: [],
});

const changeResource = (selectResources: Array<string>) => {
  syncForm.value.checkedResources = selectResources;
};

const resources = ref<Array<ResourceSync>>([]);
const regions = ref<Array<Region>>([]);

const resourcesCheckedAll = computed(() => {
  return syncForm.value.checkedResources.length === resources.value.length;
});

const checkAll = computed(() => {
  return syncForm.value.checkedRegions.length === regions.value.length;
});

/**
 * 区域选中和取消的时候触发
 * @param selectRegion
 */
const change = (selectRegion: Array<string>) => {
  syncForm.value.checkedRegions = selectRegion;
};

/**
 * 全选资源
 * @param val true 全选,false 全不选
 */
const handleCheckAllResource = (val: boolean) => {
  if (val) {
    syncForm.value.checkedResources = resources.value.map(
      (region) => region.jobName
    );
  } else {
    syncForm.value.checkedResources = [];
  }
};
/**
 * 全选改变触发函数
 * @param val 改变的值
 */
const handleCheckAllChange = (val: boolean) => {
  if (val) {
    syncForm.value.checkedRegions = regions.value.map(
      (region) => region.regionId
    );
  } else {
    syncForm.value.checkedRegions = [];
  }
};
function syncResource() {
  if (_id.value) {
    ruleFormRef.value?.validate((valid) => {
      if (valid) {
        // 构建同步对象
        const syncSubmit: SyncRequest = {
          cloudAccountId: _id.value as string,
          params: {
            REGIONS: regions.value.filter((r) =>
              syncForm.value.checkedRegions.includes(r.regionId)
            ),
          },
          syncJob: resources.value
            .filter((r) => syncForm.value.checkedResources.includes(r.jobName))
            .map((source) => {
              return {
                module: source.module,
                jobName: source.jobName,
                jobGroup: source.jobGroup,
              };
            }),
        };
        // 发送同步任务
        cloudAccountApi.syncJob(syncSubmit).then((ok) => {
          ElMessage.success("发送同步任务成功");
          close();
        });
      }
    });
  }
}

/** 账单 start **/

const ruleFormSyncBill = ref<FormInstance>();
/**
 *获取12个月之前到现在的月份
 */
const months = ref<Array<string>>(
  Array.from({ length: 12 })
    .map((item, index) => {
      return index;
    })
    .map((num) => {
      const date = new Date(
        new Date().getFullYear(),
        new Date().getMonth() - (num as number)
      );
      const month = date.getMonth() + 1;
      return (
        date.getFullYear().toString() +
        "-" +
        (month.toString().length === 1
          ? "0" + month.toString()
          : month.toString())
      );
    })
);
const billSyncForm = ref<{
  months: Array<string>;
  syncBillType: string;
  bucketSyncCycle: "current" | "all";
}>({
  months: [],
  syncBillType: "api",
  bucketSyncCycle: "current",
});
/**
 * 账单月份全选
 */
const billMonthAll = ref<boolean>(
  months.value.length === billSyncForm.value.months.length
);
/**
 * 全选选中或者取消触发函数
 * @param selected 选中还是取消
 */
const handleCheckAllBillMonthChange = (selected: boolean) => {
  billSyncForm.value.months = selected ? months.value : [];
};
/**
 * 月份改变调用函数
 * @param checkedMonths 选中的月份
 */
const changeMonths = (checkedMonths: Array<string>) => {
  billSyncForm.value.months = checkedMonths;
  billMonthAll.value = months.value.length === checkedMonths.length;
};
/**
 * 校验规则
 */
const billSyncRules = ref<FormRules>({
  months: [
    {
      required: true,
      message: "同步月份必须选择",
      trigger: "change",
    },
  ],
});

function syncBill() {
  if (_id.value) {
    ruleFormSyncBill.value?.validate((valid) => {
      if (valid) {
        // 构建同步对象
        const syncSubmit: SyncRequest = {
          cloudAccountId: _id.value as string,
          params: {
            MONTHS: billSyncForm.value.months,
          },
          syncJob: [
            {
              jobName: "SYNC_BILL",
              jobGroup: "CLOUD_ACCOUNT_BILL_SYNC_GROUP",
              module: "finance-management",
            },
          ],
        };
        if (billSyncForm.value.syncBillType === "api") {
          syncSubmit.params = {
            MONTHS: billSyncForm.value.months,
          };
        } else {
          syncSubmit.params = {
            BUCKET_CYCLE: billSyncForm.value.bucketSyncCycle,
          };
        }

        // 发送同步任务
        cloudAccountApi.syncJob(syncSubmit).then(() => {
          ElMessage.success("发送同步任务成功");
          close();
        });
      }
    });
  }
}
</script>

<template>
  <div>
    <el-dialog
      :close-on-press-escape="false"
      :close-on-click-modal="false"
      destroy-on-close
      v-model="_visible"
      title="同步资源/账单"
      width="840px"
    >
      <el-tabs v-model="activeTab">
        <template v-for="tab in tabs" :key="tab.id">
          <el-tab-pane :label="tab.name" :name="tab.id" />
        </template>
      </el-tabs>

      <el-main v-if="activeTab === 'resource'" v-loading="loading">
        <el-form ref="ruleFormRef" :rules="syncRules" :model="syncForm">
          <div>
            <el-checkbox
              style="margin-bottom: 10px"
              v-model="resourcesCheckedAll"
              @change="handleCheckAllResource"
            >
              已选资源({{ syncForm.checkedResources.length }})
            </el-checkbox>
            <el-form-item prop="checkedResources">
              <el-checkbox-group
                v-model="syncForm.checkedResources"
                @change="changeResource"
              >
                <el-checkbox
                  v-for="resource in resources"
                  :key="resource.jobName"
                  :label="resource.jobName"
                  :title="resource.resourceDesc"
                  size="large"
                >
                  <span
                    style="
                      display: inline-block;
                      width: 120px;
                      overflow: hidden;
                      text-overflow: ellipsis;
                    "
                  >
                    {{ resource.resourceDesc }}
                  </span>
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </div>
          <div>
            <el-checkbox
              style="margin-bottom: 10px"
              v-model="checkAll"
              @change="handleCheckAllChange"
            >
              已选区域({{ syncForm.checkedRegions.length }})
            </el-checkbox>
            <el-form-item prop="checkedRegions">
              <el-checkbox-group
                @change="change"
                v-model="syncForm.checkedRegions"
              >
                <el-checkbox
                  :title="region.name"
                  v-for="region in regions"
                  :key="region.regionId"
                  :label="region.regionId"
                  size="large"
                >
                  <span
                    style="
                      display: inline-block;
                      width: 120px;
                      overflow: hidden;
                      text-overflow: ellipsis;
                    "
                  >
                    {{ region.name }}
                  </span>
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </div>
        </el-form>
      </el-main>
      <el-main v-if="activeTab === 'bill'" v-loading="loading">
        <el-form
          ref="ruleFormSyncBill"
          :rules="billSyncRules"
          :model="billSyncForm"
          label-position="top"
        >
          <el-form-item
            label="账单周期:"
            v-if="billSyncForm.syncBillType === 'api'"
          >
            <el-checkbox
              style="margin-bottom: 10px"
              v-model="billMonthAll"
              @change="handleCheckAllBillMonthChange"
            >
              全选
            </el-checkbox>
            <el-form-item prop="months">
              <el-checkbox-group
                @change="changeMonths"
                v-model="billSyncForm.months"
              >
                <el-checkbox
                  :title="month"
                  v-for="month in months"
                  :key="month"
                  :label="month"
                  size="large"
                >
                  <span
                    style="
                      display: inline-block;
                      width: 120px;
                      overflow: hidden;
                      text-overflow: ellipsis;
                    "
                  >
                    {{ month }}
                  </span>
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-form-item>
          <el-form-item label="账单周期:" v-else>
            <el-radio-group v-model="billSyncForm.bucketSyncCycle" size="large">
              <el-radio-button label="current">当月账单</el-radio-button>
              <el-radio-button label="all">全部账单</el-radio-button>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </el-main>

      <template #footer>
        <el-button @click="close"> 取消 </el-button>
        <el-button
          type="primary"
          @click="syncResource"
          v-if="activeTab === 'resource'"
        >
          同步
        </el-button>
        <el-button type="primary" @click="syncBill" v-if="activeTab === 'bill'">
          同步
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
:deep(.el-dialog__body) {
  padding-top: 0;
  padding-bottom: 0;
}
:deep(.el-form-item__content) {
  padding: 16px;
  background: #f7f9fc;
  border-radius: 4px;
}
</style>
