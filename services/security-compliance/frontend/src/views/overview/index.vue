<template>
  <layout-auto-height-content>
    <template #breadcrumb>
      <breadcrumb :auto="true"></breadcrumb>
    </template>
    <div class="view_wapper">
      <el-row :gutter="12">
        <el-col :span="6">
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
            /> </el-select
        ></el-col>
      </el-row>

      <el-row :gutter="12">
        <el-col
          :span="6"
          v-for="describe in describeList"
          :key="describe.field"
        >
          <resource-count-card
            v-loading="resourceLoading"
            :format="
              describe.field === 'notCompliancePercentage'
                ? (count) =>
                    Number.isNaN(_.ceil(count, 2))
                      ? '0%'
                      : _.ceil(count, 2) + '%'
                : (count) => count
            "
            :describe="describe.describe"
            :color="describe.color"
            :count="viewCount ? viewCount[describe.field] : 0"
          ></resource-count-card>
        </el-col>
      </el-row>
      <el-row :gutter="12">
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
      <el-row :gutter="12">
        <el-col :span="12">
          <group-card
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
import _ from "lodash";
import ResourceCountCard from "@/views/overview/components/ResourceCountCard.vue";
import GroupCard from "@/views/overview/components/GroupCard.vue";
import cloudAccountApi from "@commons/api/cloud_account";
import type { KeyValue } from "@commons/api/base/type";
const router = useRouter();
const viewCount = ref<ComplianceViewCountResponse>();
const describeList = [
  {
    describe: "资源总数",
    field: "total",
    color: "rgba(254, 164, 126, 1)",
  },
  {
    describe: "合规资源",
    field: "complianceCount",
    color: "rgba(10, 216, 144, 1)",
  },
  {
    describe: "不合规资源",
    field: "notComplianceCount",
    color: "rgba(253, 126, 141, 1)",
  },
  {
    describe: "不合规占比",
    field: "notCompliancePercentage",
    color: "rgba(0, 153, 255, 1)",
  },
];

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
const getCloudAccountOptions = (
  groupDatas: Array<ComplianceViewGroupResponse>
) => {
  return {
    title: {
      text: "不合规资源按云账号分布",
      textStyle: {
        fontSize: 14,
      },
    },
    tooltip: {
      trigger: "item",
      formatter: (data: any) => {
        return `<div>
              不合规资源
              <div style='display:flex;justify-content: space-between;width:200px'><div>云平台</div><div>不合规/总数</div></div>
              <div style='display:flex;justify-content: space-between;width:200px'><div>${data.name}</div><div>${data.data.notComplianceCount}/${data.data.value}</div></div>
          </div>`;
      },
    },
    legend: {
      bottom: "5%",
      left: "center",
    },
    series: [
      {
        name: "不合规资源",
        type: "pie",
        radius: ["40%", "70%"],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: "#fff",
          borderWidth: 2,
        },
        label: {
          show: false,
          position: "center",
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: "bold",
          },
        },
        labelLine: {
          show: false,
        },
        data: groupDatas.map((group) => ({
          value: group.total,
          name: group.groupName,
          complianceCount: group.complianceCount,
          notComplianceCount: group.notComplianceCount,
          groupValue: group.groupValue,
        })),
      },
    ],
  };
};

const color = [
  "#11cfae",
  "#009ef0",
  "#627dec",
  "#0051a4",
  "#893fdc",
  "#89ffff",
  "#0099ff",
];
const getResourceTypeOptions = (
  groupDatas: Array<ComplianceViewGroupResponse>
) => {
  return {
    title: {
      text: "不合规资源按规则组分布",
      textStyle: {
        fontSize: 14,
      },
    },
    tooltip: {
      trigger: "item",
      formatter: (data: any) => {
        return `<div>
              不合规资源
              <div style='display:flex;justify-content: space-between;width:200px'><div>资源类型</div><div>不合规/总数</div></div>
              <div style='display:flex;justify-content: space-between;width:200px'><div>${data.name}</div><div>${data.data.notComplianceCount}/${data.data.value}</div></div>
          </div>`;
      },
    },
    series: [
      {
        name: "Nightingale Chart",
        type: "pie",
        radius: [20, 150],
        center: ["50%", "50%"],
        roseType: "area",
        itemStyle: {
          borderRadius: 8,
        },
        data: groupDatas.map((group) => ({
          value: group.total,
          name: group.groupName,
          complianceCount: group.complianceCount,
          notComplianceCount: group.notComplianceCount,
          groupValue: group.groupValue,
        })),
      },
    ],
  };
};
const getRuleGroupOptions = (
  groupDatas: Array<ComplianceViewGroupResponse>
) => {
  return {
    title: {
      text: "不合规资源按规则组分布",
      textStyle: {
        fontSize: 14,
      },
    },
    tooltip: {
      trigger: "item",
      formatter: (data: any) => {
        return `<div>
              不合规资源
              <div style='display:flex;justify-content: space-between;width:200px'><div>规则组名称</div><div>不合规/总数</div></div>
              <div style='display:flex;justify-content: space-between;width:200px'><div>${data.name}</div><div>${data.data.notComplianceCount}/${data.data.value}</div></div>
          </div>`;
      },
    },
    xAxis: {
      type: "category",
      data: groupDatas.map((group) => group.groupName),
    },
    yAxis: {
      type: "value",
    },
    series: [
      {
        data: groupDatas.map((group, index) => ({
          value: group.total,
          complianceCount: group.complianceCount,
          notComplianceCount: group.notComplianceCount,
          name: group.groupName,
          groupValue: group.groupValue,
          itemStyle: { color: index > 3 ? color[3] : color[index] },
        })),
        type: "bar",
      },
    ],
  };
};

