<template>
  <div>
    <el-drawer
      style="--ce-base-container-form-width: 100%"
      v-model="drawer"
      title="续期"
      direction="rtl"
      size="1200px"
      :destroy-on-close="true"
      :before-close="close"
    >
      <base-container
        v-loading="loading"
        style="--ce-base-container-height: auto"
      >
        <template #form>
          <base-container>
            <template #header>
              <span>云主机信息</span>
            </template>
            <template #content>
              <el-table
                :data="tableData"
                :height="tableData.length > 5 ? '500px' : undefined"
                style="width: 100%"
              >
                <el-table-column
                  :show-overflow-tooltip="true"
                  prop="instanceName"
                  column-key="instanceName"
                  :label="$t('commons.name')"
                  fixed
                  width="120px"
                >
                  <template #default="scope">
                    <span class="name-span-class">
                      {{ scope.row.instanceName }}
                    </span>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="accountName"
                  column-key="accountIds"
                  :label="$t('commons.cloud_account.native', '云账号')"
                  min-width="120px"
                >
                  <template #default="scope">
                    <div style="display: flex; align-items: center">
                      <PlatformIcon
                        style="height: 16px; width: 16px"
                        :platform="scope.row.platform"
                      ></PlatformIcon>
                      <span style="margin-left: 10px">{{
                        scope.row.accountName
                      }}</span>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="ipArray"
                  column-key="ipArray"
                  :label="$t('vm_cloud_server.label.ip_address')"
                  min-width="210px"
                >
                  <template #default="scope">
                    <div
                      class="role_display"
                      :data-var="
                        (scope._list = classifyIP(
                          scope.row.ipArray,
                          scope.row.remoteIp
                        ))
                      "
                    >
                      <span v-if="JSON.parse(scope.row.ipArray)?.length > 1">
                        {{ getFirstIp(scope._list) }}
                      </span>
                      <span v-if="JSON.parse(scope.row.ipArray)?.length === 1">
                        {{ getFirstIp(scope._list) }}
                      </span>
                      <el-popover
                        placement="right"
                        :width="200"
                        trigger="hover"
                        v-if="scope._list?.length > 1"
                      >
                        <template #reference>
                          <span class="role_numbers">
                            +{{ scope._list?.length - 1 }}
                          </span>
                        </template>
                        <div v-for="(item, index) in scope._list" :key="index">
                          <span>{{ item.ip }}</span>
                          <span v-if="item.isPublicIp"> (公) </span>
                        </div>
                      </el-popover>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="instanceTypeDescription"
                  column-key="instanceTypeDescription"
                  :label="$t('commons.cloud_server.instance_type')"
                  min-width="120px"
                >
                  <template #default="scope">
                    <span style="display: flex">{{
                      scope.row.instanceTypeDescription
                    }}</span>
                    <span
                      style="display: flex"
                      v-if="
                        scope.row.instanceType !==
                        scope.row.instanceTypeDescription
                      "
                      >{{ scope.row.instanceType }}</span
                    >
                  </template>
                </el-table-column>
                <el-table-column
                  prop="expiredTime"
                  column-key="expiredTime"
                  :label="$t('cloud_server.label.expired_time', '到期时间')"
                  width="180px"
                  :show="false"
                ></el-table-column>
                <el-table-column
                  prop="address"
                  width="180px"
                  label="续费后到期时间"
                >
                  <template #default="scope">
                    <RenewExpiresTime
                      :instance="scope.row"
                      :form-data="formData"
                    ></RenewExpiresTime> </template
                ></el-table-column>
                <el-table-column
                  prop="address"
                  label="续费费用(元)"
                  width="120px"
                >
                  <template #default="scope">
                    <RenewPrice
                      v-model="tablePrice[scope.row.id]"
                      :instance="scope.row"
                      :form-data="formData"
                    ></RenewPrice>
                  </template> </el-table-column></el-table
            ></template>
          </base-container>

          <base-container>
            <template #header>
              <span> 续期信息</span>
            </template>
            <template #content>
              <CeForm
                ref="renewRef"
                require-asterisk-position="right"
                label-position="top"
                label-suffix=""
                :other-params="{}"
                :formViewData="formViewData"
                v-model="formData"
              ></CeForm>
            </template>
          </base-container>
        </template>
      </base-container>
      <template #footer>
        <span class="total-price"
          ><span class="title">费用:</span
          >{{ decimalFormat.format(totalPrice, 2) }}</span
        >
        <el-button @click="close()">{{ $t("commons.btn.cancel") }} </el-button>
        <el-button type="primary" @click="renew"> 续费 </el-button>
      </template>
    </el-drawer>
  </div>
