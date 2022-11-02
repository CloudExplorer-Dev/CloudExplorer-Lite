<template>
  <layout-container :border="false">
    <template #content>
      <layout-container v-loading="loading">
        <template #header>
          <h4>监控数据</h4>
        </template>
        <template #content>
          <el-date-picker
            v-model="timestampData"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="changeTimestamp"
            @input="changeTimestamp"
          >
          </el-date-picker>
          <div
            v-for="item in states"
            :key="item.seriesName"
            style="width: 100%"
          >
            <Charts
              :data="item.data"
              chartsType="line"
              :chartsTitle="item.title"
              :chartsSeriesName="item.seriesName"
              :yDataUnit="item.yDataUnit"
            />
          </div>
        </template>
      </layout-container>
    </template>
  </layout-container>
</template>
<script setup lang="ts">
import { useRouter } from "vue-router";
import VmCloudServerApi from "@/api/vm_cloud_server";
import {
  PerfMetricConst,
  PerfMetricEntityTypeConst,
} from "@commons/utils/constants";
import Charts from "@commons/components/echart/Charts.vue";
import { ref, reactive, onMounted, type Ref } from "vue";

const useRoute = useRouter();
const vmUuid = ref<any>();
const loading = ref<boolean>(false);
const timestampData = ref<[Date, Date]>([
  new Date(new Date().getTime() - 1 * 60 * 60 * 1000),
  new Date(),
]);
//echarts数据
const states = reactive<any>([]);

const changeTimestamp = () => {
  getData();
};

const getData = () => {
  Object.keys(PerfMetricConst).forEach(function (perfMetric) {
    const request = ref<any>({
      metricName: perfMetric,
      entityType: PerfMetricEntityTypeConst.VIRTUAL_MACHINE,
      instanceId: vmUuid.value,
      startTime: timestampData.value[0].getTime(),
      endTime: timestampData.value[1].getTime(),
    });
    if (vmUuid.value) {
      VmCloudServerApi.listPerfMetricMonitor(request.value, loading).then(
        (res) => {
          if (res.data.length > 0) {
            states.forEach((state: any) => {
              if (state.metricName === perfMetric) {
                state.data.xData = res.data[0].timestamps;
                state.data.yData = res.data[0].values;
              }
            });
          }
        }
      );
    }
  });
};
const loadingData = () => {
  //查询所有指标
  Object.keys(PerfMetricConst).forEach(function (perfMetric) {
    const props = Object.keys(PerfMetricConst);
    const keyValues = props.map((el: string) => {
      return [el, el];
    });
    const keys: object = Object.fromEntries(keyValues);
    const perfMetricObj = PerfMetricConst[perfMetric as keyof typeof keys];
    const state = ref<any>({
      title: perfMetricObj["description"],
      seriesName: perfMetricObj["name"],
      yDataUnit: perfMetricObj["unit"],
      metricName: perfMetric,
      data: {
        xData: [],
        yData: [],
      },
    });
    states.push(state.value);
  });
  getData();
};
onMounted(() => {
  //云主机UUID
  vmUuid.value = useRoute.currentRoute.value.query.uuid;
  loadingData();
});
</script>
<style lang="scss">
.edit-button-container {
  text-align: center;
  line-height: 50px;
  align-items: center;
}
.permission-container {
  width: 100%;
  min-height: 100px;
}
</style>