const getRuleOptions = (groupDatas: Array<ComplianceViewGroupResponse>) => {
  groupDatas.sort(
    (pre, next) => next.notComplianceCount - pre.notComplianceCount
  );
  if (groupDatas.length > 5) {
    groupDatas = groupDatas.slice(0, 5);
  }

  return {
    backgroundColor: "#fff",

    tooltip: {
      trigger: "item",
      formatter: (data: any) => {
        return `<div>
              不合规资源
              <div style='display:flex;justify-content: space-between;width:200px'><div>规则名称</div><div>不合规/总数</div></div>
              <div style='display:flex;justify-content: space-between;width:200px'><div>${data.data.groupName}</div><div>${data.data.notComplianceCount}/${data.data.total}</div></div>
          </div>`;
      },
    },
    color: color,
    title: {
      text: "不合规规则 TOP 5",
      textStyle: {
        fontSize: 14,
      },
    },

    legend: {
      pageIconSize: [12, 12],
      itemWidth: 20,
      itemHeight: 10,
      textStyle: {
        //图例文字的样式
        fontSize: 16,
        color: "#444",
      },
      selectedMode: false,
      data: ["不合规规则 TOP 5"],
    },
    xAxis: {
      type: "value",
      splitLine: {
        show: false,
      },
      axisLabel: {
        show: false,
      },
      axisTick: {
        show: false,
      },
      axisLine: {
        show: false,
      },
    },
    yAxis: [
      {
        type: "category",
        inverse: true,
        axisLine: {
          show: false,
        },
        axisTick: {
          show: false,
        },
        axisPointer: {
          label: {
            show: true,
          },
        },
        pdaaing: [5, 0, 0, 0],
        postion: "right",
        data: groupDatas.map(
          (group) =>
            `不合规资源/资源总数:${group.notComplianceCount}/${group.total}`
        ),
        axisLabel: {
          margin: 30,
          fontSize: 14,
          align: "left",
          padding: [2, 0, 0, 0],
          color: "#000",
          rich: {
            nt1: {
              color: "#fff",
              backgroundColor: "#89ffff",
              width: 13,
              height: 10,
              fontSize: 10,
              align: "center",
              borderRadius: 100,
              lineHeight: "5",
              padding: [0, 1, 2, 1],
            },
            nt2: {
              color: "#fff",
              backgroundColor: "#11cfae",
              width: 13,
              height: 10,
              fontSize: 10,
              align: "center",
              borderRadius: 100,
              padding: [0, 1, 2, 1],
            },
            nt3: {
              color: "#fff",
              backgroundColor: "#893fdc",
              width: 13,
              height: 10,
              fontSize: 10,
              align: "center",
              borderRadius: 100,
              padding: [0, 1, 2, 1],
            },
            nt: {
              color: "#fff",
              backgroundColor: "#009ef0",
              width: 13,
              height: 10,
              fontSize: 10,
              align: "center",
              borderRadius: 100,
              padding: [0, 1, 2, 1],
              lineHeight: 5,
            },
          },
          formatter: function (value, index) {
            console.log(value, index);
            index++;
            if (index - 1 < 3) {
              return ["{nt" + index + "|" + index + "}"].join("\n");
            } else {
              return ["{nt|" + index + "}"].join("\n");
            }
          },
        },
      },
      {
        type: "category",
        inverse: true,
        axisTick: "none",
        axisLine: "none",
        show: true,
        axisLabel: {
          textStyle: {
            color: "#444",
            fontSize: "14",
          },
        },
        // data: this.attackSourcesDataFmt(attaName),
        data: groupDatas.map(
          (group) =>
            `不合规资源/资源总数:${group.notComplianceCount}/${group.total}`
        ),
      },
      {
        //名称
        type: "category",
        offset: -10,
        position: "left",
        axisLine: {
          show: false,
        },
        inverse: false,
        axisTick: {
          show: false,
        },
        axisLabel: {
          interval: 0,
          color: ["#000"],
          align: "left",
          verticalAlign: "bottom",
          lineHeight: 32,
          fontSize: 12,
        },
        data: groupDatas.map((group) => group.groupName),
      },
    ],
    series: [
      {
        zlevel: 1,
        name: "不合规资源/资源总数",
        type: "bar",
        barWidth: "15px",
        animationDuration: 1500,
        data: groupDatas.map((group, index) => ({
          value: group.notComplianceCount,
          notComplianceCount: group.notComplianceCount,
          complianceCount: group.complianceCount,
          total: group.total,
          groupName: group.groupName,
          groupValue: group.groupValue,
          itemStyle: { color: index > 3 ? color[3] : color[index] },
        })),
        align: "center",
        itemStyle: {
          barBorderRadius: 10,
        },
        label: {
          show: true,
          fontSize: 14,
          color: "#fff",
          textBorderWidth: 2,
          padding: [2, 0, 0, 0],
        },
      },
      {
        name: "资源",
        type: "bar",
        barWidth: 15,
        barGap: "-100%",
        margin: "20",
        data: groupDatas.map((group) => group.notComplianceCount),
        textStyle: {
          //图例文字的样式
          fontSize: 16,
          color: "#000",
        },
        itemStyle: {
          color: "#eee",
          fontSize: 16,
          barBorderRadius: 30,
        },
      },
    ],
    grid: {
      left: "-10%",
      right: "0%",
      width: "100%",
      bottom: "10%",
      top: "20%",
      containLabel: true,
    },
  };
};
</script>
<style scoped lang="scss">
.view_wapper {
  min-width: 900px;
}
.el-row {
  margin-bottom: 20px;
}
</style>
