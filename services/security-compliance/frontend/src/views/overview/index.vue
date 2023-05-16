<template>
  <layout-auto-height-content
    :style="{ backgroundColor: '#f2f2f2', height: 'auto' }"
    style="
      --ce-main-content-padding-top: 0;
      --ce-main-content-padding-left: 0;
      --ce-main-content-padding-right: 0;
      --ce-main-content-padding-bottom: 0;
    "
  >
    <template #breadcrumb>
      <breadcrumb :auto="true">
        <div
          style="
            height: 100%;
            display: flex;
            align-items: center;
            margin-right: 50px;
          "
        >
          <el-select
            v-model="activeCloudAccount"
            class="m-2"
            placeholder="Select"
            size="small"
          >
            <el-option
              v-for="item in cloudAccountList"
              :key="item.value"
              :label="item.key"
              :value="item.value"
            />
          </el-select>
        </div>
      </breadcrumb>
    </template>
    <div class="view_wapper">
      <SecurityInfo
        ref="securityInfo"
        :cloud-account-id="
          activeCloudAccount === 'all' ? undefined : activeCloudAccount
        "
      ></SecurityInfo>
      <el-row :gutter="8">
        <el-col :span="12">
          <group-card
            :cloud-account-id="activeCloudAccount"
            :group-type="'CLOUD_ACCOUNT'"
            :get-options="getCloudAccountOptions"
            :click-series="
              (event:any) =>
                router.push({
                  name: 'scan',
                  query: { cloudAccountId: event.data['groupValue'] },
                })
            "
          >
            <template #default="{ chartWapper }">{{ chartWapper }} </template>
          </group-card>
        </el-col>
        <el-col :span="12">
          <group-card
            :cloud-account-id="activeCloudAccount"
            :click-series="
              (event:any) =>
                router.push({
                  name: 'scan',
                  query: { resourceType: event.data['groupValue'] },
                })
            "
            :group-type="'RESOURCE_TYPE'"
            :get-options="getResourceTypeOptions"
          >
            <template #default> </template>
          </group-card>
        </el-col>
      </el-row>
      <el-row :gutter="8">
        <el-col :span="12">
          <group-card
            :body-style="{ height: '412px' }"
            :cloud-account-id="activeCloudAccount"
            :click-series="
              (event:any) =>
                router.push({
                  name: 'scan',
                  query: { ruleGroup: event.data['groupValue'] },
                })
            "
            :group-type="'RULE_GROUP'"
            :get-options="getRuleGroupOptions"
          >
            <template #default> </template>
          </group-card>
        </el-col>
        <el-col :span="12">
          <group-card
            :body-style="{ height: '412px' }"
            :cloud-account-id="activeCloudAccount"
            :click-series="
              (event:any) =>
                router.push({
                  name: 'details',
                  params: { compliance_rule_id: event.data['groupValue'],cloud_account_id:'all' },
                })
            "
            :group-type="'RULE'"
            :get-options="getRuleOptions"
          >
            <template #default> </template>
          </group-card>
        </el-col>
      </el-row>
    </div>
  </layout-auto-height-content>
</template>
<script setup lang="ts">
import { onMounted, ref, watch } from "vue";
import { useRouter } from "vue-router";
import viewApi from "@/api/view/index";
import type {
  ComplianceViewCountResponse,
  ComplianceViewGroupResponse,
} from "@/api/view/type";
import GroupCard from "@/views/overview/components/GroupCard.vue";
import cloudAccountApi from "@commons/api/cloud_account";
import type { KeyValue } from "@commons/api/base/type";
import { PieTemplate } from "@commons/utils/echarts/template/PieTemplate";
import { BarTemplate } from "@commons/utils/echarts/template/BarTemplate";
import SecurityInfo from "@commons/business/base-layout/home-page/items/SecurityInfo.vue";
import type { DataItem } from "@commons/utils/echarts/template/index";
const securityInfo = ref<InstanceType<typeof SecurityInfo>>();
const router = useRouter();
const viewCount = ref<ComplianceViewCountResponse>();

// 云账号列表
const cloudAccountList = ref<Array<KeyValue<string, string>>>([]);
// 选中的云账号
const activeCloudAccount = ref<string>("all");
// 资源count加载器
const resourceLoading = ref<boolean>(false);
watch(activeCloudAccount, () => {
  viewApi
    .count(
      {
        cloudAccountId:
          activeCloudAccount.value === "all"
            ? undefined
            : activeCloudAccount.value,
      },
      resourceLoading
    )
    .then((ok) => {
      viewCount.value = ok.data;
    });
  securityInfo.value?.refresh(
    activeCloudAccount.value === "all" ? undefined : activeCloudAccount.value
  );
});

onMounted(() => {
  viewApi
    .count(
      {
        cloudAccountId:
          activeCloudAccount.value === "all"
            ? undefined
            : activeCloudAccount.value,
      },
      resourceLoading
    )
    .then((ok) => {
      viewCount.value = ok.data;
    });
  // 查询云账号数据
  cloudAccountApi.listAll().then((ok) => {
    cloudAccountList.value = [
      { key: "全部云账号", value: "all" },
      ...ok.data.map((cloudAccount) => ({
        key: cloudAccount.name,
        value: cloudAccount.id,
      })),
    ];
  });
});
/**
 * 获取鼠标悬浮弹框内容
 * @param title     标题
 * @param titleName 标题名称
 * @param data      数据
 */