</template>
<script setup lang="ts">
import { ref, computed } from "vue";
import PlatformIcon from "@commons/components/platform-icon/index.vue";
import type { VmCloudServerVO } from "@/api/vm_cloud_server/type";
import { classifyIP } from "@commons/utils/util";
import CeForm from "@commons/components/ce-form/index.vue";
import VmCloudServerApi from "@/api/vm_cloud_server";
import type { FormView } from "@commons/components/ce-form/type";
import RenewPrice from "@/views/vm_cloud_server/RenewPrice.vue";
import RenewExpiresTime from "@/views/vm_cloud_server/RenewExpiresTime.vue";
import _ from "lodash";
import type { SimpleMap } from "@commons/api/base/type";
import decimalFormat from "@commons/utils/decimalFormat";
const drawer = ref<boolean>(false);
const loading = ref<boolean>(false);
const tableData = ref<Array<VmCloudServerVO>>([]);
const tablePrice = ref<SimpleMap<number>>({});
const formViewData = ref<Array<FormView>>([]);

const formData = ref<any>({});

const close = () => {
  tableData.value = [];
  formData.value = {};
  formViewData.value = [];
  tablePrice.value = {};
  drawer.value = false;
};
const totalPrice = computed(() => {
  return Object.values(tablePrice.value).reduce((pre, next) => pre + next, 0);
});
const renewRef = ref<InstanceType<typeof CeForm>>();
const open = (instanceList: Array<VmCloudServerVO>) => {
  tableData.value = instanceList.filter(
    (item) => item.instanceChargeType === "PrePaid"
  );
  // 如果是单条续费
  if (tableData.value.length === 1) {
    // 设置自动续费策略
    formData.value["expirePolicy"] =
      instanceList[0].autoRenew === true ? "YES" : "NO";
    // 获取表单
    VmCloudServerApi.getRenewForm(tableData.value[0].platform, loading).then(
      (ok) => {
        formViewData.value = ok.data.forms;
      }
    );
  } else {
    formViewData.value = [
      {
        inputType: "SingleSelect",
        attrs: "{}",
        field: "periodNum",
        label: "续费时长",
        type: "form-item",
        leftLabel: false,
        hideLabel: false,
        value: null,
        required: true,
        defaultValue: "1",
        defaultJsonValue: false,
        description: "",
        relationShows: [],
        relationShowValues: null,
        relationTrigger: [],
        clazz: "com.fit2cloud.vm.entity.request.RenewInstanceRequest",
        method: "getPeriodOption",
        pluginId: null,
        execModule: "",
        serviceMethod: false,
        group: null,
        step: null,
        index: 0,
        confirmGroup: null,
        confirmSpecial: false,
        confirmItemSpan: null,
        confirmPosition: null,
        footerLocation: 0,
        regexp: "",
        regexpDescription: "",
        extraInfo: "",
        hint: "",
        propsInfo: {
          style: {
            width: "120px",
          },
        },
        regexList: [],
        encrypted: false,
        valueField: "period",
        textField: "periodDisplayName",
        formatTextField: false,
        baseTextField: "",
      },
    ];
  }

  drawer.value = true;
};

const toRenewItemRequest = (instance: VmCloudServerVO) => {
  return {
    ...formData.value,
    uuid: instance.id,
  };
};
const renew = () => {
  renewRef.value?.validate().then(() => {
    VmCloudServerApi.renewInstance(
      tableData.value.map(toRenewItemRequest),
      loading
    ).then(() => {
      close();
    });
  });
};

const getFirstIp = (list: Array<any>) => {
  if (list) {
    const publicIpItem = _.find(list, ["isPublicIp", true]);
    if (publicIpItem) {
      return publicIpItem.ip + "(公)";
    }
    return list[0].ip;
  } else {
    return "";
  }
};

defineExpose({ open, close });
</script>
<style lang="scss" scoped>
:deep(.el-drawer__footer) {
  border-top: 1px solid #d5d6d8;
}
.total-price {
  float: left;
  margin-left: 100px;
  color: red;
  line-height: 32px;
  .title {
    margin-right: 20px;
    font-size: 16px;
    font-weight: 500;
  }
}
.role_display {
  height: 24px;
  line-height: 24px;
  display: flex;
  .role_numbers {
    cursor: pointer;
    margin-left: 8px;
    border-radius: 2px;
    padding: 0 6px;
    height: 24px;
    font-size: 14px;
    background-color: rgba(31, 35, 41, 0.1);
  }
  .role_numbers:hover {
    background-color: #ebf1ff;
    color: #3370ff;
  }
}
</style>