const getTooltipFormatter = (title: string, titleName: string, data: any) => {
  return `<div>
              <div style='display:flex;justify-content: space-between;width:200px'><div>${titleName}</div><div>不合规/总数</div></div>
              <div style='display:flex;justify-content: space-between;width:200px'><div>${
                data.name.length > 6
                  ? data.name.substring(0, 6) + ".."
                  : data.name
              }</div><div>${data.data.notComplianceCount}/${
    data.data.total
  }</div></div>
          </div>`;
};

/**
 * 获取云账号echarts图表
 * @param groupDatas 分组数据
 */
const getCloudAccountOptions = (
  groupDatas: Array<ComplianceViewGroupResponse>
) => {
  const pieTemplate = PieTemplate.of(
    groupDatas.map((item) => ({
      ...item,
      name: item.groupName,
      value: item.notComplianceCount,
    }))
  );
  //修改鼠标悬浮内容
  pieTemplate.update("tooltip.formatter", (data: any) => {
    return getTooltipFormatter("不合规云账号", "云账号名称", data);
  });
  // 修改标题
  pieTemplate.updateTitleText("扫描资源 - 按云账号分布");
  pieTemplate.update("legend.formatter", (name: string) => {
    const dataList: Array<DataItem> = pieTemplate.getData("series.0.data");
    const data = dataList.find((data) => data.name === name);
    return `{one|${
      (name as string).length < 18 ? name : name.substring(0, 15) + ".."
    }} {two|${Math.ceil(data?.value || 0)}}`;
  });
  // 修改图表位置
  pieTemplate.update("series.0.center", ["120px", "55%"]);
  return pieTemplate.option;
};

/**
 * 获取资源类型图表数据
 * @param groupDatas 按资源类型分组数据
 */
const getResourceTypeOptions = (
  groupDatas: Array<ComplianceViewGroupResponse>
) => {
  const pieTemplate = PieTemplate.of(
    groupDatas.map((item) => ({
      ...item,
      name: item.groupName,
      value: item.notComplianceCount,
    }))
  );
  // 修改鼠标悬浮内容
  pieTemplate.update("tooltip.formatter", (data: any) => {
    return getTooltipFormatter("不合规资源", "资源类型", data);
  });
  pieTemplate.update("legend.formatter", (name: string) => {
    const dataList: Array<DataItem> = pieTemplate.getData("series.0.data");
    const data = dataList.find((data) => data.name === name);
    return `{one|${
      (name as string).length < 18 ? name : name.substring(0, 15) + ".."
    }} {two|${Math.ceil(data?.value || 0)}}`;
  });
  // 修改图表位置
  pieTemplate.update("series.0.center", ["120px", "55%"]);
  // 修改图表标题
  pieTemplate.updateTitleText("扫描资源 - 按资源类型分布");
  return pieTemplate.option;
};

/**
 * 获取规则组图表数据
 * @param groupDatas 按规则组组分组的数据
 */
const getRuleGroupOptions = (
  groupDatas: Array<ComplianceViewGroupResponse>
) => {
  if (!groupDatas || groupDatas.length === 0) {
    return BarTemplate.ofEmpty("不合规资源-按规则组分布").option;
  }
  const barTemplate = BarTemplate.of(
    groupDatas.map((item) => ({
      ...item,
      name: item.groupName,
      value: item.notComplianceCount,
    }))
  );
  // 鼠标移入弹框内容
  barTemplate.update("tooltip.formatter", (data: any) => {
    return getTooltipFormatter("不合规规则组", "规则组名称", data);
  });
  // 如果数据长度大于9 就加上滚动条
  if (groupDatas.length >= 9) {
    barTemplate.appendXDataZoom();
  }
  // 修改标题
  barTemplate.updateTitleText("不合规资源-按规则组分布");
  return barTemplate.option;
};

/**
 * 获取规则图表数据
 * @param groupDatas 按规则分组的数据
 */
const getRuleOptions = (groupDatas: Array<ComplianceViewGroupResponse>) => {
  if (!groupDatas || groupDatas.length === 0) {
    return BarTemplate.ofEmpty("不合规资源-按规则组分布").option;
  }
  const ruleDataList = groupDatas.map((item) => ({
    ...item,
    name: item.groupName,
    value: item.notComplianceCount,
  }));
  ruleDataList.sort(
    (pre, next) => pre.notComplianceCount - next.notComplianceCount
  );
  const barTemplate = BarTemplate.of(ruleDataList);
  // 添加鼠标移入提示
  barTemplate.update("tooltip.formatter", (data: any) => {
    return getTooltipFormatter("不合规规则", "规则名称", data);
  });
  // 柱状图转为横向柱状图
  barTemplate.toHorizontal();
  // 修改ecahrts间距
  barTemplate.update("grid", {
    x: 120,
    y: 72,
    x2: 24,
    y2: 57,
  });
  // 修改title
  barTemplate.updateTitleText("不合规规则排名");
  // 如果数据长度大于9 就加上滚动条
  if (ruleDataList.length >= 9) {
    barTemplate.appendYDataZoom();
  }
  barTemplate.update("dataZoom.0.startValue", ruleDataList.length);
  return barTemplate.option;
};
</script>
<style scoped lang="scss">
.view_wapper {
  min-width: 1084px;
}
.el-row {
  margin-bottom: 16px;
}
</style>
